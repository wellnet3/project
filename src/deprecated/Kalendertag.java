package deprecated;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;

public class Kalendertag extends JPanel implements MouseListener {
    
    private final int width = 40;
    private final int height = 40;
    private final Font f;
    private final String wt;
    private final DayOfWeek wochentag;
    private final LocalDate localdate;
    private FontMetrics metrics;
    private boolean isSelected = false;
    private boolean isTitle = false;
    
    Kalendertag(DayOfWeek dow, int x, int y, String wochentag, LocalDate ld) {
	this.setBounds(x, y, width, height);
	this.setBackground(Color.green);
	this.repaint();
	this.setVisible(true);
	this.setBorder(BorderFactory.createLineBorder(Color.black));
	f = new Font("Arial", Font.BOLD, 16);
	this.setFont(f);
	this.wt = wochentag;
	localdate = ld;
	this.wochentag = dow;
	
	this.addMouseListener(this);
	this.setFocusable(true);
    }
    
    public void disable() {
	this.setFocusable(false);
	this.setBackground(Color.gray);
	this.removeMouseListener(this);
    }
    
    public void Feiertag() {
	this.setFocusable(false);
	this.setBackground(Color.red);
	this.removeMouseListener(this);
    }
    
    public void makeTitle() {
	isTitle = true;
	isSelected = false;
	this.setBackground(Color.white);
	this.setFocusable(false);
	this.removeMouseListener(this);
	
    }
    
    public DayOfWeek getWochentag() throws NullPointerException {
	return wochentag;
    }
    
    public String getDatum() {
	return wt;
    }
    
    @Override
    public void paint(Graphics g) {
	super.paint(g);
	Graphics g2d = (Graphics2D) g;
	metrics = g2d.getFontMetrics(f);
	int mittigX = (int) (width /2 - metrics.stringWidth(wt) /2);
	g2d.drawString(String.valueOf(wt), mittigX, (int) (height /3 + height /2.5));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
	if (isSelected) {
	    this.deselect();
	    Kalender.updateSelectedKalendertag(localdate.withDayOfMonth(1).getDayOfWeek().getValue() -1, -1);
	} else {
	    this.select();
	    Kalender.updateSelectedKalendertag(localdate.withDayOfMonth(1).getDayOfWeek().getValue() -1, Integer.parseInt(wt));
	    System.out.println(wochentag);
	}
    }

    @Override
    public void mousePressed(MouseEvent e) {
	
    }

    @Override
    public void mouseReleased(MouseEvent e) {
	
    }

    @Override
    public void mouseEntered(MouseEvent e) {
	
    }

    @Override
    public void mouseExited(MouseEvent e) {
	
    }
    
    public LocalDate getLocalDate() {
	//System.out.println("Local Date: " + localdate);
	return localdate;
    }
    
    public boolean isTitle() {
	return isTitle;
    }
    
    public boolean isSelected() {
	return isSelected;
    }
    
    public void deselect() {
	this.setBackground(Color.green);
	isSelected = false;
    }
    
    public void select() {
	this.setBackground(Color.getHSBColor((float) 0.5, (float) 0.9, (float) 0.92));
	isSelected = true;
    }

}

@Deprecated
enum Wochentag {
    MONTAG,
    DIENSTAG,
    MITTWOCH,
    DONNERSTAG,
    FREITAG,
    SAMSTAG,
    SONNTAG;
}
