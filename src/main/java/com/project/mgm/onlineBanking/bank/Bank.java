package com.project.mgm.onlineBanking.bank;

import com.project.mgm.onlineBanking.common.Input;
import com.project.mgm.onlineBanking.database.DatabaseConnection;

import java.sql.*;
import java.util.*;

public class Bank {
    private String name;
    private String address;
    private String phone;
    private String fax;
    private String mail;
    private String workingHours;
    private String daysOff;
    private static final Connection connection = DatabaseConnection.getConnection();

    public Bank() {

    }

    public Bank(String name, String address, String phone, String fax, String mail, String workingHours, String daysOff) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.fax = fax;
        this.mail = mail;
        this.workingHours = workingHours;
        this.daysOff = daysOff;
    }

    public void createLoan(int bankId, int serviceId) {
        Input.inputString("");
        String loanName = Input.inputString("Loan Name: ");
        int minAmount = Input.inputIntNumber("Minimum Amount: ");
        int maxAmount = Input.inputIntNumber("Maximum Amount: ");
        int maturity  = Input.inputIntNumber("Maturity: ");
        double commissionFee = Input.inputDoubleNumber("Monthly Commission Fee: ");
        int interestRate = Input.inputIntNumber("Annual Interest Rate: ");
        double dayPenalty = Input.inputDoubleNumber("Day Penalty Percent: ");

        addLoan(bankId, serviceId, loanName, minAmount, maxAmount, maturity, commissionFee, interestRate, dayPenalty);
    }

    public void createDeposit(int bankId) {
        String depositName = Input.inputString("Deposit Name: ");
        int minAmount = Input.inputIntNumber("Minimum Amount: ");

        int depositId = addDeposit(bankId, depositName, minAmount);
        if(depositId > 0) {
            System.out.println();
            System.out.println("Add Deposit Month Ranges And It's Interest Rates");
            while(true) {
                Input.inputString("");
                System.out.println("(C) Continue");
                System.out.println("(F) Finish");

                String input = Input.inputCommand();
                if(input.equals("F")) {
                    break;
                } else {
                    int lowerBound = Input.inputIntNumber("Month Lower Bound: ");
                    int upperBound = Input.inputIntNumber("Month Upper Bound: ");
                    double interestRate = Input.inputDoubleNumber("Interest Rate: ");

                    addDepositMonthRate(depositId, lowerBound, upperBound, interestRate);
                }
            }

            System.out.println("Deposit Created Successfully!");
        }
    }

    private int addDeposit(int bankId, String depositName, int minAmount) {
        int depositId = 0;
        String sql = "insert into deposit (name, min_amount, bank_id)" +
                " values (?, ?, ?)";
        PreparedStatement insertStatement;
        try {
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, depositName);
            insertStatement.setInt(2, minAmount);
            insertStatement.setInt(3, bankId);

            if(insertStatement.executeUpdate() == 1) {
                String idSql = "select max(id) from deposit";
                Statement selectStatement;
                selectStatement = connection.createStatement();
                ResultSet rs = selectStatement.executeQuery(idSql);

                if(rs.next()) {
                    depositId = rs.getInt(1);
                }
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return depositId;
    }

    private void addDepositMonthRate(int depositId, int monthLowerBound, int monthUpperBound, double interestRate) {
        String sql = "insert into deposit_month_rate values(?, ?, ?, ?)";
        PreparedStatement insertStatement;
        try {
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setInt(1, depositId);
            insertStatement.setInt(2, monthLowerBound);
            insertStatement.setInt(3, monthUpperBound);
            insertStatement.setDouble(4, interestRate);

            insertStatement.executeUpdate();
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    private void addLoan(int bankId, int serviceId, String name, int minAmount, int maxAmount, int maturity,
                         double commissionFee, int interestRate, double dayPenalty) {
        String sql = "insert into loan (loan_type, name, min_amount, max_amount, maturity, monthly_commission_fee, " +
                "annual_interest_rate, day_penalty, bank_id, service_id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String loanTypeSql = "select name from service where id=?";
        PreparedStatement insertStatement;
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(loanTypeSql);
            selectStatement.setInt(1, serviceId);
            ResultSet loanTypeResultSet = selectStatement.executeQuery();

            String loanType = "";
            if(loanTypeResultSet.next()) {
                loanType = loanTypeResultSet.getString(1);
            }

            insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, loanType);
            insertStatement.setString(2, name);
            insertStatement.setInt(3, minAmount);
            insertStatement.setInt(4, maxAmount);
            insertStatement.setInt(5, maturity);
            insertStatement.setDouble(6, commissionFee);
            insertStatement.setInt(7, interestRate);
            insertStatement.setDouble(8, dayPenalty);
            insertStatement.setInt(9, bankId);
            insertStatement.setInt(10, serviceId);

            if(insertStatement.executeUpdate() == 1) {
                sql = "insert into bank_loan values (?, ?)";
                PreparedStatement insStatement = connection.prepareStatement(sql);
                insStatement.setInt(1, bankId);
                insStatement.setInt(2, serviceId);

                if(insStatement.executeUpdate() == 1) {
                    System.out.println("Loan Created Successfully.");
                }
            }


        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
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

            if(!userIdResultSet.next()) {
                System.out.println("There Are No Users.");
                return;
            }

            userIdResultSet.previous();
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

            if(!creditResultSet.next()) {
                System.out.println("There Are No Loaned Users.");
                return;
            }

            creditResultSet.previous();
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

            if(!depositResultSet.next()) {
                System.out.println("There Are No Deposits.");
                return;
            }

            depositResultSet.previous();
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

    public Map<String, Integer> showAccountRequests(int bankId) {
        Map<String, Integer> userCardId = new HashMap<>();
        String sql = "select * from account_request where bank_id=? and response=?";
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, bankId);
            selectStatement.setString(2, "WAITING");
            ResultSet requestResultSet = selectStatement.executeQuery();
            int userId;
            String cardId;
            while(requestResultSet.next()) {
                System.out.println("User ID: " + (userId = requestResultSet.getInt("user_id")));
                System.out.println("Card ID: " + (cardId = requestResultSet.getString("card_id")));
                System.out.println();
                userCardId.put(cardId, userId);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return userCardId;
    }

    public void createResponse(int bankId, Map<String, Integer> userCardId) {
        Scanner scanner = new Scanner(System.in);
        String cardId = "";
        int userId;
        boolean isNotCorrect = true;
        do {
            userId = Input.inputIntNumber("User ID: ");

            Set<String> cardIds = userCardId.keySet();
            for(String cId : cardIds) {
                int id = userCardId.get(cId);
                if(id == userId) {
                    isNotCorrect = false;
                    cardId = cId;
                    break;
                }
            }

            if(isNotCorrect) {
                System.out.println("There Is No User With Such ID.");
            }
        }while(isNotCorrect);

        loop: while(true) {
            System.out.print("Accept? (Y/N): ");
            String answer = scanner.nextLine().toUpperCase();

            switch(answer) {
                case "Y":
                    sendAccountResponse(userId, bankId,"ACCEPTED", cardId);
                    updateAccountRequest(userId, bankId, cardId, "ACCEPTED");
                    break loop;
                case "N":
                    sendAccountResponse(userId, bankId, "DENIED", cardId);
                    updateAccountRequest(userId, bankId, cardId, "DENIED");
                    break loop;
                default:
                    System.out.println("Enter Right Command!");
            }
        }

    }

    private void sendAccountResponse(int userId, int bankId, String response, String cardId) {
        String sql = "insert into account_response values (?, ?, ?, ?, ?)";
        PreparedStatement insertStatement;
        try {
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setInt(1, userId);
            insertStatement.setInt(2, bankId);
            insertStatement.setString(3, response);
            insertStatement.setInt(4, 0);
            insertStatement.setString(5, cardId);

            if(insertStatement.executeUpdate() == 1) {
                System.out.println("Response Send Successfully.");
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

    private void updateAccountRequest(int userId, int bankId, String cardId, String response) {
        String sql = "update account_request set response=? where user_id=? and bank_id=? and card_id=?";
        PreparedStatement updateStatement;
        try {
            updateStatement = connection.prepareStatement(sql);
            updateStatement.setString(1, response);
            updateStatement.setInt(2, userId);
            updateStatement.setInt(3, bankId);
            updateStatement.setString(4, cardId);

            if(updateStatement.executeUpdate() != 1) {
                System.out.println("Request Table Isn't Updated.");
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }
    }

    @Override
    public String toString() {
        return
                "Name: " + name + ", " +
                "Address: " + address + "\n" +
                "Phone: " + phone + ", " +
                "Fax: " + fax + "\n" +
                "E-Mail: " + mail + "\n" +
                "Working Hours: " + workingHours + "\n" +
                "Days Off: " + daysOff + "\n";
    }
}
