package Service;

import DAO.AccountDAO;
import Model.Account;


public class AccountService {

    private AccountDAO accountDao;

    public AccountService() {
        this.accountDao = new AccountDAO();
    }

    /**
     * Validate user's submitted information.
     * If valid, call database to register account.
     * @param account The account object with user's submitted information.
     * @return account credentials
     */
    public Account registerAccount(Account account) {
        if (account.getUsername() == null
        || account.getUsername().equals("") 
        || account.getPassword().length() < 4
        || accountDao.getAccount(account) != null) 
        return null;

        return accountDao.registerAccount(account);
    }

    /**
     * Retrieve the account with the user provided information.
     * If there is a match, return the account credentials, otherwise null.
     * @param account The account object with user's submitted information.
     * @return account credentials
     */
    public Account authenticateAccount(Account account) {
        return accountDao.authenticateAccount(account);
    }
}
