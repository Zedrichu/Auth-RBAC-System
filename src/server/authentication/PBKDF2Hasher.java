/*
 *     Copyright (C) 2023 Adrian Zvizdenco, Jeppe Mikkelsen, Arthur Bosquetti
 *
 *     This program is free software: you can redistribute it and/or modify it under the terms
 *     of the GNU Affero General Public License as published by the Free Software Foundation,
 *     either version 3 of the License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 *     without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *     See the GNU Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public License along with
 *     this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package server.authentication;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

public class PBKDF2Hasher implements IHasher{
   private static final int ITERATIONS = 8192;
   private static final int KEY_LENGTH = 512;
   private static final int SALT_LENGTH = 16;

   /**
    * Hash the input password using HMAC SHA of configured key length
    * @param password (String) - the plaintext password
    * @return (String) - hash output in Base64 encoding
    */
   public String hashPassword(final String password) {
      try {
         byte[] salt = SecureRandom.getInstance("SHA1PRNG").generateSeed(SALT_LENGTH);
         PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
         SecretKeyFactory skf = SecretKeyFactory.getInstance(String.format("PBKDF2WithHmacSHA%d", KEY_LENGTH));

         byte[] hash = skf.generateSecret(spec).getEncoded();

         return String.format("pbkdf2$%d$%s$%s",
               ITERATIONS,
               Base64.getEncoder().encodeToString(salt),
               Base64.getEncoder().encodeToString(hash));
      } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
         throw new RuntimeException(e);
      }
   }

   public boolean verifyPassword(final String candidate, final String hashedPassword) {
      try {
         String[] hashedParts = hashedPassword.split("\\$");
         byte[] salt = Base64.getDecoder().decode(hashedParts[2]);
         byte[] hash = Base64.getDecoder().decode(hashedParts[3]);

         PBEKeySpec spec = new PBEKeySpec(candidate.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
         SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");

         byte[] candidateHash = skf.generateSecret(spec).getEncoded();

         if (candidateHash.length == hash.length) {
            return Arrays.equals(candidateHash, hash);
         }
         return false;
      } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
         throw new RuntimeException(e);
      }
   }
}
