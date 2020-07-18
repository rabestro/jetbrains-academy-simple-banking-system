package banking;

public interface AccountsRepository {
    void addAccount(Account account);

    default Account createAccount() {
        Account account = new Account();
        addAccount(account);
        return account;
    }
}
