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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.CoordinatorService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;

@Component
public class CommandCoordinator implements CommandProcessor {

    @Autowired
    public CoordinatorService coordinatorService;

    @Override
    public boolean isResponsibleFor(CommandLine cmd) {
        return cmd.hasOption("coordinator");
    }

    @Override
    public void process(CommandLine cmd) {
        if (cmd.hasOption("chunkSize")) {
            coordinatorService.setChunkSize(Integer.valueOf(cmd.getOptionValue("chunkSize")));
        }
        coordinatorService.startServer();
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("coordinator")
                        .withDescription("Application runs as coordinator web server.")
                        .withArgName("coordinator").create())
                .addOption(OptionBuilder.withLongOpt("chunkSize")
                                .withDescription("Number of work coordinates to ask from server.")
                                .hasArg()
                                .withArgName("chunkSize").create());
    }

}
