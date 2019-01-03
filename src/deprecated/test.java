package deprecated;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
    
    public static void main(String[] args) {
	
	JFrame frame = new JFrame();
	frame.setBounds(0, 0, 1500, 800);
	frame.setVisible(true);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	DbTable dbtable = new DbTable();
	dbtable.setBounds(0, 0, 800, 400);
	dbtable.setBackground(Color.white);
	dbtable.setBorder(new EmptyBorder(5, 5, 5, 5));
	dbtable.setVisible(true);
	//dbtable.setHeader(new String[] { "einZ", "Zwei", "Drei", "vier", "funf" }, new int[] { 100, 100, 100, 200, 700 });
	dbtable.addColumn();
	
	frame.add(dbtable);
	
	JScrollPane scrollPane = new JScrollPane(dbtable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
	scrollPane.setBounds(100, 100, 800, 400);
	frame.add(scrollPane);
	
	scrollPane.setViewportView(dbtable);
	
	dbtable.setLayout(null);
	
	frame.repaint();
	
	//try {
	    //Formular.createPraxisFormular(new String[] {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17"}, "FRAGEZEICHEN");
	//} catch (IOException ioex) {
	    //ioex.printStackTrace();
	    //ioex.getMessage();
	//}
	
    }

}
