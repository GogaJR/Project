package com.project.mgm.onlineBanking.database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BankOperateDatabase {
    private static final Connection connection = DatabaseConnection.getConnection();
    private static Scanner scanner = new Scanner(System.in);

    public int checkBankLogin(String enteredMail, String enteredPassword) {
        String sql = "select id, mail, password from bank where mail=? and password=?";
        PreparedStatement selectStatement;

        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, enteredMail);
            selectStatement.setString(2, enteredPassword);

            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()) {
                return rs.getInt("id");
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception");
        }

        return 0;
    }

    public void showAllUserList(int bankId) {
        String userSql = "select id, name, surname, sex, date_of_birth, place_of_birth, place_of_living, " +
                "passport_serial_number, mail from user where id=?";
        String idSql = "select user_id from user_balance_bank where bank_id=?";

        PreparedStatement selectUserStatement;
        PreparedStatement selectUserIdStatement;
        try {
            selectUserIdStatement = connection.prepareStatement(idSql);
            selectUserStatement = connection.prepareStatement(userSql);

            selectUserIdStatement.setInt(1, bankId);
            ResultSet userIdResultSet = selectUserIdStatement.executeQuery();

            while(userIdResultSet.next()) {
                selectUserStatement.setInt(1, userIdResultSet.getInt("user_id"));
                ResultSet userResultSet = selectUserStatement.executeQuery();

                if(userResultSet.next()) {
                    System.out.println("ID: " + userResultSet.getInt("id"));
                    System.out.println("Name: " + userResultSet.getString("name"));
                    System.out.println("Surname: " + userResultSet.getString("surname"));
                    System.out.println("Sex: " + userResultSet.getString("sex"));
                    System.out.println("Date of Birth: " + userResultSet.getString("date_of_birth"));
                    System.out.println("Place of Birth: " + userResultSet.getString("place_of_birth"));
                    System.out.println("Place of Living: " + userResultSet.getString("place_of_living"));
                    System.out.println("Passport Serial Number: " + userResultSet.getString("passport_serial_number"));
                    System.out.println("E-Mail: " + userResultSet.getString("mail"));
                    System.out.println();
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showLoanedUserList(int bankId) {
        String sql = "select * from taken_credits where bank_id=?";
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, bankId);
            ResultSet creditResultSet = selectStatement.executeQuery();
            while(creditResultSet.next()) {
                System.out.println("ID: " + creditResultSet.getInt("user_id"));
                System.out.println("Amount: " + creditResultSet.getInt("amount"));
                System.out.println("Time to Next Pay: " + creditResultSet.getInt("time_to_next_pay"));
                System.out.println("Time to Repay: " + creditResultSet.getInt("time"));
                System.out.println();
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showDepositList(int bankId) {
        String sql = "select * from put_in_deposits where bank_id=?";
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, bankId);
            ResultSet depositResultSet = selectStatement.executeQuery();
            while(depositResultSet.next()) {
                System.out.println("ID: " + depositResultSet.getInt("user_id"));
                System.out.println("Amount: " + depositResultSet.getInt("amount"));
                System.out.println("Time: " + depositResultSet.getInt("time"));
                System.out.println();
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public List<Integer> showAccountRequests(int bankId) {
        List<Integer> ids = new ArrayList<>();
        String sql = "select * from account_request where bank_id=? and response=?";
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, bankId);
            selectStatement.setString(2, "WAITING");
            ResultSet requestResultSet = selectStatement.executeQuery();
            int userId;
            while(requestResultSet.next()) {
                System.out.println("User ID: " + (userId = requestResultSet.getInt("user_id")));
                System.out.println("Card ID: " + requestResultSet.getString("card_id"));
                System.out.println();
                ids.add(userId);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return ids;
    }

    public void createResponse(int bankId, List<Integer> userIds) {
        int userId = 0;
        boolean isNotCorrect = true;
        do {
            System.out.print("User ID: ");
            if(scanner.hasNextInt()) {
                userId = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Enter Number!");
                continue;
            }

            for(int i=0; i<userIds.size(); i++) {
                if(userId != userIds.get(i)) {
                    System.out.println("There Is No User With Such ID!\n");
                    break;
                }

                if(i == userIds.size()-1) {
                    isNotCorrect = false;
                }
            }
        }while(isNotCorrect);

        loop: while(true) {
            System.out.print("Accept? (Y/N): ");
            String answer = scanner.nextLine().toUpperCase();

            switch(answer) {
                case "Y":
                    sendAccountResponse(userId, bankId,"ACCEPTED");
                    break loop;
                case "N":
                    sendAccountResponse(userId, bankId, "DENIED");
                    break loop;
                default:
                    System.out.println("Enter Right Command!");
            }
        }

    }

    private void sendAccountResponse(int userId, int bankId, String response) {
        String sql = "update account_request " +
                "set response=? where user_id=? and bank_id=?";
        PreparedStatement updateStatement;
        try {
            updateStatement = connection.prepareStatement(sql);
            updateStatement.setString(1, response);
            updateStatement.setInt(2, userId);
            updateStatement.setInt(3, bankId);

            if(updateStatement.executeUpdate() == 1) {

            }

            if(response == "ACCEPTED") {
                createBalance(userId, bankId);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    private void createBalance(int userId, int bankId) {
        String sql = "insert into user_balance_bank (user_id, bank_id)" +
                " values (?, ?)";
        PreparedStatement insertStatement;
        try {
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setInt(1, userId);
            insertStatement.setInt(2, bankId);

            if (insertStatement.executeUpdate() == 1) {
                System.out.println("Balance Created Successfully!");
            }

        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }
}
