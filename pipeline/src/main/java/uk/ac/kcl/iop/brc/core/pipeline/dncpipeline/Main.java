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


package uk.ac.kcl.iop.brc.core.pipeline.dncpipeline;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.log4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline.CommandHelper;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.commandline.CommandProcessor;
import uk.ac.kcl.iop.brc.core.pipeline.dncpipeline.service.DNCPipelineService;

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
        printGNULicense();

        if (requiresHelp(args)) {
            CommandHelper.printHelp();
            System.exit(0);
        }

        String path = "file:" + getCurrentFolder() + File.separator + "config" + File.separator + "applicationContext.xml";
        logger.info("Loading context from " + path);

        context = new ClassPathXmlApplicationContext(path);

        Options options = getOptions();
        CommandLineParser parser = new GnuParser();

        Runtime.getRuntime().addShutdownHook(getShutDownBehaviour());
        try {
            CommandLine cmd = parser.parse(options, args);
            processCommands(cmd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printGNULicense() {
        System.out.println("Cognition-DNC  Copyright (C) 2015  Ismail E. Kartoglu\n" +
                "    This program comes with ABSOLUTELY NO WARRANTY.\n" +
                "    This is free software, and you are welcome to redistribute it\n" +
                "    under certain conditions. Read GNU General Public License 3.0 for details.");
    }

    private static Thread getShutDownBehaviour() {
        return new Thread(() -> {
            DNCPipelineService dncPipelineService = context.getBean(DNCPipelineService.class);
            dncPipelineService.dumpFailedCoordinates();
        });
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
