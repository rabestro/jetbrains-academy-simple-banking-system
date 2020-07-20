package banking.repository;

import banking.Account;

import java.util.Optional;

public class SQLiteDatabase implements AccountsRepository {
    private final String databaseName;
    private final String url;

    public SQLiteDatabase(String databaseName) {
        this.databaseName = databaseName;
        url = "jdbc:sqlite:./" + databaseName;
    }

    @Override
    public void addAccount(Account account) {

    }

    @Override
    public Account createAccount() {
        return null;
    }

    @Override
    public Optional<Account> getAccount(String creditCardNumber, String pinNumber) {
        return Optional.empty();
    }
}
