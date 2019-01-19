package com.project.mgm.onlineBanking.user;

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
