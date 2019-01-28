package com.project.mgm.onlineBanking.user;

import com.project.mgm.onlineBanking.database.DatabaseConnection;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class User {
    private int id;
    private String name;
    private String surname;
    private String sex;
    private String dateOfBirth;
    private String countryOfBirth;
    private String cityOfBirth;
    private String countryOfLiving;
    private String cityOfLiving;
    private String passportSerialNumber;
    private String mail;
    private String password;
    private static final Connection connection = DatabaseConnection.getConnection();
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();

    public User(String name, String surname, String sex, String dateOfBirth,
                String coutryOfBirth, String cityOfBirth, String countryOfLiving, String cityOfLiving,
                String passportSerialNumber, String mail, String password) {
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.countryOfBirth = coutryOfBirth;
        this.cityOfBirth = cityOfBirth;
        this.countryOfLiving = countryOfLiving;
        this.cityOfLiving = cityOfLiving;
        this.passportSerialNumber = passportSerialNumber;
        this.mail = mail;
        this.password = password;
    }

    public User() {

    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getSex() {
        return sex;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public String getCityOfBirth() {
        return cityOfBirth;
    }

    public String getCountryOfLiving() {
        return countryOfLiving;
    }

    public String getCityOfLiving() {
        return cityOfLiving;
    }

    public String getPassportSerialNumber() {
        return passportSerialNumber;
    }

    public String getMail() {
        return mail;
    }

    public String getPassword() {
        return password;
    }

    public void showBankList(int userId) {
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

                if(bankResultSet.next()) {
                    printBankList(bankResultSet);
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public Map<Integer, String> getBankList(int userId) {
        Map<Integer, String> banks = new TreeMap<>();
        String idSql = "select bank_id from user_balance_bank where user_id=?";
        String bankSql = "select id, name from bank where id=?";

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

                while(bankResultSet.next()) {
                    int bankId = bankResultSet.getInt("id");
                    String bankName = bankResultSet.getString("name");
                    banks.put(bankId, bankName);
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return banks;
    }

    public void showAllBankList() {
        String sql = "select * from bank";

        Statement selectBankStatement;
        try {
            selectBankStatement = connection.createStatement();
            ResultSet bankResultSet = selectBankStatement.executeQuery(sql);

            while(bankResultSet.next()) {
                printBankList(bankResultSet);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showBankServiceList(int userId) {
        List<Integer> bankIds = userDatabaseOperation.getBankListById(userId);

        String depositSql = "select * from deposit where bank_id=?";
        String depositMonthRateSql = "select * from deposit_month_rate where deposit_id=?";
        String loanSql = "select * from loan where bank_id=? and service_id=?";

        PreparedStatement selectDepositStatement;
        PreparedStatement selectDepositMonthRateStatement;
        PreparedStatement selectLoanStatement;

        try {
            selectDepositStatement = connection.prepareStatement(depositSql);
            selectDepositMonthRateStatement = connection.prepareStatement(depositMonthRateSql);
            selectLoanStatement = connection.prepareStatement(loanSql);

            for(int bankId : bankIds) {
                String bankName = userDatabaseOperation.getBankNameById(bankId);
                System.out.println("Services of " + bankName + ":");
                System.out.println("Deposits:");

                selectDepositStatement.setInt(1, bankId);
                ResultSet depositResultSet = selectDepositStatement.executeQuery();
                while(depositResultSet.next()) {
                    String depositName = depositResultSet.getString("name");
                    int minAmount = depositResultSet.getInt("min_amount");
                    System.out.println("Name: " + depositName);
                    System.out.println("Minimum Amount: " + minAmount + "AMD");
                    System.out.println("Month Ranges and Interest Rates: ");
                    int depositId = depositResultSet.getInt("id");
                    selectDepositMonthRateStatement.setInt(1, depositId);
                    ResultSet depositMonthRateResultSet = selectDepositMonthRateStatement.executeQuery();
                    while(depositMonthRateResultSet.next()) {
                        System.out.print("\t\t");
                        String month = depositMonthRateResultSet.getString("month");
                        double interestRate = depositMonthRateResultSet.getDouble("interest_rate");
                        System.out.println(month + " months: " + interestRate + "%");
                    }
                }

                List<Integer> loanIds = userDatabaseOperation.getLoanListById(bankId);
                System.out.println("\nLoans:");
                for(int loanId : loanIds) {
                    selectLoanStatement.setInt(1, bankId);
                    selectLoanStatement.setInt(2, loanId);
                    ResultSet loanResultSet = selectLoanStatement.executeQuery();

                    while(loanResultSet.next()) {
                        String loanType = loanResultSet.getString("loan_type");
                        String loanName = loanResultSet.getString("name");
                        int minAmount = loanResultSet.getInt("min_amount");
                        int maxAmount = loanResultSet.getInt("max_amount");
                        int maturity = loanResultSet.getInt("maturity");
                        double commissionFee = loanResultSet.getDouble("monthly_commission_fee");
                        int interestRate = loanResultSet.getInt("annual_interest_rate");
                        double dayPenalty = loanResultSet.getDouble("day_penalty");

                        System.out.println("Loan Type: " + loanType);
                        System.out.println("Name: " + loanName);
                        System.out.println("Minimum Amount: " + minAmount + " AMD");
                        System.out.println("Maximum Amount: " + maxAmount + " AMD");
                        System.out.println("Maturity: " + maturity + " months");
                        System.out.println("Monthly Commission Fee: " + commissionFee + "%");
                        System.out.println("Interest Rate: " + interestRate + "%");
                        System.out.println("Day Penalty: " + dayPenalty + "%");
                    }
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception." + e);
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

            System.out.println();
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showBalance(int userId) {
        String balanceSql = "select bank_id, balance from user_balance_bank where user_id=?";
        String bankSql = "select name from bank where id=?";

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
                    System.out.println("You Have " + balanceResultSet.getInt("balance") + "AMD in your " +
                            bankResultSet.getString("name") + " account.");
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showBalance(int userId, int bankId) {
        String balanceSql = "select balance from user_balance_bank where user_id=? and bank_id=?";

        PreparedStatement selectBalanceStatement;
        try {
            selectBalanceStatement = connection.prepareStatement(balanceSql);
            selectBalanceStatement.setInt(1, userId);
            selectBalanceStatement.setInt(2, bankId);
            ResultSet balanceResultSet = selectBalanceStatement.executeQuery();
            if(balanceResultSet.next()) {
                System.out.println("You have " + balanceResultSet.getInt("balance") + "AMD in your " +
                        bankId + " account.");
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    public void showResponse(int userId) {
        String responseSql = "select user_id, bank_id, card_id, response, has_been_read from account_response " +
                "where user_id=? and has_been_read=?";
        PreparedStatement selectResponseStatemet;

        String bankSql = "select name from bank where id=?";
        PreparedStatement selectBankStatement;
        try {
            selectResponseStatemet = connection.prepareStatement(responseSql);
            selectBankStatement = connection.prepareStatement(bankSql);
            selectResponseStatemet.setInt(1, userId);
            selectResponseStatemet.setInt(2, 0);
            ResultSet responseResultSet = selectResponseStatemet.executeQuery();

            if(!responseResultSet.next()) {
                System.out.println("There Are No Responses.");
            }

            responseResultSet.previous();
            while (responseResultSet.next()) {
                selectBankStatement.setInt(1, responseResultSet.getInt("bank_id"));
                ResultSet bankResultSet = selectBankStatement.executeQuery();
                if(bankResultSet.next()) {
                    System.out.println("The " + bankResultSet.getString("name") + " " +
                            responseResultSet.getString("response").toLowerCase() +
                            " Your Account Request.");
                }

                String updateSql = "update account_response set has_been_read=? where user_id=? and bank_id=? and card_id=?";
                PreparedStatement updateStatement;
                try {
                    updateStatement = connection.prepareStatement(updateSql);
                    updateStatement.setInt(1, 1);
                    updateStatement.setInt(2, userId);
                    updateStatement.setInt(3, responseResultSet.getInt("bank_id"));
                    updateStatement.setString(4, responseResultSet.getString("card_id"));

                    if(updateStatement.executeUpdate() != 1) {
                        System.out.println("Response Table Isn't Updated.");
                    }
                }catch (SQLException e) {
                    System.out.println("SQL Exception.");
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception." + e);
        }

    }

    private void printBankList(ResultSet bankResultSet) throws SQLException {
        System.out.println("Name: " + bankResultSet.getString("name"));
        System.out.println("Address: " + bankResultSet.getString("address"));
        System.out.println("Phone Number(s): " + bankResultSet.getString("phone_number"));
        System.out.println("Fax: " + bankResultSet.getString("fax"));
        System.out.println("E-Mail: " + bankResultSet.getString("mail"));
        System.out.println("Working Hours: " + bankResultSet.getString("working_hours"));
        System.out.println("Days Off: " + bankResultSet.getString("days_off"));
        System.out.println();
    }

    @Override
    public String toString() {
        return
                "ID: " + id + ", " +
                "Name: " + name + ", " +
                "Surname: " + surname + "/n" +
                "Sex: " + sex + ", " +
                "Date Of Birth: " + dateOfBirth + "/n" +
                "Country Of Birth: " + countryOfBirth + ", " +
                "City Of Birth: " + cityOfBirth + "/n" +
                "Country Of Living: " + countryOfLiving + ", " +
                "City Of Living: " + cityOfLiving + "/n" +
                "E-Mail: " + mail;
    }
}
