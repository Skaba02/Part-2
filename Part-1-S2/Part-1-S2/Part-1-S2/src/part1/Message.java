package part1;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * The Message class represents a chat message with various properties.
 * It handles message creation, ID generation, and content hashing.
 */
public class Message {
    private String messageId;    // Unique 10-digit ID for the message
    private int messageNumber;   // Sequential number of the message
    private String recipient;    // Recipient of the message
    private String content;      // Content of the message (max 250 chars)
    private String contentHash;  // SHA-256 hash of the message content
    
    /**
     * Constructor for creating a new message
     * @param messageNumber The sequential number of this message
     * @param recipient The recipient of the message
     * @param content The content of the message (limited to 250 chars)
     */
    public Message(int messageNumber, String recipient, String content) {
        this.messageId = generateMessageId();
        this.messageNumber = messageNumber;
        this.recipient = recipient;
        
        // Limit content to 250 characters
        if (content.length() > 250) {
            this.content = content.substring(0, 250);
        } else {
            this.content = content;
        }
        
        // Generate SHA-256 hash of the content
        this.contentHash = calculateContentHash(this.content);
    }
    
    /**
     * Generates a random 10-digit message ID
     * @return A string containing a 10-digit random number
     */
    private String generateMessageId() {
        Random random = new Random();
        long randomNum = 1000000000L + random.nextInt(900000000); // Ensures 10 digits
        return String.valueOf(randomNum);
    }
    
    /**
     * Calculates the SHA-256 hash of the message content
     * @param content The message content to hash
     * @return The hexadecimal string representation of the hash
     */
    private String calculateContentHash(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(content.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            // In case SHA-256 is not available
            return "HASH_ERROR";
        }
    }
    
    // Getters for all properties
    
    /**
     * @return The message's unique ID
     */
    public String getMessageId() {
        return messageId;
    }
    
    /**
     * @return The message's sequential number
     */
    public int getMessageNumber() {
        return messageNumber;
    }
    
    /**
     * @return The message's recipient
     */
    public String getRecipient() {
        return recipient;
    }
    
    /**
     * @return The message's content
     */
    public String getContent() {
        return content;
    }
    
    /**
     * @return The SHA-256 hash of the message's content
     */
    public String getContentHash() {
        return contentHash;
    }
    
    /**
     * Converts the message to a string representation
     * @return A formatted string containing all message information
     */
    @Override
    public String toString() {
        return "Message #" + messageNumber + 
               "\nID: " + messageId + 
               "\nTo: " + recipient +
               "\nContent: " + content +
               "\nContent Hash: " + contentHash.substring(0, 15) + "...";
    }
}
