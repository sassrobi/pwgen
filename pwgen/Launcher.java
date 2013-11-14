package pwgen;

import javax.swing.SwingUtilities;


public class Launcher {
    public static void main(String[] args){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame("Password generator");
                mainFrame.setVisible(true);
            }
        });
    }
}
