package ru.nc.gordeev.logparser; /**
 * Created by Sovereign on 04.11.2016.
 */


import java.util.Scanner;


public class LogParser {
    public static void main(String[] args) {
        System.out.println("Greetings!\nType \"help\" for available commands.");
        Scanner scanner = new Scanner(System.in);
        String line;
        interaction: while (true) {
            System.out.println("You're in the main section.");
            line = scanner.nextLine();
            switch (line.toLowerCase()) {
                case "help":
                    System.out.println("Type:\n/path/ - specify the path to parse a .log file and put it in the library;\n" +
                            "count - to enter the count section;\n"+
                            "print - to enter print section;\n" +
                            "exit - to finish the session.");
                    break;
                case "count":
                    System.out.println("You're in the count section.\nType \"help\" for available commands. ");
                    count: while (true){
                        line = scanner.nextLine();
                        switch (line.toLowerCase()) {
                            case "help":
                                System.out.println("Type:\n/path/ - specify the file to count lines in (when parsed into library);\n" +
                                    "all - to count the number of files in the library;\n" +
                                    "print - to print paths to all the parsed files in the library;;\n" +
                                    "back - to return to the main section;\n" +
                                    "exit - to finish the session.");
                                break;
                            case "all":
                                System.out.println("The number of files in the library is: " + StorageTEST.INSTANCE.countFiles());
                                break;
                            case "print":
                                StorageTEST.INSTANCE.showAll();
                                break;
                            case "back":
                                break count;
                            case "exit":
                                System.out.println("Good bye!");
                                break interaction;
                            default:
                                System.out.println("The number of lines in the " +line+" is: "+StorageTEST.INSTANCE.countLines(line.toLowerCase()));
                        }
                    }
                    break;
                case "print":
                    System.out.println("You're in the print section.\nType \"help\" for available commands. ");
                    print: while (true){
                        line = scanner.nextLine();
                        switch (line.toLowerCase()) {
                            case "help":
                                System.out.println("Type:\n/path/ - specify the file to print onto the console (when parsed into library);\n" +
                                        "all - to print paths to all the parsed files in the library;\n" +
                                        "back - to return to the main section;\n" +
                                        "exit - to finish the session.");
                                break;
                            case "all":
                                StorageTEST.INSTANCE.showAll();
                                break;
                            case "back":
                                break print;
                            case "exit":
                                System.out.println("Good bye!");
                                break interaction;
                            default:
                                StorageTEST.INSTANCE.show(line.toLowerCase());
                        }
                    }
                    break;
                case "exit":
                    System.out.println("Good bye!");
                    break interaction;
                default:{
                    LogFile file =LogDecoder.decodeLogFile(line.toLowerCase());
                    StorageTEST.INSTANCE.insert(file);
                    System.out.println(file.getPath()+ " has been put in the library.");
                }
            }
        }
    }
}
