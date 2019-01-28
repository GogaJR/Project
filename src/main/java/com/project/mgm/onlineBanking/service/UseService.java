package com.project.mgm.onlineBanking.service;

import com.project.mgm.onlineBanking.common.Input;
import com.project.mgm.onlineBanking.database.DatabaseConnection;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UseService {
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();
    private static final Connection connection = DatabaseConnection.getConnection();

    public boolean openAccount(int userId, boolean isCooperatedUser) {
        Map<Integer, String> bankWithIds = userDatabaseOperation.getBankList();
        System.out.println("Choose Bank: ");
        Set<Integer> ids = bankWithIds.keySet();
        for(int id : ids) {
            System.out.println("(" + id + ") " + bankWithIds.get(id));
        }

        int bankId = Input.inputIntNumberWithBoards(0, bankWithIds.size());
        if(isCooperatedUser) {
            if(userDatabaseOperation.isAccountPresent(userId, bankId)) {
                System.out.println("You Already Have an Account In This Bank!");
                return false;
            }
        }

        boolean isNotCorrect = true;
        String cardId;
        Input.inputString("");
        do {
            cardId = Input.inputString("Card ID: ");

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

        return sendAccountRequest(userId, bankId, cardId);
    }

    private boolean sendAccountRequest(int userId, int bankId, String cardId) {
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
                return true;
            }

        }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
            System.out.println("You Have Already Sent Request!\n");
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return false;
    }

    public void putInDeposit(int userId, int bankId) {
        List<Integer> depositIds = new ArrayList<>();
        String depositSql = "select id, name, min_amount from deposit where bank_id=?";
        PreparedStatement selectDepositStatement;
        try {
            selectDepositStatement = connection.prepareStatement(depositSql);
            selectDepositStatement.setInt(1, bankId);
            ResultSet depositResultSet = selectDepositStatement.executeQuery();
            while (depositResultSet.next()) {
                int depositId = depositResultSet.getInt("id");
                String depositName = depositResultSet.getString("name");
                int minAmount = depositResultSet.getInt("min_amount");
                System.out.println("(" + depositId + ") " + depositName);
                System.out.println("Minimum Amount: " + minAmount + "\n");
                depositIds.add(depositId);
            }

            int depositId;
            loop: while(true) {
                depositId = Input.inputIntNumber("");
                for(int dId : depositIds) {
                    if(dId == depositId) {
                        break loop;
                    }
                }

                System.out.println("Enter Right Command!");
            }

            String depositMonthRateSql = "select * from deposit_month_rate where deposit_id=?";
            PreparedStatement depositMonthRateStatement = connection.prepareStatement(depositMonthRateSql);
            depositMonthRateStatement.setInt(1, depositId);
            ResultSet depositMonthRateResultSet = depositMonthRateStatement.executeQuery();
            while (depositMonthRateResultSet.next()) {
                String monthRanhge = depositMonthRateResultSet.getString("month");
                double interestRate = depositMonthRateResultSet.getDouble("interest_rate");
                System.out.println(monthRanhge + " months: " + interestRate);
            }

            int numberOfMonths = Input.inputIntNumber("Number of Months: ");

        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }
}
