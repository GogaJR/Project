package com.project.mgm;

import com.project.mgm.onlineBanking.*;

import java.time.LocalDate;
import java.util.*;

public class Database {
    private static List<Bank> banks;
    private static List<User> users;
    private static List<String> services;
    private static Map<String, String> accounts;

    static {
        banks = new ArrayList<>();

        Map<String, String> inecoLoanList = new HashMap<>();
        inecoLoanList.put("Fast Cunsomer Loan", "Cunsomer Loan");
        inecoLoanList.put("Secured Cunsomer Loan", "Cunsomer Loan");
        inecoLoanList.put("Affordable Housing For Young Family", "Mortgage Loan");
        inecoLoanList.put("National Mortgage Company", "Mortgage Loan");
        banks.add(new Bank("InecoBank CJSC", "17 Tumanyan str., Yerevan",
                "(+374 10) 510-510", "(+374 10) 510-573", "inecobank@inecobank.am",
                "Monday-Friday 09:05-17:00, Saturday 9:30-14:30", "Sunday", inecoLoanList));

        Map<String, String> aebLoanList = new HashMap<>();
        aebLoanList.put("Housing Microcredit", "Cunsomer Loan");
        aebLoanList.put("Solar", "Cunsomer Loan");
        aebLoanList.put("Mortgage Loan (Acquisition)", "Mortgage Loan");
        aebLoanList.put("Mortgage Loan (Renovation, construction)", "Mortgage Loan");
        banks.add(new Bank("ArmEconomBank OJSC", "Amiryan str. 23/1, Yerevan",
                "(+374 10) 510-910", "(+374 10) 538-904", "bank@aeb.am",
                "Monday-Friday 09:00-18:00", "Saturday and Sunday", aebLoanList));

        Map<String, String> abbLoanList = new HashMap<>();
        abbLoanList.put("ABB-Tourist", "Cunsomer Loan");
        abbLoanList.put("ABB-First-Class", "Cunsomer Loan");
        abbLoanList.put("ABB-Auto", "Car Loan");
        banks.add(new Bank("ArmBusinessBank CJSC", "48 Nalbandyan str., Yerevan",
                "(+374 10) 59-20-00, (+374 60)37-25-00", "(+374 10) 59-20-64", "info@armbusinessbank.am",
                    "Monday-Friday 09:15-16:45", "Saturday and Sunday", abbLoanList));

        users = new ArrayList<>();
        users.add(new User(1, "Georgi", "Shahnazaryan", "Male", LocalDate.of(1998, 6, 11),
                "Armenia", "Yerevan", "Armenia", "Yerevan",
                "goga", "1234"));

        services = new ArrayList<>();
        services.add("Opening and servicing of accounts");
        services.add("Crediting");
        services.add("Deposits");
        services.add("Transfers");

        accounts = new HashMap<>();
        accounts.put("goga", "1234");
    }

    public void addUser(User user) {
        users.add(user);
    }

    private void createAccount(String mail, String password) {
        accounts.put(mail, password);
    }

    public boolean checkAccount(String mail, String password) {
        if(!accounts.containsKey(mail)) {
            createAccount(mail, password);
            return true;
        }

        return false;
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
        Set<String> mails = accounts.keySet();
        for(String m : mails) {
            if(m.equals(mail)) {
                if(accounts.get(m).equals(password)) {
                    return true;
                }
            }
        }

        return false;
    }

    public Bank getBank(String bankName) {
        Bank bank = null;
        for(Bank b : banks) {
            if(b.getName().equals(bankName)) {
                bank = b;
            }
        }

        return bank;
    }
}
