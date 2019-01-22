package com.project.mgm.onlineBanking.main;

import com.project.mgm.onlineBanking.database.BankOperateDatabase;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;
import com.project.mgm.onlineBanking.service.UseService;
import java.util.List;
import java.util.Scanner;

public class AppController {
    private static Scanner scanner = new Scanner(System.in);
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();
    private static BankOperateDatabase bankDatabaseOperation = new BankOperateDatabase();
    private static UseService useService = new UseService();
    private int id;

    public AppController(int id) {
        this.id = id;
    }

    /*public int getId() {
        return id;
    }*/

    public void cooperatedUser() {
        while(true) {
            System.out.println();
            System.out.println("(MB) Show My Bank List");
            System.out.println("(MS) Show My Service List");
            System.out.println("(AB) Show Available Bank List");
            System.out.println("(AS) Show Available Service List");
            System.out.println("(B) Show Balance(s)");
            System.out.println("(U) To Make Use of Service");
            System.out.println("(O) Open Account");
            System.out.println("(R) Show Responses");
            System.out.println("(Q) Quit");

            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "MB":
                    userDatabaseOperation.showUserBankList(id);
                    break;
                case "MS":
                    userDatabaseOperation.showUserServiceList(id);
                    break;
                case "AB":
                    userDatabaseOperation.showBankList();
                    break;
                case "AS":
                    userDatabaseOperation.showServiceList();
                    break;
                case "B":
                    loop1: while(true) {
                        System.out.println();
                        System.out.println("(C) Show Balance of Concrete Bank");
                        System.out.println("(A) All");

                        input = scanner.nextLine().toUpperCase();
                        switch (input) {
                            case "C":
                                System.out.println("Choose Bank: ");
                                List<String> userBanks = userDatabaseOperation.getUserBankList(id);
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
                                }while(inputNumber<=0 || inputNumber > userBanks.size());

                                userDatabaseOperation.showBalance(id, userBanks.get(inputNumber-1));
                                break loop1;
                            case "A":
                                userDatabaseOperation.showBalance(id);
                                break loop1;
                            default:
                                System.out.println("Enter Right Command!\n");
                        }
                    }
                    break;
                case "U":
                    break;
                case "O":
                    useService.openAccount(id, true);
                    break;
                case "R":
                    userDatabaseOperation.showResponse(id);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter Right Command!");
            }
        }
    }

    public void notCooperatedUser() {
        boolean isCooperatedUser = false;
        while(true) {
            System.out.println();
            System.out.println("(B) Show Available Bank List");
            System.out.println("(S) Show Available Service List");
            System.out.println("(O) Open Account");
            System.out.println("(R) Show Responses");
            System.out.println("(Q) Quit");

            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "B":
                    userDatabaseOperation.showBankList();
                    break;
                case "S":
                    userDatabaseOperation.showServiceList();
                    break;
                case "O":
                    if(userDatabaseOperation.isAccountPresent(id)) {
                        isCooperatedUser = true;
                    }
                    useService.openAccount(id, isCooperatedUser);
                    break;
                case "R":
                    userDatabaseOperation.showResponse(id);
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }

    public void bankMain() {
        while(true) {
            System.out.println();
            System.out.println("(SA) Show All User List");
            System.out.println("(SL) Show Loaned User List");
            System.out.println("(SD) Show Deposit List");
            System.out.println("(SR) Show Requests");
            System.out.println("(Q) Quit");

            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "SA":
                    bankDatabaseOperation.showAllUserList(id);
                    break;
                case "SL":
                    bankDatabaseOperation.showLoanedUserList(id);
                    break;
                case "SD":
                    bankDatabaseOperation.showDepositList(id);
                    break;
                case "SR":
                    List<Integer> userIds = bankDatabaseOperation.showAccountRequests(id);

                    if(userIds.size() != 0) {
                        loop: while(true) {
                            System.out.println("(SR) Send Response");
                            System.out.println("(Q) Quit");

                            input = scanner.nextLine().toUpperCase();
                            switch (input) {
                                case "SR":
                                    bankDatabaseOperation.createResponse(id, userIds);
                                    break loop;
                                case "Q":
                                    break loop;
                                default:
                                    System.out.println("Enter Right Command!");
                            }
                        }
                    } else {
                        System.out.println("There Are No Requests.");
                    }
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter Right Command!");
            }
        }
    }
}
