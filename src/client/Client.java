package client;

import util.IPrinterService;
import util.ITokenProvider;
import server.User;
import util.Token;

import java.io.Console;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
    private static IPrinterService printerService;
    private static ITokenProvider tokenProvider;
    private static Token token = new Token("client");

    public static void main(String[] args) throws MalformedURLException,
            NotBoundException {

        try {
            printerService = (IPrinterService)
                    Naming.lookup("rmi://localhost:8035/printer");
            tokenProvider = (ITokenProvider)
                    Naming.lookup("rmi://localhost:8035/token");
            executeOperation();
        } catch (RemoteException rex) {
            System.out.println("Server cannot be found. I'm quiting this");
            System.exit(1);
        }
    }

//    private static void usePrinter(IPrinterService printer) {
//        LogOperation("print", () -> printer.print("authentication_lab.pdf", "best printer", sessionToken));
//        LogOperation("queue", () -> printer.queue("printer"));
//        LogOperation("topQueue", () -> printer.topQueue("printer", 0));
//        LogOperation("start", () -> printer.start());
//        LogOperation("stop", () -> printer.stop());
//        LogOperation("restart", () -> printer.restart());
//        LogOperation("status", () -> printer.status("printer", ));
//        LogOperation("readConfig", () -> printer.readConfig("parameter", sessionToken));
//        LogOperation("setConfig", () -> printer.setConfig("parameter", "value", sessionToken));
//    }
//
//    private static <T> void LogOperation(String operation, Callable<T> op) {
//        try{
//            System.out.println("Calling operation: " + operation);
//            op.call();
//            SuccessMessage("Operation finished");
//        } catch(AuthenticationFailedException e) {
//            FailMessage("Authentication error: " + e.getMessage());
//        } catch (Exception e) {
//            FailMessage("An error has occured. Contact your administrator.");
//        }
//    }


    // Tries to connect client to server and handles the server response
//    private static boolean connect(IAppServer app) throws RemoteException,
//            NotBoundException, MalformedURLException {
//        ResponseCode response = null;
//        for (int i=0; i<MAX_RETRIES; i++){
//            try {
//                response = app.connect("client", token);
//            } catch (RemoteException rex) {
//                System.out.println("Server cannot be contacted. I'm quiting this");
//                break;
//            }
//
//            switch (response) {
//                case OK -> {
//                    printerService = (IPrinterService)
//                                        Naming.lookup("rmi://localhost:8035/printer");
//                    System.out.println("Client was already authenticated!");
//                    return true;
//                }
//                case UNAUTHORIZED -> {
//                    authService = (ITokenService)
//                                    Naming.lookup("rmi://localhost:8035/authenticator");
//                    authenticate();
//                    try {
//                        response = app.connect("client", token);
//                    } catch (RemoteException rex) {
//                        System.out.println("Server cannot be contacted. I'm quiting this");
//                        break;
//                    }
//                    if (response == ResponseCode.OK) {
//                        return true;
//                    }
//                }
//                case INVALID_USER -> {
//
//                }
//            }
//        }
//        return false;
//    }

    private static void authenticate() throws RemoteException {
//        ResponseCode response = authService.authenticate("user123456", "password", token);
//
//        if (response == ResponseCode.OK) {
//            System.out.println("I am authenticated now");
//        } else {
//            System.out.println("I was not authenticated");
//        }
    }

    private static User loginCredentials() {
        Console console = System.console();
        if (console == null) {
            System.err.println("No console available.");
            System.exit(1);
        }

        System.out.println("Please provide your credentials for authentication:");
        String username = console.readLine("Username: ");
        char[] passArray = console.readPassword("Password: ");
        String password = new String(passArray);
        return new User(username, password);
    }

    private static final String RESET = "\033[0m";
    private static final String YELLOW = "\033[0;33m";
    private static final String PURPLE = "\033[0;35m";  // PURPLE
    private static final String RED = "\033[0;31m";     // RED
    private static final String GREEN = "\033[0;32m";   // GREEN
}
