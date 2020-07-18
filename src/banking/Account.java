package banking;

public class Account {
    private static final String IIN = "400000";
    private static long lastAccountNumber = 1;

    private final long customerAccountNumber;
    private final int checksum;
    private String pin;

    public Account() {
        customerAccountNumber = lastAccountNumber++;
        checksum = 0;
    }

    public String getCardNumber() {
        return String.format("%s%09d%d", IIN, customerAccountNumber, checksum);
    }

}