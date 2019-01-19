package com.project.mgm.onlineBanking.database;

import java.util.List;
import java.util.Scanner;

public class UseService {
    private static Scanner scanner = new Scanner(System.in);
    private static OperateDatabase databaseOperation = new OperateDatabase();

    public void openAccount() {
        List<String> banks = databaseOperation.getBankList();
        System.out.println("Choose Bank: ");
        for(int i=1; i<=banks.size(); i++) {
            System.out.println("(" + i + ") " + banks.get(i-1));
        }

        int inputNumber = 0;
        do {
            if(scanner.hasNextInt()) {
                inputNumber = scanner.nextInt();
            }else {
                scanner.next();
                System.out.println("Enter Number!");
                continue;
            }

            if(inputNumber <=0 || inputNumber > banks.size()) {
                System.out.println("Enter Right Command!");
            }
        }while(inputNumber <= 0 || inputNumber > banks.size());

        boolean isNotCorrect = true;
        String cardId;
        do {
            System.out.print("Card ID: ");
            cardId = scanner.nextLine();

            if(cardId.length() == 16) {
                for(int i=0; i<cardId.length(); i++) {
                    if(cardId.charAt(i) < 48 || cardId.charAt(i) > 57) {
                        System.out.println("Wrong Card ID!\n");
                        break;
                    }

                    if(i == cardId.length()-1) {
                        isNotCorrect = false;
                    }
                }
            }else {
                System.out.println("Wrong Card ID!\n");
            }
        }while(isNotCorrect);

        System.out.println("Request Send Successfully!");
        //sendAccountRequest(inputNumber, cardId);
    }
}
