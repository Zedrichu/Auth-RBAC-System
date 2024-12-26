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
package server;

import server.acpolicy.AccessManager;
import server.printer.PrinterServant;
import server.sessions.ICredentialManager;
import server.sessions.SessionManager;
import shared.IPrinterService;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

public class AppServer {
   private static final Logger logger = Logger.getLogger(AppServer.class.getName());

   private static final int REGISTRY_PORT = 8035;
   private static IPrinterService printer;
   private static Registry registry;

   public static void main(String[] args) throws RemoteException {
      System.out.println("Initializing the RMI objects...");
      ICredentialManager credentialManager = ICredentialManager.create("token");
      AccessManager accessManager = new AccessManager();
      printer = new PrinterServant(credentialManager, accessManager);

      System.out.println("Creating RMI registry on port " + REGISTRY_PORT);
      registry = LocateRegistry.createRegistry(REGISTRY_PORT);

      System.out.println("Rebinding SessionProvider and Printer services to RMI route-names.");
      registry.rebind(ICredentialManager.routeName, credentialManager);
      registry.rebind(IPrinterService.routeName, printer);

   }
}
