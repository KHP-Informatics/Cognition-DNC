/*
        Copyright (c) 2015 King's College London

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
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
                "Create mode\n" +
                "--------------------\n" +
                "From jSON file:     --createMode --file=/path/to/file\n" +
                "From view/table     --createMode --coordinatesFromDB\n" +

                "\n\nAdd --noPseudonym argument to skip pseudonymisation." +
                "\n\nAdd --skipN=? argument to skip the first N coordinates.");

    }


}
