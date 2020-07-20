package banking.repository;

import banking.domain.Account;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDatabase implements AccountsRepository {
    private static final Logger log = Logger.getLogger(SQLiteDatabase.class.getName());
    private static final String SQL_ADD_ACCOUNT = "INSERT INTO card (number, pin) VALUES (?, ?)";
    private static final String SQL_FIND_ACCOUNT = "SELECT number, pin, balance FROM card WHERE number = ? AND pin = ?";

    private final String databaseName;
    private final String url;

    public SQLiteDatabase(String databaseName) {
        this.databaseName = databaseName;
        url = "jdbc:sqlite:./" + databaseName;
        log.info("Used SQLite3 Database with url=" + url);
    }

    @Override
    public Optional<Account> createAccount() {
        log.info("Create Account");
        try (final var connection = DriverManager.getConnection(url);
             final var sql = connection.prepareStatement(SQL_ADD_ACCOUNT)) {

            final var account = new Account(generateAccountId());
            sql.setString(1, account.getCardNumber());
            sql.setString(2, account.getPinNumber());
            log.finest(sql::toString);
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
    public Optional<Account> getAccount(final String creditCardNumber, final String pinNumber) {
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

    private static long generateAccountId() {
        return ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
}
