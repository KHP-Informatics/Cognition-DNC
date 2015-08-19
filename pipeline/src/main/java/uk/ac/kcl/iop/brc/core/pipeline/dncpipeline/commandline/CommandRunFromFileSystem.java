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

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.CoordinatorClientService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class CommandRunFromFileSystem implements CommandProcessor {

    private static Logger logger = Logger.getLogger(CommandRunFromFileSystem.class);

    @Autowired
    private DNCPipelineService dncPipelineService;

    private String folderAbsolutePath;

    @Override
    public boolean isResponsibleFor(CommandLine cmd) {
        return cmd.hasOption("folder");
    }

    @Override
    public void process(CommandLine cmd) {
        folderAbsolutePath = cmd.getOptionValue("folder");
        File folder = new File(folderAbsolutePath);
        String[] files = folder.list();
        List<String> fileList = new ArrayList<>();
        fileList.addAll(Arrays.asList(files));

        fileList.parallelStream().forEach(file -> {
            String absoluteFilePath = folderAbsolutePath + file;
            logger.info("Processing " + absoluteFilePath);
            dncPipelineService.processFile(absoluteFilePath);
        });

        logger.info("Finished all files in directory " + folderAbsolutePath);
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("folder")
                                .withDescription("The absolute path of the folder in which binary files reside")
                                .hasArg()
                                .withArgName("folder").create());
    }

}
