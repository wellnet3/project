package deprecated;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class createNewCheck {
    
    private static JFrame frame;
    private Font f = new Font("Arial", Font.BOLD, 24);
    private DbTable dbtable;
    private DbTable dbtable2;
    private JPanel profilpanel;
    private JLabel lblprofil;
    private JPanel zahlungspanel;
    private JLabel lblzahlung;
    
    createNewCheck(Component comp) {
	init(comp);
	/*Desktop desktop = Desktop.getDesktop();
	try {
	    desktop.browse(new URI("https://youtube.com/results?search_query="));
	} catch (Exception ioex) {
	    ioex.printStackTrace();
	    ioex.getMessage();
	}*/
    }
    
    private void init(Component comp) {
	frame = new JFrame();
	frame.setBounds(0, 0, 1200, 700);
	frame.setLocationRelativeTo(comp);
	frame.setLayout(null);
	frame.setVisible(true);
	frame.setBackground(Color.white);
	frame.getContentPane().setBackground(Color.white);
	//frame.setResizable(false);
	
	profilpanel = new JPanel();
	profilpanel.setBounds(0, 50, 1200, 50);
	profilpanel.setBorder(BorderFactory.createLineBorder(Color.black));
	profilpanel.setLayout(new BorderLayout());
	profilpanel.setVisible(true);
	profilpanel.setBackground(Color.white);
	frame.add(profilpanel);
	
	lblprofil = new JLabel();
	lblprofil.setBounds(5, 20, 1200, 25);
	lblprofil.setFont(f);
	lblprofil.setText("Profil:");
	lblprofil.setLayout(null);
	lblprofil.setVisible(true);
	frame.add(lblprofil);
	
	zahlungspanel = new JPanel();
	zahlungspanel.setBounds(0, 150, 1200, 50);
	zahlungspanel.setBorder(BorderFactory.createLineBorder(Color.black));
	zahlungspanel.setLayout(new BorderLayout());
	zahlungspanel.setVisible(true);
	zahlungspanel.setBackground(Color.white);
	frame.add(zahlungspanel);
	
	lblzahlung = new JLabel();
	lblzahlung.setBounds(5, 100, 1200, 50);
	lblzahlung.setFont(f);
	lblzahlung.setText("Zahlungsinformationen:");
	lblzahlung.setLayout(null);
	lblzahlung.setVisible(true);
	lblzahlung.setBackground(Color.white);
	frame.add(lblzahlung);
	
	dbtable = new DbTable();
	int[] width = new int[8];
	ZellenListener[] zlistener = new ZellenListener[8];
	for (int i = 0; i < 8; i++) {
	    width[i] = 150;
	    zlistener[i] = ZellenListener.PROFILLISTENER;
	}
	dbtable.setHeader(new String[] { "Firma", "Anrede", "Vorname", "Name", "Anschrift", "Anschrift2", "PLZ", "Ort"}, width, zlistener);
	dbtable.setVisible(true);
	dbtable.setLayout(null);
	
	frame.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		//JOptionPane.showMessageDialog(null, "CLICKED");
		ArrayList<Zelle[]> zellen = dbtable.getCells();
		for (int i = 0; i < zellen.size(); i++) {
		    for (int j = 0; j < zellen.get(i).length; j++) {
			try {
			    createNewCheck.LetMeFocus();
			    zellen.get(i)[j].setFocusable(false);
			    createNewCheck.LetMeFocus();
			    zellen.get(i)[j].hideListenerFrame();
			    System.out.println("Zelle[" + i + "][" + j + "] " + zellen.get(i)[j].frame.isVisible());
			} catch (NullPointerException npex) {
			    System.out.println("Zelle[" + i + "][" + j + "] has not been initialized");
			} finally {
			    zellen.get(i)[j].setFocusable(true);
			}
		    }
		}
	    }
	});
	
	profilpanel.add(dbtable);
	dbtable.addColumn();
	
	dbtable2 = new DbTable();
	zlistener = new ZellenListener[] { ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.LISTENER,
		ZellenListener.NULL, ZellenListener.LISTENER, ZellenListener.LISTENER };
	dbtable2.setHeader(new String[] { "Rechnungsnummer", "Datum", "Frist", "Betrag", "Kategorie", "Name Patient", "Titel Dienstleistung", "Datum Erbringung" }, width, zlistener);
	dbtable2.setVisible(true);
	dbtable2.setLayout(null);
	ArrayList<String[]> al = new ArrayList<String[]>();
	al.add(new String[] { "1", "2", "3", "4", "5", " " });
	al.add(new String[] { "6", "7", "8", "9", "10", " " });
	al.add(new String[] { "11", "12", "13", "14", "15", " " });
	dbtable2.setListenerVorschläge(al);
	
	zahlungspanel.add(dbtable2);
	dbtable2.addColumn();
    }
    
    public static void LetMeFocus() {
	frame.toFront();
	frame.requestFocus();
    }

}
