/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu, Richard G. Jackson

        This program is free software: you can redistribute it and/or modify
        it under the terms of the GNU General Public License as published by
        the Free Software Foundation, either version 3 of the License, or
        (at your option) any later version.

        This program is distributed in the hope that it will be useful,
        but WITHOUT ANY WARRANTY; without even the implied warranty of
        MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
        GNU General Public License for more details.

        You should have received a copy of the GNU General Public License
        along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline;

public class CommandHelper {

    public static void printHelp() {
        System.out.println(
                "*************************************************************\n" +
                "\n" +
                "    Cognition-DNC (Dynamic Name Concealer)\n" +
                "    Ismail E. Kartoglu\n" +
                "    ismail.e.kartoglu@kcl.ac.uk\n\n" +
                "    Richard Jackson\n" +
                "    richard.r.jackson@kcl.ac.uk\n" +
                "\n" +
                "*************************************************************\n" +
                "\n");

        System.out.println(
                "Usage:\n" +
                "===============================\n" +
                "\n" +
                "Standalone create mode\n" +
                "--------------------\n" +
                "From jSON file:     --createMode --file=/path/to/file\n" +
                "From view/table     --createMode --coordinatesFromDB\n\n" +

                "Coordinator (web server assigning coordinates to clients)\n" +
                "--------------------\n" +
                "From view/table     --coordinator\n" +

                "Clients (clients requesting coordinates from the server)\n" +
                "--------------------\n" +
                "From view/table     --client --server=http://ip_of_coordinator:4567 --cognitionName=ClientName01" +

                "\n\nAdd --noPseudonym argument to skip pseudonymisation.");

    }


}
