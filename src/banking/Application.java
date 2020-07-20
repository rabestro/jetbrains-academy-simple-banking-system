package banking;

import banking.domain.Account;
import banking.repository.bankDatabase;

import java.util.Scanner;
import java.util.logging.Logger;

public final class Application implements Runnable {
    private static final Logger log = Logger.getLogger(Application.class.getName());

    private final Scanner scanner = new Scanner(System.in);
    private final bankDatabase repository;

    public Application(bankDatabase repository) {
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
        log.info("1. Create an account");
        repository.createAccount()
                .ifPresentOrElse(this::printCardCreated,
                        () -> System.out.println("Can't create an account"));
    }

    private void printCardCreated(Account card) {
        System.out.println("Your card has been created\n" +
                "Your card number:\n" + card.getCardNumber() +
                "\nYour card PIN:\n" + card.getPinNumber());

    }

    private void logIntoAccount() {
        log.info("Log into account");

        System.out.println("Enter your card number:");
        final var cardNumber = scanner.nextLine();
        System.out.println("Enter your PIN:");
        final var pinNumber = scanner.nextLine();

        repository.findAccount(cardNumber, pinNumber)
                .ifPresentOrElse(this::manageAccount, this::wrongAccount);
    }

    private void wrongAccount() {
        log.warning("Wrong card number or PIN!");
        System.out.println("Wrong card number or PIN!");
    }

    private void manageAccount(final Account account) {
        log.info("The user has successfully logged in!");
        System.out.println("You have successfully logged in!");
        while (true) {
            System.out.println(String.join("\n",
                    "1. Balance",
                    "2. Add income",
                    "3. Do transfer",
                    "4. Close account",
                    "5. Log out",
                    "0. Exit"));
            final int choice = getMenuItem();
            switch (choice) {
                case 0:
                    System.exit(0);
                case 1:
                    System.out.println("Balance: " + account.getBalance());
                    break;
                case 2:
                    System.out.println("Enter income:");

                    break;
                case 3:
                    System.out.println("Transfer");
                    break;
                case 4:
                    System.out.println("The account has been closed!");
                    break;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
            }
        }

    }

    private int getMenuItem() {
        return Integer.parseInt(scanner.nextLine());
    }
}
