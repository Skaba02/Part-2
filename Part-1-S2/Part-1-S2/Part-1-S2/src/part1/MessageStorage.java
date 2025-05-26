package part1;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * The MessageStorage class handles saving and loading messages to/from a JSON file.
 * It maintains a list of messages in memory and provides functionality to save them to disk.
 */
public class MessageStorage {
    private static final String STORAGE_FILE = "messages.json";
    private List<Message> messages;
    
    /**
     * Constructor - initializes the messages list and attempts to load existing messages
     */
    public MessageStorage() {
        this.messages = new ArrayList<>();
        loadMessages();
    }
    
    /**
     * Add a message to the storage
     * @param message The message to add
     */
    public void addMessage(Message message) {
        messages.add(message);
    }
    
    /**
     * Get all messages currently in storage
     * @return List of all messages
     */
    public List<Message> getMessages() {
        return messages;
    }
    
    /**
     * Save all messages in the list to the JSON file
     * @return true if successful, false otherwise
     */
    @SuppressWarnings("unchecked")
    public boolean saveMessages() {
        try {
            // Create a JSON array to hold all messages
            JSONArray jsonMessages = new JSONArray();
            
            // Convert each Message object to a JSON object
            for (Message message : messages) {
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put("messageId", message.getMessageId());
                jsonMessage.put("messageNumber", message.getMessageNumber());
                jsonMessage.put("recipient", message.getRecipient());
                jsonMessage.put("content", message.getContent());
                jsonMessage.put("contentHash", message.getContentHash());
                
                // Add this message to the array
                jsonMessages.add(jsonMessage);
            }
            
            // Write the JSON array to file
            try (FileWriter file = new FileWriter(STORAGE_FILE)) {
                file.write(jsonMessages.toJSONString());
                file.flush();
                return true;
            }
        } catch (IOException e) {
            System.out.println("Error saving messages: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Load messages from the JSON file into the messages list
     * @return true if successful, false otherwise
     */
    public boolean loadMessages() {
        try {
            // Create JSON parser
            JSONParser jsonParser = new JSONParser();
            
            // Read and parse the JSON file
            try (FileReader reader = new FileReader(STORAGE_FILE)) {
                Object obj = jsonParser.parse(reader);
                JSONArray jsonMessages = (JSONArray) obj;
                
                // Clear current messages
                messages.clear();
                
                // Load each message from the JSON array
                for (Object jsonMessage : jsonMessages) {
                    JSONObject messageObj = (JSONObject) jsonMessage;
                    
                    // Extract message properties
                    int messageNumber = ((Long) messageObj.get("messageNumber")).intValue();
                    String recipient = (String) messageObj.get("recipient");
                    String content = (String) messageObj.get("content");
                    
                    // Create a new Message object and add it to the list
                    Message message = new Message(messageNumber, recipient, content);
                    messages.add(message);
                }
                return true;
            }
        } catch (IOException | ParseException e) {
            // File might not exist yet or could have invalid format
            // This is expected on first run, so we'll just start with an empty list
            return false;
        }
    }
    
    /**
     * Get the next message number based on the current messages list
     * @return The next sequential message number
     */
    public int getNextMessageNumber() {
        if (messages.isEmpty()) {
            return 1;
        } else {
            // Find the highest message number and add 1
            int maxNumber = 0;
            for (Message message : messages) {
                if (message.getMessageNumber() > maxNumber) {
                    maxNumber = message.getMessageNumber();
                }
            }
            return maxNumber + 1;
        }
    }
}
