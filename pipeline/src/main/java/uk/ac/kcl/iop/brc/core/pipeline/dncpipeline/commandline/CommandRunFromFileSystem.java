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
