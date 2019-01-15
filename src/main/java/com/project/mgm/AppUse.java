package com.project.mgm;

import java.util.Scanner;

public class AppUse {
    private static Scanner scanner = new Scanner(System.in);
    private static Database db = new Database();
    private static ServiceUsing useService = new ServiceUsing();

    public void start() {
        while(true) {
            System.out.println("\n(S) Show Service List");
            System.out.println("(B) Show Bank List");
            System.out.println("(U) To Make Use of Service");
            System.out.println("(Q) Quit");

            String input = scanner.next().toUpperCase();
            switch (input) {
                case "S":
                    db.getServiceList();
                    break;
                case "B":
                    db.getBankList();
                    break;
                case "U":
                    useService.chooseService();
                    break;
                case "Q":
                    return;
                default:
                    System.out.println("Enter right command!\n");
            }
        }
    }
}
