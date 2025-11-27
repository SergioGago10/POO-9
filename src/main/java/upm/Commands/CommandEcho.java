package upm.Commands;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandEcho extends Command {


    public CommandEcho(String text) {
        super(text);
    }

    @Override
    public boolean apply(String[] args) {
        // Validaciones
        if (args == null || args.length < 2) {
            return false;
        }

        // Primero echo
        if (!args[0].equalsIgnoreCase("echo")) {
            return false;
        }

        // Segundo argumento: texto con comillas
        String rawText = args[1];

        // Quitamos las comillas: "texto"
        if (rawText.length() >= 2 && rawText.startsWith("\"") && rawText.endsWith("\"")) {
            String text = rawText.substring(1, rawText.length() - 1);
            System.out.println(text);
            return true;
        }

        // Sin comillas es falso
        return false;
    }

}

