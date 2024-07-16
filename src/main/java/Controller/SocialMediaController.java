package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {

    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        // Account related APIs
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        // Message related APIs
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByMessageIdHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesFromAccountHandler);
        app.post("/messages", this::createMessageHandler);
        app.delete("/messages/{message_id}", this::deleteMessageByMessageIdHandler);
        app.patch("/messages/{message_id}", this::updateMessagedHandler);

        return app;
    }

    /**
     * Handle user account registration
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account registeredAccount = accountService.registerAccount(account);
        if(registeredAccount != null ){
            context.json(mapper.writeValueAsString(registeredAccount));
        }else{
            context.status(400);
        }
    }

    /**
     * Handle user account login verification
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        Account authenticatedAccount = accountService.authenticateAccount(account);
        if(authenticatedAccount != null ){
            context.json(mapper.writeValueAsString(authenticatedAccount));
        }else{
            context.status(401);
        }
    }

    /**
     * Retrieve all messages in the database.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        List<Message> messageList = messageService.getAllMessages();
        if (messageList != null) {
            context.json(mapper.writeValueAsString(messageList));
        }
    }

    /**
     * Create a message using user's provided information
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void createMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        Message createdMessage = messageService.createMessage(message);
        if (createdMessage != null) {
            context.json(mapper.writeValueAsString(createdMessage));
        } else {
            context.status(400);
        }
    }

    /**
     * Get Message object by the message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void getMessageByMessageIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByMessageId(id);
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        }
    }

    /**
     * Delete Message object by the message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    private void deleteMessageByMessageIdHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int id = Integer.parseInt(context.pathParam("message_id"));
        Message deletedMessage = messageService.deleteMessageByMessageId(id);
        if (deletedMessage != null) {
            context.json(mapper.writeValueAsString(deletedMessage));
        }
    }

    /**
     * Update Message object by the message_id
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    public void updateMessagedHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(context.body(), Message.class);
        int id = Integer.parseInt(context.pathParam("message_id"));
        message.setMessage_id(id);
        Message updatedMessage = messageService.updateMessage(message);
        if (updatedMessage != null) {
            context.json(mapper.writeValueAsString(updatedMessage));
        } else {
            context.status(400);
        }
    }

    /**
     * Retrieve a list of messages posted by an account.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * @throws JsonProcessingException
     */
    public void getMessagesFromAccountHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int accountId = Integer.parseInt(context.pathParam("account_id"));
        List<Message> messagesFromAccount = messageService.getAllMessagesFromAccount(accountId);
        if (messagesFromAccount != null) {
            context.json(mapper.writeValueAsString(messagesFromAccount));
        }
    }

}