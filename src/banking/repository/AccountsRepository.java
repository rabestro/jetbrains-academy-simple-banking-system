package banking.repository;

import banking.Account;

import java.util.Optional;

public interface AccountsRepository {
    void addAccount(Account account);

    default Account createAccount() {
        Account account = new Account();
        addAccount(account);
        return account;
    }

    Optional<Account> getAccount(final String creditCardNumber, final String pinNumber);
}
