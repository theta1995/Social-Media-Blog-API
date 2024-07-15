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
}
