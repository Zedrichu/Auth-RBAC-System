package client;

import util.*;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private static final int REGISTRY_PORT = 8035;
    private static final String REGISTRY_HOST = "localhost";

    private static final String RESET = "\033[0m";
    private static final String YELLOW = "\033[0;33m";
    private static final String PURPLE = "\033[0;35m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";

    public static void main(String[] args) throws MalformedURLException, NotBoundException {
        CLIMenu menu;
        try {
            IPrinterService printerService = (IPrinterService) Naming.lookup(
                    "rmi://" + REGISTRY_HOST +":" + REGISTRY_PORT +"/" + IPrinterService.routeName);
            ISessionProvider tokenProvider = (ISessionProvider) Naming.lookup(
                    "rmi://" + REGISTRY_HOST +":" + REGISTRY_PORT +"/" + ISessionProvider.routeName);
            menu = new CLIMenu();

            while (true) {
                User user = menu.createUser();
                boolean useSingleUse = menu.selectSessionMode();
                SessionResponse response;
                if (useSingleUse) response = tokenProvider.loginSingleUse(user.username, user.password);
                else response = tokenProvider.loginSession(user.username, user.password);

                if (handleResponse(response)) {
                    System.out.println(YELLOW + "Client |" + response.session.username
                            + "| has access to session id: |" + response.session.getId() + "|" + RESET);
                    try {
                        // Returns false if user exited
                        if (!menu.selectOperation(printerService, response.session, useSingleUse)) {
                            break;
                        }
                    } catch (InvalidAccessException iaex) {
                        System.out.println(RED + "INVALID ACCESS:" + iaex.getMessage() + RESET);
                    }
                } else {
                    System.out.println(RED + "Session could not be provided!" + RESET);
                }
            }
        } catch (RemoteException rex) {
            rex.printStackTrace();
            System.out.println(RED + "An issue occurred. Exiting..." + RESET);
            System.exit(1);
        }
    }

    private static boolean handleResponse(SessionResponse response){
        ResponseCode rc = response.responseCode;
        if (rc == ResponseCode.OK) {
            System.out.println(GREEN + "Session has been provided for single use on printer" + RESET);
            return true;
        } else if (rc == ResponseCode.INVALID_USER) {
            System.out.println(YELLOW + "Non-existent user in the database." + RESET);
        } else if (rc == ResponseCode.UNAUTHORIZED) {
            //TODO: later implementation of Access Control
            System.out.println(RED + "Unauthorized user or invalid credentials" + RESET);
        } else {
            System.out.println(PURPLE + "SERVER-SIDE ERROR: run it's exploding!" + RESET);
        }
        return false;
    }

}
