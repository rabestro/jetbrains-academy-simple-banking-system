package banking;

import banking.repository.AccountsRepository;

import java.util.Scanner;
import java.util.logging.Logger;

public final class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());

    private final Scanner scanner = new Scanner(System.in);
    private final AccountsRepository repository;

    public Application(AccountsRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run() {
        while (true) {
            System.out.println("1. Create an account\n"
                    + "2. Log into account\n" + "0. Exit");
            switch (getMenuItem()) {
                case 0:
                    System.exit(0);
                case 1:
                    createAccount();
                    break;
                case 2:
                    logIntoAccount();
                    break;
                default:
                    System.out.println("Incorrect menu number!");
            }
        }
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

        repository.getAccount(cardNumber, pinNumber)
                .ifPresentOrElse(this::manageAccount, this::wrongAccount);
    }

    private void wrongAccount() {
        log.warning("Wrong card number or PIN!");
        System.out.println("Wrong card number or PIN!");
    }

    private void manageAccount(final Account account) {
        log.info("You have successfully logged in!");
        System.out.println("You have successfully logged in!");
        while (true) {
            System.out.println("1. Balance\n" + "2. Log out\n" + "0. Exit");
            final int choice = getMenuItem();
            switch (choice) {
                case 0:
                    System.exit(0);
                case 1:
                    System.out.println("Balance: " + account.getBalance());
                    break;
                case 2:
                    return;
            }
        }

    }

    private int getMenuItem() {
        return Integer.parseInt(scanner.nextLine());
    }
}
