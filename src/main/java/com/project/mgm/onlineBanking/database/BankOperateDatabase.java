package com.project.mgm.onlineBanking.database;

import java.sql.*;
import java.util.Map;
import java.util.TreeMap;

public class BankOperateDatabase {
    private static final Connection connection = DatabaseConnection.getConnection();

    public int getRequestNumber(int bankId) {
        int requestNumber = 0;
        String sql = "select * from account_request where bank_id=? and response=?";
        PreparedStatement selectStatement;
        try {
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setInt(1, bankId);
            selectStatement.setString(2, "WAITING");
            ResultSet rs = selectStatement.executeQuery();

            while(rs.next()) {
                requestNumber++;
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return requestNumber;
    }

    public Map<Integer, String> getServiceList() {
        Map<Integer, String> services = new TreeMap<>();
        String sql = "select * from service";

        Statement selectServiceStatement;
        try {
            selectServiceStatement = connection.createStatement();
            ResultSet serviceResultSet = selectServiceStatement.executeQuery(sql);

            while(serviceResultSet.next()) {
                int serviceId = serviceResultSet.getInt("id");
                String serviceName = serviceResultSet.getString("name");
                services.put(serviceId, serviceName);
            }
        }catch (SQLException e) {
            System.out.println("SQL Exception.");
        }

        return services;
    }
}
