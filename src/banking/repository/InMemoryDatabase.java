package banking.repository;

import banking.domain.Account;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class InMemoryDatabase implements bankDatabase {
    private static final Logger log = Logger.getLogger(InMemoryDatabase.class.getName());
    private static long lastAccountNumber = 1;
    private final List<Account> accounts;

    public InMemoryDatabase(final List<Account> accounts) {
        this.accounts = accounts;
        log.warning("Used InMemory Database. The data will not be saved.");
    }

    @Override
    public Optional<Account> createAccount() {
        Account account = new Account(lastAccountNumber++);
        accounts.add(account);
        return Optional.of(account);
    }

    @Override
    public Optional<Account> findAccount(final String creditCardNumber, final String pinNumber) {
        return accounts.stream()
                .filter(account -> account.getCardNumber().equals(creditCardNumber)
                        && account.getPinNumber().equals(pinNumber))
                .findFirst();
    }

    @Override
    public Optional<Account> updateAccount(final Account account) {
        return Optional.ofNullable(account);
    }

}
