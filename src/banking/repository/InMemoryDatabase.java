package banking.repository;

import banking.Account;

import java.util.List;
import java.util.Optional;

public class InMemoryDatabase implements AccountsRepository {
    private List<Account> accounts;

    public InMemoryDatabase(List<Account> accounts) {
        this.accounts = accounts;
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
