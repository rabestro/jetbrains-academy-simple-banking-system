package banking.repository;

import banking.Account;

public interface AccountsRepository {
    void addAccount(Account account);

    default Account createAccount() {
        Account account = new Account();
        addAccount(account);
        return account;
    }
}
