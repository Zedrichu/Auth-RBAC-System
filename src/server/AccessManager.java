package server;

import java.io.*;
import java.util.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class AccessManager {
    public static void main(String[] args) {
        String accessControlFilePath = System.getProperty("user.dir") + "/src/resources/AccessControlJSON.json";
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(accessControlFilePath));
            JSONObject jsonObject = (JSONObject)obj;
            JSONArray users = (JSONArray) jsonObject.get("users");
            System.out.println("username | print | queue | topQueue | start | stop | restart | readConfig | setConfig");
            for (int i = 0; i < users.size(); i++) {
                JSONObject user = (JSONObject) users.get(i);
                String username = (String) user.get("username");
                JSONObject access = (JSONObject) user.get("access");
                boolean print = (boolean) access.get("print");
                boolean queue = (boolean) access.get("queue");
                boolean topQueue = (boolean) access.get("topQueue");
                boolean start = (boolean) access.get("start");
                boolean stop = (boolean) access.get("stop");
                boolean restart = (boolean) access.get("restart");
                boolean status = (boolean) access.get("status");
                boolean readConfig = (boolean) access.get("readConfig");
                boolean setConfig = (boolean) access.get("setConfig");
                System.out.printf("%s has access to: %b, %b, %b, %b, %b, %b, %b, %b, %b%n",
                        username, print, queue, topQueue, start, stop, restart, status, readConfig, setConfig);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
