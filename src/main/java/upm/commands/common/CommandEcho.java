package upm.commands.common;


import upm.CLI;
import upm.commands.core.Command;

public class CommandEcho extends Command {


    public CommandEcho() {
        super("echo");
    }

    @Override
    public boolean apply(String[] args) {
        if (args == null || args.length < 2) {
            CLI.printErrorNextLine("Error -> Format must be: echo \"<text>\"");
            return true;
        }

        String rawText = args[1];

        if (rawText.length() >= 2 && rawText.startsWith("\"") && rawText.endsWith("\"")) {
            String text = rawText.substring(1, rawText.length() - 1);
            CLI.printNextLine(text);
        } else {
         CLI.printErrorNextLine("Error -> Format must be: echo \"<text>\"");
        }
        return true;
    }

}

