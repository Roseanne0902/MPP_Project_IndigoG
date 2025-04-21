package indigo;

import indigo.screen.LoginScreen;

public class Main {
    public static void main(String[] args) {
        DatabaseInitializer.initialize();

        javax.swing.SwingUtilities.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}
