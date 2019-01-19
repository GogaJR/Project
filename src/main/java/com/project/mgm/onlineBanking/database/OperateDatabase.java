package com.project.mgm.onlineBanking.database;

import com.project.mgm.onlineBanking.user.User;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class OperateDatabase {
    private static final Connection connection = getConnection();

    private static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/online_banking",
                                                                "root",
                                                                "");
            return connection;
        }catch (SQLException e) {
            System.out.println("SQL Exception.");;
            return null;
        }
    }

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

    public int checkLogin(String enteredMail, String enteredPassword) {
        String sql = "select id, mail, password from user where mail=? and password=?";
        PreparedStatement selectStatement;

        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, enteredMail);
            selectStatement.setString(2, enteredPassword);

            ResultSet rs = selectStatement.executeQuery();
            if(!rs.next()) {
                System.out.println("Wrong E-Mail or Password!Try Again!\n");
            } else {
                return rs.getInt("id");
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception");
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

    public void showUserBankList(int userId) {
        String idSql = "select bank_id from user_balance_bank where user_id=?";
        String bankSql = "select * from bank where id=?";

        PreparedStatement selectIdStatement;
        PreparedStatement selectBankStatement;
        try {
            selectIdStatement = connection.prepareStatement(idSql);
            selectIdStatement.setInt(1, userId);
            ResultSet bankIdResultSet = selectIdStatement.executeQuery();

            selectBankStatement = connection.prepareStatement(bankSql);
            while(bankIdResultSet.next()) {
                selectBankStatement.setInt(1, bankIdResultSet.getInt("bank_id"));
                ResultSet bankResultSet = selectBankStatement.executeQuery();

                if(bankIdResultSet.next()) {
                    System.out.print("Name: " + bankResultSet.getString("name"));
                    System.out.print("Address: " + bankResultSet.getString("address"));
                    System.out.print("Phone Number(s): " + bankResultSet.getString("phone_number"));
                    System.out.print("Fax: " + bankResultSet.getString("fax"));
                    System.out.print("E-Mail: " + bankResultSet.getString("mail"));
                    System.out.print("Working Hours: " + bankResultSet.getString("working_hours"));
                    System.out.print("Days Off: " + bankResultSet.getString("days_off"));
                    System.out.println();
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showUserServiceList(int userId) {
        String idSql = "select service_id from user_service where user_id=?";
        String serviceSql = "select * from service where id=?";

        PreparedStatement selectIdStatement;
        PreparedStatement selectServiceStatement;
        try {
            selectIdStatement = connection.prepareStatement(idSql);
            selectIdStatement.setInt(1, userId);
            ResultSet serviceIdResultSet = selectIdStatement.executeQuery();

            selectServiceStatement = connection.prepareStatement(serviceSql);
            while(serviceIdResultSet.next()) {
                selectServiceStatement.setInt(1, serviceIdResultSet.getInt("service_id"));
                ResultSet serviceResultSet = selectServiceStatement.executeQuery();

                if(serviceIdResultSet.next()) {
                    System.out.print(serviceResultSet.getString("name"));
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showBankList() {
        String sql = "select * from bank";

        Statement selectBankStatement;
        try {
            selectBankStatement = connection.createStatement();
            ResultSet bankResultSet = selectBankStatement.executeQuery(sql);

            while(bankResultSet.next()) {
                System.out.println("Name: " + bankResultSet.getString("name"));
                System.out.println("Address: " + bankResultSet.getString("address"));
                System.out.println("Phone Number(s): " + bankResultSet.getString("phone_number"));
                System.out.println("Fax: " + bankResultSet.getString("fax"));
                System.out.println("E-Mail: " + bankResultSet.getString("mail"));
                System.out.println("Working Hours: " + bankResultSet.getString("working_hours"));
                System.out.println("Days Off: " + bankResultSet.getString("days_off"));
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showServiceList() {
        String sql = "select * from service";

        Statement selectServiceStatement;
        try {
            selectServiceStatement = connection.createStatement();
            ResultSet serviceResultSet = selectServiceStatement.executeQuery(sql);

            while(serviceResultSet.next()) {
                System.out.println(serviceResultSet.getString("name"));
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showBalance(int userId) {
        String balanceSql = "select bank_id, balance from user_balance_bank where user_id=?";
        String bankSql = "select name from bank where bank_id=?";

        PreparedStatement selectBalanceStatement;
        PreparedStatement selectBankStatement;
        try {
            selectBalanceStatement = connection.prepareStatement(balanceSql);
            selectBalanceStatement.setInt(1, userId);

            ResultSet balanceResultSet = selectBalanceStatement.executeQuery();
            selectBankStatement = connection.prepareStatement(bankSql);
            while(balanceResultSet.next()) {
                selectBankStatement.setInt(1, balanceResultSet.getInt("bank_id"));
                ResultSet bankResultSet = selectBankStatement.executeQuery();

                if(bankResultSet.next()) {
                    System.out.println("You Have " + balanceResultSet.getInt("balance") + " in " +
                            bankResultSet.getString("name") + ".");
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showBalance(int userId, String bankName) {
        String bankIdSql = "select id from bank where name=?";
        String balanceSql = "select balance from user_balance_bank where user_id=? and bank_id=?";

        PreparedStatement selectBalanceStatement;
        PreparedStatement selectBankIdStatement;
        try {
            selectBankIdStatement = connection.prepareStatement(bankIdSql);
            selectBankIdStatement.setString(1, bankName);

            ResultSet bankIdResultSet = selectBankIdStatement.executeQuery();
            if(bankIdResultSet.next()) {
                selectBalanceStatement = connection.prepareStatement(balanceSql);
                selectBalanceStatement.setInt(1, userId);
                selectBalanceStatement.setInt(2, bankIdResultSet.getInt("id"));

                ResultSet balanceResultSet = selectBalanceStatement.executeQuery();
                if(balanceResultSet.next()) {
                    System.out.println("You have " + balanceResultSet.getInt("balance") + " in " +
                                        bankName + ".");
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
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
}
