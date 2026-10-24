package com.scc.ui;

import com.scc.dao.UserDao;
import com.scc.model.User;

import java.util.List;
import java.util.Scanner;

public class AdminMenu {
    private final UserDao userDao;
    private final Scanner sc;

    public AdminMenu(UserDao userDao, Scanner sc) {
        this.userDao = userDao;
        this.sc = sc;
    }

    public void display(User admin) {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("     CAYUNDA'S MEAT SHOP");
            System.out.println("     Administrator Menu");
            System.out.println("=====================================");
            System.out.println("[1] User Management");
            System.out.println("[2] Product Management");
            System.out.println("[3] Supplier Management");
            System.out.println("[4] Sales Management");
            System.out.println("[5] Reports");
            System.out.println("[6] Activity Logs");
            System.out.println("[0] Logout");
            System.out.println("=====================================");
            System.out.print("Enter choice: ");

            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    userManagementMenu();
                    break;
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                    comingSoon();
                    break;
                case "0":
                    System.out.println("Logging out... Goodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 0 to 6.");
            }
        }
    }

    // ---------- USER MANAGEMENT SUBMENU ----------

    private void userManagementMenu() {
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("=====================================");
            System.out.println("          USER MANAGEMENT");
            System.out.println("=====================================");
            System.out.println("[1] View All System Users");
            System.out.println("[2] View Pending Registrations");
            System.out.println("[3] Approve User Account");
            System.out.println("[0] Back");
            System.out.println("=====================================");
            System.out.print("Enter choice: ");

            String input = sc.nextLine().trim();

            switch (input) {
                case "1":
                    System.out.println("\n--- ALL SYSTEM USERS ---");
                    printUserTable(userDao.getAllUsers());
                    break;
                case "2":
                    System.out.println("\n--- PENDING REGISTRATIONS ---");
                    printUserTable(userDao.getPendingUsers());
                    break;
                case "3":
                    approveUserAccount();
                    break;
                case "0":
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 0 to 3.");
            }
        }
    }

    private void approveUserAccount() {
        List<User> pending = userDao.getPendingUsers();

        System.out.println("\n--- APPROVE USER ACCOUNT ---");
        if (pending.isEmpty()) {
            System.out.println("-> No pending accounts to approve.");
            return;
        }

        printUserTable(pending);

        while (true) {
            System.out.print("Enter Target User ID (0 to cancel): ");
            String input = sc.nextLine().trim();

            int targetId;
            try {
                targetId = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid! Please enter a number.");
                continue;
            }

            if (targetId == 0) {
                System.out.println("-> Approval cancelled.");
                return;
            }

            User target = findById(pending, targetId);
            if (target == null) {
                System.out.println("Invalid! ID " + targetId + " is not in the pending list.");
                continue;
            }

            if (userDao.approveUser(targetId)) {
                System.out.println("-> SUCCESS: " + target.full_name() + " (" + target.username()
                        + ") is now APPROVED.");
            } else {
                System.out.println("-> Approval failed. Please try again.");
            }
            return;
        }
    }

    private User findById(List<User> users, int id) {
        for (User u : users) {
            if (u.user_id() == id) {
                return u;
            }
        }
        return null;
    }

    // ---------- TABLE HELPER (reusable sa bisan unsang List<User>) ----------

    private void printUserTable(List<User> users) {
        String format = "| %-5s | %-15s | %-25s | %-15s | %-10s |%n";
        String border = "+-------+-----------------+---------------------------+-----------------+------------+";

        System.out.println(border);
        System.out.printf(format, "ID", "Full Name", "Username", "Role", "Status");
        System.out.println(border);

        if (users.isEmpty()) {
            System.out.printf("| %-82s |%n", "No records found.");
        } else {
            for (User u : users) {
                System.out.printf(format,
                        u.user_id(),
                        truncate(u.full_name(), 15),
                        truncate(u.username(), 25),
                        truncate(u.role(), 15),
                        u.status());
            }
        }
        System.out.println(border);
        System.out.println("Total: " + users.size());
    }

    private String truncate(String text, int max) {
        if (text == null) return "";
        return text.length() <= max ? text : text.substring(0, max - 3) + "...";
    }

    private void comingSoon() {
        System.out.println(">> Coming soon.");
    }
}