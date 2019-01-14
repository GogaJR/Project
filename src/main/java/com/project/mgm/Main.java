package com.project.mgm;

import com.project.mgm.onlineBanking.User;
import java.util.Scanner;
import java.time.LocalDate;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static AppUse appUse = new AppUse();
    private static Database db = new Database();
    private static int userId = 0;

    public static void main(String[] args) {
        while(true) {
            System.out.println("\t\t\t\t\tWelcome To MGM Online Banking");
            System.out.println("(L) Login");
            System.out.println("(R) Registration");
            System.out.println("(E) Exit");

            String input = scanner.next().toUpperCase();
            switch (input) {
                case "L":
                    login();
                    break;
                case "R":
                    registration();
                    appUse.start();
                    break;
                case "E":
                    return;
                default:
                    System.out.println("Enter right command!\n");
            }
        }
    }

    private static void login() {
        System.out.print("E-Mail: ");
        String mail = scanner.next();

        System.out.print("Password: ");
        String password = scanner.next();

        if(db.checkLogin(mail, password)) {
            appUse.start();
        } else {
            System.out.println("Wrong e-mail or password! Try again.\n");
        }
    }

    private static void registration() {
        System.out.print("Name: ");
        String name = scanner.next();

        System.out.print("Surname: ");
        String surname = scanner.next();

        System.out.print("Sex: ");
        String sex = scanner.next();

        int year = inputNumber("Year of Birth: ", 1900, 1998);
        int month = inputNumber("Month of Birth: ", 1, 12);
        int day = inputNumber("Day of Birth: ", 1, 31);

        System.out.print("Country of Birth: ");
        String birthCountry = scanner.next();

        System.out.print("City of Birth: ");
        String birthCity = scanner.next();

        System.out.print("Country of Living: ");
        String livingCountry = scanner.next();

        System.out.print("City of Living: ");
        String livingCity = scanner.next();

        System.out.print("E-Mail: ");
        String mail = scanner.next();

        System.out.print("Password: ");
        String password = scanner.next();

        User user = new User(++userId, name, surname, sex, LocalDate.of(year, month, day),
                birthCountry, birthCity, livingCountry, livingCity, mail, password);
        db.addUser(user);
        db.createAccount(mail, password);

        System.out.println("Your registration is done successfully!");
    }

    private static int inputNumber(String inputMessage, int lowerBoard, int upperBoard) {
        int number = 0;
        do {
            System.out.print(inputMessage);
            if(scanner.hasNextInt()) {
                number = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Enter an integer!");
                continue;
            }

            if(number < lowerBoard || number > upperBoard) {
                System.out.println("Enter valid number from " + lowerBoard + " to " + upperBoard + "!");
            }
        }while(number < lowerBoard || number > upperBoard);

        return number;
    }
}
