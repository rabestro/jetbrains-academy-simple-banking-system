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
        accounts.add(account);
        return account;
    }

    @Override
    public Optional<Account> getAccount(String creditCardNumber, String pinNumber) {
        return accounts.stream()
                .filter(account -> account.getCardNumber().equals(creditCardNumber)
                        && account.getPinNumber().equals(pinNumber))
                .findFirst();
    }
}
