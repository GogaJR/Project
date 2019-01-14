package com.project.mgm.onlineBanking;

public class Bank {
    private String name;
    private String address;
    private String phone;
    private String fax;
    private String mail;
    private String workingHours;
    private String daysOff;

    public Bank(String name, String address, String phone, String fax, String mail, String workingHours, String daysOff) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.fax = fax;
        this.mail = mail;
        this.workingHours = workingHours;
        this.daysOff = daysOff;
    }

    @Override
    public String toString() {
        return
                "Name: " + name + ", " +
                "Address: " + address + "\n" +
                "Phone: " + phone + ", " +
                "Fax: " + fax + "\n" +
                "E-Mail: " + mail + "\n" +
                "Working Hours: " + workingHours + ", " +
                "Days Off: " + daysOff;
    }
}
