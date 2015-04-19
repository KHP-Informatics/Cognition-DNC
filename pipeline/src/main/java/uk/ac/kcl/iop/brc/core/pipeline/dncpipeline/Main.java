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

package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline.CommandHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline.CommandProcessor;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Map;

public class Main {

    private static ClassPathXmlApplicationContext context;

    private static Logger logger = Logger.getLogger(Main.class);

    /**
     * Entry point of Cognition-DNC
     */
    public static void main(String[] args) {
        if (requiresHelp(args)) {
            CommandHelper.printHelp();
            System.exit(0);
        }

        String path = "file:" + getCurrentFolder() + "/config/applicationContext.xml";

        context = new ClassPathXmlApplicationContext(path);

        Options options = getOptions();
        CommandLineParser parser = new GnuParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            processCommands(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCurrentFolder() {
        CodeSource codeSource = Main.class.getProtectionDomain().getCodeSource();
        File jarFile;
        try {
            jarFile = new File(codeSource.getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return "";
        }
        return jarFile.getParentFile().getPath();
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
