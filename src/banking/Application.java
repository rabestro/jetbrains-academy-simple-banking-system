package banking;

import banking.domain.Account;
import banking.domain.LuhnAlgorithm;
import banking.menu.Menu;
import banking.repository.bankDatabase;

import java.util.Scanner;
import java.util.logging.Logger;

public final class Application {
    private static final Logger log = Logger.getLogger(Application.class.getName());

    private final Scanner scanner = new Scanner(System.in);
    private final bankDatabase repository;

    public Application(bankDatabase repository) {
        this.repository = repository;
    }

    public void createAccount() {
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

    public void logIntoAccount() {
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
        log.info(() -> "The user has logged in #" + account.getCardNumber());
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
                    account.addIncome(Long.parseLong(scanner.nextLine()));
                    repository.updateAccount(account)
                            .ifPresentOrElse(
                                    a -> System.out.println("Income was added!"),
                                    () -> System.out.println("Account was not updated!"));
                    break;
                case 3:
                    transfer(account);
                    break;
                case 4:
                    if (repository.deleteAccount(account)) {
                        System.out.println("The account has been closed!");
                        return;
                    }
                case 5:
                    System.out.println("You have successfully logged out!");
                    return;
            }
        }

    }

    private int getMenuItem() {
        return Integer.parseInt(scanner.nextLine());
    }

    private void transfer(final Account account) {
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        final var cardNumber = scanner.nextLine();
        if (!LuhnAlgorithm.isCorrectNumber(cardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
        final var recipient = repository.findAccount(cardNumber);

        if (recipient.isEmpty()) {
            System.out.println("Such a card does not exist.");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        final var money = Long.parseLong(scanner.nextLine());
        if (money > account.getBalance()) {
            System.out.println("Not enough money!");
            return;
        }
        account.addIncome(-money);
        recipient.get().addIncome(money);
        repository.updateAccount(account);
        repository.updateAccount(recipient.get());
        System.out.println("Success!");
    }
}
