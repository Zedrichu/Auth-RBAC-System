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

package server.database;

import org.json.simple.JSONObject;
import server.acpolicy.AccessControlUser;
import server.printer.Operation;

import java.sql.*;
import java.util.List;

public class DBManager {
    public Connection connection;

    private static DBManager dbManager = null;

    private DBManager() {}

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public boolean isConnected() {
        return connection != null;
    }

    public void connect() {
        try {
            DriverManager.registerDriver(new org.h2.Driver());
            connection = DriverManager
                    .getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
            System.out.println("Connected to the DB!");
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }

    public ResultSet queryUserCredentials(String username) throws SQLException {
        String query = "SELECT PASSHASH FROM USERS WHERE ID = ?";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, username);
        return prepStatement.executeQuery();
    }

    public void insertACLUser(AccessControlUser user) throws SQLException {
        String query = "INSERT INTO ACL_USERS VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, user.username);
        prepStatement.setBoolean(2, user.print);
        prepStatement.setBoolean(3, user.queue);
        prepStatement.setBoolean(4, user.topQueue);
        prepStatement.setBoolean(5, user.start);
        prepStatement.setBoolean(6, user.restart);
        prepStatement.setBoolean(7, user.stop);
        prepStatement.setBoolean(8, user.status);
        prepStatement.setBoolean(9, user.readConfig);
        prepStatement.setBoolean(10, user.setConfig);
        prepStatement.execute();
        prepStatement.close();
    }

    public void insertRole(JSONObject role) throws SQLException {
        String query = "INSERT INTO ROLES VALUES(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement prepStatement = connection.prepareStatement(query);

        String roleName = (String) role.get("role");
        JSONObject access = (JSONObject) role.get("access");
        prepStatement.setString(1, roleName);
        prepStatement.setBoolean(2, (boolean) access.get("print"));
        prepStatement.setBoolean(3, (boolean) access.get("queue"));
        prepStatement.setBoolean(4, (boolean) access.get("topQueue"));
        prepStatement.setBoolean(5, (boolean) access.get("start"));
        prepStatement.setBoolean(6, (boolean) access.get("restart"));
        prepStatement.setBoolean(7, (boolean) access.get("stop"));
        prepStatement.setBoolean(8, (boolean) access.get("status"));
        prepStatement.setBoolean(9, (boolean) access.get("readConfig"));
        prepStatement.setBoolean(10, (boolean) access.get("setConfig"));
        prepStatement.execute();
        prepStatement.close();
    }

    public void insertRBACUser(String username, List<String> roles) throws SQLException {
        String query = "INSERT INTO RBAC_USERS VALUES(?,?)";
        PreparedStatement prepStatement = connection.prepareStatement(query);

        prepStatement.setString(1, username);
        for (String role : roles) {
            prepStatement.setString(2, role);
            prepStatement.execute();
        }
        prepStatement.close();
    }

    public boolean queryACLUserAccess(String username, Operation operation) throws SQLException {
        String query = "SELECT * FROM ACL_USERS WHERE USERNAME=?";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, username);
        ResultSet result = prepStatement.executeQuery();
        result.next();
        return result.getBoolean(operation.name());
    }

    public boolean queryRBACUserAccess(String username, Operation operation) throws SQLException {
        String query = "SELECT * FROM RBAC_USERS NATURAL JOIN ROLES WHERE USERNAME=?";
        PreparedStatement prepStatement = connection.prepareStatement(query);
        prepStatement.setString(1, username);
        ResultSet result = prepStatement.executeQuery();
        while (result.next()) {
            if (result.getBoolean(operation.name())) return true;
        }
        return false;
    }

    public void clearACLUsers() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS ACL_USERS");
        statement.execute("CREATE TABLE ACL_USERS(" +
                "USERNAME VARCHAR(256) PRIMARY KEY, " +
                "PRINT BOOLEAN, " +
                "QUEUE BOOLEAN, " +
                "TOPQUEUE BOOLEAN, " +
                "START BOOLEAN, " +
                "RESTART BOOLEAN, " +
                "STOP BOOLEAN, " +
                "STATUS BOOLEAN, " +
                "READCONFIG BOOLEAN, " +
                "SETCONFIG BOOLEAN, " +
                "FOREIGN KEY (USERNAME) REFERENCES USERS(ID))");
    }

    public void clearRBACData() throws SQLException {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS RBAC_USERS");
        statement.execute("DROP TABLE IF EXISTS ROLES");
        statement.execute("CREATE TABLE ROLES(" +
                "ROLE VARCHAR(256) PRIMARY KEY, " +
                "PRINT BOOLEAN, " +
                "QUEUE BOOLEAN, " +
                "TOPQUEUE BOOLEAN, " +
                "START BOOLEAN, " +
                "RESTART BOOLEAN, " +
                "STOP BOOLEAN, " +
                "STATUS BOOLEAN, " +
                "READCONFIG BOOLEAN, " +
                "SETCONFIG BOOLEAN)");
        statement.execute("CREATE TABLE RBAC_USERS(" +
                "USERNAME VARCHAR(256), " +
                "ROLE VARCHAR(256), " +
                "PRIMARY KEY(USERNAME, ROLE)," +
                "FOREIGN KEY (USERNAME) REFERENCES USERS(ID)," +
                "FOREIGN KEY (ROLE) REFERENCES ROLES(ROLE))");
    }
}
