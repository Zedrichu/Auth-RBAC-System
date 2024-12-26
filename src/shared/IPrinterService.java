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

package shared;

import util.InvalidAccessException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IPrinterService extends Remote {
    String routeName = "printer";

    void print(String filename, String printer, IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void queue(String printer, IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void topQueue(String printer, int job, IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void start(IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void stop(IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void restart(IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void status(String printer, IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void readConfig(String parameter, IAuthCredential credential) throws RemoteException, InvalidAccessException;
    void setConfig(String parameter, String value, IAuthCredential credential) throws RemoteException, InvalidAccessException;

}
