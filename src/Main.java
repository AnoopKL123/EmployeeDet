import ui.MainUI;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });
    }

    private static void createGUI() {
        MainUI ui = new MainUI();
        JTabbedPane tab = ui.getMainTabbedPane();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(tab);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        ImageIcon logo = new ImageIcon("res/Developer.png");
        frame.setIconImage(logo.getImage());
    }
}
