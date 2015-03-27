/*
        The MIT License (MIT)
        Copyright (c) 2015 King's College London

        Permission is hereby granted, free of charge, to any person obtaining a copy
        of this software and associated documentation files (the "Software"), to deal
        in the Software without restriction, including without limitation the rights
        to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
        copies of the Software, and to permit persons to whom the Software is
        furnished to do so, subject to the following conditions:

        The above copyright notice and this permission notice shall be included in
        all copies or substantial portions of the Software.

        THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
        IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
        FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
        AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
        LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
        OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
        THE SOFTWARE.
*/

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline;

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CreateModeFromWebService implements CommandProcessor {

    @Autowired
    private DNCPipelineService dncPipelineService;

    @Override
    public boolean isResponsibleFor(CommandLine cmd) {
        boolean createMode = cmd.hasOption("createMode");
        boolean webService = cmd.hasOption("webService");

        return createMode && webService;
    }

    @Override
    public void process(CommandLine cmd) {
        dncPipelineService.startCreateModeFromWS();
    }

    @Override
    public void addOption(Options options) {
        options.addOption(OptionBuilder.withLongOpt("webService")
                .withDescription("Application gets jobs from the web service.").
                        withArgName("webService").create());
    }

}
