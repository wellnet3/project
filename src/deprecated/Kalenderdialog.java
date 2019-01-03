package deprecated;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.concurrent.TimeUnit;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Kalenderdialog extends JDialog {
    
    private static JDialog dialog;
    private static String date = "";
    
    public static String showOpenDialog(Component comp) throws IllegalArgumentException {
	JFrame frame = new JFrame();
	dialog = new JDialog(frame, "Kalender", true);
	dialog.getContentPane().setLayout(null);
	
	Kalender k = new Kalender();
	k.setLayout(null);
	k.setBounds(0, 0, 684, 462);
	k.setBackground(Color.white);
	
	k.createMonth(0);
	dialog.setContentPane(k);
	
	/*
	 * Größe der ContentPane des Dialogs:
	 * width = 684;
	 * height = 462;
	 */
	
	dialog.setSize(700, 500);
	dialog.setLocationRelativeTo(comp);
	dialog.setVisible(true);
	//dialog.dispose();
	
	return date;
    }
    
    private static String[] Calendar() {
	
	return null;
    }
    
    public static void save(String daten) {
	date = daten;
	dialog.dispose();
    }

}
