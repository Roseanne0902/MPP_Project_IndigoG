package indigo.screen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class DashboardScreen extends JFrame {
    JButton myButton;
    JLabel myLabel;

    public DashboardScreen() {
        // Initialize components
        myButton = new JButton("Click Me");
        myButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {



            }
        });

        myLabel = new JLabel("Hello, Swing!");

        // Add components to the frame
        this.getContentPane().add(myLabel);
        this.getContentPane().add(myButton);

        // Set layout (optional)
        this.setLayout(new FlowLayout());

        // Set frame properties
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // or other options
        this.setSize(300, 200); // Example size
        this.setVisible(true); // Make the frame visible
    }
}