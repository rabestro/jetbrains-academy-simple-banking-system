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
        log.info("Created SQLiteDatabase with url=" + url);
    }

    @Override
    public Account createAccount() {
        log.info("Create Account");
        try (final var connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);
            final var account = new Account(generateAccountId());
            final var query = String.format("INSERT INTO card (number, pin) VALUES (%s, %s)",
                    account.getCardNumber(), account.getPinNumber());

            try (final var statement = connection.createStatement()) {
                statement.executeUpdate(query);
                connection.commit();
                log.info(() -> String.format("Saved to database: Card: %s Pin: %s Balance: %d",
                        account.getCardNumber(), account.getPinNumber(), account.getBalance()));
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
    public Optional<Account> getAccount(final String creditCardNumber, final String pinNumber) {
        try (final var connection = DriverManager.getConnection(url)) {
            connection.setAutoCommit(false);

            final var query = String.format("SELECT number, pin, balance FROM card WHERE number = %s AND pin = %s",
                    creditCardNumber, pinNumber);
            try (final var statement = connection.createStatement();
                 final var resultSet = statement.executeQuery(query)) {

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
                log.log(Level.WARNING, "Can't get account from " + databaseName, e);
            }
        } catch (SQLException e) {
            log.log(Level.WARNING, "Can't connect to " + databaseName, e);
        }
        return Optional.empty();
    }

    private static long generateAccountId() {
        return ThreadLocalRandom.current().nextLong(100000000L, 999999999L);
    }
}
