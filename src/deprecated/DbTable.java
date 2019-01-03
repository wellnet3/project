package deprecated;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class DbTable extends JPanel {
    
    private JTextField vorlage;
    private static ArrayList<Zelle[]> zellen;
    private int spaltenanzahl;
    private int[] spaltenbreite;
    private String[] headername;
    private ZellenListener[] zls;
    private int cZeile;
    private int listenerspaltenposition = 0;
    private ArrayList<String[]> listenervorschläge;
    private Thread listener;
    private PLZ plz;
    
    /*
     * Die Dateien im Ordner TextVorschläge einlesen und anschließend an die entsprechenden Zellen übergeben
     * 
     */
    
    DbTable() {
	super();
	zellen = new ArrayList<Zelle[]>();
	plz = new PLZ();
	try {
	    plz.einlesen();
	} catch (IOException ioex) {
	    ioex.printStackTrace();
	    ioex.getMessage();
	}
    }
    
    public void setHeader(String[] header, int[] headerlength, ZellenListener[] zlistener) throws IllegalArgumentException {
	if (header.length != headerlength.length) {
	    throw new IllegalArgumentException();
	}
	headername = header;
	spaltenanzahl = header.length;
	spaltenbreite = headerlength;
	int pointer = 0;
	for (int i = 0; i < header.length; i++) {
	    addHeaderCell(pointer, headerlength[i], header[i]);
	    pointer = pointer + headerlength[i];
	}
	this.setPreferredSize(new Dimension(pointer, this.getHeight()));
	zls = zlistener;
	listenervorschläge = new ArrayList<String[]>();
    }
    
    private void addHeaderCell(int x, int width, String headertext) {
	vorlage = new JTextField();
	vorlage.setEditable(false);
	vorlage.setBounds(x, 0, width, 25);
	vorlage.setText(headertext);
	vorlage.setVisible(true);
	vorlage.setBackground(Color.lightGray);
	this.add(vorlage);
    }
    
    public void addColumn() {
	Zelle[] felder = new Zelle[spaltenanzahl];
	int pointer = 0;
	cZeile = zellen.size() +1;
	for (int i = 0; i < spaltenanzahl; i++) {
	    felder[i] = createNewCell(pointer, (zellen.size() * 25) +25,  spaltenbreite[i], cZeile, i, zls[i]);
	    pointer = pointer + spaltenbreite[i];
	    this.add(felder[i]);
	}
	zellen.add(felder);
	listenerspaltenposition = 0;
    }
    
    private Zelle createNewCell(int x, int y, int width, int zeile, int spalte, ZellenListener zl) {
	Zelle z = new Zelle(zeile, spalte, 1);
	z.setBounds(x, y, width, 25);
	z.setVisible(true);
	z.setText("");
	z.setBackground(Color.yellow);
	if (zl.equals(ZellenListener.LISTENER)) {
	    z.addListener(ZellenListener.LISTENER);
	    //z.setWerte(new String[] { "einZ", "Zwei", "drei" });
	    System.out.println("listenervorschläge: " + listenervorschläge.get(0));
	    z.setWerte(listenervorschläge.get(0));
	    z.addFocusListener(new FocusListener() {

		@Override
		public void focusGained(FocusEvent e) {
		    z.showSuggestions();
		}
		
		@Override
		public void focusLost(FocusEvent e) {
		    //null
		}
	    });
	    /*try {
		z.setWerte(listenervorschläge.get(listenerspaltenposition));
	    } catch (NullPointerException npex) {
		JOptionPane.showMessageDialog(null, "keine Werte gesetzt");
	    } catch (StringIndexOutOfBoundsException sioobex) {
		JOptionPane.showMessageDialog(null, "IndexOutOfRange");
	    }
	    listenerspaltenposition++;*/
	} else if (zl.equals(ZellenListener.PROFILLISTENER)) {
	    z.addListener(ZellenListener.PROFILLISTENER);
	    //listener = new Thread(z);
	    z.addFocusListener(new FocusListener() {
	        
	        @Override
	        public void focusLost(FocusEvent e) {
	        }
	        
	        @Override
	        public void focusGained(FocusEvent e) {
	            if (!z.isFrameVisible()) {
	        	z.showSuggestions();
	        	z.grabFocus();
	            }
	        }
	    });
	    
	    z.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			z.hideListenerFrame();
		    }
		    z.showSuggestions();
		    z.grabFocus();
		    /*
		     * Aktualisiert leider nur später, bzw nach dem InputEvent
		     * vllt würde ein Document-Listener bei dem Eingabefeld dies besser tun
		     */
		}
	    });
	} else if (zl.equals(ZellenListener.PLZLISTENER)) {
	    z.addListener(ZellenListener.PLZLISTENER); // <-- stehen lassen, ist wichtig!!!
	    z.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			try {
			    if (!plz.getOrt(z.getText()).equals(null)) {
				zellen.get(zeile -1)[spalte +1].setText(plz.getOrt(z.getText()));
				} 
			} catch (NullPointerException npex) {
			    try {
				if (!plz.getPLZ(z.getText()).equals(null)) {
				    zellen.get(zeile -1)[spalte -1].setText(plz.getPLZ(z.getText()));
				}
			    } catch (NullPointerException npex2) {
				JOptionPane.showMessageDialog(null, "PLZ / Ort konnte nicht gefunden werden");
			    }
			}  
		    } 
		}
	    });
	} else if (zl.equals(ZellenListener.CALCULATE)) {
	    z.addListener(ZellenListener.CALCULATE);
	    z.addFocusListener(new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
		    
		}
		
		@Override
		public void focusLost(FocusEvent e) {
		    
		}
	    });
	}
	
	return z;
    }
    
    public void setListenerVorschläge(ArrayList<String[]> Listenervorschläge) {
	listenervorschläge = Listenervorschläge;
    }
    
    public void addInputListener(int zeile, int[] eingabezellen, int ausgabezelle, String[] Rechenoperationen, double Faktor) {
	zellen.get(zeile)[ausgabezelle].setEditable(false);
	zellen.get(zeile)[ausgabezelle].setText("0");
	for (int i = 0; i < eingabezellen.length; i++) {
	    zellen.get(zeile)[eingabezellen[i]].getDocument().addDocumentListener(new DocumentListener() {
		
		@Override
		public void insertUpdate(DocumentEvent e) {
		    notifyCell(zeile, eingabezellen, ausgabezelle, Rechenoperationen, Faktor);
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
		    notifyCell(zeile, eingabezellen, ausgabezelle, Rechenoperationen, Faktor);
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		    //null
		}
	    });
	}
    }
    
    private void notifyCell(int zeile, int[] eingabezellenSpalten, int ausgabezelle, String[] Rechenoperationen, double Faktor) {
	try {
	    double[] eingaben = new double[eingabezellenSpalten.length];
	    double value = 0;
	    for (int i = 0; i < eingabezellenSpalten.length; i++) {
		if (!zellen.get(zeile)[eingabezellenSpalten[i]].getText().endsWith("€")) {
		    eingaben[i] = Double.parseDouble(zellen.get(zeile)[eingabezellenSpalten[i]].getText());
		} else {
		    eingaben[i] = Double.parseDouble(zellen.get(zeile)[eingabezellenSpalten[i]].getText().replace("€", ""));
		}
	    }
	    value = eingaben[0];
	    for (int i = 0; i < eingaben.length -1; i++) {
		if (eingaben.length == 1) {
		    value = value * Faktor;
		    zellen.get(zeile)[ausgabezelle].setText(String.valueOf(value));
		    break;
		} else {
		    switch (Rechenoperationen[i]) {
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
	    value = value * Faktor;
	    zellen.get(zeile)[ausgabezelle].setText(String.valueOf(value));
	    zellen.get(zeile)[ausgabezelle].setText(zellen.get(zeile)[ausgabezelle].getText() + "€");
	} catch (NumberFormatException nfex) {
	    System.out.println("ILLEGAL NUMBER FORMAT");
	    zellen.get(zeile)[ausgabezelle].setText("NULL");
	}
    }
    
    public static void setCellProfilText(String[] eingabe, int zeile) {
	int v = 0;
	for (int i = 0; i < zellen.get(0).length; i++) {
	    if (zellen.get(zeile -1)[i].isProfilListener()) {
		zellen.get(zeile -1)[i].setText(eingabe[v]);
		v++;
	    }
	}
    }
    
    public String getSpaltenName(final int position) {
	return headername[position].toLowerCase();
    }
    
    public void removeLastColumn() {
	for (int i = 0; i < spaltenanzahl; i++) {
	    this.remove(this.getComponentCount() -1);
	}
	zellen.remove(zellen.size() -1);
	this.repaint();
    }
    
    public void setText(int column, String[] eingaben) {
	for (int i = 0; i < eingaben.length; i++) {
	    zellen.get(column)[i].setText(eingaben[i]);
	}
    }
    
    public ArrayList<Zelle[]> getCells() {
	return zellen;
    }

}
