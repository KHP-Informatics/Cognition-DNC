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
