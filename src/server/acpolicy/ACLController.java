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

public class ACLController implements IAccessController{
    private final DBManager dbManager;

    public ACLController() {
        dbManager = DBManager.getInstance();
        dbManager.connect();
        populate();
    }

    private void populate() {
        String accessControlFilePath = System.getProperty("user.dir") + "/src/resources/AccessControlListUpdate.json";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(accessControlFilePath));
            JSONObject jsonObject = (JSONObject)obj;

            dbManager.clearACLUsers();
            JSONArray users = (JSONArray) jsonObject.get("users");
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                AccessControlUser accessControlUser = new AccessControlUser((String) user.get("username"));
                JSONObject access = (JSONObject) user.get("access");
                accessControlUser.print = (boolean) access.get("print");
                accessControlUser.queue = (boolean) access.get("queue");
                accessControlUser.topQueue = (boolean) access.get("topQueue");
                accessControlUser.start = (boolean) access.get("start");
                accessControlUser.stop = (boolean) access.get("stop");
                accessControlUser.restart = (boolean) access.get("restart");
                accessControlUser.status = (boolean) access.get("status");
                accessControlUser.readConfig = (boolean) access.get("readConfig");
                accessControlUser.setConfig = (boolean) access.get("setConfig");
                dbManager.insertACLUser(accessControlUser);
            }
        } catch(SQLException | RuntimeException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean grantAccess(String username, Operation operation) throws InvalidAccessException {
        try {
            if (!dbManager.queryACLUserAccess(username, operation)) {
                throw new InvalidAccessException(
                        String.format("User %s is not allowed to access operation %s", username, operation));
            };
            return true;
        }
        // catches potential access control not specified
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }

}
