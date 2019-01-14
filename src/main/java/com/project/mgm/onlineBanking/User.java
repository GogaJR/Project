package com.project.mgm.onlineBanking;

import java.time.LocalDate;

public class User {
    private int id;
    private String name;
    private String surname;
    private String sex;
    private LocalDate dateOfBirth;
    private String countryOfBirth;
    private String cityOfBirth;
    private String countryOfLiving;
    private String cityOfLiving;
    private String mail;
    private String password;

    public User(int id, String name, String surname, String sex, LocalDate dateOfBirth,
                String coutryOfBirth, String cityOfBirth, String countryOfLiving, String cityOfLiving, String mail, String password) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.sex = sex;
        this.dateOfBirth = dateOfBirth;
        this.countryOfBirth = coutryOfBirth;
        this.cityOfBirth = cityOfBirth;
        this.countryOfLiving = countryOfLiving;
        this.cityOfLiving = cityOfLiving;
        this.mail = mail;
        this.password = password;
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
