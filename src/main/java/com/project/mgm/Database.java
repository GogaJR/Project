package com.project.mgm;

import com.project.mgm.onlineBanking.*;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;

public class Database {
    private static List<Bank> banks;
    private static List<User> users;
    private static List<String> services;
    private static Map<String, String> accounts;

    static {
        banks = new ArrayList<>();
        banks.add(new Bank("InecoBank CJSC", "17 Tumanyan str., Yerevan",
                "(+374 10) 510-510", "(+374 10) 510-573", "inecobank@inecobank.am",
                "Monday-Friday 09:05-17:00\nSaturday 9:30-14:30", "Sunday"));
        banks.add(new Bank("ArmEconomBank OJSC", "Amiryan str. 23/1, Yerevan",
                "(+374 10) 510-910", "(+374 10) 538-904", "bank@aeb.am",
                "Monday-Friday 09:00-18:00", "Saturday and Sunday"));
        banks.add(new Bank("ArmBusinessBank CJSC", "48 Nalbandyan str., Yerevan",
                "(+374 10) 59-20-00\n(+374 60)37-25-00", "(+374 10) 59-20-64", "info@armbusinessbank.am",
                    "Monday-Friday 09:15-16:45", "Saturday and Sunday"));

        users = new ArrayList<>();

        services = new ArrayList<>();
        services.add("Crediting");
        services.add("Deposits");
        services.add("Transfers");
        services.add("Opening and servicing of accounts");

        accounts = new HashMap<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void createAccount(String mail, String password) {
        accounts.put(mail, password);
    }

    public void getBankList() {
        for(Bank bank : banks) {
            System.out.println(bank);
        }
    }

    public void getServiceList() {
        for(String service : services) {
            System.out.println(service);
        }
    }

    public boolean checkLogin(String mail, String password) {
        //TODO
        return false;
    }
}
