package server.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {

    private static DBManager dbManager = null;

    private double randomId;

    private DBManager(){
        randomId = Math.random();
    }

    public static DBManager getInstance() {
        if (dbManager == null) {
            dbManager = new DBManager();
        }
        return dbManager;
    }

    public double getRandomId() {
        return randomId;
    }

    public static void main(String[] args) {
        String url = "jdbc:h2:file:./data/db_file";
        String user = "Group85";
        String password = "";

        try {
            DriverManager.registerDriver(new org.h2.Driver());
            Connection connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the DB!");
            connection.close();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }

    }


}
