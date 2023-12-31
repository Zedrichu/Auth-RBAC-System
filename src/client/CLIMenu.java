package client;

import util.IPrinterService;
import util.InvalidAccessException;
import util.Ticket;

import java.rmi.RemoteException;
import java.util.Scanner;

public class CLIMenu {
    private Scanner scanner;

    public CLIMenu() {
        scanner = new Scanner(System.in);
    }

    public User createUser() {
        System.out.println("Please provide your credentials for authentication");
        System.out.print("Username: ");
        String username = scanner.next();
        System.out.print("Password: ");
        String password = scanner.next();
        return new User(username, password);
    }

    public boolean selectSessionMode() {
        System.out.println("Please select your authentication mode:");
        System.out.println("1: Single Use");
        System.out.println("2: Session Mode");
        int sessionMode = scanner.nextInt();
        return sessionMode == 1;
    }

    public boolean selectOperation(IPrinterService printerService, Ticket ticket, boolean singleUse) throws RemoteException, InvalidAccessException {
        do {
            System.out.println("Please choose one of the following operations (enter -1 to exit)");
            System.out.println("1: print(filename, printer)");
            System.out.println("2: queue(printer)");
            System.out.println("3: topQueue(printer, job)");
            System.out.println("4: start()");
            System.out.println("5: restart()");
            System.out.println("6: stop()");
            System.out.println("7: status(printer)");
            System.out.println("8: readConfig(parameter)");
            System.out.println("9: setConfig(parameter, value)");
            System.out.print("Enter your choice number: ");
            int choice = scanner.nextInt();
            if (choice == -1) return false;
            switch (choice) {
                case 1 -> {
                    System.out.print("Enter the filename: ");
                    String filename = scanner.next();
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.print(filename, printer, ticket);

                }
                case 2 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.queue(printer, ticket);
                }
                case 3 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    System.out.print("Enter the job number: ");
                    int job = scanner.nextInt();
                    printerService.topQueue(printer, job, ticket);
                }
                case 4 -> {
                    printerService.start(ticket);
                }
                case 5 -> {
                    printerService.restart(ticket);
                }
                case 6 -> {
                    printerService.stop(ticket);
                }
                case 7 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.status(printer, ticket);
                }
                case 8 -> {
                    System.out.print("Enter the parameter name: ");
                    String parameter = scanner.next();
                    printerService.readConfig(parameter, ticket);
                }
                case 9 -> {
                    System.out.print("Enter the parameter name: ");
                    String parameter = scanner.next();
                    System.out.print("Enter the value: ");
                    String value = scanner.next();
                    printerService.setConfig(parameter, value, ticket);
                }
            }
            Success("Printer operation performed successfully!");
        } while (!singleUse);
        return true;
    }

    //color coded status messages for user actions
    private static void Success(String msg) {System.out.println("\033[0;32m" + msg + "\033[0m");}
}
