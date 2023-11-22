package server.acpolicy;

import java.io.*;
import java.sql.SQLException;

import org.json.simple.*;
import org.json.simple.parser.*;
import server.DBManager;
import server.Operation;
import util.InvalidAccessException;

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
        DBManager dbManager = DBManager.getInstance();
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
