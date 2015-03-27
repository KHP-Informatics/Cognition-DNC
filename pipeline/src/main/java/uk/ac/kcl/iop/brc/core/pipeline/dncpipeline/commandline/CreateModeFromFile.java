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
import org.springframework.util.StringUtils;

@Component
public class CreateModeFromFile implements CommandProcessor {

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
    }

}
