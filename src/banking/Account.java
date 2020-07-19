package banking;

import java.util.Random;

import static java.lang.String.format;

public final class Account {
    private static final Random random = new Random();
    private static final String IIN = "400000";

    private final long accountNumber;
    private final int checksum;
    private int pin;
    private long balance;

    public Account(final long id) {
        accountNumber = id;
        checksum = LuhnAlgorithm.calculateChecksum(IIN + getAccountNumber());
        balance = 0;
        pin = generatePin();
    }

    public String getPinNumber() {
        return format("%04d", pin);
    }

    public String getCardNumber() {
        return format("%s%09d%d", IIN, accountNumber, checksum);
    }

    public String getAccountNumber() {
        return format("%09d", accountNumber);
    }

    private static int generatePin() {
        return random.nextInt(10000);
    }

    public long getBalance() {
        return balance;
    }
}