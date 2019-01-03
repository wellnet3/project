package db;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class �bersicht {
    
    private ArrayList<Zelle[]> felder;
    
    �bersicht() {
	
	JFrame frame = new JFrame();
	frame.setBounds(200, 200, 1000, 600);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	
	JPanel contentPane = new JPanel();
	contentPane.setBounds(0, 0, 1000, 600);
	contentPane.setLayout(null);
	contentPane.setBackground(Color.getHSBColor((float) 0.254, (float) 1, (float) 0.906));
	frame.setContentPane(contentPane);
	
	JButton btnPraxis = new JButton();
	btnPraxis.setBounds(100, 50, 200, 25);
	btnPraxis.setText("neue Praxisrechnung");
	btnPraxis.setVisible(true);
	frame.add(btnPraxis);
	
	btnPraxis.addActionListener((event) -> {
	    Interface.main(null);
	    frame.setVisible(false);
	});
	
	JButton btnPrivat = new JButton();
	btnPrivat.setBounds(350, 50, 200, 25);
	btnPrivat.setText("neue Privat-Rechnung");
	btnPrivat.setVisible(true);
	frame.add(btnPrivat);
	
	btnPrivat.addActionListener((event) -> {
	    Interface.main(null);
	    frame.setVisible(false);
	});
	
	DbTable tabel = new DbTable();
	tabel.setBounds(100, 100, 700, 275);
	//tabel.setHeader(new String[] { "�bersicht", "in Rechnung gestellt", "eingegangen", "noch offen" }, new int[] { 250, 150, 150, 150 });
	tabel.setLayout(null);
	tabel.setVisible(true);
	
	for (int i = 0; i < 10; i++) {
	    tabel.addColumn();
	}
	
	String[] w�rter = new String[] { "Coaching, Seminar, Training", "Auslagenersatz", "Kostenanteil f�r Kostengemeinschaft", "Therapie auf Kassenrezept",
		 "Therapie auf Privatrezept", "Therapie ohne Rezept", "Therapienahe Leistung", "Einnahme aus Raum�berlassung", "sonstiges", "Gesamt"};
	
	felder = tabel.getCells();
	for (int i = 0; i < 10; i++) {
	    felder.get(i)[0].setText(w�rter[i]);
	}
	
	frame.repaint();
	
	frame.add(tabel);
	
    }
    
    private void getData() throws IOException {
	
    }
    
    public static void main(String[] args) {
	�bersicht �s = new �bersicht();
    }

}
