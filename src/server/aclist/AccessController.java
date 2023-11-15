package server.aclist;

import java.io.*;
import java.sql.SQLException;

import org.json.simple.*;
import org.json.simple.parser.*;
import server.DBManager;
import server.Operation;
import server.aclist.AccessControlUser;

public class AccessController {
    private final DBManager dbManager;

    public AccessController() {
        dbManager = DBManager.getInstance();
        dbManager.connect();
        populate();
    }

    private void populate() {
        String accessControlFilePath = System.getProperty("user.dir") + "/src/resources/AccessControlJSON.json";
        JSONParser parser = new JSONParser();
        DBManager dbManager = DBManager.getInstance();
        try {
            Object obj = parser.parse(new FileReader(accessControlFilePath));
            JSONObject jsonObject = (JSONObject)obj;
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
                dbManager.insertAccessControlUser(accessControlUser);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public boolean verifyAccess(String username, Operation operation) {
        try {
            return dbManager.queryUserAccess(username, operation);
        }
        // catches potential access control not specified
        catch(SQLException e){
            return false;
        }
    }

}
