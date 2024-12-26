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

package util;

import server.authentication.IHasher;
import server.database.DBConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Enrollment {
    private static Connection connection;
    public static void main(String[] args) throws SQLException {

        DriverManager.registerDriver(new org.h2.Driver());
        connection = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
        System.out.println("Connected to the DB!");
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE IF EXISTS RBAC_USERS");
        statement.execute("DROP TABLE IF EXISTS ACL_USERS");
        statement.execute("DROP TABLE IF EXISTS USERS");
        statement.execute("CREATE TABLE USERS(ID VARCHAR(256) PRIMARY KEY, PASSHASH VARCHAR(128))");
        statement.close();

        List<String> usernames = new ArrayList<>(Arrays.asList("Alice","Ida","Cecilia","David","Erica","Fred","George", "Henry"));
        List<String> passwords = new ArrayList<>(Arrays.asList("alicePW","idaPW","ceciliaPW","davidPW","ericaPW","fredPW","georgePW", "henryPW"));

        // Choose your hero hasher (see options in IHasher)
        IHasher hasher = IHasher.create("BCrypt");

        for (int i = 0; i < usernames.size(); i++) {
            String hash = hasher.hashPassword(passwords.get(i));
            insertUser(usernames.get(i), hash);
        }
        System.out.println("Populated the DB!");
        if (connection != null) connection.close();

    }

    private static void insertUser(String username, String hash) throws SQLException {
        String insertQuery = "INSERT INTO USERS VALUES(?, ?)";
        PreparedStatement prepStatement = connection.prepareStatement(insertQuery);
        prepStatement.setString(1, username);
        prepStatement.setString(2, hash);
        prepStatement.execute();
        prepStatement.close();
    }

}
