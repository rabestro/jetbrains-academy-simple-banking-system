package banking;

import java.util.Random;

public class Account {
    private static final Random random = new Random();
    private static final String IIN = "400000";
    private static long lastAccountNumber = 1;

    private final long customerAccountNumber;
    private final int checksum;
    private int pin;
    private long balance;

    public Account() {
        customerAccountNumber = lastAccountNumber++;
        checksum = 0;
        balance = 0;
        pin = generatePin();
    }

    public String getPinNumber() {
        return String.format("%04d", pin);
    }

    public String getCardNumber() {
        return String.format("%s%09d%d", IIN, customerAccountNumber, checksum);
    }

    private static int generatePin() {
        return random.nextInt(10000);
    }

    public long getBalance() {
        return balance;
    }
}