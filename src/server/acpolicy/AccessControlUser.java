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

package server.acpolicy;

public class AccessControlUser {

    public String username;
    public boolean print;
    public boolean queue;
    public boolean topQueue;
    public boolean start;
    public boolean stop;
    public boolean restart;
    public boolean status;
    public boolean readConfig;
    public boolean setConfig;

    public AccessControlUser(String username) {
        this.username = username;
    }

}
