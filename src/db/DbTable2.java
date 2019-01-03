package db;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DbTable2 extends JPanel {
    
    private DynamicListener[] dls;
    private String[] headername;
    private int[] spaltenbreite;
    private int spaltenhöhe;
    private int spaltenanzahl;
    private JTextField vorlage;
    private JTextField[] header;
    private final Font f1 = new Font("Arial", Font.PLAIN, 17);
    private ArrayList<Zelle[]> zellen;
    private ArrayList<Zeilenpanel> zpanels;
    private zeilenpanelmodus zpm = zeilenpanelmodus.SINGLE_SELECTION;
    protected ArrayList<String> selectedPanel;
    private ArrayList<String[]> listenervorschläge;
    
    private int zlänge = 300;
    private int zhöhe = 400;
    
    private int PREFERRED_HEIGHT = 100; // updaten nachdem eine Zeile hinzukommt;
    
    DbTable2() {
	super();
	zellen = new ArrayList<Zelle[]>();
	zpanels = new ArrayList<Zeilenpanel>();
	selectedPanel = new ArrayList<String>();
    }
    
    public void setHeader(String[] header, int[] headerlength, int headerheight, DynamicListener[] dls) throws IllegalArgumentException {
	if (header.length != headerlength.length) {
	    throw new IllegalArgumentException();
	}
	headername = header;
	spaltenanzahl = header.length;
	spaltenbreite = headerlength;
	spaltenhöhe = headerheight;
	this.header = new JTextField[header.length];
	this.dls = dls;
	
	int pointer = 0;
	for (int i = 0; i < spaltenanzahl; i++) {
	    this.header[i] = setHeaderCell(pointer, headerlength[i], headerheight, header[i]);
	    pointer = pointer + headerlength[i];
	}
	this.setPreferredSize(new Dimension(pointer, PREFERRED_HEIGHT));
    }
    
    private JTextField setHeaderCell(int x, int width, int height, String headertext) {
	vorlage = new JTextField();
	vorlage.setEditable(false);
	vorlage.setBounds(x, 0, width, height);
	vorlage.setText(headertext);
	vorlage.setVisible(true);
	vorlage.setBackground(Color.lightGray);
	this.add(vorlage);
	vorlage.setFont(f1);
	
	if (x == 0) {
	    vorlage.addActionListener(new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
		    
		}
	    });
	}
	return vorlage;
    }
    
    public void addColumn(String[] werte) {
	if (werte == null) {
	    werte = new String[spaltenanzahl];
	    for (int i = 0; i < spaltenanzahl; i++) {
		werte[i] = "";
	    }
	}
	//Zelle[] felder = new Zelle[spaltenanzahl];
	int pointer = 0;
	for (int i = 0; i < spaltenanzahl; i++) {
	    //felder[i] = createNewCell(pointer, (zellen.size() *spaltenhöhe) + spaltenhöhe, spaltenbreite[i], spaltenhöhe, zellen.size() +1, i, werte[i], dls[i]);
	    //pointer = pointer + spaltenbreite[i];
	    //felder[i].setFont(f1);
	    //this.add(felder[i]);
	}
	createNewCellColumn(pointer, (zellen.size() *spaltenhöhe) + spaltenhöhe, spaltenbreite[0], spaltenhöhe, zellen.size() +1, werte);
	//zellen.add(felder);
	//PREFERRED_HEIGHT = PREFERRED_HEIGHT + spaltenhöhe;
    }
    
    private void createNewCellColumn(int x, int y, int width, int height, int zeile, String[] texte) {
	width = 0;
	for (int i = 0; i < spaltenanzahl; i++) {
	    width = width + spaltenbreite[i];
	}
	Zeilenpanel zp = new Zeilenpanel(zeile);
	zp.setBounds(x, y, width, height);
	zp.setLayout(null);
	zp.setBorder(BorderFactory.createLoweredBevelBorder()); // Border ja/nein ?
	zp.setVisible(true);
	zp.setBackground(Color.white);
	zp.setFont(f1);
	zp.addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (zpm.equals(zeilenpanelmodus.SINGLE_SELECTION)) {
		    if (zp.isSelected()) {
			zp.deselect();
			selectedPanel.clear();
		    } else {
			zp.select();
			selectedPanel.clear();
			selectedPanel.add(String.valueOf(zp.getZeile()));
		    }
		} else {
		    if (zp.isSelected()) {
			zp.deselect();
			selectedPanel.remove(String.valueOf(zp.getZeile()));
		    } else {
		   	zp.select();
		   	selectedPanel.add(String.valueOf(zp.getZeile()));
		    }
		}
		//+ hideListenerFrame();
		selectZeilen();
	    }
	});
	zpanels.add(zp);
	this.add(zp);
	y = (int) (0.25 * height);
	height = height - (int) (0.5 * height);
	
	Zelle[] felder = new Zelle[spaltenanzahl];
	int pointer = 0;
	for (int i = 0; i < spaltenanzahl; i++) {
	    felder[i] = createNewCell(pointer, y, spaltenbreite[i], height, zeile, i, texte[i], dls[i]);
	    pointer = pointer + spaltenbreite[i];
	    felder[i].setFont(f1);
	    zp.add(felder[i]);
	    felder[i].repaint();
	}
	zellen.add(felder);
	PREFERRED_HEIGHT = PREFERRED_HEIGHT + spaltenhöhe;
	this.setPreferredSize(new Dimension(this.getWidth(), PREFERRED_HEIGHT));
	this.revalidate();
    }
    
    private Zelle createNewCell(int x, int y, int width, int height, int zeile, int spalte, String text, DynamicListener dl) {
	Zelle z = new Zelle(zeile, spalte);
	z.setBounds(x, y, width, height);
	z.setText(text);
	z.addListener(dl);
	z.setVisible(true);
	z.setBackground(Color.yellow);
	z.titel = header[spalte].getText();
	z.addFocusListener(new FocusListener() {
	    @Override
	    public void focusLost(FocusEvent e) {
		//null
	    }
	    @Override
	    public void focusGained(FocusEvent e) {
		//z.setFocusable(false);
		z.getParent().requestFocus();
		z.showSuggestions();
		z.setFocusable(true);
		
	    } 
	});
	return z;
    }
    
    @Deprecated
    public void removeLastColumn() {
   	for (int i = 0; i < spaltenanzahl; i++) {
   	    this.remove(this.getComponentCount() -1);
   	}
   	zellen.remove(zellen.size() -1);
   	PREFERRED_HEIGHT = PREFERRED_HEIGHT - spaltenhöhe;
   	this.repaint();
    }
    
    public void removeColumn(int zeile) {
	zellen.remove(zeile);
	this.remove(zpanels.get(zeile));
	zpanels.remove(zeile);
	
	for (int i = zeile; i < zpanels.size(); i++) {
	    zpanels.get(i).setBounds(zpanels.get(i).getX(), zpanels.get(i).getY() - this.spaltenhöhe, zpanels.get(i).getWidth(), zpanels.get(i).getHeight());
	    zpanels.get(i).setZeile(zpanels.get(i).getZeile() -1);
	}
	for (String s : selectedPanel) {
	    //zpanels.get(Integer.parseInt(s) -1).deselect();
	}
    }
    
    public void setEditCheckText(String[] text, int zeile) {
	Zelle[] zellen = this.getCells().get(zeile);
	zellen[4].setText(text[0]);
	zellen[3].setText(text[1]);
	zellen[13].setText(text[2]);
	zellen[14].setText(text[3]);
	zellen[15].setText(text[4]);
	zellen[16].setText(text[5]);
	zellen[17].setText(text[6]);
	zellen[20].setText(text[7]);
	zellen[23].setText(text[8]);
	zellen[24].setText(text[9]);
	zellen[25].setText(text[10]);
	zellen[26].setText(text[11]);
	zellen[27].setText(text[12]);
	this.getCells().set(zeile, zellen);
    }
    
    public DynamicListener editCheckListener(int zeile) {
	return new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		setEditCheckText(createNewCheck.editCheck(null, getCells().get(zeile)), zeile);
	    }
	};
    }
    
    
    public ArrayList<Zelle[]> getCells() {
	return zellen;
    }
    
    public void setZellen(ArrayList<Zelle[]> al) {
	//INSTANZPRÜFUNG!
	zellen = al;
	updatePanels();
	this.repaint();
    }
    
    private void updatePanels() {
	for (int i = 0; i < zpanels.size(); i++) {
	    zpanels.get(i).removeAll();
	    for (int j = 0; j < this.getCells().get(0).length; j++) {
		zpanels.get(i).add(this.getCells().get(i)[j]);
	    }
	}
    }
    
    public JTextField[] getHeader() {
	return header;
    }
    
    public void setModus(zeilenpanelmodus modus) { zpm = modus; }
    public zeilenpanelmodus getModus() { return zpm; }
    
    public void selectZeilen() {
	for (int i = 0; i < zpanels.size(); i++) {
	    System.out.println("zpanels[" + i + "].isSelected: " + zpanels.get(i).isSelected());
	    zpanels.get(i).deselect();
	    System.out.println("zpanels[" + i + "].isSelected: " + zpanels.get(i).isSelected());
	}
	for (String s : selectedPanel) {
	    zpanels.get(Integer.parseInt(s) -1).select();
	}
    }
    
    public void setListenerVorschläge(ArrayList<String[]> Listenervorschläge) {
	listenervorschläge = Listenervorschläge;
    }
    
    public void setText(int zeile, String[] eingaben) {
	for (int i = 0; i < eingaben.length; i++) {
	    zellen.get(zeile)[i].setText(eingaben[i]);
	}
    }
    
    public String[] getText(int zeile) {
	String[] ausgaben = new String[zellen.get(zeile).length];
	for (int i = 0; i < ausgaben.length; i++) {
	    ausgaben[i] = zellen.get(zeile)[i].getText();
	}
	return ausgaben;
    }

}

class Zeilenpanel extends JPanel {
    
    private boolean isSelected = false;
    private int zeile;
    
    Zeilenpanel(int zeile) {
	super();
	this.zeile = zeile;
    }
    
    public boolean isSelected() {
	return isSelected;
    }
    
    public void setSelected(boolean b) {
	if (b) {
	    this.select();
	} else {
	    this.deselect();
	}
    }
    
    public void select() {
	isSelected = true;
	this.setBackground(Color.cyan);
	this.repaint();
    }
    
    public void deselect() {
	isSelected = false;
	this.setBackground(Color.white);
	this.repaint();
    }
    
    public int getZeile() {
	return zeile;
    }
    
    public void setZeile(int zeile) {
	this.zeile = zeile;
    }
    
}

enum zeilenpanelmodus {
    SINGLE_SELECTION,
    MULTIPLE_SELECTION;
}
