package banking.repository;

import banking.Account;

import java.util.List;
import java.util.Optional;

public class InMemoryDatabase implements AccountsRepository {
    private static long lastAccountNumber = 1;
    private List<Account> accounts;

    public InMemoryDatabase(List<Account> accounts) {
        this.accounts = accounts;
    }

    @Override
    public Account createAccount() {
        Account account = new Account(lastAccountNumber++);
        addAccount(account);
        return account;
    }

    @Override
    public void addAccount(Account account) {
        accounts.add(account);
    }

    @Override
    public Optional<Account> getAccount(String creditCardNumber, String pinNumber) {
        return accounts.stream()
                .filter(account -> account.getCardNumber().equals(creditCardNumber)
                        && account.getPinNumber().equals(pinNumber))
                .findFirst();
    }
}
