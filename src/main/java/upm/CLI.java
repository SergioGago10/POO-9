package upm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CLI {
    private static Scanner sc;

    public CLI(String[] args) {
        CLI.sc = createScanner(args);
    }

    public static String[] nextLine(boolean isInteractive) {
        String line = sc.nextLine().trim();
        if (isInteractive)
            System.out.println(line);
        String [] resul=line.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        return resul;
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static Scanner createScanner(String[] args) {
        Scanner scanner = null;
        try {
            if (args.length == 0)
                scanner = new Scanner(System.in);
            else {
                File file = new File(args[0]);
                scanner = new Scanner(file);
            }
        } catch (FileNotFoundException ex) {
            CLI.print(ex.getMessage());
            scanner = new Scanner(System.in);
        }
        return scanner;
    }
}
