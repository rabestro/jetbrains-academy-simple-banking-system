import banking.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleBankSystemTest extends StageTest {


    public SimpleBankSystemTest() {
        super(Main.class);
    }

    private static String correctCardNumber = "";
    private static String correctPin = "";
    private static String incorrectCardNumber = "";
    private static String incorrectPin = "";
    private static boolean isCompleted = false;

    @Override
    public List<TestCase> generate() {
        return List.of(
            //Check card credentials
            new TestCase()
                .setInput("1")
                .addInput(output -> {

                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    if (!cardNumberMatcher.find()) {
                        return new CheckResult(false, "You are printing the card number " +
                            "incorrectly. The card number should look like in the example: " +
                            "400000DDDDDDDDDD, where D is a digit.");
                    }

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!pinMatcher.find()) {
                        return new CheckResult(false, "You are printing the card PIN " +
                            "incorrectly. The PIN should look like in the example: DDDD, where D is " +
                            "a digit.");
                    }
                    correctPin = pinMatcher.group().trim();
                    correctCardNumber = cardNumberMatcher.group();

                    return "1";
                })
                .addInput(output -> {

                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    if (!cardNumberMatcher.find()) {
                        return new CheckResult(false, "You are printing the card number " +
                            "incorrectly. The card number should look like in the example: " +
                            "400000DDDDDDDDDD, where D is a digit.");
                    }

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!pinMatcher.find()) {
                        return new CheckResult(false, "You are printing the card PIN " +
                            "incorrectly. The PIN should look like in the example: DDDD, where D is " +
                            "a digit.");
                    }

                    String anotherCardNumber = cardNumberMatcher.group();

                    if (anotherCardNumber.equals(correctCardNumber)) {
                        return new CheckResult(false, "Your program generates two identical card numbers!");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check log in and log out
            new TestCase()
                .setInput("1")
                .addInput(output -> {
                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    if (!cardNumberMatcher.find()) {
                        return new CheckResult(false, "You are printing the card number " +
                            "incorrectly. The card number should look like in the example: " +
                            "400000DDDDDDDDDD, where D is a digit.");
                    }

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!pinMatcher.find()) {
                        return new CheckResult(false, "You are printing the card PIN " +
                            "incorrectly. The PIN should look like in the example: DDDD, where D is " +
                            "a digit.");
                    }
                    correctPin = pinMatcher.group().trim();
                    correctCardNumber = cardNumberMatcher.group();

                    return "2";
                })
                .addInput(output -> correctCardNumber + "\n" + correctPin)
                .addInput(output -> {

                    if (!output.toString().toLowerCase().contains("successfully")) {
                        return new CheckResult(false, "The user should be signed in after " +
                            "entering the correct card information.");
                    }

                    return "2";
                })
                .addInput(output -> {

                    if (!output.toString().toLowerCase().contains("create an account")) {
                        return new CheckResult(false, "The user should be logged out after choosing 'Log out' option");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check log in with wrong PIN
            new TestCase()
                .setInput("1")
                .addInput(output -> {
                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!cardNumberMatcher.find() || !pinMatcher.find()) {
                        return new CheckResult(false, "You should output card number and PIN like in example!");
                    }

                    correctCardNumber = cardNumberMatcher.group();
                    correctPin = pinMatcher.group();

                    Random random = new Random();

                    incorrectPin = correctPin;

                    while (correctPin.equals(incorrectPin)) {
                        incorrectPin = String.valueOf(1000 + random.nextInt(8999));
                    }

                    return "2";
                })
                .addInput(output -> correctCardNumber + "\n" + incorrectPin)
                .addInput(output -> {

                    if (output.toString().toLowerCase().contains("successfully")) {
                        return new CheckResult(false, "The user should not be signed in after" +
                            " entering incorrect card information.");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check log in to not existing account
            new TestCase()
                .setInput("1")
                .addInput(output -> {
                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!cardNumberMatcher.find() || !pinMatcher.find()) {
                        return new CheckResult(false, "You should output card number " +
                            "and PIN like in example");
                    }

                    correctPin = pinMatcher.group().trim();
                    correctCardNumber = cardNumberMatcher.group();

                    Random random = new Random();

                    incorrectCardNumber = correctCardNumber;

                    while (correctCardNumber.equals(incorrectCardNumber)) {
                        incorrectCardNumber = "400000" + (1_000_000_00 + random.nextInt(8_000_000_00));
                    }

                    return "2";
                })
                .addInput(output -> incorrectCardNumber + "\n" + correctPin)
                .addInput(output -> {

                    if (output.toString().toLowerCase().contains("successfully")) {
                        return new CheckResult(false, "The user should not be signed" +
                            " in after entering the information of a non-existing card.");
                    }

                    isCompleted = true;
                    return "0";
                }),

            // Check balance
            new TestCase()
                .setInput("1")
                .addInput(output -> {
                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!cardNumberMatcher.find() || !pinMatcher.find()) {
                        return new CheckResult(false, "You should output card number and PIN like in example");
                    }

                    correctPin = pinMatcher.group().trim();
                    correctCardNumber = cardNumberMatcher.group();

                    return "2";
                })
                .addInput(output -> correctCardNumber + "\n" + correctPin)
                .addInput(output -> "1")
                .addInput(output -> {
                    if (!output.toString().contains("0")) {
                        return new CheckResult(false, "Expected balance: 0");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check Luhn algorithm
            new TestCase()
                .setInput("1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n1\n")
                .addInput(output -> {
                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    boolean isSomeCardFound = false;
                    int foundCards = 0;

                    while (cardNumberMatcher.find()) {

                        foundCards++;

                        if (!isSomeCardFound) {
                            isSomeCardFound = true;
                        }

                        String cardNumber = cardNumberMatcher.group();

                        if (!checkLuhnAlgorithm(cardNumber)) {
                            return new CheckResult(false, String.format("The card number %s doesn’t pass the Luhn algorithm.", cardNumber));
                        }
                    }

                    if (!isSomeCardFound) {
                        return new CheckResult(false, "You should output card number and PIN like in example");
                    }

                    if (foundCards != 20) {
                        return new CheckResult(false, "Tried to generate 20 cards, but found " + foundCards);
                    }

                    isCompleted = true;
                    return "0";
                })
        );
    }

    @Override
    public CheckResult check(String reply, Object attach) {
        if (!isCompleted)
            return new CheckResult(false, "Looks like your program unexpectedly terminated without choosing 'exit' item");
        else
            isCompleted = false;
        return CheckResult.correct();
    }

    private boolean checkLuhnAlgorithm(String cardNumber) {
        int result = 0;
        for (int i = 0; i < cardNumber.length(); i++) {
            int digit = Character.getNumericValue(cardNumber.charAt(i));
            if (i % 2 == 0) {
                int doubleDigit = digit * 2 > 9 ? digit * 2 - 9 : digit * 2;
                result += doubleDigit;
                continue;
            }
            result += digit;
        }
        return result % 10 == 0;
    }
}
