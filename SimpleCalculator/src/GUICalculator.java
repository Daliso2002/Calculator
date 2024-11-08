import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

public class GUICalculator extends JFrame implements ActionListener {
    private JTextField display;
    private StringBuilder equation = new StringBuilder();

    public GUICalculator() {
        // Set up the frame
        setTitle("Calculator");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("My Custom Calculator");
        headerLabel.setFont(new Font("Serif", Font.BOLD, 20));
        headerLabel.setHorizontalAlignment(JLabel.CENTER);
        add(headerLabel, BorderLayout.NORTH);

        // Create the display field
        display = new JTextField();
        display.setFont(new Font("Arial", Font.BOLD, 80));
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // Create the panel for buttons
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));

        // Button labels
        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", "C", "=", "+"
        };

        // Add buttons to the panel
        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            button.setFont(new Font("Arial", Font.BOLD, 30));
            
            if (label.equals("=")) {
                button.setBackground(Color.blue);
            } else if (label.equals("C")) {
                button.setBackground(Color.red);
            } else if ("/*-+".contains(label)) {
                button.setBackground(Color.white);
            } else {
                button.setBackground(Color.LIGHT_GRAY);
            }
            button.addActionListener(this);
            panel.add(button);
        }

        // Add panel to frame
        add(panel, BorderLayout.CENTER);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.charAt(0) >= '0' && command.charAt(0) <= '9') { // Number button clicked
            equation.append(command);
            display.setText(equation.toString());
        } else if (command.equals("C")) { // Clear button clicked
            equation.setLength(0);
            display.setText("");
        } else if (command.equals("=")) { // Equal button clicked
            try {
                // Evaluate the full equation
                double result = evaluate(equation.toString());
                display.setText(String.valueOf(result));
                equation.setLength(0); // Clear the equation after showing result
            } catch (Exception ex) {
                display.setText("Error");
                equation.setLength(0); // Clear equation in case of error
            }
        } else { // Operator button clicked
            equation.append(" ").append(command).append(" ");
            display.setText(equation.toString());
        }
    }

    // Method to evaluate the equation string manually
    private double evaluate(String equation) {
        String[] tokens = equation.split(" ");
        Stack<Double> values = new Stack<>();
        Stack<String> operators = new Stack<>();

        for (String token : tokens) {
            if (isNumeric(token)) {
                values.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                while (!operators.isEmpty() && hasPrecedence(token, operators.peek())) {
                    values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
                }
                operators.push(token);
            }
        }

        while (!operators.isEmpty()) {
            values.push(applyOperator(operators.pop(), values.pop(), values.pop()));
        }
        
        return values.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || token.equals("*") || token.equals("/");
    }

    private boolean hasPrecedence(String op1, String op2) {
        if ((op1.equals("*") || op1.equals("/")) && (op2.equals("+") || op2.equals("-"))) {
            return false;
        } else {
            return true;
        }
    }

    private double applyOperator(String operator, double b, double a) {
        switch (operator) {
            case "+": return a + b;
            case "-": return a - b;
            case "*": return a * b;
            case "/": return b != 0 ? a / b : 0; // Avoid division by zero
            default: return 0;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GUICalculator calculator = new GUICalculator();
            calculator.setVisible(true);
        });
    }
}
