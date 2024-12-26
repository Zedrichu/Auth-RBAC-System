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

package server.acpolicy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.database.DBManager;
import server.printer.Operation;
import util.InvalidAccessException;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

public class RBACController implements IAccessController {
    private DBManager dbManager;

    public RBACController() {
        dbManager = DBManager.getInstance();
        dbManager.connect();
        populate();
    }


    @Override
    public boolean grantAccess(String username, Operation operation) throws InvalidAccessException {
        try {
            if (!dbManager.queryRBACUserAccess(username, operation)) {
                throw new InvalidAccessException(
                        String.format("User %s does not have a role authorized to perform %s", username, operation));
            }
            return true;
        }
        // catches potential access control not specified
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private void populate(){
        String accessControlFilePath = System.getProperty("user.dir") + "/src/resources/RBACPolicyUpdate.json";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(accessControlFilePath));
            JSONObject jsonObject = (JSONObject) obj;

            // Clear RBAC Data
            dbManager.clearRBACData();

            // Insert Roles
            JSONArray roles = (JSONArray) jsonObject.get("roles");
            for (int i = 0; i < roles.size(); i++){
                JSONObject role = (JSONObject) roles.get(i);
                dbManager.insertRole(role);
            }

            // Insert Users
            JSONArray users = (JSONArray) jsonObject.get("users");
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                AccessControlUser ac_user = new AccessControlUser((String) user.get("username"));
                JSONArray userRoles = (JSONArray) user.get("roles");
                Stream<String> stream = Arrays.stream(userRoles.toArray()).map(item -> (String) item);
                dbManager.insertRBACUser(ac_user.username, stream.toList());
            }

        } catch(SQLException | RuntimeException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}
