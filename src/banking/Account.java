package banking;

public class Account {
    private static final String IIN = "400000";
    private static long lastAccountNumber = 1;

    private long customerAccountNumber;
    private int checksum = 0;

    private String pin;

    public String getCardNumber() {
        return String.format("%s%09d%d", IIN, customerAccountNumber, checksum);
    }
}