package com.project.mgm.onlineBanking.bank;

import java.util.Map;

public class Bank {
    private String name;
    private String address;
    private String phone;
    private String fax;
    private String mail;
    private String workingHours;
    private String daysOff;
    private Map<String, String> loanList;

    public Bank(String name, String address, String phone, String fax, String mail, String workingHours, String daysOff,
                Map<String, String> loanList) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.fax = fax;
        this.mail = mail;
        this.workingHours = workingHours;
        this.daysOff = daysOff;
        this.loanList = loanList;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, String> getLoanList() {
        return this.loanList;
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
