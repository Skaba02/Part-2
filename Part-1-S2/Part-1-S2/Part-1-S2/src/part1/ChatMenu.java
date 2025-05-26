package part1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ChatMenu provides the main interface for the QuickChat application.
 * It allows users to send messages, view recent messages, and exit the application.
 */
public class ChatMenu extends JFrame {
    private JButton btnSendMessages;
    private JButton btnShowRecent;
    private JButton btnExit;
    private JLabel lblWelcome;
    private MessageStorage messageStorage;
    
    /**
     * Constructor for the ChatMenu
     */
    public ChatMenu() {
        initComponents();
        // Initialize message storage for handling messages
        messageStorage = new MessageStorage();
    }
    
    /**
     * Initializes the UI components
     */
    private void initComponents() {
        // Setup the frame
        setTitle("QuickChat Menu");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Create components
        lblWelcome = new JLabel("Welcome to QuickChat");
        lblWelcome.setFont(new Font("Arial", Font.BOLD, 18));
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        
        btnSendMessages = new JButton("Send Messages");
        btnShowRecent = new JButton("Show Recent Messages");
        btnExit = new JButton("Exit");
        
        // Set layout
        setLayout(new BorderLayout());
        add(lblWelcome, BorderLayout.NORTH);
        
        // Add buttons to a panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(3, 1, 10, 10));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));
        buttonPanel.add(btnSendMessages);
        buttonPanel.add(btnShowRecent);
        buttonPanel.add(btnExit);
        
        add(buttonPanel, BorderLayout.CENTER);
        
        // Add action listeners
        btnSendMessages.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSendMessages();
            }
        });
        
        btnShowRecent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(ChatMenu.this, 
                        "Coming soon!", 
                        "Recent Messages", 
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
    
    /**
     * Handles the process of sending messages
     */
    private void handleSendMessages() {
        // Ask how many messages the user wants to send
        String input = JOptionPane.showInputDialog(this, 
                "How many messages would you like to send?", 
                "Number of Messages", 
                JOptionPane.QUESTION_MESSAGE);
        
        // Validate input
        if (input == null) {
            return; // User canceled
        }
        
        int numMessages;
        try {
            numMessages = Integer.parseInt(input);
            if (numMessages <= 0) {
                JOptionPane.showMessageDialog(this, 
                        "Please enter a positive number.", 
                        "Invalid Input", 
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                    "Please enter a valid number.", 
                    "Invalid Input", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Process each message
        int messagesSent = 0;
        int currentMessageNumber = messageStorage.getNextMessageNumber();
        
        for (int i = 0; i < numMessages; i++) {
            // Check if we've reached the limit
            if (messagesSent >= numMessages) {
                JOptionPane.showMessageDialog(this, 
                        "You've reached your message limit of " + numMessages, 
                        "Limit Reached", 
                        JOptionPane.INFORMATION_MESSAGE);
                break;
            }
            
            // Get recipient
            String recipient = JOptionPane.showInputDialog(this, 
                    "Message " + (i+1) + " of " + numMessages + "\nEnter recipient:", 
                    "Message Recipient", 
                    JOptionPane.QUESTION_MESSAGE);
            
            if (recipient == null || recipient.trim().isEmpty()) {
                // User canceled or entered empty recipient
                int choice = JOptionPane.showConfirmDialog(this, 
                        "Skip this message?", 
                        "Skip Message", 
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    continue;
                } else {
                    i--; // Retry this message
                    continue;
                }
            }
            
            // Get message content
            String content = JOptionPane.showInputDialog(this, 
                    "Message " + (i+1) + " of " + numMessages + "\nEnter message content (max 250 chars):", 
                    "Message Content", 
                    JOptionPane.QUESTION_MESSAGE);
            
            if (content == null) {
                // User canceled
                int choice = JOptionPane.showConfirmDialog(this, 
                        "Skip this message?", 
                        "Skip Message", 
                        JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    continue;
                } else {
                    i--; // Retry this message
                    continue;
                }
            }
            
            // Create message
            Message message = new Message(currentMessageNumber++, recipient, content);
            
            // Show message and ask what to do with it
            String[] options = {"Send", "Store", "Discard"};
            int choice = JOptionPane.showOptionDialog(this, 
                    message.toString() + "\n\nWhat would you like to do with this message?", 
                    "Message Created", 
                    JOptionPane.DEFAULT_OPTION, 
                    JOptionPane.QUESTION_MESSAGE, 
                    null, 
                    options, 
                    options[0]);
            
            switch (choice) {
                case 0: // Send
                    messageStorage.addMessage(message);
                    messageStorage.saveMessages();
                    JOptionPane.showMessageDialog(this, 
                            "Message sent successfully!", 
                            "Message Sent", 
                            JOptionPane.INFORMATION_MESSAGE);
                    messagesSent++;
                    break;
                case 1: // Store
                    messageStorage.addMessage(message);
                    messageStorage.saveMessages();
                    JOptionPane.showMessageDialog(this, 
                            "Message stored for later sending.", 
                            "Message Stored", 
                            JOptionPane.INFORMATION_MESSAGE);
                    messagesSent++;
                    break;
                case 2: // Discard
                    JOptionPane.showMessageDialog(this, 
                            "Message discarded.", 
                            "Message Discarded", 
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
                default: // User closed dialog
                    JOptionPane.showMessageDialog(this, 
                            "Message discarded.", 
                            "Message Discarded", 
                            JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
        }
        
        // Final summary
        if (messagesSent > 0) {
            JOptionPane.showMessageDialog(this, 
                    "You have sent/stored " + messagesSent + " message(s).", 
                    "Message Summary", 
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Main method for testing the ChatMenu independently
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ChatMenu().setVisible(true);
            }
        });
    }
}
