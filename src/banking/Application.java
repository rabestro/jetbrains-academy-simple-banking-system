package banking;

import banking.repository.AccountsRepository;

import java.util.Scanner;
import java.util.logging.Logger;

public class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());

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
                createAccount();
                break;
            case 2:
                logIntoAccount();
                break;
            default:
                System.out.println("Incorrect number!");
        }
        return true;
    }

    private void createAccount() {
        log.info("Create an account");

        final var account = repository.createAccount();

        System.out.println("Your card has been created\n" +
                "Your card number:\n" + account.getCardNumber() +
                "\nYour card PIN:\n" + account.getPinNumber());
    }

    private void logIntoAccount() {
        log.info("Log into account");

        System.out.println("Enter your card number:");
        final var cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        final var pinNumber = scanner.nextLine();
    }
}
