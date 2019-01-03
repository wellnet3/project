package db;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.print.DocFlavor.INPUT_STREAM;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DbEingabeDialog extends JDialog {
    
    public static JDialog dialog;
    public static JFrame frame;
    public static DbEingabe[] dbes;
    private static final Font f1 = new Font("Arial", Font.BOLD, 24);
    private static String wert;
    private static String[] ausgabe;
    private static JButton btnSpeichern;
    private static JButton btnAbbrechen;
    
    private static int[] dbeXbounds = new int[] { 20, 440, 20, 440, 20, 440 };
    private static int[] dbeYbounds = new int[] { 20, 20, 220, 220, 420, 420 };
    
    public static String[] showOpenDialog(Component comp, String titel, Zelle[] zellen, boolean inputlistener, String[] rechenoperationen, double faktor) {
	frame = new JFrame();
	dialog = new JDialog(frame, titel, true);
	dialog.getContentPane().setLayout(null);
	dialog.getContentPane().setBackground(Color.lightGray);
	
	btnSpeichern = new JButton();
	btnSpeichern.setBounds(240, (((zellen.length +1) /2) * 200) + 25, 190, 30);
	btnSpeichern.setText("Speichern");
	btnSpeichern.setFont(f1);
	btnSpeichern.setVisible(true);
	dialog.add(btnSpeichern);
	
	btnSpeichern.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		speichern();
	    }
	});
	
	btnAbbrechen = new JButton();
	btnAbbrechen.setBounds(20, (((zellen.length +1) /2) * 200) + 25, 190, 30);
	btnAbbrechen.setText("Abbrechen");
	btnAbbrechen.setFont(f1);
	btnAbbrechen.setVisible(true);
	dialog.add(btnAbbrechen);
	
	btnAbbrechen.addActionListener(new ActionListener() {
	    @Override
	    public void actionPerformed(ActionEvent e) {
		ausgabe = null;
		frame.dispose();
	    }
	});
	
	dbes = new DbEingabe[zellen.length];
	
	dialog.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent e) {
		
	    }
	});
	
	for (int i = 0; i < dbes.length; i++) {
	    //dbes[i] = new DbEingabe(20, (200 * i) +20, 400, 175, zellen[i].titel);
	    dbes[i] = new DbEingabe(dbeXbounds[i], dbeYbounds[i], 400, 175, zellen[i].titel);
	    System.out.println("dbes[i].getY(): " + dbes[i].getY());
	    System.out.println("dbes[i].getX(): " + dbes[i].getX());
	    dbes[i].setCellHeight(125);
	    dbes[i].setCellWidth(400);
	    dbes[i].getCell().setFont(f1);
	    dbes[i].getHeader().setFont(f1);
	    dbes[i].setText(zellen[i].getText());
	    dialog.add(dbes[i]);
	}
	
	ausgabe = new String[zellen.length];

	
	wert = dbes[zellen.length -1].getText();
	dbes[zellen.length -1].getCell().addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    speichern();
		}
	    }
	});
	
	if (inputlistener) {
	    addInputListener(rechenoperationen, faktor);
	} else {
	    ausgabe[0] = wert;
	}
	
	if (zellen.length > 1) {
	    dialog.setSize(900, (zellen.length / 2) *200 + 150);
	} else {
	    dialog.setSize(500, 350);
	}
	
	//dialog.setSize(600, 700);
	dialog.setLocationRelativeTo(comp);
	dialog.setVisible(true);
	
	return ausgabe;
    }
    
    public static void addInputListener(String[] rechenoperationen, double faktor) {
	for (int i = 0; i < dbes.length -1; i++) {
	    dbes[i].getCell().getDocument().addDocumentListener(new DocumentListener() {
		
	        @Override
	        public void removeUpdate(DocumentEvent e) {
	            notifyCell(rechenoperationen, faktor);
	        }
	        
	        @Override
	        public void insertUpdate(DocumentEvent e) {
	            notifyCell(rechenoperationen, faktor);
	        }
	        
	        @Override
	        public void changedUpdate(DocumentEvent e) {
	            //null
	        }
	    });
	}
    }
    
    private static void notifyCell(String[] rechenoperationen, double faktor) {
	try {
	    double[] eingaben = new double[dbes.length -1];
	    double value = 0;
	    for (int i = 0; i < dbes.length -1; i++) {
		if (!dbes[i].getText().endsWith("€")) {
		    eingaben[i] = Double.parseDouble(dbes[i].getText());
		} else {
		    eingaben[i] = Double.parseDouble(dbes[i].getText().replace("€", ""));
		}
	    }
	    value = eingaben[0];
	    for (int i = 0; i < eingaben.length -1; i++) {
		if (eingaben.length == 1) {
		    value = value * faktor;
		    dbes[i +1].getCell().setText(String.valueOf(value));
		    break;
		} else {
		    switch (rechenoperationen[i]) {
		    case "+":
			value = value + eingaben[i +1];
			break;
		    case "-":
			value = value - eingaben[i +1];
			break;
		    case "*":
			value = value * eingaben[i +1];
			break;
		    case "/":
			value = value / eingaben[i +1];
			break;
		    }
		}
	    }
	    value = value * faktor;
	    //zellen.get(zeile)[ausgabezelle].setText(String.valueOf(value));
	    //zellen.get(zeile)[ausgabezelle].setText(zellen.get(zeile)[ausgabezelle].getText() + "€");
	    dbes[dbes.length -1].setText(String.valueOf(value) + "€");
	} catch (NumberFormatException nfex) {
	    System.out.println("ILLEGAL NUMBER FORMAT");
	    dbes[dbes.length -1].setText("NULL");
	}
    }
    
    private static void speichern() {
	//wert = dbes[dbes.length -1].getText();
	for (int i = 0; i < dbes.length; i++) {
	    ausgabe[i] = dbes[i].getText();
	}
	dialog.dispose();
    }

}
