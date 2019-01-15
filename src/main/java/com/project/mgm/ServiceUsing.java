package com.project.mgm;

import com.project.mgm.onlineBanking.Bank;

import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class ServiceUsing {
    private static Scanner scanner = new Scanner(System.in);
    private static Database db = new Database();

    public void chooseService() {
        System.out.println("Which service do you want to choose?");

        while(true) {
            System.out.println("(O) Opening and Servicing of Accounts");
            System.out.println("(C) Crediting");
            System.out.println("(D) Deposits");
            System.out.println("(T) Transfers");
            System.out.println("(Q) Quit");

            String input = scanner.next().toUpperCase();
            switch (input) {
                case "O":
                    break;
                case "C":
                    showCreditList();
                    break;
                case "D":
                    break;
                case "T":
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter right command!\n");
            }
        }
    }

    private void showCreditList() {
        System.out.println("Choose Loan Type:");

        while(true) {
            System.out.println("(C) Cunsomer Loans");
            System.out.println("(S) Student Loans");
            System.out.println("(CAR) Car Loans");
            System.out.println("(M) Mortgage Loans");
            System.out.println("(Q) Quit");

            String loanInput = scanner.next().toUpperCase();
            String loan = null;
            switch (loanInput) {
                case "C":
                    loan = "Cunsomer Loan";
                    break;
                case "S":
                    loan = "Student Loan";
                    break;
                case "CAR":
                    loan = "Car Loan";
                    break;
                case "M":
                    loan = "Mortgage Loan";
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter right command!\n");
            }

            System.out.println("\nChoose Bank From Which You Want to Get Loan:");

            while(true) {
                System.out.println("(I) InecoBank CJSC");
                System.out.println("(AEB) ArmEconomBank OJSC");
                System.out.println("(ABB) ArmBusinessBank CJSC");
                System.out.println("(Q) Quit");

                String bankInput = scanner.next().toUpperCase();
                String bank = null;
                switch (bankInput) {
                    case "I":
                        bank = "InecoBank CJSC";
                        showCunsomerLoanList(loan, bank);
                        break;
                    case "AEB":
                        bank = "ArmEconomBank OJSC";
                        showCunsomerLoanList(loan, bank);
                        break;
                    case "ABB":
                        bank = "ArmBusinessBank CJSC";
                        showCunsomerLoanList(loan, bank);
                        break;
                    case "Q":
                        return;
                    default:
                        System.out.println("Enter right command!\n");
                }
            }
        }
    }

    public void showCunsomerLoanList(String loan, String bank) {
        Bank choosenBank = db.getBank(bank);
        Map<String, String> loanList = choosenBank.getLoanList();

        Set<Map.Entry<String, String>> loans = loanList.entrySet();
        boolean checkList = false;
        int index = 1;
        for(Map.Entry<String, String> l : loans) {
            if(l.getValue().equals(loan)) {
                checkList = true;
                System.out.print("(" + index + ") ");
                System.out.println(l.getKey());
                index++;
            }
        }

        System.out.println();
        if(!checkList) {
            System.out.println("There Is No Such Loan In This Bank!\n");
        }
    }
}
