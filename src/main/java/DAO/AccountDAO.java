package DAO;

import Util.ConnectionUtil;
import Model.Account;

import java.sql.*;

public class AccountDAO {
    /**
     * Register a new account.
     * @param account 
     * @return account with full credentials
     */
    public Account registerAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        Account registeredAccount = null;
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?,?)";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            int affectedRow = ps.executeUpdate();
            if (affectedRow >= 1) {
                registeredAccount = getAccount(account);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return registeredAccount;
    }

    /**
     * Retrieve the account via username in the database.
     * @param account The account in question.
     * @return account if existed, null otherwise.
     */
    public Account getAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        Account selectedAccount = null;
        try {
            String sql = "SELECT * FROM account WHERE username LIKE ? ";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                selectedAccount = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return selectedAccount;
    }

    /**
     * Authenticate an account by finding an exisiting account
     * in the database with the user's provided account.
     * @param account Account with username and password.
     * @return account with full credential
     */
    public Account authenticateAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        Account authenticatedAccount = null;
        try {
            String sql = "SELECT * FROM account WHERE username LIKE ? AND password LIKE ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                authenticatedAccount = new Account(
                    rs.getInt("account_id"), 
                    rs.getString("username"),
                    rs.getString("password")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return authenticatedAccount;
    }
}
