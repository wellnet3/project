package db;

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
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
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
import org.apache.poi.xwpf.usermodel.XWPFDocument;

public class Interface {
    
    private JPanel contentPane;
    //protected JTable tabelle;
    //protected DbTable tabelle;
    protected DbTable2 tabelle2;
    private Font f = new Font("Arial", Font.BOLD, 24);
    //private static final String path = "C:\\Users\\User\\Desktop\\db.txt";
    public static String path2 = "C:\\Users\\User\\Desktop\\TextVorschläge\\";
    private ArrayList<String[]> daten;
    private JButton btnAddRow;
    private JButton btnDelRow;
    private JButton btnDrucken;
    private JButton btnSpeichern;
    private JButton btnSelect;
    private String kalenderzeiten;
    private Image img1;
    private Image img2;
    private BufferedWriter out;
    private ArrayList<Zelle[]> zellen;
    private ArrayList<String[]> listenervorschläge;
    private JScrollPane scrollPane;
    //private static String pdfpath = "C:\\Users\\User\\Desktop\\Vorlage Rechnung Praxis1.dotm";
    protected static String currentPath;
    private static final String cversion = "0.0"; //Immer um 0.1 erhöhen!
    
    protected static JFrame frame;
    private static PLZ plz;
    private static Profil profile;
    
    /*private static final String url = "https://raw.githubusercontent.com/wellnet3/project/master/";
    private static final String cversion = "0.1";
    private static final String urlCheck = url + "about.txt";
    private static final String urlJar = "https://github.com/wellnet3/project/raw/master/buchhaltung_test.jar";
    private static String gitversion = "";*/
    
