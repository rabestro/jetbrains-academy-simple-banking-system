package banking.repository;

import banking.Account;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteDatabase implements AccountsRepository {
    private static final Logger log = Logger.getLogger(SQLiteDatabase.class.getName());

    private final String databaseName;
    private final String url;

    public SQLiteDatabase(String databaseName) {
        this.databaseName = databaseName;
        url = "jdbc:sqlite:./" + databaseName;
    }

    @Override
    public Account createAccount() {
        log.info("Create Account");
        try (final var connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);
            final var account = new Account(generateAccountId());
            final var query = String.format("insert into card (number, pin) values (%s, %s)",
                    account.getCardNumber(), account.getPinNumber());

            try (final var statement = connection.createStatement()) {
                statement.executeUpdate(query);
                connection.commit();
                return account;
            } catch (SQLException e) {
                log.log(Level.WARNING, "Can't add new account to " + databaseName, e);
            }
        } catch (SQLException e) {
            log.log(Level.WARNING, "Can't connect to " + databaseName, e);
        }
        return null;
    }

    @Override
    public Optional<Account> getAccount(String creditCardNumber, String pinNumber) {
        try (final var connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private static long generateAccountId() {
        return ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
}
