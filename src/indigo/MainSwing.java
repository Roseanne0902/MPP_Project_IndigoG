package indigo;

import indigo.screen.LoginScreen;

import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class MainSwing implements Runnable {

    public static void main(String[] args) {

        System.out.println("What is your name?");

        Scanner scanner = new Scanner(System.in);

        String name = scanner.nextLine();

        System.out.println(name + " is a nice name!");


        EventQueue.invokeLater(new MainSwing());
    }

    @Override
    public void run() {
//        JFrame loginFrame = new JFrame("Login Title");
//
//        loginFrame.setLocation(new Point(100, 100));
//        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//
//        JPanel mainPanel = new JPanel();
//        mainPanel.setLayout(new BorderLayout());
//        loginFrame.getContentPane().add(mainPanel);
//        mainPanel.add(new JButton("Button Text"), BorderLayout.CENTER);
//
//        loginFrame.pack();
//        loginFrame.setLocationByPlatform(true);
//        loginFrame.setVisible(true);
        JFrame frame = new LoginScreen();
        frame.setVisible(true);

//        JFrame dashboardScreen = new DashboardScreen();
//        dashboardScreen.pack();
//        dashboardScreen.setLocationByPlatform(true);
//        dashboardScreen.setVisible(true);
    }
}