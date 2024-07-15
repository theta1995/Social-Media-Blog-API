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
}

// message_id int primary key auto_increment,
//     posted_by int,
//     message_text varchar(255),
//     time_posted_epoch bigint,
//     foreign key (posted_by) references  account(account_id)