package banking.repository;

import banking.domain.Account;

import java.util.Optional;

public interface bankDatabase {
    Optional<Account> createAccount();

    Optional<Account> findAccount(final String creditCardNumber, final String pinNumber);

    void updateAccount(final Account account);
}
