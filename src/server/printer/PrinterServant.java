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

package server.printer;

import server.acpolicy.AccessManager;
import server.sessions.ICredentialManager;
import shared.IAuthCredential;
import shared.IPrinterService;
import util.InvalidAccessException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class PrinterServant extends UnicastRemoteObject implements IPrinterService {

    private final ICredentialManager credentialManager;
    private final AccessManager accessManager;

    public PrinterServant(ICredentialManager credentialManager, AccessManager accessManager) throws RemoteException, InvalidAccessException {
        super();
        this.credentialManager = credentialManager;
        this.accessManager =  accessManager;

    }

    @Override
    public void print(String filename, String printer, IAuthCredential credential) throws RemoteException, InvalidAccessException {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.PRINT)) return;
        System.out.printf("|> %s called print(%s,%s)%n", credential.getUsername(), filename, printer);
    }

    @Override
    public void queue(String printer, IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.QUEUE)) return;
        System.out.printf("|> %s called queue(%s)%n", credential.getUsername(), printer);
    }

    @Override
    public void topQueue(String printer, int job, IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.TOPQUEUE)) return;
        System.out.printf("|> %s called topQueue(%s, %d)%n", credential.getUsername(), printer, job);
    }

    @Override
    public void start(IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.START)) return;
        System.out.printf("|> %s started printer%n", credential.getUsername());
    }

    @Override
    public void restart(IAuthCredential credential) throws RemoteException, InvalidAccessException {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.RESTART)) return;
        System.out.printf("|> %s restarted printer%n", credential.getUsername());
    }

    @Override
    public void stop(IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.STOP)) return;
        System.out.printf("|> %s stopped printer%n", credential.getUsername());
    }

    @Override
    public void status(String printer, IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.STATUS)) return;
        System.out.printf("|> %s called status(%s)%n", credential.getUsername(), printer);
    }

    @Override
    public void readConfig(String parameter, IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.READCONFIG)) return;
        System.out.printf("|> %s called readConfig(%s)%n", credential.getUsername(), parameter);
    }

    @Override
    public void setConfig(String parameter, String value, IAuthCredential credential) throws RemoteException, InvalidAccessException  {
        if (!credentialManager.validateCredential(credential)) return;
        if (!accessManager.verifyAccess(credential.getUsername(), Operation.SETCONFIG)) return;
        System.out.printf("|> %s called setConfig(%s, %s)%n", credential.getUsername(), parameter, value);
    }
}
