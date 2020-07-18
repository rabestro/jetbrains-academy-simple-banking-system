package banking;

public final class LuhnAlgorithm {

    public static int getControlNumber(final String number) {
        int sum = 0;
        for (int i = 0; i < number.length(); i++) {
            int digit = Character.getNumericValue(number.charAt(i));
            if (i % 2 == 0) {
                digit *= 2;
            }
            if (digit > 16) {
                digit -= 9;
            }
            sum += digit;
        }
        return sum;
    }

    public static int calculateChecksum(final String number) {
        final int checksum = 10 - getControlNumber(number) % 10;
        return checksum % 10;
    }
}
