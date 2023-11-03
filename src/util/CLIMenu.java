package util;

import server.User;

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

    public boolean selectOperation(IPrinterService printerService, Session session, boolean singleUse) throws RemoteException {
        do {
            System.out.println("Please choose one of the following operations (enter -1 to exit)");
            System.out.println("1: print(filename, printer)");
            System.out.println("2: queue(printer)");
            System.out.println("3: topQueue(printer, job)");
            System.out.println("4: start()");
            System.out.println("5: stop()");
            System.out.println("6: restart()");
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
                    printerService.print(filename, printer, session);
                }
                case 2 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.queue(printer, session);
                }
                case 3 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    System.out.print("Enter the job number: ");
                    int job = scanner.nextInt();
                    printerService.topQueue(printer, job, session);
                }
                case 4 -> {
                    printerService.start(session);
                }
                case 5 -> {
                    printerService.stop(session);
                }
                case 6 -> {
                    printerService.restart(session);
                }
                case 7 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.status(printer, session);
                }
                case 8 -> {
                    System.out.print("Enter the parameter name: ");
                    String parameter = scanner.next();
                    printerService.readConfig(parameter, session);
                }
                case 9 -> {
                    System.out.print("Enter the parameter name: ");
                    String parameter = scanner.next();
                    System.out.print("Enter the value: ");
                    String value = scanner.next();
                    printerService.setConfig(parameter, value, session);
                }
            }
        } while (!singleUse);
        return true;
    }

}
