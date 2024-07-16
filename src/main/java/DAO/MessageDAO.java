package DAO;

import Util.ConnectionUtil;
import Model.Message;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class MessageDAO {
    /**
     * Retrieve all messages in database.
     * @return a list of message objects
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messageList = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()) {
                messageList.add( new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                    )
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } 
        return messageList;
    }

    /**
     * Insert the message object into database.
     * @param message
     * @return message objects with full details.
     */
    public Message createMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        Message createdMessage = null;
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            int affectedRow = ps.executeUpdate();
            if (affectedRow == 0) {
                throw new SQLException("Inserting message failed, no row affected.");
            }
            ResultSet generatedKey = ps.getGeneratedKeys();
            if (generatedKey.next()) {
                int id = generatedKey.getInt(1);
                createdMessage = getMessageByMessageId(id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return createdMessage; 
    }

    /**
     * Check against database for an account id
     * @param message
     * @return a boolean indicates whether the account exist.
     */
    public boolean isAccountIdExist(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        int posted_by = message.getPosted_by();
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM account WHERE account_id = " + posted_by);
            return rs.next();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Retrieve message object from database using the message_id
     * @param id
     * @return Message object with detailed information
     */
    public Message getMessageByMessageId(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message message = null;
        try {
            Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM message WHERE message_id = " + id);
            if (rs.next()) {
                message = new Message(
                    id,
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return message;
    }

    /**
     * Delete message in the database by it's id
     * @param id
     * @return the deleted message object.
     */
    public Message deleteMessageByMessageId(int id) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {
            Message toBeDeletedMessage = getMessageByMessageId(id);
            Statement st = connection.createStatement();
            int affectedRow = st.executeUpdate("DELETE FROM message WHERE message_id = " + id);
            if (affectedRow >= 1) {
                deletedMessage = toBeDeletedMessage;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deletedMessage;
    }

    /**
     * Update message object by it's message_id.
     * @param message
     * @return the updated message object
     */
    public Message updateMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        Message updatedMessage = null;
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            int id = message.getMessage_id();
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, message.getMessage_text());
            st.setInt(2, id);
            int affectedRow = st.executeUpdate();
            if (affectedRow >= 1) {
                updatedMessage = getMessageByMessageId(id);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return updatedMessage;
    }

    /**
     * Retrieve a list of messages posted by an account.
     * @param accountId
     * @return a list of messages from the account.
     */
    public List<Message> getAllMessagesFromAccount(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> allMessages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                allMessages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return allMessages;
    }
}
