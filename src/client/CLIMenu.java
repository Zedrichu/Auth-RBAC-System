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

package client;

import shared.IAuthCredential;
import shared.IPrinterService;
import util.InvalidAccessException;

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

    public boolean selectOperation(IPrinterService printerService, IAuthCredential sessionTicket, boolean singleUse) throws RemoteException, InvalidAccessException {
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
                    printerService.print(filename, printer, sessionTicket);

                }
                case 2 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.queue(printer, sessionTicket);
                }
                case 3 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    System.out.print("Enter the job number: ");
                    int job = scanner.nextInt();
                    printerService.topQueue(printer, job, sessionTicket);
                }
                case 4 -> {
                    printerService.start(sessionTicket);
                }
                case 5 -> {
                    printerService.restart(sessionTicket);
                }
                case 6 -> {
                    printerService.stop(sessionTicket);
                }
                case 7 -> {
                    System.out.print("Enter the printer name: ");
                    String printer = scanner.next();
                    printerService.status(printer, sessionTicket);
                }
                case 8 -> {
                    System.out.print("Enter the parameter name: ");
                    String parameter = scanner.next();
                    printerService.readConfig(parameter, sessionTicket);
                }
                case 9 -> {
                    System.out.print("Enter the parameter name: ");
                    String parameter = scanner.next();
                    System.out.print("Enter the value: ");
                    String value = scanner.next();
                    printerService.setConfig(parameter, value, sessionTicket);
                }
            }
            Success("Printer operation performed successfully!");
        } while (!singleUse);
        return true;
    }

    //color coded status messages for user actions
    private static void Success(String msg) {System.out.println("\033[0;32m" + msg + "\033[0m");}
}
