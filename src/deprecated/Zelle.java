package deprecated;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Event;
import java.awt.Point;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

public class Zelle extends JTextField implements Runnable {
    
    private final int ZEILE;
    private final int SPALTE;
    private String[] werte;
    private DefaultListModel<String> model;
    public JFrame frame;
    private JList<String> liste;
    private Thread listener;
    private ZellenListener zlistener;
    private String option = "";
    private Profil profile;
    private int position;
    private ArrayList<String[]> profildata;
    
    private final int framewidth = 300;
    private final int frameheight = 400;
    
    @Deprecated
    Zelle() {
	super();
	ZEILE = 0;
	SPALTE = 0;
    }
    
    Zelle(int zeile, int spalte) {
	super();
	ZEILE = zeile;
	SPALTE = spalte;
    }
    
    Zelle(int zeile, int spalte, int position) {
	super();
	ZEILE = zeile;
	SPALTE = spalte;
	this.position = position;
	switch (position) {
	case 1:
	    option = "firma";
	    break;
	case 2:
	    option = "anrede";
	    break;
	case 3:
	    option = "vorname";
	    break;
	case 4:
	    option = "nachname";
	    break;
	case 5:
	    option = "anschrift";
	    break;
	case 6:
	    option = "anschrift2";
	    break;
	case 7:
	    option = "PLZ";
	    break;
	case 8:
	    option = "ort";
	    break;
	}
	profildata = new ArrayList<String[]>();
    }
    
    public void addListener(ZellenListener zl) throws IllegalArgumentException {
	zlistener = zl;
	if (zl.equals(ZellenListener.LISTENER)) {
	    frame = new JFrame();
	    //frame.setBounds(this.getX() +10, this.getY() + this.getHeight() +30, this.getWidth(), 200);
	    frame.setLayout(null);
	    frame.setUndecorated(true);
	    frame.setVisible(false);
	    /*
	     * this.getLocation();
	     */
	    
	    JScrollPane scrollPane = new JScrollPane(liste, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setBounds(0, 0, framewidth, frameheight);
	    frame.add(scrollPane);
	    
	    model = new DefaultListModel<String>();
	    liste = new JList<String>();
	    liste.setModel(model);
	    liste.setBounds(0, 0, framewidth, frameheight);
	    liste.setVisible(true);
	    
	    liste.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            //JOptionPane.showMessageDialog(null, "TRUE");
	            setText(werte[liste.getSelectedIndex()]);
	            setFocusable(false);
	            frame.setVisible(false);
	            setFocusable(true);
	        }
	    });
	    
	    scrollPane.setViewportView(liste);
	    
	} else if (zl.equals(ZellenListener.PLZLISTENER)) {
	    
	    /*
	     * Kein PLZListener nötig; dies am besten vom DBTable aus kontrollieren!
	     * 
	     */
	    
	} else if (zl.equals(ZellenListener.PROFILLISTENER)) {
	    
	    frame = new JFrame();
	    //frame.setBounds(this.getX() +10, this.getY() + this.getHeight() +30, this.getWidth(), 400);
	    //frame.setBounds(100, 100, 500, 500);
	    frame.setLayout(null);
	    frame.setUndecorated(true);
	    frame.setVisible(false);
	    frame.setAlwaysOnTop(true);
	    
	    JScrollPane scrollPane = new JScrollPane(liste, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
	    scrollPane.setBounds(0, 0, framewidth, frameheight);
	    frame.add(scrollPane);
	    
	    model = new DefaultListModel<String>();
	    liste = new JList<String>();
	    liste.setModel(model);
	    liste.setBounds(0, 0, framewidth, frameheight);
	    liste.setVisible(true);
	    
	    liste.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            werte = profildata.get(liste.getSelectedIndex());
	            setText(werte[position -1]);
	            setFocusable(false);
	            frame.setVisible(false);
	            setFocusable(true);
	            
	            DbTable.setCellProfilText(werte, ZEILE);
	            
	        }
	    });
	    
	    scrollPane.setViewportView(liste);
	    
	    profile = new Profil();
	    try {
		profile.einlesen();
	    } catch (IOException ioex) {
		ioex.printStackTrace();
		ioex.getMessage();
	    }
	    
	} else {
	    throw new IllegalArgumentException();
	}
    }
    
    public void showSuggestions() {
	if (zlistener.equals(ZellenListener.LISTENER)) {
	    zeigeVorschläge(werte);
	} else if (zlistener.equals(ZellenListener.PLZLISTENER)) {
	    //NULL
	} else if (zlistener.equals(ZellenListener.PROFILLISTENER)) {
	    listener = new Thread(Zelle.this);
	    frame.setVisible(true);
	    listener.start();
	}
    }
    
    private void zeigeVorschläge(String[] vorschläge) {
	model.clear();
	frame.setBounds((int) this.getLocationOnScreen().getX(), (int) this.getLocationOnScreen().getY() + this.getHeight(), framewidth, frameheight);
	frame.setVisible(true);
	for (int i = 0; i < vorschläge.length; i++) {
	    model.addElement(werte[i]);
	}
	frame.repaint();
    }
    
    private void zeigeProfilVorschläge(ArrayList<String[]> vorschläge) {
	profildata = vorschläge;
	model.clear();
	frame.setBounds((int) this.getLocationOnScreen().getX(), (int) this.getLocationOnScreen().getY() + this.getHeight(), framewidth, frameheight);
	frame.setVisible(true);
	for (int i = 0; i < vorschläge.size(); i++) {
	    String vorschlag = "";
	    for (int j = 0; j < vorschläge.get(i).length; j++) {
		vorschlag = vorschlag.concat(vorschläge.get(i)[j]).concat("  ");
	    }
	    model.addElement(vorschlag);
	}
	frame.repaint();
	//Interface.LetMeFocus();
	createNewCheck.LetMeFocus();
	this.setFocusable(true);
	this.grabFocus();
	this.requestFocus();
    }
    
    public void setWerte(String[] vorschläge) {
	werte = vorschläge;
    }
    
    
    public int getSpalte() {
	return SPALTE;
    }
    
    public int getZeile() {
	return ZEILE;
    }
    
    @Deprecated
    public String[] getProfilStrings() {
	return werte;
    }

    @Override
    public void run() {
	String eingabe = this.getText();
	zeigeProfilVorschläge(profile.vervollständigen(option, eingabe));
    }
    
    public void hideListenerFrame() {
	frame.setVisible(false);
    }
    
    public boolean isFrameVisible() {
	if (frame.isVisible()) {
	    return true;
	} else {
	    return false;
	}
    }
    
    public boolean isProfilListener() {
	/*
	 * Die PLZ wird bei den Profilen gespeichert; jedoch lässt sich nicht anhand der PLZ
	 * die Person bestimmen, deswegen diese Konstellation;
	 */
	try {
	    if (zlistener.equals(ZellenListener.PROFILLISTENER) ||zlistener.equals(ZellenListener.PLZLISTENER)) {
		return true;
	    } else {
		return false;
	    }
	} catch (NullPointerException npex) {
	    return false;
	}
    }
}

enum ZellenListener {
    LISTENER,
    PLZLISTENER,
    PROFILLISTENER,
    NULL,
    CALCULATE;
}
