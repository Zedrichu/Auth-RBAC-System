package client;

import util.*;
import server.User;

import java.io.Console;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private static IPrinterService printerService;
    private static ITokenProvider tokenProvider;
    private static Scanner scanner;

    public static void main(String[] args) throws MalformedURLException,
            NotBoundException {
        scanner = new Scanner(System.in);
        try {
            printerService = (IPrinterService)
                    Naming.lookup("rmi://localhost:8035/printer");
            tokenProvider = (ITokenProvider)
                    Naming.lookup("rmi://localhost:8035/token");
            executeOperation();
        } catch (RemoteException rex) {
            rex.printStackTrace();
            System.out.println(RED + "Server cannot be found. I'm quiting this" + RESET);
            System.exit(1);
        }
    }

    public static void executeOperation() {
        User user = loginCredentials();
        try {
            TokenResponse response = tokenProvider.generateSingleUse(user.username, user.password);
            handleResponse(response);
        } catch (RemoteException rex) {
            System.out.println(RED + "Token could not be provided!" + RESET);
        }
    }

    public static void executeSession(){
        User user = loginCredentials();
        try {
            TokenResponse response = tokenProvider.generateToken(user.username, user.password);
            handleResponse(response);
        } catch (RemoteException rex) {
            System.out.println(RED + "Token could not be provided!" + RESET);
        }

    }
    private static void handleResponse(TokenResponse response){
        try {
            ResponseCode rc = response.responseCode;
            if (rc == ResponseCode.OK) {
                System.out.println(GREEN + "Token has been provided for single use on printer" + RESET);
                printerService.start(response.token);
            } else if (rc == ResponseCode.INVALID_USER){
                System.out.println(YELLOW + "Non-existent user in the database." + RESET);
            } else if (rc == ResponseCode.UNAUTHORIZED){ //TODO: later implementation of Acces Control
                System.out.println(RED + "Unauthorized user or invalid credentials" + RESET);
            } else {
                System.out.println(PURPLE + "SERVER-SIDE ERROR: run it's exploding!" + RESET);
            }
            printerService.start(response.token);
        } catch (RemoteException rex) {
            System.out.println(PURPLE + "Operation failed on printer!" + RESET);
        }

    }

    private static User loginCredentials() {

        System.out.println("Please provide your credentials for authentication");
        System.out.print("Username:");
        String username = scanner.next();
        System.out.print("Password:");
        String password = scanner.next();
        return new User(username, password);

    }

    private static final String RESET = "\033[0m";
    private static final String YELLOW = "\033[0;33m";
    private static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String RED = "\033[0;31m";     // RED
    private static final String GREEN = "\033[0;32m";   // GREEN
}
