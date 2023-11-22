package server.acpolicy;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import server.DBManager;
import server.Operation;
import server.Roles;
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
                        String.format("User %s does not have a valid role to perform %s", username, operation));
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
        DBManager dbManager = DBManager.getInstance();
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
