package com.juhnkim.view.consoleApplication;

import com.juhnkim.model.User;
import com.juhnkim.service.LoginService;
import com.juhnkim.service.PasswordService;
import com.juhnkim.service.UserService;
import com.juhnkim.view.consoleColors.ConsoleColors;

import java.util.List;
import java.util.Scanner;

public class UserMenu {
    private final UserService userService;
    private final LoginService loginService;
    private final Scanner scan;

    public UserMenu(UserService userService, LoginService loginService, Scanner scan) {
        this.userService = userService;
        this.loginService = loginService;
        this.scan = scan;
    }

    public void displayUserMenu(User loggedInUser) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println("                        1. Show User information                    ");
        System.out.println("                        2. Edit User information                    ");
        System.out.println("                        3. Delete User                              ");
        System.out.println("                        6. Previous                                 ");
        System.out.println("--------------------------------------------------------------------");

        int userOption = scan.nextInt();
        scan.nextLine();
        handleUserMenu(userOption, loggedInUser);
    }

    public void handleUserMenu(int userOption, User loggedInUser) {
        switch (userOption) {
            case 1:
                displayUserInformation(loggedInUser);
                break;
            case 2:
                updateUserInformation(loggedInUser);
                break;
            case 3:
                deleteExistingUser(loggedInUser);
                break;
            case 6:
                break;
            default:
                System.out.println("Invalid option. Please try again.");
        }
    }

    public void displayUserInformation(User loggedInUser) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println(ConsoleColors.GREEN);
        System.out.println("        SSN: " + loggedInUser.getSsn());
        System.out.println("        Name: " + loggedInUser.getName());
        System.out.println("        Email: " + loggedInUser.getEmail());
        System.out.println("        Address: " + loggedInUser.getAddress());
        System.out.println("        Phone: " + loggedInUser.getPhone());
        System.out.println("        Created: " + loggedInUser.getCreated());
        System.out.println(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
    }

    public void updateUserInformation(User loggedInUser) {
        System.out.println("Enter name: ");
        String name = scan.nextLine();
        System.out.println("Enter email: ");
        String email = scan.nextLine();
        System.out.println("Enter new password: ");
        String password = scan.nextLine();
        System.out.println("Enter phone: ");
        String phone = scan.nextLine();
        System.out.println("Enter address: ");
        String address = scan.nextLine();
        userService.updateUser(new User(loggedInUser.getId(), name, loggedInUser.getSsn(), email, false, phone, address, password));
    }

    public void deleteExistingUser(User loggedInUser) {
        System.out.println("--------------------------------------------------------------------");
        System.out.println(ConsoleColors.BLUE);
        System.out.println("        All your bank accounts will be closed and your account\n will permanently be deleted.");
        System.out.println("        Are you still sure you want to delete your account?         ");
        System.out.println("                                y/n:                                ");
        System.out.println(ConsoleColors.RESET);
        System.out.println("--------------------------------------------------------------------");
        String userOption = scan.nextLine();

        if (userOption.equals("y")) {

            boolean isUserDeleted = userService.deleteUser(loggedInUser);

            if(isUserDeleted){
                loginService.setUserLogged(false);

                System.out.println("--------------------------------------------------------------------");
                System.out.println(ConsoleColors.BLUE);
                System.out.println("                Your account has been deleted!                      ");
            }else{
                System.out.println("--------------------------------------------------------------------");
                System.out.println(ConsoleColors.RED);
                System.out.println("                Something went wrong.. Please try again later       ");
            }
            System.out.println(ConsoleColors.RESET);
            System.out.println("--------------------------------------------------------------------");
        }
    }
}
