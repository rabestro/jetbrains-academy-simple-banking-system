package banking.repository;

import banking.Account;

import java.util.Optional;

public interface AccountsRepository {
    Optional<Account> createAccount();

    Optional<Account> getAccount(final String creditCardNumber, final String pinNumber);
}
