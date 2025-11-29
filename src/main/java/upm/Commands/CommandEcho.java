package upm.Commands;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandEcho extends Command {


    public CommandEcho() {
        super("echo");
    }

    @Override
    public boolean apply(String[] args) {
        // Validaciones básicas
        if (args == null || args.length < 2) {
            return false;
        }

        // El primer token debe ser "echo"
        if (!args[0].equalsIgnoreCase("echo")) {
            return false;
        }

        // Segundo argumento: texto con comillas
        String rawText = args[1];

        // Quitamos las comillas: "texto" → texto
        if (rawText.length() >= 2 && rawText.startsWith("\"") && rawText.endsWith("\"")) {
            String text = rawText.substring(1, rawText.length() - 1);
            System.out.println(text);
            return true;
        }

        // Si no viene con comillas, comando mal formado
        return false;
    }

}

