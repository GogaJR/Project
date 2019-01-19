package com.project.mgm.onlineBanking.main;

import com.project.mgm.onlineBanking.database.OperateDatabase;
import com.project.mgm.onlineBanking.user.User;
import java.util.Scanner;

public class Main {
    private static Scanner scanner = new Scanner(System.in);
    private static OperateDatabase databaseOperation = new OperateDatabase();

    public static void main(String[] args) {
        System.out.println("\t\t\t\t\tWelcome To MGM Online Banking");

        while(true) {
            System.out.println("(L) Login");
            System.out.println("(R) Registration");
            System.out.println("(E) Exit");

            String input = scanner.nextLine().toUpperCase();
            switch (input) {
                case "L":
                    login();
                    break;
                case "R":
                    registration();
                    break;
                case "E":
                    return;
                default:
                    System.out.println("Enter Right Command!\n");
            }
        }
    }

    private static void login() {
        System.out.print("E-Mail: ");
        String mail = scanner.nextLine();

        System.out.print("Password: ");
        String password = scanner.nextLine();

        int id;
        if((id = databaseOperation.checkLogin(mail, password)) != 0) {
            AppUse appUse = new AppUse(id);
            if(databaseOperation.checkUserBankBond(id)) {
                appUse.cooperatedUser();
            } else {
                appUse.notCooperatedUser();
            }
        }
    }

    private static void registration() {
        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Surname: ");
        String surname = scanner.nextLine();

        String sex;
        do {
            System.out.print("Sex (M) Male, (F) Female: ");
            sex = scanner.nextLine().toUpperCase();

            if(!(sex.equals("M") || sex.equals("F"))) {
                System.out.println("Enter Right Command!");
                continue;
            }
        }while (!(sex.equals("M") || sex.equals("F")));

        int year = inputNumber("Year of Birth: ");
        int month = inputNumber("Month of Birth: ");
        int day = inputNumber("Day of Birth: ");

        System.out.print("Country of Birth: ");
        String birthCountry = scanner.nextLine();

        System.out.print("City of Birth: ");
        String birthCity = scanner.nextLine();

        System.out.print("Country of Living: ");
        String livingCountry = scanner.nextLine();

        System.out.print("City of Living: ");
        String livingCity = scanner.nextLine();

        System.out.print("Passport Serial Number: ");
        String serialNumber = scanner.nextLine();

        String mail, password;
        while(true) {
            System.out.print("E-Mail: ");
            mail = scanner.nextLine();

            System.out.print("Password: ");
            password = scanner.nextLine();

            if(databaseOperation.checkAccount(mail)) {
                break;
            }
        }

        User user = new User(name, surname, sex, year+"-"+month+"-"+day,
                birthCountry, birthCity, livingCountry, livingCity, serialNumber, mail, password);
        int userId = databaseOperation.createUser(user);

        AppUse appUse = new AppUse(userId);
        appUse.notCooperatedUser();
    }

    private static int inputNumber(String inputMessage) {
        int number = 0;
        do {
            System.out.print(inputMessage);
            if(scanner.hasNextInt()) {
                number = scanner.nextInt();
            } else {
                scanner.next();
                System.out.println("Enter Number!");
                continue;
            }
        }while(number <= 0);

        return number;
    }
}
