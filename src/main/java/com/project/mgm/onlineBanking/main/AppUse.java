package com.project.mgm.onlineBanking.main;

import com.project.mgm.onlineBanking.database.OperateDatabase;

import java.util.List;
import java.util.Scanner;

public class AppUse {
    private static Scanner scanner = new Scanner(System.in);
    private static OperateDatabase databaseOperation = new OperateDatabase();
    private int userId;

    public AppUse(int userId) {
        this.userId = userId;
    }

    /*public int getUserId() {
        return userId;
    }*/

    public void cooperatedUser() {
        while(true) {
            System.out.println("\n");
            System.out.println("(MB) Show My Bank List");
            System.out.println("(MS) Show My Service List");
            System.out.println("(AB) Show Available Bank List");
            System.out.println("(AS) Show Available Service List");
            System.out.println("(B) Show Balance(s)");
            System.out.println("(U) To Make Use of Service");
            System.out.println("(C) Cooperate with Bank");
            System.out.println("(Q) Quit");

            String input = scanner.next().toUpperCase();
            switch (input) {
                case "MB":
                    databaseOperation.showUserBankList(userId);
                    break;
                case "MS":
                    databaseOperation.showUserServiceList(userId);
                    break;
                case "AB":
                    databaseOperation.showBankList();
                    break;
                case "AS":
                    databaseOperation.showServiceList();
                    break;
                case "B":
                    loop1: while(true) {
                        System.out.println();
                        System.out.println("(C) Show Balance of Concrete Bank");
                        System.out.println("(A) All");

                        input = scanner.next().toUpperCase();
                        switch (input) {
                            case "C":
                                System.out.println("Choose Bank: ");
                                List<String> userBanks = databaseOperation.getUserBankList(userId);
                                int inputNumber = 0;
                                do {
                                    for(int i=1; i<=userBanks.size(); i++) {
                                        System.out.println("(" + i + ")" + userBanks.get(i-1));
                                    }

                                    if(scanner.hasNextInt()) {
                                        inputNumber = scanner.nextInt();
                                    }else {
                                        scanner.next();
                                        System.out.println("Enter Number!\n");
                                        continue;
                                    }

                                    if(inputNumber <= 0 || inputNumber > userBanks.size()) {
                                        System.out.println("Enter Right Command!\n");
                                    }
                                }while(inputNumber<=0);

                                databaseOperation.showBalance(userId, userBanks.get(inputNumber-1));
                                break loop1;
                            case "A":
                                databaseOperation.showBalance(userId);
                                break loop1;
                            default:
                                System.out.println("Enter Right Command!\n");
                        }
                    }
                    break;
                case "U":
                    break;
                case "C":
                    //cooperate();
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }

    public void notCooperatedUser() {
        while(true) {
            System.out.println("\n");
            System.out.println("(B) Show Available Bank List");
            System.out.println("(S) Show Available Service List");
            System.out.println("(C) Cooperate with Bank");
            System.out.println("(Q) Quit");

            String input = scanner.next().toUpperCase();
            switch (input) {
                case "B":
                    databaseOperation.showBankList();
                    break;
                case "S":
                    databaseOperation.showServiceList();
                    break;

                case "C":
                    //coopeate();
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }
}
