package server.acpolicy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.DBManager;
import server.Operation;
import server.Roles;

import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.stream.Stream;

public class RBACController implements IAccessController {
    private DBManager dbManager;

    public void RBACController(){
        dbManager = DBManager.getInstance();
        dbManager.connect();
        populate();
    }
    @Override
    public boolean grantAccess(String username, Operation operation) {
        // Lookup database for roles corresponding to username
        // For each role lookup database for permission on operation
        return true;
    }


    private void populate(){
        String accessControlFilePath = System.getProperty("user.dir") + "/src/resources/RBACPolicy.json";
        JSONParser parser = new JSONParser();
        DBManager dbManager = DBManager.getInstance();
        try {
            dbManager.clearAccessControlUsers();
            Object obj = parser.parse(new FileReader(accessControlFilePath));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray users = (JSONArray) jsonObject.get("users");
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                AccessControlUser ac_user = new AccessControlUser((String) user.get("username"));
                JSONArray roles =  (JSONArray) user.get("roles");
                Stream<String> stream = Arrays.stream(roles.toArray()).map(item -> (String) item);
                dbManager.insertUserRoles(ac_user.username, (String[]) stream.toArray());
            }


            JSONArray roles = (JSONArray) jsonObject.get("roles");
            for (int i = 0; i < roles.size(); i++){
                JSONObject role = (JSONObject) roles.get(i);
                 Roles roleName = (Roles) role.get("role");
                 JSONObject access = (JSONObject) role.get("access");
                 // #TODO: implement the population of role permissions table
//                 roleControl.print = (boolean) access.get("print");
//                 roleControl.queue = (boolean) access.get("queue");
//                 roleControl.topQueue = (boolean) access.get("topQueue");
//                 roleControl.stop = (boolean) access.get("stop");
//                 roleControl.start = (boolean) access.get("start");
//                 roleControl.restart = (boolean) access.get("restart");
//                 roleControl.status = (boolean) access.get("status");
//                 roleControl.readConfig = (boolean) access.get("readConfig");
//                 roleControl.setConfig = (boolean) access.get("setConfig");
            }

        } catch(SQLException | RuntimeException | ParseException | IOException e) {
            e.printStackTrace();
        }
    }

}
