package banking.repository;

import banking.domain.Account;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDatabase implements bankDatabase {
    private static final Logger log = Logger.getLogger(SQLiteDatabase.class.getName());
    private static final String SQL_ADD_ACCOUNT = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private static final String SQL_FIND_ACCOUNT = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";
    private static final String SQL_UPDATE_ACCOUNT = "UPDATE card SET balance = ? WHERE number = ?";

    private final String databaseName;
    private final String url;

    public SQLiteDatabase(final String databaseName) {
        this.databaseName = databaseName;
        url = "jdbc:sqlite:./" + databaseName;
        log.info("Used SQLite3 Database with url=" + url);
    }

    @Override
    public Optional<Account> createAccount() {
        final var account = new Account(generateAccountId());
        log.info(() -> "Create Account #" + account.getCardNumber());

        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_ADD_ACCOUNT)) {

            sql.setString(1, account.getCardNumber());
            sql.setString(2, account.getPinNumber());
            sql.executeUpdate();

            log.info(() -> String.format("Saved to database: Card: %s Pin: %s Balance: %d",
                    account.getCardNumber(), account.getPinNumber(), account.getBalance()));

            return Optional.of(account);
        } catch (SQLException e) {
            log.log(Level.WARNING, "Can't add account to " + databaseName, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> findAccount(final String creditCardNumber, final String pinNumber) {
        log.info(() -> "Searching for account #" + creditCardNumber);

        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_FIND_ACCOUNT)) {
            sql.setString(1, creditCardNumber);
            sql.setString(2, pinNumber);
            final var resultSet = sql.executeQuery();
            if (!resultSet.next()) {
                return Optional.empty();
            }
            final var bankAccount = Account.builder()
                    .setCard(resultSet.getString("number"))
                    .setPin(resultSet.getString("pin"))
                    .setBalance(resultSet.getInt("balance"))
                    .build();
            return Optional.of(bankAccount);
        } catch (SQLException e) {
            log.log(Level.WARNING, "Can't connect to " + databaseName, e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Account> updateAccount(final Account account) {
        log.info(() -> "Update Account #" + account.getCardNumber());

        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_UPDATE_ACCOUNT)) {

            sql.setLong(1, account.getBalance());
            sql.setString(2, account.getCardNumber());
            sql.executeUpdate();

            log.info(() -> String.format("Updated card# %s with balance: %d",
                    account.getCardNumber(), account.getBalance()));

            return Optional.of(account);
        } catch (SQLException e) {
            log.log(Level.WARNING, "Can't add account to " + databaseName, e);
        }
        return Optional.empty();

    }

    @Override
    public boolean deleteAccount(final Account account) {
        return false;
    }

    private static long generateAccountId() {
        return ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
}
