package banking;

import java.util.Random;

public class Account {
    private static final Random random = new Random();
    private static final String IIN = "400000";
    private static long lastAccountNumber = 1;

    private final long customerAccountNumber;
    private final int checksum;
    private int pin;

    public Account() {
        customerAccountNumber = lastAccountNumber++;
        checksum = 0;
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
}