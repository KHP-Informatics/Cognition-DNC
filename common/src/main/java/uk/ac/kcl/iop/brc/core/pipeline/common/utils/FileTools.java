/*
        Cognition-DNC (Dynamic Name Concealer)
        Binary to text document converter and database pseudonymiser.

        Copyright (C) 2015 Ismail E. Kartoglu

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


package uk.ac.kcl.iop.brc.core.pipeline.common.utils;

import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FileTools {

    private static Logger logger = Logger.getLogger(FileTools.class);

    public static void deleteFiles(File...files) {
        for (File file : files) {
            try {
                file.deleteOnExit();
                org.apache.commons.io.FileUtils.forceDelete(file);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        }
    }
}
