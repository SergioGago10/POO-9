package upm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class CLI {
    private static Scanner sc;
    private final boolean isInteractive;
    private static final String RED = "\u001B[31m";
    private static final String RESET = "\u001B[0m";

    public CLI(String[] args) {
        this.isInteractive = (args.length == 0);
        CLI.sc = createScanner(args);
        sc.useLocale(Locale.US);
    }

    public String[] nextLine() {
        String line = sc.nextLine().trim();
        if (!this.isInteractive){
            System.out.println(line);
            System.out.flush();
        }

        return line.split(" +(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    }

    public static void printNextLine(String message) {
        System.out.println(message);
    }
    public static void print(String message) {
        System.out.print(message);
    }
    public static void printFormat(String format, Object ... args) {
        System.out.printf(format, args);
    }
    public static void printError(String message) {
        System.out.print(RED + message + RESET);
    }
    public static void printErrorNextLine(String message) {
        System.out.println(RED + message + RESET);
    }

    public static Scanner createScanner(String[] args) {
        Scanner scanner;
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

    public static void closeSc() {
        sc.close();
    }
}
