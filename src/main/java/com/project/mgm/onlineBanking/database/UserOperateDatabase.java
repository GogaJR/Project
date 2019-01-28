package com.project.mgm.onlineBanking.database;

import com.project.mgm.onlineBanking.user.User;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;
import java.util.Map;
import java.util.TreeMap;

public class UserOperateDatabase {
    private static final Connection connection = DatabaseConnection.getConnection();

    public int createUser(User user) {
        String sql = "insert into user (name, surname, sex, date_of_birth, place_of_birth, place_of_living, " +
                                    "passport_serial_number, mail, password)" +
                " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement insertStatement;
        try {
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, user.getName());
            insertStatement.setString(2, user.getSurname());
            insertStatement.setString(3, user.getSex());
            insertStatement.setString(4, user.getDateOfBirth());
            insertStatement.setString(5, user.getCountryOfBirth() + ", " + user.getCityOfBirth());
            insertStatement.setString(6, user.getCountryOfLiving() + ", " + user.getCityOfLiving());
            insertStatement.setString(7, user.getPassportSerialNumber());
            insertStatement.setString(8, user.getMail());
            insertStatement.setString(9, user.getPassword());

            int result = insertStatement.executeUpdate();
            if(result == 1) {
                System.out.println("Registration Is Done Successfully!\n");
            }else {
                System.out.println("Something Went Wrong!Try Again!\n");
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception." + e);
        }

        sql = "select max(id) from user";

        Statement selectStatement;
        try {
            selectStatement = connection.createStatement();
            ResultSet rs = selectStatement.executeQuery(sql);
            rs.next();

            return rs.getInt("id");
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return 0;
    }

    public boolean checkAccount(String regMail) {
        String sql = "select mail from user where mail=?";
        PreparedStatement selectStatement;

        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, regMail);

            ResultSet rs = selectStatement.executeQuery();
            if(!rs.next()) {
                return true;
            }else {
                System.out.println("There is an Account with This Mail!\n");
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return false;
    }

    public boolean checkUserBankBond(int userId) {
        String sql = "select user_id from user_balance_bank where user_id=?";

        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, userId);

            ResultSet rs = selectStatement.executeQuery();
            if(rs.next()) {
                return true;
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return false;
    }

    public List<String> getUserBankList(int userId) {
        List<String> userBanks = new ArrayList<>();
        String bankIdSql = "select bank_id from user_balance_bank where user_id=?";
        String bankNameSql = "select name from bank where id=?";

        PreparedStatement selectBankIdStatement;
        PreparedStatement selectBankNameStatement;
        try {
            selectBankIdStatement = connection.prepareStatement(bankIdSql);
            selectBankIdStatement.setInt(1, userId);

            ResultSet bankIdResultSet = selectBankIdStatement.executeQuery();
            selectBankNameStatement = connection.prepareStatement(bankNameSql);
            while(bankIdResultSet.next()) {
                selectBankNameStatement.setInt(1, bankIdResultSet.getInt("bank_id"));
                ResultSet bankNameResultSet = selectBankNameStatement.executeQuery();

                if(bankNameResultSet.next()) {
                    userBanks.add(bankNameResultSet.getString("name"));
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return userBanks;
    }

    public Map<Integer, String> getBankList() {
        Map<Integer, String> bankWithIds = new TreeMap<>();
        String bankNameSql = "select id, name from bank";

        Statement selectBankNameStatement;
        try {
            selectBankNameStatement = connection.createStatement();
            ResultSet bankNameResultSet = selectBankNameStatement.executeQuery(bankNameSql);
            while(bankNameResultSet.next()) {
                int id = bankNameResultSet.getInt("id");
                String name = bankNameResultSet.getString("name");
                bankWithIds.put(id, name);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return bankWithIds;
    }

    public boolean isAccountPresent(int userId, int bankId) {
        String sql = "select user_id from user_balance_bank where bank_id=?";
        PreparedStatement selectBankStatement;
        try {
            selectBankStatement = connection.prepareStatement(sql);
            selectBankStatement.setInt(1, bankId);
            ResultSet bankIdResultSet = selectBankStatement.executeQuery();

            while(bankIdResultSet.next()) {
                int id = bankIdResultSet.getInt("user_id");
                if(id == userId) {
                    return true;
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return false;
    }

    public boolean isAccountPresent(int userId) {
        String sql = "select user_id from user_balance_bank where user_id=?";
        PreparedStatement selectBankStatement;
        try {
            selectBankStatement = connection.prepareStatement(sql);
            selectBankStatement.setInt(1, userId);
            ResultSet userIdResultSet = selectBankStatement.executeQuery();

            if(userIdResultSet.next()) {
                return true;
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return false;
    }

    public int getResponseNumber(int userId) {
        int responseNumber = 0;
        String sql = "select * from account_response where user_id=? and has_been_read=?";
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, userId);
            selectStatement.setInt(2, 0);
            ResultSet rs = selectStatement.executeQuery();

            while(rs.next()) {
                responseNumber++;
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return responseNumber;
    }

    public List<Integer> getBankListById(int userId) {
        List<Integer> bankIds = new ArrayList<>();
        String bankIdSql = "select bank_id from user_balance_bank where user_id=?";
        PreparedStatement selectBankIdStatement;
        try {
            selectBankIdStatement = connection.prepareStatement(bankIdSql);
            selectBankIdStatement.setInt(1, userId);
            ResultSet bankIdResultSet = selectBankIdStatement.executeQuery();
            while(bankIdResultSet.next()) {
                bankIds.add(bankIdResultSet.getInt("bank_id"));
            }
        }catch(SQLException e) {
            System.out.println("SQL Exception.");
        }

        return bankIds;
    }

    public String getBankNameById(int bankId) {
        String name = "";
        String bankNameSql = "select name from bank where id=?";
        PreparedStatement selectBankNameStatement;
        try {
            selectBankNameStatement = connection.prepareStatement(bankNameSql);
            selectBankNameStatement.setInt(1, bankId);
            ResultSet bankNameResultSet = selectBankNameStatement.executeQuery();
            if(bankNameResultSet.next()) {
                name = bankNameResultSet.getString("name");
            }
        }catch(SQLException e) {
            System.out.println("SQL Exception.");
        }

        return name;
    }

    public List<Integer> getLoanListById(int bankId) {
        List<Integer> loanIds = new ArrayList<>();
        String servcieIdSql = "select service_id from bank_loan where bank_id=?";
        PreparedStatement selectServiceIdStatement;
        try {
            selectServiceIdStatement = connection.prepareStatement(servcieIdSql);
            selectServiceIdStatement.setInt(1, bankId);
            ResultSet serviceIdResultSet = selectServiceIdStatement.executeQuery();
            while(serviceIdResultSet.next()) {
                loanIds.add(serviceIdResultSet.getInt(1));
            }
        }catch(SQLException e) {
            System.out.println("SQL Exception.");
        }

        return loanIds;
    }
}
