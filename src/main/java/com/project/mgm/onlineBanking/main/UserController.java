package com.project.mgm.onlineBanking.main;

import com.project.mgm.onlineBanking.common.Input;
import com.project.mgm.onlineBanking.common.LoginReg;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;
import com.project.mgm.onlineBanking.service.UseService;
import com.project.mgm.onlineBanking.user.User;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserController {
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();
    private static User user = new User();
    private static UseService useService = new UseService();
    private static ServerSocket serverSocket;
    private int responseNumber;
    private int userId;

    public UserController(int userId) {
        this.userId = userId;
        responseNumber = userDatabaseOperation.getResponseNumber(userId);
    }

    public static void main(String[] args) {
        System.out.println("\t\t\t\t\tWelcome To MGM Online Banking");

        while(true) {
            System.out.println("(L) Login");
            System.out.println("(R) Registration");
            System.out.println("(E) Exit");

            String input = Input.inputCommand();
            switch (input) {
                case "L":
                    int id = LoginReg.login("user");
                    if(id != 0) {
                        UserController userController = new UserController(id);
                        if(userDatabaseOperation.checkUserBankBond(id)) {
                            userController.cooperatedUser();
                        } else {
                            userController.notCooperatedUser();
                        }
                    } else {
                        System.out.println("Wrong E-Mail or Password!Try Again!\n");
                    }
                    break;
                case "R":
                    id = LoginReg.registration();
                    new UserController(id).notCooperatedUser();
                    break;
                case "E":
                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }

    public void cooperatedUser() {
        try {
            serverSocket = new ServerSocket(8081);
            new Thread(new BankResponseProcessor()).start();
        }catch (IOException e) {
            System.out.println("I/O Exception.");
        }

        while(true) {
            System.out.println();
            System.out.println("(MB) Show My Bank List");
            System.out.println("(MS) Show My Bank's Service List");
            System.out.println("(AB) Show Available Bank List");
            System.out.println("(AS) Show Available Service List");
            System.out.println("(B)  Show Balance(s)");
            System.out.println("(U)  To Make Use of Service");
            System.out.println("(O)  Open Account");
            System.out.println("(R)  Show Responses " + "(" + responseNumber + ")");
            System.out.println("(Q)  Quit");

            String input = Input.inputCommand();
            switch (input) {
                case "MB":
                    user.showBankList(userId);
                    break;
                case "MS":
                    user.showBankServiceList(userId);
                    break;
                case "AB":
                    user.showAllBankList();
                    break;
                case "AS":
                    user.showServiceList();
                    break;
                case "B":
                    loop1: while(true) {
                        System.out.println();
                        System.out.println("(C) Show Balance of Concrete Bank");
                        System.out.println("(A) All");

                        input = Input.inputCommand();
                        switch (input) {
                            case "C":
                                Map<Integer, String> banks = user.getBankList(userId);
                                int bankId = Input.checkIdInput(banks);
                                user.showBalance(userId, bankId);
                                break loop1;
                            case "A":
                                user.showBalance(userId);
                                break loop1;
                            default:
                                System.out.println("Enter Right Command!\n");
                        }
                    }
                    break;
                case "U":
                    loop: while(true) {
                        System.out.println("(D) Put In Deposit");
                        System.out.println("(L) Take Loan");
                        System.out.println("(Q) Quit");

                        input = Input.inputCommand();
                        switch (input) {
                            case "D":
                                Map<Integer, String> banks = user.getBankList(userId);
                                int bankId = Input.checkIdInput(banks);
                                useService.putInDeposit(userId, bankId);
                                break loop;
                            case "L":
                                break loop;
                            case "Q":
                                break loop;
                            default:
                                System.out.println("Enter Right Command!");
                        }
                    }
                    break;
                case "O":
                    if(useService.openAccount(userId, true)) {
                        try {
                            new Socket("127.0.0.1", 8080);
                        }catch (IOException e) {
                            System.out.println("I/O Exception.");
                        }
                    }
                    break;
                case "R":
                    user.showResponse(userId);
                    responseNumber = 0;
                    break;
                case "Q":
                    try {
                        serverSocket.close();
                    }catch (IOException e) {
                        System.out.println("I/O Exception.");
                    }

                    return;
                default:
                    System.out.println("Enter Right Command!");
            }
        }
    }

    private void notCooperatedUser() {
        try {
            serverSocket = new ServerSocket(8081);
            new Thread(new BankResponseProcessor()).start();
        }catch (IOException e) {
            System.out.println("I/O Exception.");
        }

        boolean isCooperatedUser = false;
        while(true) {
            System.out.println();
            System.out.println("(B) Show Available Bank List");
            System.out.println("(S) Show Available Service List");
            System.out.println("(O) Open Account");
            System.out.println("(R) Show Responses " + "(" + responseNumber + ")");
            System.out.println("(Q) Quit");

            String input = Input.inputCommand();
            switch (input) {
                case "B":
                    user.showAllBankList();
                    break;
                case "S":
                    user.showServiceList();
                    break;
                case "O":
                    if(userDatabaseOperation.isAccountPresent(userId)) {
                        isCooperatedUser = true;
                    }

                    if(useService.openAccount(userId, isCooperatedUser)) {
                        try {
                            new Socket("127.0.0.1", 8080);
                        }catch (IOException e) {
                            System.out.println("I/O Exception.");
                        }
                    }
                    break;
                case "R":
                    user.showResponse(userId);
                    responseNumber = 0;
                    break;
                case "Q":
                    try {
                        serverSocket.close();
                    }catch (IOException e) {
                        System.out.println("I/O Exception.");
                    }

                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }

    class BankResponseProcessor implements Runnable {

        private void acceptConnection() {
            try {
                while(true) {
                    serverSocket.accept();
                    responseNumber++;
                }
            }catch (IOException e) {

            }
        }

        @Override
        public void run() {
            acceptConnection();
        }
    }
}