    Interface() {
	
	frame = new JFrame();
	frame.setBounds(0, 0, 1920, 1080);
	frame.setTitle("");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	
	frame.addWindowListener(new WindowAdapter() {
	    @Override
	    public void windowClosing(WindowEvent we) {
		/*try {
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
		}*/
	    }
	});
	
	try {
	    currentPath = Interface.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	    currentPath = new File(currentPath).getParent() + "\\";
	} catch (URISyntaxException URIex) {
	    currentPath = null;
	}
	
	path2 = currentPath + "\\TextVorschläge\\";
	
	contentPane = new JPanel();
	contentPane.setBounds(0, 0, 1700, 900);
	contentPane.setBackground(Color.white);
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	contentPane.setLayout(null);
	frame.setContentPane(contentPane);
	
	img1 = new ImageIcon(Interface.class.getResource("/db/neueRechnung.png")).getImage();
	img1 = img1.getScaledInstance(150, 25, Image.SCALE_SMOOTH);
	
	btnAddRow = new JButton();
	btnAddRow.setBounds(1725, 50, 150, 25);
	btnAddRow.setVisible(true);
	btnAddRow.setIcon(new ImageIcon(img1));
	frame.add(btnAddRow);
	
	img2 = new ImageIcon(Interface.class.getResource("/db/RechnungLöschen.png")).getImage();
	img2 = img2.getScaledInstance(150, 25, Image.SCALE_SMOOTH);
	
	btnDelRow = new JButton();
	btnDelRow.setBounds(1725, 125, 150, 25);
	btnDelRow.setVisible(true);
	btnDelRow.setIcon(new ImageIcon(img2));
	frame.add(btnDelRow);
	
	btnDrucken = new JButton();
	btnDrucken.setBounds(1725, 200, 150, 25);
	btnDrucken.setVisible(true);
	btnDrucken.setText("Exportieren");
	frame.add(btnDrucken);
	
	/*
	 * zwietes Textfeld mit mehreren Datumseinträgen
	 */
	
	btnSpeichern = new JButton();
	btnSpeichern.setBounds(1725, 275, 150, 25);
	btnSpeichern.setVisible(true);
	btnSpeichern.setText("Speichern");
	frame.add(btnSpeichern);
	
	btnSelect = new JButton();
	btnSelect.setBounds(1725, 350, 150, 25);
	btnSelect.setVisible(true);
	btnSelect.setText("Selection: SINGLE");
	frame.add(btnSelect);
	
	btnSelect.addActionListener((event) -> {
	    if (btnSelect.getText().equals("Selection: SINGLE")) {
		btnSelect.setText("Selection: MULTIPLE");
		tabelle2.setModus(zeilenpanelmodus.MULTIPLE_SELECTION);
	    } else {
		btnSelect.setText("Selection: SINGLE");
		tabelle2.setModus(zeilenpanelmodus.SINGLE_SELECTION);
	    }
	});
	
	btnSpeichern.addActionListener((event) -> {
	    try {
		//Speicherdialog.showOpenDialog();
		daten = new ArrayList<String[]>();
		for (int i = 0; i < zellen.size(); i++) {
		    String[] zeile = new String[28];
		    for (int j = 0; j < 28; j++) {
			zeile[j] = zellen.get(i)[j].getText().concat(";");
		    }
		    daten.add(zeile);
		}
		
		BufferedWriter out = new BufferedWriter(new FileWriter(currentPath + "daten.txt"));
		for (int i = 0; i < daten.size(); i++) {
		    for (int j = 0; j < daten.get(i).length; j++) {
			out.write(daten.get(i)[j]);
		    }
		    out.newLine();
		}
		out.close();
		JOptionPane.showMessageDialog(null, "Daten erfolgreich gespeichert");
	    } catch (IOException ioex) {
		JOptionPane.showMessageDialog(null, "Fehler beim Speichern");
	    }
	});
	
	btnDrucken.addActionListener((event) -> {
	    try {
		for (int i = 0; i < tabelle2.selectedPanel.size(); i++) {
		    createFormular(Integer.parseInt(tabelle2.selectedPanel.get(i)) -1);
		}
		JOptionPane.showMessageDialog(null, "Datei erfolgreich exportiert");
	    } catch (IOException ioex) {
		ioex.printStackTrace();
		ioex.getMessage();
		JOptionPane.showMessageDialog(null, "Fehler beim Speichern");
	    }
	});
	
	
	btnDelRow.addActionListener((event) -> {
	    tabelle2.selectZeilen();
	    for (int i = 0; i < tabelle2.selectedPanel.size(); i++) {
		tabelle2.removeColumn(Integer.parseInt(tabelle2.selectedPanel.get(i)) -1);
	    }
	    zellen = tabelle2.getCells();
	    frame.repaint();
	});
	
	btnAddRow.addActionListener((event) -> {
	    Thread cnc = new Thread(new Runnable() {
		@Override
		public void run() {
		    tabelle2.addColumn(new String[] { "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""  });
		    //tabelle.setBounds(0, 0, 1700, 900);
		    zellen = tabelle2.getCells();
		    tabelle2.setText(zellen.size() -1, getDefaultSettings(zellen.size() -1));
		    frame.repaint();
		    scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		    //String[] Rechnungstexte = createNewCheck.editCheck(null, zellen.get(zellen.size() -1));
		    String[] Rechnungstexte = createNewCheck.editCheck(frame, zellen.get(zellen.size() -1));
		    System.out.println(Rechnungstexte);
		    tabelle2.setText(zellen.size() -1, editCheckTODbTable(zellen.size() -1, Rechnungstexte));
		    //tabelle.addInputListener(zellen.size() -1, new int[] { 17 }, 19, null, 0.3);
		    //tabelle.addInputListener(zellen.size() -1, new int[] { 18, 19, 20 }, 21, new String[] { "+", "+" }, 0.19);   <---- @ wichtig!!!
		    /*
		     * Mwst setzt sich aus Honorar, Reisekosten und Auslagen für Material zusammen, oder?
		     */
		    //tabelle.addInputListener(zellen.size() -1, new int[] { 18, 19, 20, 21}, 22, new String[] { "+", "+", "+" }, 1);
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
		    scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getMaximum());
		}
	    });
	    cnc.start();
	});
	
	scrollPane = new JScrollPane(tabelle2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scrollPane.setBounds(0, 0, 1700, 900);
	contentPane.add(scrollPane);
	
	//tabelle = new DbTable();
	int[] width = new int[28];
	for (int i = 0; i < 28; i++) {
	    width[i] = 150;
	}
	
	DynamicListener dl1 = new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		int focussedzeile = zeileOfFocusOwner();
		Thread tdl = new Thread(new Runnable() {
		    @Override
		    public void run() {
			tabelle2.setText(focussedzeile, editCheckTODbTable(focussedzeile, createNewCheck.editCheck(frame, zellen.get(focussedzeile))));
		    }
		});
		tdl.start();
	    }
	};
	
	DynamicListener dl2 = new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		int focussedzeile = zeileOfFocusOwner();
		int focussedspalte = SpalteOfFocusOwner();
		tabelle2.getCells().get(focussedzeile)[focussedspalte].setText(DbEingabeDialog.showOpenDialog(frame, tabelle2.getHeader()[focussedspalte].getText(), new Zelle[] { tabelle2.getCells().get(focussedzeile)[focussedspalte] }, false, null, 0 )[0]);
	    }
	};
	
	DynamicListener dl3 = new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		int focussedzeile = zeileOfFocusOwner();
		int focussedspalte = SpalteOfFocusOwner();
		String[] werte = DbEingabeDialog.showOpenDialog(frame, tabelle2.getHeader()[focussedspalte].getText(), new Zelle[] { tabelle2.getCells().get(focussedzeile)[17], tabelle2.getCells().get(focussedzeile)[19] }, true, null, 0.3);
		tabelle2.getCells().get(focussedzeile)[17].setText(werte[0]);
		tabelle2.getCells().get(focussedzeile)[19].setText(werte[1]);
	    }
	};
	
	DynamicListener dl4 = new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		int focussedzeile = zeileOfFocusOwner();
		int focussedspalte = SpalteOfFocusOwner();
		String[] werte = DbEingabeDialog.showOpenDialog(frame, tabelle2.getHeader()[focussedspalte].getText(), new Zelle[] { tabelle2.getCells().get(focussedzeile)[18], tabelle2.getCells().get(focussedzeile)[19], tabelle2.getCells().get(focussedzeile)[20], tabelle2.getCells().get(focussedzeile)[21] }, true, new String[] { "+", "+" }, 0.19);
		tabelle2.getCells().get(focussedzeile)[18].setText(werte[0]);
		tabelle2.getCells().get(focussedzeile)[19].setText(werte[1]);
		tabelle2.getCells().get(focussedzeile)[20].setText(werte[2]);
		tabelle2.getCells().get(focussedzeile)[21].setText(werte[3]);
	    }
	};
	
	DynamicListener dl5 = new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		int focussedzeile = zeileOfFocusOwner();
		int focussedspalte = SpalteOfFocusOwner();
		String[] werte = DbEingabeDialog.showOpenDialog(frame, tabelle2.getHeader()[focussedspalte].getText(), new Zelle[] { tabelle2.getCells().get(focussedzeile)[18], tabelle2.getCells().get(focussedzeile)[19], tabelle2.getCells().get(focussedzeile)[20], tabelle2.getCells().get(focussedzeile)[21], tabelle2.getCells().get(focussedzeile)[22] }, true, new String[] { "+", "+", "+" }, 1);
		tabelle2.getCells().get(focussedzeile)[18].setText(werte[0]);
		tabelle2.getCells().get(focussedzeile)[19].setText(werte[1]);
		tabelle2.getCells().get(focussedzeile)[20].setText(werte[2]);
		tabelle2.getCells().get(focussedzeile)[21].setText(werte[3]);
		tabelle2.getCells().get(focussedzeile)[22].setText(werte[4]);
	    }
	};
	
	tabelle2 = new DbTable2();
	tabelle2.setHeader(new String[] { "Rechnungsnummer", "Datum", "Frist", "Betrag", "Kategorie", "Firma",
		"Anrede", "Vorname", "Name", "Anschrift", "Anschrift2", "PLZ", "Ort", "Name Patient", "Titel Dienstleistung", "Datum Erbringung",
		"Adresse Erbringung", "Entfernung in km", "Honorar", "Reisekosten", "Auslagen für Material", "Mwst", "Summe", "Text", "Hinweis",
		"Eingang", "Anmerkung", "Kalenderdaten" }, width, 100, new DynamicListener[] { dl2, dl2, dl2, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl1, dl3, dl4, dl3, dl4, dl4, dl5, dl2, dl2, dl1, dl2, dl1 });
	tabelle2.setLayout(null);
	tabelle2.setVisible(true);
	tabelle2.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		zellen = tabelle2.getCells();
		for (int i = 0; i < zellen.size(); i++) {
		    for (int j = 0; j < zellen.get(i).length; j++) {
			try {
			    zellen.get(i)[j].setFocusable(true);
			    Interface.LetMeFocus();
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
	//tabelle2.addColumn(new String[] { "hey na" });
	//tabelle2.addColumn(new String[] { "EXCUSE ME WTF" });
	
	/*ZellenListener[] zlistener = new ZellenListener[] {ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.NULL, ZellenListener.LISTENER,
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
	});*/
	
	scrollPane.setViewportView(tabelle2);
	daten = new ArrayList<String[]>();
	
	zellen = tabelle2.getCells();
	
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
    
    public int zeileOfFocusOwner() {
	for (int i = 0; i < zellen.size(); i++) {
	    for (int j = 0; j < zellen.get(i).length; j++) {
		if (zellen.get(i)[j].isFocusOwner()) {
		    System.out.println("zellen.get(" + i + ")[" + j + "] is Focus Owner!"); 
		    return i;
		}
	    }
	}
	return -1;
    }
    
    public int SpalteOfFocusOwner() {
	for (int i = 0; i < zellen.size(); i++) {
	    for (int j = 0; j < zellen.get(i).length; j++) {
		if (zellen.get(i)[j].isFocusOwner()) {
		    System.out.println("zellen.get(" + i + ")[" + j + "] is Focus Owner!"); 
		    return j;
		}
	    }
	}
	return -1;
    }
    
    private String[] editCheckTODbTable(int zeile, String[] editCheck) {
	String[] ausgabe = tabelle2.getText(zeile);
	final int[] reihenfolge = new int[] { 4, 3, 13, 14, 15, 16, 17, 20, 23, 24, 25, 26, 27 };
	for (int i = 0; i < reihenfolge.length; i++) {
	    ausgabe[reihenfolge[i]] = editCheck[i];
	}
	String[] editCheck2 = profilStringToArray(editCheck[13]);
	for (int i = 0; i < editCheck2.length; i++) {
	    ausgabe[i +5] = editCheck2[i];
	}
	return ausgabe;
    }
    
    private String[] profilStringToArray(String eingabe) {
	System.out.println("eingabe: " + eingabe);
	String[] ausgabe = new String[8];
	int i = 0;
	//System.out.println(eingabe.substring(0, eingabe.indexOf("  ")));
	while (eingabe.contains("  ")) {
	    ausgabe[i] = eingabe.substring(0, eingabe.indexOf("  "));
	    eingabe = eingabe.substring(eingabe.indexOf("  ") +2);
	    System.out.println("ausgabe[i]: " + ausgabe[i]);
	    i++;
	}
	return ausgabe;
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
		//plz = new PLZ(IF.currentPath + "PLZ.txt");
		profile = new Profil(IF.currentPath + "profile.txt");
		try {
		    TimeUnit.MILLISECONDS.sleep(100);
		} catch (InterruptedException iex) {
		    
		}
		try {
		    //plz.einlesen();
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
		    JOptionPane.showMessageDialog(null, "Fehler beim Einlesen: " + ioex.getMessage());
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
	try {
	    UpdateManager.update(cversion);
	} catch (InterruptedException iex) {
	    iex.printStackTrace();
	    iex.getMessage();
	} catch (URISyntaxException URIex) {
	    
	} catch (IOException ioex) {
	    JOptionPane.showMessageDialog(null, "Fehler beim Updaten");
	}
    }
    
    public void einlesen() throws IOException {
	BufferedReader in = new BufferedReader(new FileReader(currentPath + "daten.txt"));
	String zeile;
	while ((zeile = in.readLine()) != null)  {
	    String[] s = new String[28];
	    for (int i = 0; i < 28; i++) {
		try {
		    s[i] = zeile.substring(0, zeile.indexOf(";"));
		    zeile = zeile.substring(zeile.indexOf(";") +1);
		} catch (NullPointerException npex) {
		    s[i] = zeile;
		}
	    }
	    daten.add(s);
	}
	in.close();
	
	for (int i = 0; i < daten.size(); i++) {
	    tabelle2.addColumn(daten.get(i));
	}
	tabelle2.repaint();
    }
    
    public void ListenerVorschlägeEinlesen() throws IOException {
	/*
	 * liest die Dateien ein; hängt an den letzten Platz des Arrays jeweils den Namen der Datei
	 * Bitte sicherstellen, dass sich im Ordner "TextVorschläge" keine DIRs befinden
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
	tabelle2.setListenerVorschläge(listenervorschläge);
    }
    
    private void createFormular(int row) throws IOException {
	/*
	 * Am besten einen Dialog erstellen, wo dann die Zeilen ausgesucht werden, sowie die Titel
	 * dateiname = Nummer?
	 */
	Formular.setPath(currentPath);
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
	
	//Formular.convertToPdf(new XWPFDocument(new FileInputStream(pdfpath)), daten[8]);
    }
    
    @Deprecated
    private static void update() throws IOException, URISyntaxException {
	/*URL url = new URL(urlCheck);
	BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	gitversion = in.readLine();
	System.out.println(gitversion);
	in.close();
	
	double version = Double.parseDouble(gitversion.substring(9));
	System.out.println(version);
	if (version < Double.parseDouble(cversion)) {
	    String thisFile = Interface.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	    //Runtime.getRuntime().exec("cmd.exe /c start " + thisFile + "UpdateManager.jar");
	    JOptionPane.showMessageDialog(null, "auszuführender Path: " + thisFile + "UpdateManager.jar");
	}*/
    }
    
    @Deprecated
    private static void removeMe() throws IOException, URISyntaxException {
	String thisFile = Interface.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	String batchFile = thisFile + "remove.bat";
	thisFile = thisFile.concat(Interface.class.getName());
	System.out.println("thisfile: " + thisFile);
	System.out.println("batchfile: " + batchFile);
	Runtime.getRuntime().exec("cmd.exe /c start " + batchFile + " " + thisFile);
    }

}
