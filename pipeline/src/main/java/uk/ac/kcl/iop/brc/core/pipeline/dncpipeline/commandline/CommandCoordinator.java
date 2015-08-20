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
        coordinatorService.startServer();
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("coordinator")
                        .withDescription("Application runs as coordinator web server.")
                        .withArgName("coordinator").create());
    }

}
