package com.project.mgm.onlineBanking.main;

import com.project.mgm.onlineBanking.database.BankOperateDatabase;
import com.project.mgm.onlineBanking.database.UserOperateDatabase;
import com.project.mgm.onlineBanking.user.User;

import java.util.Scanner;

public class MainController {
    private static Scanner scanner = new Scanner(System.in);
    private static UserOperateDatabase userDatabaseOperation = new UserOperateDatabase();
    private static BankOperateDatabase bankDatabaseOperation = new BankOperateDatabase();

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
        if((id = userDatabaseOperation.checkUserLogin(mail, password)) != 0) {
            AppController appController = new AppController(id);
            if(userDatabaseOperation.checkUserBankBond(id)) {
                appController.cooperatedUser();
            } else {
                appController.notCooperatedUser();
            }
        } else if((id = bankDatabaseOperation.checkBankLogin(mail, password)) != 0) {
            AppController appController = new AppController(id);
            appController.bankMain();
        } else {
            System.out.println("Wrong E-Mail or Password!Try Again!\n");
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

            if(userDatabaseOperation.checkAccount(mail)) {
                break;
            }
        }

        User user = new User(name, surname, sex, year+"-"+month+"-"+day,
                birthCountry, birthCity, livingCountry, livingCity, serialNumber, mail, password);
        int userId = userDatabaseOperation.createUser(user);

        AppController appController = new AppController(userId);
        appController.notCooperatedUser();
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
