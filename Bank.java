import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Bank implements ActionListener {
    private JFrame mainFrame;
    private JTextField inputField;
    private JPasswordField pinField;
    private JLabel balanceLabel;
    private JComboBox<String> currencyBox;
    private double balance = 1000.00; // Starting balance in INR
    private boolean authenticated = false;

    private final double INR_TO_USD = 80.0, INR_TO_EUR = 90.0, INR_TO_GBP = 100.0;

    public Bank() {
        mainFrame = new JFrame("ATM - Secure Bank");
        mainFrame.setSize(500, 450);
        mainFrame.setLayout(new GridBagLayout());
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Color bgColor = new Color(30, 40, 50);
        Color panelColor = new Color(44, 62, 80);
        Color btnColor = new Color(41, 128, 185);
        Color textColor = Color.WHITE;
        Font font = new Font("Arial", Font.BOLD, 16);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(panelColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainFrame.getContentPane().setBackground(bgColor);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel pinLabel = new JLabel("Enter PIN:");
        pinLabel.setForeground(textColor);
        pinLabel.setFont(font);
        pinField = new JPasswordField(10);
        JButton authenticateButton = new JButton("Authenticate");
        styleButton(authenticateButton, btnColor, textColor);

        JLabel denominationLabel = new JLabel("Enter Amount:");
        denominationLabel.setForeground(textColor);
        denominationLabel.setFont(font);
        inputField = new JTextField(10);

        JLabel currencyLabel = new JLabel("Choose Currency:");
        currencyLabel.setForeground(textColor);
        currencyLabel.setFont(font);
        String[] currencies = {"INR", "USD", "EUR", "GBP"};
        currencyBox = new JComboBox<>(currencies);

        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        styleButton(depositButton, new Color(39, 174, 96), textColor);
        styleButton(withdrawButton, new Color(192, 57, 43), textColor);

        balanceLabel = new JLabel("Please authenticate to view your balance.");
        balanceLabel.setForeground(textColor);
        balanceLabel.setFont(font);

        gbc.gridx = 0; gbc.gridy = 0; panel.add(pinLabel, gbc);
        gbc.gridx = 1; panel.add(pinField, gbc);
        gbc.gridx = 2; panel.add(authenticateButton, gbc);
        gbc.gridx = 0; gbc.gridy = 1; panel.add(denominationLabel, gbc);
        gbc.gridx = 1; panel.add(inputField, gbc);
        gbc.gridx = 0; gbc.gridy = 2; panel.add(currencyLabel, gbc);
        gbc.gridx = 1; panel.add(currencyBox, gbc);
        gbc.gridx = 0; gbc.gridy = 3; panel.add(depositButton, gbc);
        gbc.gridx = 1; panel.add(withdrawButton, gbc);
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 3;
        panel.add(balanceLabel, gbc);

        authenticateButton.addActionListener(this);
        depositButton.addActionListener(this);
        withdrawButton.addActionListener(this);

        mainFrame.add(panel);
        mainFrame.setVisible(true);
    }

    private void styleButton(JButton button, Color bgColor, Color fgColor) {
        button.setBackground(bgColor);
        button.setForeground(fgColor);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 1), 
                BorderFactory.createEmptyBorder(5, 15, 5, 15)));
    }

    private double convertToINR(double amount, String currency) {
        switch (currency) {
            case "USD": return amount * INR_TO_USD;
            case "EUR": return amount * INR_TO_EUR;
            case "GBP": return amount * INR_TO_GBP;
            default: return amount;
        }
    }

    private double convertFromINR(double amount, String currency) {
        switch (currency) {
            case "USD": return amount / INR_TO_USD;
            case "EUR": return amount / INR_TO_EUR;
            case "GBP": return amount / INR_TO_GBP;
            default: return amount;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            char[] pinChars = pinField.getPassword();
            String pin = new String(pinChars);
            java.util.Arrays.fill(pinChars, '0');

            if (e.getActionCommand().equals("Authenticate")) {
                if (pin.equals("1234")) {
                    authenticated = true;
                    balanceLabel.setText("Authentication successful! Balance: " + balance + " INR");
                } else {
                    balanceLabel.setText("Incorrect PIN, try again.");
                    return;
                }
            }

            if (!authenticated) {
                balanceLabel.setText("Please authenticate first.");
                return;
            }

            double amount = Double.parseDouble(inputField.getText());
            if (amount <= 0) {
                balanceLabel.setText("Enter a valid positive amount.");
                return;
            }

            String selectedCurrency = (String) currencyBox.getSelectedItem();
            double convertedAmount = convertToINR(amount, selectedCurrency);

            if (e.getActionCommand().equals("Deposit")) {
                balance += convertedAmount;
            } else if (e.getActionCommand().equals("Withdraw")) {
                if (convertedAmount > balance) {
                    balanceLabel.setText("Insufficient balance.");
                    return;
                }
                balance -= convertedAmount;
            }

            double displayedBalance = convertFromINR(balance, selectedCurrency);
            balanceLabel.setText("Balance: " + String.format("%.2f", displayedBalance) + " " + selectedCurrency);
            inputField.setText("");
        } catch (NumberFormatException ex) {
            balanceLabel.setText("Please enter a valid amount!");
        }
    }

    public static void main(String[] args) {
        new Bank();
    }
}
