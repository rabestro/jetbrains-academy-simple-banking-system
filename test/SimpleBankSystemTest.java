import banking.Main;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testcase.TestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SimpleBankSystemTest extends StageTest<String> {

    public SimpleBankSystemTest() {
        super(Main.class);
    }

    private static String correctCardNumber = "";
    private static String correctPin = "";
    private static String incorrectCardNumber = "";
    private static String incorrectPin = "";

    private static String databaseFileName = "card.s3db";
    private static String tempDatabaseFileName = "tempDatabase.s3db";

    private static Map<String, String> correctData = new HashMap<>();

    private static Connection connection;

    private static boolean isCompleted = false;

    @Override
    public List<TestCase<String>> generate() {
        return List.of(

            //Check DB file
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
                .setInput("0")
                .setCheckFunc(SimpleBankSystemTest::checkDatabaseFile),

            //Check connection to DV
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
                .setInput("0")
                .setCheckFunc(SimpleBankSystemTest::checkConnection),

            //Check card table
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
                .setInput("0")
                .setCheckFunc(SimpleBankSystemTest::checkIfTableExists),

            //Check table columns
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
                .setInput("0")
                .setCheckFunc(SimpleBankSystemTest::checkColumns),

            //Check adding row to the table
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
                .setInput("1")
                .addInput(output -> {
                    String out = output.trim().toLowerCase();
                    if (!getData(out)) {
                        return new CheckResult(false, "You should output card number and PIN like in example\n" +
                            "Or it doesn't pass the Luhn algorithm");
                    }
                    return "1";
                })
                .addInput(output -> {
                    String out = output.trim().toLowerCase();
                    if (!getData(out)) {
                        return new CheckResult(false, "You should output card number and PIN like in example\n" +
                            "Or it doesn't pass the Luhn algorithm");
                    }
                    return "1";
                })
                .addInput(output -> {
                    String out = output.trim().toLowerCase();
                    if (!getData(out)) {
                        return new CheckResult(false, "You should output card number and PIN like in example\n" +
                            "Or it doesn't pass the Luhn algorithm");
                    }
                    return "1";
                })
                .addInput(output -> {
                    isCompleted = true;
                    return "0";
                })
                .setCheckFunc(SimpleBankSystemTest::checkData),

            //Check log in
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
                .setInput("1")
                .addInput(output -> {
                    String out = output.toString().trim().toLowerCase();

                    Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
                    Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

                    if (!cardNumberMatcher.find()) {
                        return new CheckResult(false, "You are printing the card number " +
                            "incorrectly. The card number should look like in the example:" +
                            " 400000DDDDDDDDDD, where D is a digit.");
                    }

                    Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
                    Matcher pinMatcher = pinPattern.matcher(out);

                    if (!pinMatcher.find()) {
                        return new CheckResult(false, "You are printing the card PIN " +
                            "incorrectly. The PIN should look like in the example: DDDD, where D is a digit.");
                    }
                    correctPin = pinMatcher.group().trim();
                    correctCardNumber = cardNumberMatcher.group();

                    return "2";
                })
                .addInput(output -> correctCardNumber + "\n" + correctPin)
                .addInput(output -> {

                    if (!output.toString().toLowerCase().contains("successfully")) {
                        return new CheckResult(false, "The user should be signed in after" +
                            " entering the correct card information.");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check log in with wrong PIN
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
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
                        return new CheckResult(false, "The user should not be signed in" +
                            " after entering incorrect card information.");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check log in to not existing account
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
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
                        return new CheckResult(false, "The user should not be signed in " +
                            "after entering the information of a non-existing card.");
                    }

                    isCompleted = true;
                    return "0";
                }),
            //Check balance
            new TestCase<String>()
                .addArguments("-fileName", databaseFileName)
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
                })

        );
    }

    private static CheckResult checkDatabaseFile(String attach, String reply) {

        File file = new File(databaseFileName);
        boolean result = file.exists();

        if (!result) {
            return new CheckResult(false, "You should create a database file " +
                "named " + databaseFileName + ". The file name should be taken from the command line arguments.");
        }
        return new CheckResult(true);

    }

    private static CheckResult checkColumns(String attach, String reply) {
        boolean result = true;
        try {
            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("PRAGMA table_info(card);");

            Map<String, String> columns = new HashMap<>();

            while (resultSet.next()) {
                columns.put(resultSet.getString("name"), resultSet.getString("type"));
            }

            String[][] correctColumns = {{"id", "INTEGER"}, {"number", "TEXT"}, {"pin", "TEXT"}, {"balance", "INTEGER"}};

            for (String[] correctColumn : correctColumns) {
                if (!columns.containsKey(correctColumn[0])) {
                    result = false;
                    break;
                } else if (!columns.get(correctColumn[0]).equals(correctColumn[1])) {
                    result = false;
                    break;
                }
            }

            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
            return CheckResult.wrong("Can't connect the database!");
        }

        if (!result)
            return new CheckResult(false, "Your table should have columns described in " +
                "the stage instructions.");

        return new CheckResult(true);
    }

    private static CheckResult checkIfTableExists(String attach, String reply) {

        boolean result = false;

        try {

            Connection connection = getConnection();
            Statement statement = connection.createStatement();


            ResultSet resultSet = statement.executeQuery(
                "SELECT \n" +
                    "    name\n" +
                    "FROM \n" +
                    "    sqlite_master \n" +
                    "WHERE \n" +
                    "    type ='table' AND \n" +
                    "    name NOT LIKE 'sqlite_%';");

            while (resultSet.next()) {
                if (resultSet.getString("name").equals("card")) {
                    result = true;
                }
            }


        } catch (SQLException e) {
            return new CheckResult(false, "Can't execute a query in your database! Make sure that your database isn't broken!");
        }

        if (!result) {
            return new CheckResult(false, "Your database doesn’t have a table named ‘card’");
        } else {
            return new CheckResult(true);
        }

    }

    private static CheckResult checkConnection(String attach, String reply) {
        Connection connection = getConnection();
        if (connection == null) {
            closeConnection();
            return new CheckResult(false, "Cannot access the database");
        }
        closeConnection();
        return new CheckResult(true);
    }

    private static CheckResult checkData(String attach, String reply) {
        try {

            Connection connection = getConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM card");

            Map<String, String> userData = new HashMap<>();

            while (resultSet.next()) {
                if (resultSet.getInt("balance") != 0) {
                    return new CheckResult(false, "Expected balance is 0");
                }
                userData.put(resultSet.getString("number"), resultSet.getString("pin"));
            }

            for (Map.Entry<String, String> entry : correctData.entrySet()) {
                if (!userData.containsKey(entry.getKey())) {
                    return new CheckResult(false, "Your database doesn’t save newly created cards.");
                } else if (!userData.get(entry.getKey()).equals(entry.getValue())) {
                    return new CheckResult(false, "Correct PIN for card number " + entry.getKey() + " should " +
                        "be " + entry.getValue());
                }
            }

            closeConnection();

        } catch (SQLException e) {
            e.printStackTrace();
            return CheckResult.wrong("Can't connect the database!");
        }

        if (!isCompleted)
            return new CheckResult(false, "Looks like your program unexpectedly terminated without choosing 'exit' item");
        else
            isCompleted = false;
        return CheckResult.correct();
    }

    @Override
    public CheckResult check(String reply, String attach) {
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

    @BeforeClass
    public static void createTempDatabase() throws IOException {

        Path userDbFile = Paths.get(databaseFileName);
        Path tempDbFile = Paths.get(tempDatabaseFileName);

        if (!userDbFile.toFile().exists())
            return;

        Files.deleteIfExists(tempDbFile);
        Files.copy(userDbFile, tempDbFile);

    }

    @AfterClass
    public static void deleteTempDatabase() throws IOException {

        Path userDbFile = Paths.get(databaseFileName);
        Path tempDbFile = Paths.get(tempDatabaseFileName);

        if (!tempDbFile.toFile().exists())
            return;

        Files.deleteIfExists(userDbFile);
        Files.move(tempDbFile, userDbFile);

    }

    private static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:sqlite:" + databaseFileName);
            } catch (SQLException ignored) {
            }
        }
        return connection;
    }

    private static void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ignored) {
        }
        connection = null;
    }

    private boolean getData(String out) {

        Pattern cardNumberPattern = Pattern.compile("400000\\d{10}");
        Matcher cardNumberMatcher = cardNumberPattern.matcher(out);

        Pattern pinPattern = Pattern.compile("^\\d{4}$", Pattern.MULTILINE);
        Matcher pinMatcher = pinPattern.matcher(out);

        if (!cardNumberMatcher.find() || !pinMatcher.find()) {
            return false;
        }

        String number = cardNumberMatcher.group();
        String PIN = pinMatcher.group();

        if (!checkLuhnAlgorithm(number)) {
            return false;
        }

        correctData.put(number, PIN);

        return true;
    }
}
