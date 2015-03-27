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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline;

import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline.CommandHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline.CommandProcessor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class Main {

    private static ClassPathXmlApplicationContext context;

    private static Logger logger = Logger.getLogger(Main.class);

    /**
     * Entry point of APR (Anonymisation Pipeline Runner)
     */
    public static void main(String[] args) {
        if (requiresHelp(args)) {
            CommandHelper.printHelp();
            System.exit(0);
        }
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        Options options = getOptions();
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            processCommands(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Finds the correct command processor for the given command and executes it.
     * @param cmd Commands given from the command line
     */
    private static void processCommands(CommandLine cmd) {
        Map<String, CommandProcessor> commands = context.getBeansOfType(CommandProcessor.class);
        for (CommandProcessor commandProcessor : commands.values()) {
            if (commandProcessor.isResponsibleFor(cmd)) {
                commandProcessor.process(cmd);
                return;
            }
        }
        logger.error("No valid command was given.");
        CommandHelper.printHelp();
    }

    /**
     * @return The available commands that can be processed.
     */
    public static Options getOptions() {
        Map<String, CommandProcessor> commands = context.getBeansOfType(CommandProcessor.class);
        Options options = new Options();

        for (CommandProcessor commandProcessor : commands.values()) {
            commandProcessor.addOption(options);
        }

        return options;
    }

    private static boolean requiresHelp(String[] args) {
        if (args == null) {
            return true;
        }

        for (String arg : args) {
            if (arg.contains("help")) {
                return true;
            }
        }

        return false;
    }

}
