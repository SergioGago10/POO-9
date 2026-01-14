package upm.commands.common;


import upm.CLI;
import upm.commands.core.Command;

public class CommandEcho extends Command {


    public CommandEcho() {
        super("echo");
    }

    @Override
    public boolean apply(String[] args) {
        // Validaciones básicas
        if (args == null || args.length < 2) {
            return true;
        }

        // El primer token debe ser "echo"
        if (!args[0].equalsIgnoreCase("echo")) {
            return true;
        }

        // Segundo argumento: texto con comillas
        String rawText = args[1];

        // Quitamos las comillas: "texto" → texto
        if (rawText.length() >= 2 && rawText.startsWith("\"") && rawText.endsWith("\"")) {
            String text = rawText.substring(1, rawText.length() - 1);
            CLI.printNextLine(text);
            return true;
        }

        // Si no viene con comillas, comando mal formado
        return true;
    }

}

