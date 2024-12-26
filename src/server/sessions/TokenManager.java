/*
 *   Copyright (C) 2023 Adrian Zvizdenco, Jeppe Mikkelsen, Arthur Bosquetti
 *
 *       This program is free software: you can redistribute it and/or modify it under the terms
 *       of the GNU Affero General Public License as published by the Free Software Foundation,
 *       either version 3 of the License, or (at your option) any later version.
 *
 *       This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *       without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *       See the GNU Affero General Public License for more details.
 *
 *       You should have received a copy of the GNU Affero General Public License along with
 *       this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package server.sessions;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import server.authentication.Authenticator;
import shared.CredentialResponse;
import shared.IAuthCredential;
import shared.Token;
import util.ResponseCode;
import javax.crypto.SecretKey;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Date;
import java.util.HashSet;
import java.util.UUID;

public class TokenManager extends UnicastRemoteObject implements ICredentialManager {
   private final Authenticator authenticator;
   private static final int TTL_SECONDS = 10 * 60;
//   private static SecretKey signingKey = null;
   private final JWTVerifier verifier;
   private final Algorithm algorithm;
   private final HashSet<UUID> tokenBlacklist;

   public TokenManager() throws RemoteException {
      super();
      this.tokenBlacklist = new HashSet<>();
      this.authenticator = new Authenticator();
      this.algorithm = Algorithm.HMAC256("very-secret-key");
      this.verifier = JWT.require(algorithm).build();
   }

   @Override
   public boolean validateCredential(IAuthCredential credential) throws RemoteException {
      if (credential == null) return false;
      Token token = ((Token) credential);
      try {
         DecodedJWT decodedJWT = verifier.verify(token.jwt_token);
         if (token.singleUse) {
            UUID invalid = decodedJWT.getClaim("jti").as(UUID.class);
            tokenBlacklist.add(invalid);
            return true;
         }
         return !tokenBlacklist.contains(decodedJWT.getClaim("jti").as(UUID.class));
      } catch (JWTVerificationException e) {
         return false;
      }
   }

   @Override
   public CredentialResponse loginSession(String userId, String userPassword) throws RemoteException {
      ResponseCode authResult = authenticator.authenticateUser(userId, userPassword);
      Token token = null;
      if (authResult == ResponseCode.OK) {
         String jwt_token = JWT.create()
               .withSubject(userId)
               .withIssuer("printService")
               .withClaim("jti", UUID.randomUUID().toString())
               .withIssuedAt(new Date(System.currentTimeMillis()))
               .withExpiresAt(new Date(System.currentTimeMillis() + TTL_SECONDS * 1000))
               .sign(this.algorithm);
         token = new Token(userId, jwt_token, false);
      };
      return new CredentialResponse(authResult, token);
   }

   @Override
   public CredentialResponse loginSingleUse(String userId, String userPassword) throws RemoteException {
      ResponseCode authResult = authenticator.authenticateUser(userId, userPassword);
      Token token = null;
      if (authResult == ResponseCode.OK) {
         String jwt_token = JWT.create()
               .withSubject(userId)
               .withIssuer("printService")
               .withIssuedAt(new Date(System.currentTimeMillis()))
               .withExpiresAt(new Date(System.currentTimeMillis() + TTL_SECONDS * 1000))
               .sign(this.algorithm);
         token = new Token(userId, jwt_token, true);
      };
      return new CredentialResponse(authResult, token);
   }
}
