package com.project.mgm.onlineBanking.common;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Input {
    private static Scanner scanner = new Scanner(System.in);

    public static int inputIntNumber(String inputMessage) {
        int number = 0;
        do {
            System.out.print(inputMessage);
            if(scanner.hasNextInt()) {
                number = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Enter Number!");
                continue;
            }
        }while(number <= 0);

        return number;
    }

    public static int inputIntNumberWithBoards(int lowerBoard, int upperBoard) {
        int number = 0;
        do {
            if(scanner.hasNextInt()) {
                number = scanner.nextInt();
            }else {
                scanner.next();
                System.out.println("Enter Number!\n");
                continue;
            }

            if(number<=lowerBoard || number>upperBoard) {
                System.out.println("Enter Right Command!\n");
            }
        }while(number<=lowerBoard || number > upperBoard);

        return number;
    }

    public static double inputDoubleNumber(String inputMessage) {
        double number = 0;
        do {
            System.out.print(inputMessage);
            if(scanner.hasNextDouble()) {
                number = scanner.nextDouble();
            } else {
                scanner.next();
                System.out.println("Enter Number!");
                continue;
            }
        }while(number <= 0);

        return number;
    }

    public static String inputString(String message) {
        System.out.print(message);
        String input = scanner.nextLine();

        return input;
    }

    public static String inputCommand() {
        String command = scanner.nextLine().toUpperCase();

        return command;
    }

    public static int checkIdInput(Map<Integer, String> banks) {
        int bId;
        Set<Integer> bankIds = banks.keySet();
        for(int id : bankIds) {
            System.out.println("(" + id + ") " + banks.get(id));
        }

        loop: while (true) {
            int bankId = inputIntNumber("");
            Input.inputString("");
            for(int id : bankIds) {
                if(bankId == id) {
                    bId = bankId;
                    break loop;
                }
            }

            System.out.println("Enter Right Command!");
        }

        return bId;
    }
}
