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
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
