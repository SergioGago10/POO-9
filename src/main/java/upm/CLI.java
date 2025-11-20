package upm;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CLI {
    private Scanner sc;
    public CLI(String[] args) {
         this.sc = createScanner(args);
    }

    public String [] nextLine(){
        String line= sc.nextLine();
        return line.split(" +(?=([^\"]\"[^\"]\")[^\"]$)");
    }

    public static void print(String message){
        System.out.println(message);
    }

    private Scanner createScanner(String[] args) {
        Scanner scanner = null;
        try {
            if (args.length == 0)
                scanner = new Scanner(System.in);
            else {
                File file = new File(args[0]);
                scanner = new Scanner(file);
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        }
        return scanner;
    }
}
