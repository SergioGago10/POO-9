package upm.commands.client;

import upm.CLI;
import upm.commands.core.Command;
import upm.users.Client;
import upm.users.TypeClient;
import upm.users.UserManager;

public class ClientAddCommand extends Command {
    public ClientAddCommand() {
        super("add");
    }

    @Override
    public boolean apply(String[] args) {
        UserManager userManager = UserManager.getInstance();

        if (args.length < 6) {
            CLI.printErrorNextLine("Error -> Format must be: client add \"<nombre>\" (<DNI>|<NIF>) <email> <cashId>");
            return true;
        }

        String rawName = args[2];
        if (!(rawName.startsWith("\"") && rawName.endsWith("\""))) {
            CLI.printErrorNextLine("Error -> The name must be enclosed in quotes.");
            return true;
        }

        try {
            for (int i = 0; i < args.length; i++) {
                args[i] = args[i]
                        .replace("\"", "")
                        .replace("“", "")
                        .replace("”", "")
                        .trim();
            }

            String name = args[2];
            String identificator = args[3];
            String email = args[4];
            String cashierId = args[5];
            TypeClient type;

            if(!isNifValid(identificator)){
                CLI.printErrorNextLine("Error -> the NIF/DNI you entered is invalid.");
                CLI.printErrorNextLine("Check this page for more information: https://es.wikipedia.org/wiki/N%C3%BAmero_de_identificaci%C3%B3n_fiscal");
                return true;
            }


            if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
                CLI.printErrorNextLine("Error -> Invalid email format.");
                return true;
            }

            if (!userManager.idExists(cashierId)) {
                CLI.printErrorNextLine("Error -> Cashier ID does not exist.");
                return true;
            }

            if (Character.isDigit(identificator.charAt(identificator.length() - 1))) {
                type = TypeClient.COMPANY;
            } else {
                type = TypeClient.CLIENT;
            }

            Client client = new Client(name, identificator, email, cashierId,type);

            if (!userManager.addClient(client)) {
                CLI.printErrorNextLine("Error -> Client could not be added.");
                return true;
            }

            CLI.printNextLine(client.toString());
            CLI.printNextLine("client add: ok");
        } catch (Exception ex) {
            CLI.printErrorNextLine("Error -> client could not be added: " + ex.getMessage());
        }

        return true;
    }


    public static boolean isNifValid(String nif) {
        if (nif == null || nif.length() != 9) return false;

        nif = nif.toUpperCase();
        char firstChar = nif.charAt(0);

        // DNI o NIE (Personas físicas)
        // Empieza por número o por X, Y, Z
        if (Character.isDigit(firstChar) || "XYZ".indexOf(firstChar) != -1) {
            return validateDNIorNIE(nif);
        }

        // Personas Jurídicas (Empresas - Antiguo CIF)
        // Empieza por A, B, C, D, E, F, G, H, J, P, Q, R, S, U, V, N, W
        if ("ABCDEFGHJPQRSUVNW".indexOf(firstChar) != -1) {
            return validateCIF(nif);
        }

        return false;
    }

    private static boolean validateDNIorNIE(String nif) {
        String secuenciaLetrasNIF = "TRWAGMYFPDXBNJZSQVHLCKE";
        String numeroNIF = nif.substring(0, 8)
                .replace("X", "0")
                .replace("Y", "1")
                .replace("Z", "2");

        try {
            int num = Integer.parseInt(numeroNIF);
            char letraEsperada = secuenciaLetrasNIF.charAt(num % 23);
            return nif.charAt(8) == letraEsperada;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean validateCIF(String cif) {
        try {
            String digits = cif.substring(1, 8);
            int sumaPares = 0;
            int sumaImpares = 0;

            for (int i = 0; i < digits.length(); i++) {
                int n = Character.getNumericValue(digits.charAt(i));
                if ((i + 1) % 2 == 0) {
                    sumaPares += n;
                } else {
                    int multi = n * 2;
                    sumaImpares += (multi > 9) ? (multi - 9) : multi;
                }
            }

            int total = sumaPares + sumaImpares;
            int unidad = total % 10;
            int digitoControlEsperado = (unidad == 0) ? 0 : (10 - unidad);

            char lastChar = cif.charAt(8);

            // El dígito de control puede ser un número o una letra según el tipo de empresa
            if (Character.isDigit(lastChar)) {
                return Character.getNumericValue(lastChar) == digitoControlEsperado;
            } else {
                // Conversión de número a letra (1=A, 2=B, etc.)
                char letraControlEsperada = (char) ('A' + (digitoControlEsperado - 1));
                // Nota: Algunas entidades usan J para el 0
                if (digitoControlEsperado == 0) letraControlEsperada = 'J';
                return lastChar == letraControlEsperada;
            }
        } catch (Exception e) {
            return false;
        }
    }

}


