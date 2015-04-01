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
import org.springframework.stereotype.Component;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

@Component
public class AppendModeFromFile implements CommandProcessor {

    @Override
    public boolean isResponsibleFor(CommandLine cmd) {
        boolean appendMode = cmd.hasOption("appendMode");
        boolean file = cmd.hasOption("file");

        return appendMode && file;
    }

    @Override
    public void process(CommandLine cmd) {
        throw new NotImplementedException();
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("appendMode")
                .withDescription("Application appends the latest binary entries as text.").
                        withArgName("appendMode").create());
    }

}
