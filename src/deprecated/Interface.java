package deprecated;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.apache.poi.util.SystemOutLogger;

public class Interface {
    
    private JPanel contentPane;
    //protected JTable tabelle;
    protected DbTable tabelle;
    private Font f = new Font("Arial", Font.BOLD, 24);
    private String path = "C:\\Users\\User\\Desktop\\db.txt";
    private String path2 = "C:\\Users\\User\\Desktop\\TextVorschläge\\";
    private ArrayList<String[]> daten;
    private JButton btnAddRow;
    private JButton btnDelRow;
    private JButton btnDrucken;
    private JButton btnSpeichern;
    private JButton btnKalender;
    private String kalenderzeiten;
    private Image img1;
    private Image img2;
    private BufferedWriter out;
    private ArrayList<Zelle[]> zellen;
    private ArrayList<String[]> listenervorschläge;
    
    private static JFrame frame;
    private static PLZ plz;
    private static Profil profile;
    
    Interface() {
	
	frame = new JFrame();
	frame.setBounds(0, 0, 1920, 1080);
	frame.setTitle("");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent we) {
		try {
		    out = new BufferedWriter(new FileWriter(path));
		} catch (IOException ioex) {
		    ioex.printStackTrace();
		    ioex.getMessage();
		    JOptionPane.showMessageDialog(null, "Fehler beim Speichern");
		} finally {
		    try {
			out.close();
		    } catch (IOException ioex2) {
			ioex2.printStackTrace();
			ioex2.getMessage();
		    }
		}
	    }
	});
	
	contentPane = new JPanel();
	contentPane.setBounds(0, 0, 1700, 900);
	contentPane.setBackground(Color.white);
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(null);
	frame.setContentPane(contentPane);
	
	img1 = new ImageIcon(Interface.class.getResource("/db/addRow.png")).getImage();
	img1 = img1.getScaledInstance(150, 25, Image.SCALE_SMOOTH);
	
	btnAddRow = new JButton();
	btnAddRow.setBounds(1725, 50, 150, 25);
	btnAddRow.setVisible(true);
	btnAddRow.setIcon(new ImageIcon(img1));
	frame.add(btnAddRow);
	
	img2 = new ImageIcon(Interface.class.getResource("/db/delRow.png")).getImage();
	img2 = img2.getScaledInstance(150, 25, Image.SCALE_SMOOTH);
	
	btnDelRow = new JButton();
	btnDelRow.setBounds(1725, 125, 150, 25);
	btnDelRow.setVisible(true);
	btnDelRow.setIcon(new ImageIcon(img2));
	frame.add(btnDelRow);
	
	btnDrucken = new JButton();
	btnDrucken.setBounds(1725, 200, 150, 25);
	btnDrucken.setVisible(true);
	btnDrucken.setText("Drucken");
	frame.add(btnDrucken);
	
	/*
	 * zwietes Textfeld mit mehreren Datumseinträgen
	 */
	
	btnSpeichern = new JButton();
	btnSpeichern.setBounds(1725, 275, 150, 25);
	btnSpeichern.setVisible(true);
	btnSpeichern.setText("Speichern");
	frame.add(btnSpeichern);
	
	btnKalender = new JButton();
	btnKalender.setBounds(1725, 350, 150, 25);
	btnKalender.setVisible(true);
	btnKalender.setText("Kalendar");
	frame.add(btnKalender);
	
	btnKalender.addActionListener((event) -> {
	    try {
		kalenderzeiten = Kalenderdialog.showOpenDialog(frame);
		System.out.println("kalenderzeiten: " + kalenderzeiten);
	    } catch (IllegalArgumentException iaex) {
		JOptionPane.showMessageDialog(null, "Ungültige Auswahl");
	    }
	});
	
	btnSpeichern.addActionListener((event) -> {
	    try {
		Speicherdialog.showOpenDialog();
	    } catch (IOException ioex) {
		JOptionPane.showMessageDialog(null, "Fehler beim Speichern");
	    }
	});
	
	btnDrucken.addActionListener((event) -> {
	    try {
		createFormular(0);
	    } catch (IOException ioex) {
		ioex.printStackTrace();
		ioex.getMessage();
		JOptionPane.showMessageDialog(null, "Fehler beim Speichern");
	    }
	});
	
	btnDelRow.addActionListener((event) -> {
	    tabelle.removeLastColumn();
	    zellen = tabelle.getCells();
	    frame.repaint();
	});
	
	btnAddRow.addActionListener((event) -> {
	    createNewCheck cnc = new createNewCheck(frame);
	    tabelle.addColumn();
	    //tabelle.setBounds(0, 0, 1700, 900);
	    zellen = tabelle.getCells();
	    tabelle.setText(zellen.size() -1, getDefaultSettings(zellen.size() -1));
	    tabelle.addInputListener(zellen.size() -1, new int[] { 17 }, 19, null, 0.3);
	    tabelle.addInputListener(zellen.size() -1, new int[] { 18, 19, 20 }, 21, new String[] { "+", "+" }, 0.19);
	    /*
	     * Mwst setzt sich aus Honorar, Reisekosten und Auslagen für Material zusammen, oder?
	     */
	    tabelle.addInputListener(zellen.size() -1, new int[] { 18, 19, 20, 21}, 22, new String[] { "+", "+", "+" }, 1);
	    zellen.get(zellen.size() -1)[27].addFocusListener(new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
		    zellen.get(zellen.size() -1)[27].setFocusable(false);
		    kalenderzeiten = Kalenderdialog.showOpenDialog(frame);
		    zellen.get(zellen.size() -1)[27].setText(kalenderzeiten);
		    System.out.println("kalenderzeiten: " + kalenderzeiten);
		}

		@Override
		public void focusLost(FocusEvent e) {
		    try {
			TimeUnit.MILLISECONDS.sleep(100);
		    } catch (InterruptedException iex) {
			
		    }
		    zellen.get(zellen.size() -1)[27].setFocusable(true);
		}
	    });
	    frame.repaint();
	});
	
	JScrollPane scrollPane = new JScrollPane(tabelle, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scrollPane.setBounds(0, 0, 1700, 900);
	contentPane.add(scrollPane);
	
	tabelle = new DbTable();
	int[] width = new int[28];
	for (int i = 0; i < 28; i++) {
	    width[i] = 150;
	}
	
	ZellenListener[] zlistener = new ZellenListener[] {ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.LISTENER,
		ZellenListener.PROFILLISTENER, ZellenListener.PROFILLISTENER, ZellenListener.PROFILLISTENER, ZellenListener.PROFILLISTENER, ZellenListener.PROFILLISTENER,
		ZellenListener.PROFILLISTENER, ZellenListener.PLZLISTENER, ZellenListener.PLZLISTENER, ZellenListener.NULL, ZellenListener.LISTENER, ZellenListener.LISTENER,
		ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL,
		ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.LISTENER, ZellenListener.NULL};
	
	tabelle.setHeader(new String[] {"Rechnungsnummer", "Datum", "Frist", "Betrag", "Kategorie", "Firma",
		"Anrede", "Vorname", "Name", "Anschrift", "Anschrift2", "PLZ", "Ort", "Name Patient", "Titel Dienstleistung", "Datum Erbringung",
		"Adresse Erbringung", "Entfernung in km", "Honorar", "Reisekosten", "Auslagen für Material", "Mwst", "Summe", "Text", "Hinweis",
		"Eingang", "Anmerkung", "Kalenderdaten" }, width, zlistener);
	tabelle.setVisible(true);
	tabelle.setLayout(null);
	
	tabelle.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		//JOptionPane.showMessageDialog(null, "CLICKED");
		zellen = tabelle.getCells();
		for (int i = 0; i < zellen.size(); i++) {
		    for (int j = 0; j < zellen.get(i).length; j++) {
			//if (zellen.get(i)[j].isFrameVisible()) {
			try {
			    zellen.get(i)[j].setFocusable(false);
			    Interface.LetMeFocus();
			    zellen.get(i)[j].hideListenerFrame();
			    System.out.println("Zelle[" + i + "][" + j + "] " + zellen.get(i)[j].frame.isVisible());
			} catch (NullPointerException npex) {
			    System.out.println("Zelle[" + i + "][" + j + "] has not been initialized");
			} finally {
			    zellen.get(i)[j].setFocusable(true);
			}
			//}
		    }
		}
	    }
	});
	
	scrollPane.setViewportView(tabelle);
	daten = new ArrayList<String[]>();
	
	zellen = tabelle.getCells();
	
	frame.repaint();
    }
    
    private String[] getDefaultSettings(int row) {
	String[] settings = new String[28];
	for (int i = 0; i < 28; i++) {
	    settings[i] = "";
	}
	if (row > 0) {
	    try {
		settings[0] = String.valueOf(Integer.parseInt(zellen.get(zellen.size() -2)[0].getText()) +1);
	    } catch (NumberFormatException nfex) {
		settings[0] = "";
	    }
	}
	settings[1] = zeitKonverter(String.valueOf(LocalDate.now()));
	settings[2] = zeitKonverter(String.valueOf(LocalDate.now().plus(2, ChronoUnit.WEEKS)));
	
	return settings;
    }
    
    public String zeitKonverter(String LocalDate) {
	String Jahr = LocalDate.substring(0, 4);
	String monat = LocalDate.substring(5, 7);
	String tag = LocalDate.substring(8, 10);
	return tag.concat(".").concat(monat).concat(".").concat(Jahr);
    }
    
    public static void LetMeFocus() {
	frame.toFront();
	frame.requestFocus();
    }

    
    public static void main(String[] args) {
	Interface IF = new Interface();
	Thread t1 = new Thread(new Runnable() {
	    @Override
	    public void run() {
		plz = new PLZ();
		profile = new Profil();
		try {
		    TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException iex) {
		    
		}
		try {
		    plz.einlesen();
		    StatusDialog.UpdatePB(33);
		    profile.einlesen();
		    StatusDialog.UpdatePB(67);
		    IF.einlesen();
		    IF.ListenerVorschlägeEinlesen();
		    StatusDialog.UpdatePB(100);
		    try {
			TimeUnit.SECONDS.sleep(0);
		    } catch (InterruptedException iex) {
			
		    }
		} catch (IOException ioex) {
		    ioex.printStackTrace();
		    ioex.getMessage();
		    JOptionPane.showMessageDialog(null, "Fehler beim Einlesen");
		} finally {
		    StatusDialog.exit();
		}
		
	    }
	});
	t1.start();
	StatusDialog.showOpenDialog(null);
	try {
	    t1.join();
	} catch (InterruptedException iex) {
	    iex.printStackTrace();
	    iex.getMessage();
	}
	
    }
    
    public void einlesen() throws IOException {
	BufferedReader in = new BufferedReader(new FileReader(path));
	String zeile;
	while ((zeile = in.readLine()) != null)  {
	    String[] s = new String[28];
	    for (int i = 0; i < 28; i++) {
		try {
		    s[i] = zeile.substring(0, zeile.indexOf(","));
		    zeile.substring(0, zeile.indexOf(","));
		} catch (NullPointerException npex) {
		    s[i] = zeile;
		}
	    }
	    daten.add(s);
	}
	in.close();
    }
    
    public void ListenerVorschlägeEinlesen() throws IOException {
	/*
	 * liest die Dateien ein; hängt an den letzten Platz des Arrays jeweils den Namen der Datei
	 * 
	 */
	listenervorschläge = new ArrayList<String[]>();
	File f = new File(path2);
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
	    listenervorschläge.add(cString);
	    in.close();
	}
	tabelle.setListenerVorschläge(listenervorschläge);
    }
    
    private void createFormular(int row) throws IOException {
	/*
	 * Am besten einen Dialog erstellen, wo dann die Zeilen ausgesucht werden, sowie die Titel
	 * dateiname = Nummer?
	 */
	String[] daten = new String[17];
	for (int i = 0; i < 8; i++) {
	    daten[i] = zellen.get(row)[i +5].getText();
	}
	daten[8] = zellen.get(row)[0].getText();
	daten[9] = zellen.get(row)[1].getText();
	daten[10] = zellen.get(row)[23].getText();
	daten[11] = zellen.get(row)[24].getText();
	daten[12] = zellen.get(row)[2].getText();
	daten[13] = zellen.get(row)[4].getText();
	daten[14] = zellen.get(row)[3].getText();
	daten[15] = zellen.get(row)[21].getText();
	daten[16] = zellen.get(row)[22].getText();
	Formular.createPraxisFormular(daten, daten[8]);
	//Formular.convertToPdf(doc, daten[8]);
    }

}
