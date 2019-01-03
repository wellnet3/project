package db;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.rmi.UnexpectedException;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DbEingabe extends JPanel {
    
    private JTextField tfheader;
    private Zelle tfeingabe;
    private Font f1 = new Font("Arial", Font.PLAIN, 17);
    
    private String WERT;
    
    DbEingabe(int x, int y, int width, int height, String headername) {
	super();
	super.setBounds(x, y, width, height);
	super.setLayout(null);
	super.setBorder(BorderFactory.createLineBorder(Color.black));
	super.setVisible(true);
	super.setBackground(Color.white);
	
	createHeader(headername);
	createEingabe();
	this.repaint();
    }
    
    private void createHeader(String headername) {
	tfheader = new JTextField();
	tfheader.setBounds(0, 0, this.getWidth(), this.getHeight() /2);
	tfheader.setEditable(false);
	tfheader.setText(headername);
	tfheader.setVisible(true);
	tfheader.setBackground(Color.white);
	tfheader.setFont(f1);
	this.add(tfheader);
    }
    
    private void createEingabe() {
	tfeingabe = new Zelle();
	tfeingabe.setBounds(0, this.getHeight() /2, this.getWidth(), this.getHeight() /2);
	tfeingabe.setVisible(true);
	tfeingabe.setBackground(Color.yellow);
	tfeingabe.setFont(f1);
	tfeingabe.setFocusable(true);
	tfeingabe.grabFocus();
	this.add(tfeingabe);
    }
    
    public void addListener(ZellenListener zl, String[] eingabewerte) {
	if (zl == null || zl.equals(ZellenListener.NULL)) {
	    //Null
	} else if (zl.equals(ZellenListener.LISTENER)) {
	    tfeingabe.setFrameheight(300);
	    tfeingabe.setFramewidth(this.getWidth());
	    tfeingabe.addListener(zl);
	    tfeingabe.frame.setAlwaysOnTop(true);
	    tfeingabe.addFocusListener(new FocusListener() {
		@Override
		public void focusGained(FocusEvent e) {
		    if (!tfeingabe.isFrameVisible()) {
	        	tfeingabe.showSuggestions();
	        	tfeingabe.grabFocus();
	            }
		}
		
		@Override
		public void focusLost(FocusEvent e) {
		}
	    });
	    tfeingabe.addKeyListener(new KeyAdapter() {
		@Override
		public void keyPressed(KeyEvent e) {
		    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			tfeingabe.hideListenerFrame();
		    } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			//muss noch implementiert werden!!!
		    }
		}
	    });
	    tfeingabe.setWerte(eingabewerte);
	    tfeingabe.enableX();
	}
    }
    
    public void addListener(DynamicListener dl) {
	tfeingabe.addListener(dl);
	tfeingabe.addFocusListener(new FocusListener() {
	    @Override
	    public void focusLost(FocusEvent e) {
	    }
	    
	    @Override
	    public void focusGained(FocusEvent e) {
		tfeingabe.setFocusable(false);
		dl.Action_on_Focus();
		tfeingabe.setFocusable(true);
	    }
	});
    }
    
    public void setText(String text) {
	tfeingabe.setText(text);
    }
    
    public String getText() {
	return tfeingabe.getText();
    }
    
    public void setCellHeight(int newCellHeight) {
	tfheader.setBounds(tfheader.getX(), tfheader.getY(), tfheader.getWidth(), this.getHeight() - newCellHeight);
	tfeingabe.setBounds(tfeingabe.getX(), tfheader.getHeight(), tfeingabe.getWidth(), newCellHeight);
	this.repaint();
    }
    
    public void setCellWidth(int newCellWidth) {
	tfheader.setBounds(tfheader.getX(), tfheader.getY(), newCellWidth, tfheader.getHeight());
	tfeingabe.setBounds(tfeingabe.getX(), tfheader.getHeight(), newCellWidth, tfeingabe.getHeight());
	this.repaint();
    }
    
    public Zelle getCell() {
	return tfeingabe;
    }
    
    public JTextField getHeader() {
	return tfheader;
    }
    
    public void hideListenerFrames() {
	try {
	    createNewCheck.LetMeFocus();
	    tfeingabe.setFocusable(false);
	    createNewCheck.LetMeFocus();
	    tfeingabe.hideListenerFrame();
	} catch (NullPointerException npex) {
	    
	} finally {
	    tfeingabe.setFocusable(true);
	}
    }
}

