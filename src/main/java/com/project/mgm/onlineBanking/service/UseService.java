package com.project.mgm.onlineBanking.service;

import com.project.mgm.onlineBanking.database.DatabaseConnection;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UseService {
    private static Scanner scanner = new Scanner(System.in);
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();
    private static final Connection connection = DatabaseConnection.getConnection();

    public void openAccount(int userId, boolean isCooperatedUser) {
        List<String> banks = userDatabaseOperation.getBankList();
        System.out.println("Choose Bank: ");
        for(int i=1; i<=banks.size(); i++) {
            System.out.println("(" + i + ") " + banks.get(banks.size()-i));
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

        if(isCooperatedUser) {
            if(userDatabaseOperation.isAccountPresent(userId, inputNumber)) {
                System.out.println("You Already Have an Account In This Bank!");
                return;
            }
        }

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

        sendAccountRequest(userId, inputNumber, cardId);
    }

    private void sendAccountRequest(int userId, int bankId, String cardId) {
        String sql = "insert into account_request (user_id, bank_id, card_id, response)" +
                " values (?, ?, ?, ?)";
        PreparedStatement insertStatement;
        try {
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setInt(1, userId);
            insertStatement.setInt(2, bankId);
            insertStatement.setString(3, cardId);
            insertStatement.setString(4, "WAITING");

            if(insertStatement.executeUpdate() == 1) {
                System.out.println("Request Send Successfully!\n");
            }

        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            System.out.println("You Have Already Sent Request!\n");
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }
}
