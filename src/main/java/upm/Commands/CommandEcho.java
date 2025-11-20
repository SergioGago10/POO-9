package upm.Commands;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandEcho extends Command {


    // Regex para: echo "lo que sea"
    private static final Pattern ECHO_PATTERN =
            Pattern.compile("^echo\\s+\"(.*)\"\\s*$", Pattern.CASE_INSENSITIVE);

    public CommandEcho(String text) {
        super("echo");
    }

    @Override
    public boolean apply(String[] args) {
        if (args[0].equalsIgnoreCase(text)) {
            //No hay argumentos o está vacío, no nos importa
            if (args == null || args.length == 0) {
                return false;
            }

            // El primer token debe ser "echo"
            if (!args[0].equalsIgnoreCase("echo")) {
                return false;
            }

            // Reconstruimos la línea original a partir de los tokens
            String line = String.join(" ", args).trim();

            Matcher matcher = ECHO_PATTERN.matcher(line);
            if (!matcher.matches()) {
                // Formato incorrecto: echo "<texto>"
                // Devolvemos false para que el sistema lo trate como comando inválido
                return false;
            }

            String text = matcher.group(1);
            System.out.println(text);
            return true;
        } else {
            return false;
        }
    }
}

