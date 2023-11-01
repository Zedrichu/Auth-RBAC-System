package client;

import server.ISessionValidator;
import util.*;
import server.User;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Client {
    private static IPrinterService printerService;
    private static ISessionProvider tokenProvider;
    private static Scanner scanner;

    public static void main(String[] args) throws MalformedURLException,
            NotBoundException {
        scanner = new Scanner(System.in);
        try {
            printerService = (IPrinterService)
                    Naming.lookup("rmi://localhost:8035/" + IPrinterService.routeName);
            tokenProvider = (ISessionProvider)
                    Naming.lookup("rmi://localhost:8035/" + ISessionProvider.routeName);
            executeOperation();
        } catch (RemoteException rex) {
            rex.printStackTrace();
            System.out.println(RED + "Server cannot be found. I'm quiting this" + RESET);
            System.exit(1);
        }
    }

    public static void executeOperation() {
        while (true) {
            User user = loginCredentials();
            try {
                SessionResponse response = tokenProvider.loginSingleUse(user.username, user.password);
                if (handleResponse(response)) {
                    System.out.println("Please choose one of the following operations (enter -1 to exit):");
                    System.out.println("1: print(filename, printer)");
                    System.out.println("2: queue(printer)");
                    System.out.println("3: topQueue(printer, job)");
                    System.out.println("4: status(printer)");
                    System.out.println("5: readConfig(parameter)");
                    System.out.println("6: setConfig(parameter, value)");
                    System.out.print("Enter your choice number: ");
                    int choice = scanner.nextInt();
                    if (choice == -1) break;
                    switch (choice) {
                        case 1 -> {
                            System.out.print("Enter the filename: ");
                            String filename = scanner.next();
                            System.out.print("Enter the printer name: ");
                            String printer = scanner.next();
                            printerService.print(filename, printer, response.session);
                        }
                        case 2 -> {
                            System.out.print("Enter the printer name: ");
                            String printer = scanner.next();
                            printerService.queue(printer, response.session);
                        }
                        case 3 -> {
                            System.out.print("Enter the printer name: ");
                            String printer = scanner.next();
                            System.out.print("Enter the job number: ");
                            int job = scanner.nextInt();
                            printerService.topQueue(printer, job, response.session);
                        }
                        case 4 -> {
                            System.out.print("Enter the printer name: ");
                            String printer = scanner.next();
                            System.out.println(printerService.status(printer, response.session));
                        }
                        case 5 -> {
                            System.out.print("Enter the parameter name: ");
                            String parameter = scanner.next();
                            printerService.readConfig(parameter, response.session);
                        }
                        case 6 -> {
                            System.out.print("Enter the parameter name: ");
                            String parameter = scanner.next();
                            System.out.print("Enter the value: ");
                            String value = scanner.next();
                            printerService.setConfig(parameter, value, response.session);
                        }
                    }
                }
            } catch (RemoteException rex) {
                System.out.println(RED + "Session could not be provided!" + RESET);
                break;
            }
        }

    }

    public static void executeSession(){
        User user = loginCredentials();
        try {
            SessionResponse response = tokenProvider.loginSession(user.username, user.password);
            handleResponse(response);
        } catch (RemoteException rex) {
            System.out.println(RED + "Session could not be provided!" + RESET);
        }

    }
    private static boolean handleResponse(SessionResponse response){
        try {
            ResponseCode rc = response.responseCode;
            if (rc == ResponseCode.OK) {
                System.out.println(GREEN + "Session has been provided for single use on printer" + RESET);
                printerService.start(response.session);
                return true;
            } else if (rc == ResponseCode.INVALID_USER){
                System.out.println(YELLOW + "Non-existent user in the database." + RESET);
            } else if (rc == ResponseCode.UNAUTHORIZED){ //TODO: later implementation of Acces Control
                System.out.println(RED + "Unauthorized user or invalid credentials" + RESET);
            } else {
                System.out.println(PURPLE + "SERVER-SIDE ERROR: run it's exploding!" + RESET);
            }
            printerService.start(response.session);
        } catch (RemoteException rex) {
            System.out.println(PURPLE + "Operation failed on printer!" + RESET);
        }
        return false;
    }

    private static User loginCredentials() {
        System.out.println("Please provide your credentials for authentication");
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        return new User(username, password);

    }

    private static final String RESET = "\033[0m";
    private static final String YELLOW = "\033[0;33m";
    private static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String RED = "\033[0;31m";     // RED
    private static final String GREEN = "\033[0;32m";   // GREEN
}
