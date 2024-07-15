package Service;

import DAO.MessageDAO;
import Model.Message;

import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    /**
     * Calls the messageDAO to retrieve a list of message from the database.
     */
    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    /**
     * Calls the messageDAO to insert new message object.
     * The creation of the message will be successful if and only if the message_text is not blank, is not over 255 characters, and posted_by refers to a real, existing user.
     * If successful, the response body should contain a JSON of the message, including its message_id.
     * @param message the message object to be inserted into the database.
     */
    public Message createMessage(Message message) {
        String text = message.getMessage_text();
        if (text == null || text.equals("") || text.length() > 255) return null;
        if (!messageDAO.isAccountIdExist(message)) return null;
        return messageDAO.createMessage(message);
    }
}
