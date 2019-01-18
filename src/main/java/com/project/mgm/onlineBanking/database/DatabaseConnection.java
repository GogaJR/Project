package com.project.mgm.onlineBanking.database;

import com.project.mgm.onlineBanking.user.User;
import java.sql.*;

public class DatabaseConnection {
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

    public void createUser(User user) {
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
    }

    public boolean checkLogin(String enteredMail, String enteredPassword) {
        String sql = "select mail, password from user where mail=? and password=?";
        PreparedStatement selectStatement;

        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, enteredMail);
            selectStatement.setString(2, enteredPassword);

            ResultSet rs = selectStatement.executeQuery();
            if(!rs.next()) {
                System.out.println("Wrong E-Mail or Password!Try Again!\n");
                return false;
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception");
        }

        return true;
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
}
