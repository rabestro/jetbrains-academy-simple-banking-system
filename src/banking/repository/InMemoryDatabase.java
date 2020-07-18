package banking.repository;

import banking.Account;

import java.util.List;

public class InMemoryDatabase implements AccountsRepository {
    private List<Account> accounts;

    @Override
    public void addAccount(Account account) {

    }
}
