package db;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class test {
    
    private static JFrame frame;
    private static DbTable2 tabelle;
    
    public static void main(String[] args) {
	
	Zelle[] zellen = new Zelle[28];
	for (int i = 0; i < 28; i++) {
	    zellen[i] = new Zelle(0, i);
	}
	zellen[4].setText("HEY NA");
	
	DynamicListener dl = new DynamicListener() {
	    @Override
	    public void Action_on_Focus() {
		System.out.println("Clicked");
	    }
	};
	
	frame = new JFrame("TEST");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setBounds(0, 0, 1000, 600);
	frame.setVisible(true);
	frame.setBackground(Color.green);
	frame.getContentPane().setBackground(Color.green);
	
	tabelle = new DbTable2();
	tabelle.setHeader(new String[] { "SELECT", "ZWEI", "DREI" }, new int[] { 250, 250, 250 }, 80, new DynamicListener[] { dl, dl, dl });
	tabelle.setVisible(true);
	tabelle.setLayout(null);
	tabelle.addMouseListener(new MouseAdapter() {
	    //@Override
	});
	
	frame.add(tabelle);
	tabelle.addColumn(new String[] { "1", "2", "3" });
	tabelle.addColumn(new String[] { "4", "5", "6" });
	tabelle.addColumn(new String[] { "7", "8", "9" });
	tabelle.addColumn(new String[] { "7", "8", "9" });
	tabelle.addColumn(new String[] { "7", "8", "9" });
	tabelle.addColumn(new String[] { "7", "8", "9" });
	tabelle.addColumn(new String[] { "7", "8", "9" });
	frame.repaint();
	
	ArrayList<Zelle[]> al = tabelle.getCells();
	for (int i = 0; i < al.size(); i++) {
	    Zelle[] tmp = new Zelle[al.get(i).length -1];
	    tmp[0] = al.get(i)[1];
	    tmp[1] = al.get(i)[2];
	    al.remove(i);
	    al.add(i, tmp);
	}
	tabelle.setZellen(al);
	
	JTextField[] header = tabelle.getHeader();
	header[0].addMouseListener(new MouseAdapter() {
	    @Override
	    public void mouseClicked(MouseEvent e) {
		if (header[0].getText().equals("SELECT: SINGLE")) {
		    header[0].setText("SELECT: MULTIPLE");
		    tabelle.setModus(zeilenpanelmodus.MULTIPLE_SELECTION);
		} else {
		    header[0].setText("SELECT: SINGLE");
		    tabelle.setModus(zeilenpanelmodus.SINGLE_SELECTION);
		}
	    }
	});
	
	//try {
	    //Formular.createPraxisFormular(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"}, "FRAGEZEICHEN");
	//} catch (IOException ioex) {
	    //ioex.printStackTrace();
	    //ioex.getMessage();
	//}
	
    }

}
