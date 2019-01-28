package com.project.mgm.onlineBanking.main;

import com.project.mgm.onlineBanking.bank.Bank;
import com.project.mgm.onlineBanking.common.Input;
import com.project.mgm.onlineBanking.common.LoginReg;
import com.project.mgm.onlineBanking.database.BankOperateDatabase;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

public class BankController {
    private static BankOperateDatabase bankDatabaseOperation = new BankOperateDatabase();
    private static Bank bank = new Bank();
    private int bankId;
    private int requestNumber;
    private static ServerSocket serverSocket;

    public BankController(int bankId) {
        this.bankId = bankId;
        requestNumber = bankDatabaseOperation.getRequestNumber(bankId);
    }

    public static void main(String[] args) {
        System.out.println("\t\t\t\t\tWelcome To MGM Online Banking");

        while(true) {
            System.out.println("(L) Login");
            System.out.println("(E) Exit");

            String input = Input.inputCommand();
            switch (input) {
                case "L":
                    int id = LoginReg.login("bank");
                    if(id != 0) {
                        new BankController(id).bankMain();
                    }
                    break;
                case "E":
                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }

    private void bankMain() {
        try {
            serverSocket = new ServerSocket(8080);
            new Thread(new UserRequestProcessor()).start();
        }catch (IOException e) {
            System.out.println("I/O Exception.");
        }

        while(true) {
            System.out.println();
            System.out.println("(SA) Show All User List");
            System.out.println("(SL) Show Loaned User List");
            System.out.println("(SD) Show Deposit List");
            System.out.println("(SR) Show Requests " + "(" + requestNumber + ")");
            System.out.println("(A)  Add Service");
            System.out.println("(Q)  Quit");

            String input = Input.inputCommand();
            switch (input) {
                case "SA":
                    bank.showAllUserList(bankId);
                    break;
                case "SL":
                    bank.showLoanedUserList(bankId);
                    break;
                case "SD":
                    bank.showDepositList(bankId);
                    break;
                case "SR":
                    Map<String, Integer> userCardId = bank.showAccountRequests(bankId);

                    if(userCardId.size() != 0) {
                        loop: while(true) {
                            System.out.println("(SR) Send Response");
                            System.out.println("(Q) Quit");

                            input = Input.inputCommand();
                            switch (input) {
                                case "SR":
                                    bank.createResponse(bankId, userCardId);
                                    requestNumber--;

                                    try {
                                        new Socket("127.0.0.1", 8081);
                                    }catch (IOException e) {
                                        System.out.println("I/O Exception.");
                                    }

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
                case "A":
                    loop: while(true) {
                        Map<Integer, String> services = bankDatabaseOperation.getServiceList();
                        System.out.println("(L) Add Loan");
                        System.out.println("(D) Add Deposit");
                        System.out.println("(Q) Quit");

                        input = Input.inputCommand();
                        switch (input) {
                            case "D":
                                bank.createDeposit(bankId);
                                break loop;
                            case "L":
                                Set<Integer> serviceIds = services.keySet();
                                for(int id : serviceIds) {
                                    String serviceName = services.get(id);
                                    if(!serviceName.equals("Deposits")) {
                                        System.out.println("(" + id + ") " + serviceName);
                                    }
                                }

                                int serviceId = Input.inputIntNumberWithBoards(0, services.size()-1);
                                bank.createLoan(bankId, serviceId);
                                break loop;
                            case "Q":
                                break loop;
                            default:
                                System.out.println("Enter Right Command.");
                        }
                    }
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

    class UserRequestProcessor implements Runnable {

        private void acceptConnection() {
            try {
                while(true) {
                    serverSocket.accept();
                    requestNumber++;
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
