package db;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
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

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.apache.poi.util.SystemOutLogger;

public class Zelle extends JTextArea implements Runnable {
    
    public static int ZEILE;
    private final int SPALTE;
    private String[] werte;
    private DefaultListModel<String> model;
    public JFrame frame;
    public Xpanel xpanel;
    private JList<String> liste;
    private Thread listener;
    public static Thread x;
    protected ZellenListener zlistener;
    private Profil profile;
    private int position;
    private ArrayList<String[]> profildata;
    private Image img1;
    public DynamicListener dl;
    public String titel = "NOOT NOOT";
    
    private int framewidth = 300;
    private int frameheight = 400;
    
    private int Xwidth = 16;
    private int Xheight = 16;
    
    Zelle() {
	super();
	ZEILE = 0;
	SPALTE = 0;
	this.setLineWrap(true);
	this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
    }
    
    Zelle(int zeile, int spalte) {
	super();
	ZEILE = zeile;
	SPALTE = spalte;
	profildata = new ArrayList<String[]>();
	this.setLineWrap(true);
	this.setBorder(BorderFactory.createLineBorder(Color.lightGray));
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
	            //System.out.println(liste.getModel().getElementAt(liste.getSelectedIndex()));
	            try {
	        	setText(liste.getModel().getElementAt(liste.getSelectedIndex()));
	            } catch (ArrayIndexOutOfBoundsException aioobex) {
	        	System.out.println("ArrayIndexOutOfBoundsException: " + "keine Vorschläge in der Liste vorhanden");
	        	hideListenerFrame();
	            }
	            setFocusable(false);
	            frame.setVisible(false);
	            setFocusable(true);
	            
	        }
	    });
	    
	    scrollPane.setViewportView(liste);
	    
	    profile = new Profil(Interface.currentPath + "profile.txt");
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
    
    public void addListener(DynamicListener dl) {
	this.dl = dl;
	zlistener = ZellenListener.DYNAMIC;
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
	} else if(zlistener.equals(ZellenListener.DYNAMIC)) {
	    dl.Action_on_Focus();
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
	removeDoppelteVorschläge();
	//Interface.LetMeFocus();
	createNewCheck.LetMeFocus();
	this.setFocusable(true);
	this.grabFocus();
	this.requestFocus();
    }
    
    private void removeDoppelteVorschläge() {
	for (int i = 0; i < model.size(); i++) {
	    for (int j = 0; j < model.size(); j++) {
		if (i == j) {
		    //null
		} else {
		    if (model.getElementAt(i).equals(model.getElementAt(j))) {
			model.removeElementAt(j);
			j--;
		    }
		}
	    }
	}
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
    
    public void setFramewidth(int fwidth) {
	this.framewidth = fwidth;
    }
    
    public void setFrameheight(int fheight) {
	this.frameheight = fheight;
    }
    
    @Deprecated
    public String[] getProfilStrings() {
	return werte;
    }

    @Override
    public void run() {
	String eingabe = this.getText();
	zeigeProfilVorschläge(profile.vervollständigen(eingabe));
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
    
    @Deprecated
    public void enableX() {
	if (xpanel == null) {
	    makeX();
	    this.add(xpanel);
	} else {
	    xpanel.setVisible(true);
	    xpanel.setFocusable(true);
	}
	System.out.println(xpanel.isVisible());
    }
    
    @Deprecated
    private void makeX() {
	img1 = new ImageIcon(Zelle.class.getResource("/db/X.png")).getImage();
	img1 = img1.getScaledInstance(Xwidth, Xheight, Image.SCALE_SMOOTH);
	xpanel = new Xpanel(img1);
	xpanel.setBounds((int) this.getLocationOnScreen().getX() + this.getWidth() - (int) (1.2* Xwidth), (int) (this.getLocationOnScreen().getY() + 0.35 *Xheight), Xwidth, Xheight);
	xpanel.setLayout(null);
	xpanel.setBackground(Color.red);
	xpanel.setVisible(true);
	xpanel.repaint();
	System.out.println("bounds: " + xpanel.getBounds().x + "   " + xpanel.getBounds().y);
	
	xpanel.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mousePressed(MouseEvent e) {
		setText("");
		//disableX();
	    }
	});
    }
    @Deprecated
    public void disableX() {
	xpanel.setVisible(false);
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
    DYNAMIC,
    PLZLISTENER,
    PROFILLISTENER,
    NULL,
    CALCULATE;
}

@Deprecated
class Xpanel extends JPanel {
    
    private Image img;
    
    Xpanel(Image img) {
	super();
	this.img = img;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	g.drawImage(img, 0, 0, null);
    }
    
    
}
