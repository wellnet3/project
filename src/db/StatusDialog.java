package db;

import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class StatusDialog extends JDialog {
    
    private static JDialog dialog;
    private static JProgressBar fortschrittsleiste;
    private static JLabel lblLaden;
    
    public static void showOpenDialog(Component comp) {
	JFrame frame = new JFrame();
	dialog = new JDialog(frame, "Lade", true);
	dialog.getContentPane().setLayout(null);
	
	fortschrittsleiste = new JProgressBar(0, 100);
	fortschrittsleiste.setBounds(10, 80, 360, 25);
	fortschrittsleiste.setValue(0);
	fortschrittsleiste.setStringPainted(true);
	
	lblLaden = new JLabel();
	lblLaden.setBounds(10, 30, 360, 25);
	lblLaden.setText("Lade Dateien...");
	
	dialog.getContentPane().add(lblLaden);
	dialog.getContentPane().add(fortschrittsleiste);
	
	dialog.setSize(400, 200);
	dialog.setLocationRelativeTo(comp);
	dialog.setVisible(true);
	dialog.dispose();
    }
    
    public static void UpdatePB(int value) {
	fortschrittsleiste.setValue(value);
    }
    
    public static void exit() {
	dialog.setVisible(false);
    }

}
