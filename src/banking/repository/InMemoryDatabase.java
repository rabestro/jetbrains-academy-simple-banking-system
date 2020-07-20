package banking.repository;

import banking.Account;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InMemoryDatabase implements AccountsRepository {
    private static final Logger log = Logger.getLogger(SQLiteDatabase.class.getName());
    private static long lastAccountNumber = 1;
    private final List<Account> accounts;

    public InMemoryDatabase(List<Account> accounts) {
        this.accounts = accounts;
        log.warning("Used InMemory Database. The data will not be saved.");
    }

    @Override
    public Optional<Account>  createAccount() {
        Account account = new Account(lastAccountNumber++);
        accounts.add(account);
        return Optional.of(account);
    }

    @Override
    public Optional<Account> getAccount(String creditCardNumber, String pinNumber) {
        return accounts.stream()
                .filter(account -> account.getCardNumber().equals(creditCardNumber)
                        && account.getPinNumber().equals(pinNumber))
                .findFirst();
    }
}
