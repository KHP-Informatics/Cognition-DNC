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

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class CommandCreateModeFromFile implements CommandProcessor {

    @Autowired
    public DNCPipelineService dncPipelineService;

    @Override
    public boolean isResponsibleFor(CommandLine cmd) {
        boolean createMode = cmd.hasOption("createMode");
        boolean file = cmd.hasOption("file");

        return createMode && file;
    }

    @Override
    public void process(CommandLine cmd) {
        String filePath = cmd.getOptionValue("file");

        if (StringUtils.isEmpty(filePath)) {
            throw new IllegalArgumentException("File argument must be specified.");
        }

        if (cmd.hasOption("noPseudonym")) {
            dncPipelineService.getCommandLineArgHolder().setNoPseudonym(true);
        }

        if (cmd.hasOption("instantOCR")) {
            dncPipelineService.getCommandLineArgHolder().setInstantOCR(true);
        }

        dncPipelineService.startCreateModeWithFile(filePath);
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("createMode")
                .withDescription("Application processes all records from scratch.").
                        withArgName("createMode").create());

        options.addOption(OptionBuilder.withLongOpt("file")
                .withDescription("Json file to process").hasOptionalArg().
                        withArgName("file").create());

        options.addOption(OptionBuilder.withLongOpt("instantOCR")
                .withDescription("Process OCR documents as soon as they arrive (By default, Cognition puts them " +
                        "in a separate queue and processes the queue last").hasOptionalArg().
                        withArgName("instantOCR").create());
    }

}
