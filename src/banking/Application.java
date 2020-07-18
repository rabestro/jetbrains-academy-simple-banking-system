package banking;

import java.util.Scanner;

public class Application implements Runnable {
    private final Scanner scanner = new Scanner(System.in);
    private final AccountsRepository repository;

    public Application(AccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        while (menu()) {
        }
    }

    private boolean menu() {
        System.out.println("1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit");
        switch (scanner.nextInt()) {
            case 0:
                return false;
            case 1:
            case 2:
            default:
                System.out.println("Incorrect number!");
        }
        return true;
    }

    private void createAccount() {
        System.out.println("Your card has been created\n" +
                "Your card number:\n" +
                "4000004938320895\n" +
                "Your card PIN:\n" +
                "6826");
    }

    private String createCardNumber() {
        return "4000004938320895";
    }
}
