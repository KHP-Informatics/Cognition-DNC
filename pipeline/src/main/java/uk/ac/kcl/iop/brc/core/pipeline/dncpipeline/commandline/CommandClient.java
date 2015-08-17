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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.CoordinatorClientService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.CoordinatorService;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;

@Component
public class CommandClient implements CommandProcessor {

    @Autowired
    private CoordinatorClientService clientService;

    @Autowired
    private DNCPipelineService dncPipelineService;

    @Override
    public boolean isResponsibleFor(CommandLine cmd) {
        boolean client = cmd.hasOption("client");
        boolean serverAddress = cmd.hasOption("server");

        return client && serverAddress;
    }

    @Override
    public void process(CommandLine cmd) {
        clientService.setServerAddress(cmd.getOptionValue("server"));
        if (cmd.hasOption("cognitionName")) {
            clientService.setCognitionName(cmd.getOptionValue("cognitionName"));
        }
        if (cmd.hasOption("chunkSize")) {
            String chunkSize = cmd.getOptionValue("chunkSize");
            if (! StringUtils.isNumeric(chunkSize)) {
                throw new IllegalArgumentException("The specified chunkSize must be an integer.");
            }
            clientService.setChunkSize(chunkSize);
        }
        clientService.startProcessing();
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("client")
                        .withDescription("Application runs as a client of a coordinator web server.")
                        .withArgName("client").create())
                .addOption(OptionBuilder.withLongOpt("server")
                                .withDescription("The address of the coordinator server")
                                .hasArg()
                                .withArgName("server").create()
                ).addOption(OptionBuilder.withLongOpt("cognitionName")
                        .withDescription("Cognition name of the computer")
                        .hasArg()
                        .withArgName("cognitionName").create()
                ).addOption(OptionBuilder.withLongOpt("chunkSize")
                        .withDescription("Number of work coordinates to ask from server.")
                        .hasArg()
                        .withArgName("chunkSize").create());
    }

}
