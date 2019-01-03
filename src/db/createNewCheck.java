package db;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class createNewCheck implements Runnable {
    
    private static JFrame frame;
    private Font f = new Font("Arial", Font.BOLD, 24);
    private DbTable dbtable;
    private JPanel contentpanel;
    private JPanel scrollpanel;
    private JPanel profilpanel;
    private JButton btnSpeichern;
    private JButton btnAbbrechen;
    
    private DbEingabe[] dbeingaben;
    private String[] dbkategorien = new String[] { "Kategorie", "Betrag", "Name Patient", "Titel Dienstleistung", "Datum Erbringung", "Addresse Erbringung",
		"Entfernung in km", "Ausgaben für Material", "Text", "Hinweis", "Eingang", "Anmerkung", "Kalenderdaten" };
    private int[] dbY = new int[] { 150, 225, 300, 375, 450, 525, 600, 150, 225, 300, 375, 450, 525 };
    private int[] dbX = new int[] { 50, 50, 50, 50, 50, 50, 50, 500, 500, 500, 500, 500, 500 };
    private String[][] dbeingabewerte = new String[][] { { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }, { " " }};
    
    private String[] werte;
    
    public Thread t1 = new Thread(this);
    private final Lock lock = new ReentrantLock();
    
    private createNewCheck(Component comp, Zelle[] zellen) {
	init(comp, zellen);
    }
    
    public static String[] editCheck(Component comp, Zelle[] zellen) {
	/*
	 * um eine neue Rechnung erstellen: zellen == null;
	 * um eine Rechnung zu bearbeiten zellen != null;
	 */
	createNewCheck cnc = new createNewCheck(comp, zellen);
	try {
	    System.out.println("t1: " + cnc.t1.getState());
	    cnc.t1.join();
	} catch (InterruptedException iex) {
	    System.out.println("cnc.t1.interrupted");
	    System.exit(1);
	} finally {
	    comp.setFocusable(true);
	}
	return cnc.werte;
    }
    
    private void init(Component comp, Zelle[] zellen) {
	frame = new JFrame();
	frame.setType(Type.NORMAL);
	frame.setBounds(0, 0, 1000, 700);
	frame.setLocationRelativeTo(comp);
	frame.setLayout(null);
	frame.setVisible(true);
	frame.setBackground(Color.red);
	frame.getContentPane().setBackground(Color.white);
	//frame.setResizable(false);
	
	frame.addWindowListener(new WindowListener() {

	    @Override
	    public void windowOpened(WindowEvent e) {
	    }

	    @Override
	    public void windowClosing(WindowEvent e) {
		t1.interrupt();
		werte = null;
		hideAllFrames();
	    }

	    @Override
	    public void windowClosed(WindowEvent e) {
	    }

	    @Override
	    public void windowIconified(WindowEvent e) {
	    }

	    @Override
	    public void windowDeiconified(WindowEvent e) {
	    }

	    @Override
	    public void windowActivated(WindowEvent e) {
	    }

	    @Override
	    public void windowDeactivated(WindowEvent e) {
	    }
	    
	});
	
	scrollpanel = new JPanel();
	scrollpanel.setBounds(0, 0, frame.getWidth() -20, frame.getHeight());
	scrollpanel.setLayout(null);
	scrollpanel.setBackground(Color.white);
	scrollpanel.setVisible(true);
	frame.add(scrollpanel);
	
	contentpanel = new JPanel();
	//contentpanel.setBounds(0, 0, frame.getWidth() -20, frame.getHeight() +1000);
	contentpanel.setPreferredSize(new Dimension(frame.getWidth(), frame.getHeight() +100));
	contentpanel.setLayout(null);
	contentpanel.setBackground(Color.gray);
	contentpanel.setVisible(true);
	frame.add(contentpanel);
	
	JScrollPane scrollPane = new JScrollPane(contentpanel, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	scrollPane.setBounds(0, 0, frame.getWidth() -20, frame.getHeight());
	scrollpanel.add(scrollPane);
	
	profilpanel = new JPanel();
	profilpanel.setBounds(100, 50, 700, 50);
	profilpanel.setBorder(BorderFactory.createLineBorder(Color.black));
	profilpanel.setLayout(new BorderLayout());
	profilpanel.setVisible(true);
	profilpanel.setBackground(Color.white);
	contentpanel.add(profilpanel);
	
	dbtable = new DbTable();
	dbtable.setHeader(new String[] { "Firmenprofil" }, new int[] { 700 }, new ZellenListener[] { ZellenListener.PROFILLISTENER });
	dbtable.setVisible(true);
	dbtable.setLayout(null);
	
	dbtable.setZellenlänge(500);
	dbtable.setZellenhöhe(350);
	
	contentpanel.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		//JOptionPane.showMessageDialog(null, "CLICKED");
		ArrayList<Zelle[]> zellen = dbtable.getCells();
		for (int i = 0; i < zellen.size(); i++) {
		    for (int j = 0; j < zellen.get(i).length; j++) {
			try {
			    hideAllFrames();
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
	frame.addMouseListener(contentpanel.getMouseListeners()[0]);
	
	profilpanel.add(dbtable);
	dbtable.addColumn();
	
	try {
	    dbeingabewerte = ListenerVorschlägeEinlesen();
	} catch (IOException ioex) {
	    ioex.printStackTrace();
	    ioex.getMessage();
	    JOptionPane.showMessageDialog(null, "Fehler beim Einlesen: " + ioex.getMessage());
	}
	dbeingaben = new DbEingabe[13];
	
	for (int i = 0; i < 13; i++) {
	    dbeingaben[i] = new DbEingabe(dbX[i], dbY[i], 400, 60, dbkategorien[i]);
	    contentpanel.add(dbeingaben[i]);
	    if (i <= 11) {
		dbeingaben[i].addListener(ZellenListener.LISTENER, dbeingabewerte[i]);
	    } else {
		dbeingaben[i].addListener(new DynamicListener() {
		    @Override
		    public void Action_on_Focus() {
			dbeingaben[12].setText(Kalenderdialog.showOpenDialog(frame));
		    }
		});
	    }
	}
	
	
	btnSpeichern = new JButton();
	btnSpeichern.setBounds(710, 620, 190, 30);
	btnSpeichern.setFont(f);
	btnSpeichern.setText("Speichern");
	btnSpeichern.setVisible(true);
	contentpanel.add(btnSpeichern);
	
	btnSpeichern.addActionListener((event) -> {
	    Speichern();
	    hideAllFrames();
	    frame.dispose();
	});
	
	btnAbbrechen = new JButton();
	btnAbbrechen.setBounds(500, 620, 190, 30);
	btnAbbrechen.setFont(f);
	btnAbbrechen.setText("Abbrechen");
	btnAbbrechen.setVisible(true);
	contentpanel.add(btnAbbrechen);
	
	btnAbbrechen.addActionListener((event) -> {
	    werte = null;
	    this.t1.interrupt();
	    hideAllFrames();
	    frame.dispose();
	});
	zeileEinlesen(zellen);
	
	for (int i = 0; i < dbtable.getCells().size(); i++) {
	    for (int j = 0; j < dbtable.getCells().get(i).length; j++) {
		dbtable.getCells().get(i)[j].hideListenerFrame();
	    }
	}
	
	frame.repaint();
	contentpanel.revalidate();
	t1.setName("WAITING_THREAD - STATUS: BLOCKED/TIMED_WAITING");
	this.t1.start();
	System.out.println(t1.getState());
	//t1.interrupt();
    }
    
    public void Speichern() {
	werte = new String[14];
	for (int i = 0; i < 13; i++) {
	    werte[i] = dbeingaben[i].getText();
	}
	werte[13] = dbtable.getCells().get(dbtable.getCells().size() -1)[0].getText();
	this.t1.interrupt();
    }
    
    public void run() {
	try {
	    while (true) {
		Thread.sleep(1000);
		System.out.println("sleeping...");
	    }
	} catch (InterruptedException iex) {
	    System.out.println("Thread interrupted");
	}
    }
    
    public static void LetMeFocus() {
	frame.toFront();
	frame.requestFocus();
    }
    
    public void hideAllFrames() {
	for (int i = 0; i < dbeingaben.length; i++) {
	    dbeingaben[i].hideListenerFrames();
	}
	dbtable.getCells().get(0)[0].hideListenerFrame();
    }
    
    private void zeileEinlesen(Zelle[] zellen) {
	if (zellen == null) {
	    frame.setTitle("Rechnung erstellen");
	    return;
	}
	String profil = "";
	for (int i = 0; i < 8; i++) {
	    if (!zellen[i +5].getText().equals("")) {
		profil = profil.concat(zellen[i +5].getText().concat("  "));
	    }
	}
	dbtable.setText(0, new String[] { profil });
	dbtable.getCells().get(0)[0].hideListenerFrame();
	frame.setTitle("Rechnung bearbeiten - ID: " + zellen[0].getText());
	dbeingaben[0].setText(zellen[4].getText());
	dbeingaben[1].setText(zellen[3].getText());
	dbeingaben[2].setText(zellen[13].getText());
	dbeingaben[3].setText(zellen[14].getText());
	dbeingaben[4].setText(zellen[15].getText());
	dbeingaben[5].setText(zellen[16].getText());
	dbeingaben[6].setText(zellen[17].getText());
	dbeingaben[7].setText(zellen[20].getText());
	dbeingaben[8].setText(zellen[23].getText());
	dbeingaben[9].setText(zellen[24].getText());
	dbeingaben[10].setText(zellen[25].getText());
	dbeingaben[11].setText(zellen[26].getText());
	dbeingaben[12].setText(zellen[27].getText());
	frame.setFocusable(true);
	contentpanel.setFocusable(true);
	contentpanel.grabFocus();
	frame.invalidate();
	System.out.println("focus owner: " + frame.getFocusOwner());
	//JOptionPane.showMessageDialog(null, "WAS IST DA LOS");
    }
    
    private String[][] ListenerVorschlägeEinlesen() throws IOException {
	/*
	 * liest die Dateien ein; hängt an den letzten Platz des Arrays jeweils den Namen der Datei
	 * Notwendig?, letzendlich wurden die Daten ja schon mal eingelesen...
	 * 
	 */
	ArrayList<String[]> al = new ArrayList<String[]>();
	File f = new File(Interface.path2);
	for (int i = 0; i < f.list().length; i++) {
	    String zeile = "";
	    BufferedReader in = new BufferedReader(new FileReader(f.listFiles()[i].getPath()));
	    ArrayList<String> cArrayList = new ArrayList<String>();
	    while ((zeile = in.readLine()) != null) {
		cArrayList.add(zeile);
	    }
	    String[] cString = new String[cArrayList.size() +1];
	    for (int j = 0; j < cArrayList.size(); j++) {
		cString[j] = cArrayList.get(j);
	    }
	    cString[cString.length -1] = f.list()[i];
	    al.add(cString);
	    in.close();
	}
	String[][] ausgabe = new String[13][];
	String[] dateinamen = new String[] { "kategorie.txt", "betrag.txt", "name patient.txt", "titel dienstleistung.txt", "datum erbringung.txt", "addresse erbringung.txt", 
		"entfernung in km.txt", "ausgaben für material.txt", "text.txt", "hinweis.txt", "eingang.txt", "anmerkung.txt", "kalenderdaten.txt" };
	for (int k = 0; k < 13; k++) {
	    for (int i = 0; i < 13; i++) {
		try {
		    System.out.println("al.get(i)[al.get(i).length -1]: " + al.get(i)[al.get(i).length -1]);
		    if (al.get(i)[al.get(i).length -1].toLowerCase().equals(dateinamen[k])) {
			String[] tmp = new String[al.get(i).length -1];
			for (int j = 0; j < al.get(i).length -1; j++) {
			    tmp[j] = al.get(i)[j];
			    System.out.println("tmp[j]: " + tmp[j]);
			}
			ausgabe[k] = tmp;
			break;
		    }
		} catch (IndexOutOfBoundsException ioobex) {
		    System.out.println("ioobex!");
		}
	    }
	}
	
	return ausgabe;
    }

}
