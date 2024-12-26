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

package server.authentication;

import server.database.DBManager;
import util.ResponseCode;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Authenticator class - Handles all authentication for the DBManager
 */
public class Authenticator {
   private final DBManager dbManager;

   public Authenticator() {
      dbManager = DBManager.getInstance();
      dbManager.connect();
   }

   // Test Authenticator on a specific user in the database
   public static void main(String[] args) {
      Authenticator auth = new Authenticator();
      System.out.println(auth.authenticateUser("Alice", "alicePW"));
   }

   public ResponseCode authenticateUser(String username, String password) {
      if (!dbManager.isConnected()) dbManager.connect();

      ResultSet queryResult;
      try {
         queryResult = dbManager.queryUserCredentials(username);

         if (!queryResult.next()) {
            return ResponseCode.INVALID_USER;
         }

         // Extract user info: hashed password prepended with salt
         String passhash = queryResult.getString("PASSHASH");
         queryResult.close();

         // Choose your hero hasher (see options in IHasher)
         IHasher hasher = IHasher.create("BCrypt");

         // Verify the password against existing hash
         boolean valid = hasher.verifyPassword(password, passhash);

         if (valid) return ResponseCode.OK;
         return ResponseCode.UNAUTHORIZED;
      } catch (SQLException sql) {
         System.out.println(sql.getMessage());
         return ResponseCode.FAIL;
      }
   }
}
