package banking.domain;

import java.util.Random;

import static java.lang.String.format;

public final class Account {
    private static final Random random = new Random();
    private static final String IIN = "400000";

    private final String cardNumber;
    private String pinNumber;
    private long balance;

    public Account(final long id) {
        var checksum = LuhnAlgorithm.calculateChecksum(IIN + format("%09d", id));
        balance = 0;
        cardNumber = format("%s%09d%d", IIN, id, checksum);
        pinNumber = format("%04d", generatePin());
    }

    public Account(String card, String pin, long balance) {
        this.cardNumber = card;
        this.pinNumber = pin;
        this.balance = balance;
    }

    public String getPinNumber() {
        return pinNumber;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public long getBalance() {
        return balance;
    }

    public void addIncome(final long income) {
        balance += income;
    }

    public static AccountBuilderCard builder() {
        return card -> pin -> balance -> () -> new Account(card, pin, balance);
    }

    public interface AccountBuilderCard {
        AccountBuilderPin setCard(final String cardNumber);
    }

    public interface AccountBuilderPin {
        AccountBuilderBalance setPin(final String pin);
    }

    public interface AccountBuilderBalance {
        AccountBuilder setBalance(final long balance);
    }

    public interface AccountBuilder {
        Account build();
    }

    private static int generatePin() {
        return random.nextInt(10000);
    }

}