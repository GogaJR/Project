package com.project.mgm.onlineBanking.common;

import com.project.mgm.onlineBanking.database.DatabaseConnection;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;
import com.project.mgm.onlineBanking.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginReg {
    private static final Connection connection = DatabaseConnection.getConnection();
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();

    public static int login(String tableName) {
        String mail = Input.inputString("E-Mail: ");
        String password = Input.inputString("Password: ");

        int id;
        if ((id = checkLogin(mail, password, tableName)) == 0) {
            System.out.println("Wrong E-Mail or Password!Try Again!\n");
        }

        return id;
    }

    public static int registration() {
        String name = Input.inputString("Name: ");
        String surname = Input.inputString("Surname: ");

        String sex;
        do {
            System.out.print("Sex (M) Male, (F) Female: ");
            sex = Input.inputCommand();

            if(!(sex.equals("M") || sex.equals("F"))) {
                System.out.println("Enter Right Command!");
                continue;
            }
        }while (!(sex.equals("M") || sex.equals("F")));

        int year = Input.inputIntNumber("Year of Birth: ");
        int month = Input.inputIntNumber("Month of Birth: ");
        int day = Input.inputIntNumber("Day of Birth: ");
        Input.inputString("");
        String birthCountry = Input.inputString("Country of Birth: ");
        String birthCity = Input.inputString("City of Birth: ");
        String livingCountry = Input.inputString("Country of Living: ");
        String livingCity = Input.inputString("City of Living: ");
        String serialNumber = Input.inputString("Passport Serial Number: ");

        String mail, password;
        while(true) {
            mail = Input.inputString("E-Mail: ");
            password = Input.inputString("Password: ");

            if(userDatabaseOperation.checkAccount(mail)) {
                break;
            }
        }

        User user = new User(name, surname, sex, year+"-"+month+"-"+day,
                birthCountry, birthCity, livingCountry, livingCity, serialNumber, mail, password);

        return userDatabaseOperation.createUser(user);
    }

    private static int checkLogin(String enteredMail, String enteredPassword, String tableName) {
        String sql = "select id, mail, password from " + tableName + " where mail=? and password=?";
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
}
