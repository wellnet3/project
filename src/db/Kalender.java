package db;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.sql.Date;
import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Stack;
import java.util.spi.CalendarDataProvider;

import javax.print.attribute.DateTimeSyntax;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class Kalender extends JPanel {
    
    private JPanel kalenderpanel;
    private JPanel datechooserpanel;
    private String monat;
    private int tage;
    private final Font f;
    private static ArrayList<Kalendertag> kalenderKomponenten;
    private LocalDate todaydate;
    private Image img1;
    private Image img2;
    
    private static JTextField tfZeitraum;
    private static JLabel lblMY;
    private static JButton btnNächstes;
    private static JButton btnVorheriges;
    private static int cMonat;
    private static int cYear;
    private static DateTimeFormatter formatter;
    private static String textEingabe;
    
    private String[] shortdayname = new String[] { "MO", "DI", "MI", "DO", "FR", "SA", "SO" };
    
    private static int[] selectedDays = new int[] { -1, -1 };
    private static String[] selectedDates = new String[] { "", "" };
    private static boolean[] selKalendertag = new boolean[] { false, false };
    
    Kalender() {
	System.out.println(LocalDate.now());
	LocalDate ld = LocalDate.now();
	monat = ld.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN);
	tage = ld.getMonth().length(ld.isLeapYear());
	System.out.println("monat: " + monat);
	System.out.println("tage: " + tage);
	System.out.println("derzeitiger Tag: " + ld.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.GERMAN));
	System.out.println("erster Tag des Monats: " + ld.withDayOfMonth(1));
	System.out.println("bsp: " + ld.format(DateTimeFormatter.ofPattern("d.MM.uuuu")));
	/*
	 * erster Tag des Monats bzw der erste Woche des Monats
	 */
	//todaydate = LocalDate.now();
	todaydate = LocalDate.of(2018, 9, 1);
	System.out.println("todayydate: ");
	System.out.println(todaydate.withDayOfMonth(1).getDayOfWeek());
	kalenderKomponenten = new ArrayList<Kalendertag>();
	f = new Font("Arial", Font.BOLD, 16);
	formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu", Locale.GERMAN);
	
	kalenderpanel = new JPanel();
	kalenderpanel.setBounds(0, 0, 300, 462);
	kalenderpanel.setLayout(null);
	kalenderpanel.setVisible(true);
	
	datechooserpanel = new JPanel();
	datechooserpanel.setBounds(300, 0, 384, 462);
	datechooserpanel.setLayout(null);
	datechooserpanel.setVisible(true);
	
	cMonat = todaydate.getMonthValue();
	cYear = todaydate.getYear();
	
	img1 = new ImageIcon(Interface.class.getResource("/db/nächstes.png")).getImage();
	img1 = img1.getScaledInstance(50, 30, Image.SCALE_SMOOTH);
	
	img2 = new ImageIcon(Interface.class.getResource("/db/vorheriges.png")).getImage();
	img2= img2.getScaledInstance(50, 30, Image.SCALE_SMOOTH);
	
	this.add(kalenderpanel);
	this.add(datechooserpanel);
    }
    
    public void createMonth(int monat) {
	createHeader();
	createBody(todaydate.withDayOfMonth(1).getDayOfWeek().getValue() -1, todaydate.getMonth().length(todaydate.isLeapYear()));
	createTextComponents();
    }
    
    private void createHeader() {
	Kalendertag[] kheader = new Kalendertag[7];
	for (int i = 0; i < 7; i++) {
	    kheader[i] = new Kalendertag(null, i * 40, 40, shortdayname[i], null);
	    kheader[i].makeTitle();
	    kalenderpanel.add(kheader[i]);
	}
    }
    
    private void createBody(int offset, int monatslänge) {
	for (int j = 0; j < 6; j++) {
	    for (int i = 1; i < 8; i++) {
		try {
		    kalenderKomponenten.add(new Kalendertag(todaydate.withDayOfMonth(i).getDayOfWeek(), i * 40 -40, (j +2) * 40, String.valueOf( i + (j * 7) -offset), todaydate.withDayOfMonth(i + (j * 7))));
		} catch (DateTimeException dtex) {
		    kalenderKomponenten.add(new Kalendertag(todaydate.withDayOfMonth(i).getDayOfWeek(), i * 40 -40, (j +2) * 40, String.valueOf( i + (j * 7) -offset), todaydate.withDayOfMonth(1)));
		}
		kalenderpanel.add(kalenderKomponenten.get(kalenderKomponenten.size() -1));
		if (Integer.parseInt(kalenderKomponenten.get(kalenderKomponenten.size() -1).getDatum()) < 1 || Integer.parseInt(kalenderKomponenten.get(kalenderKomponenten.size() -1).getDatum()) > monatslänge) {
		    kalenderKomponenten.get(kalenderKomponenten.size() -1).disable();
		}
	    }
	}
    }
    
    private void createTextComponents() {
	tfZeitraum = new JTextField();
	tfZeitraum.setBounds(20, 5, 240, 30);
	tfZeitraum.setFont(f);
	tfZeitraum.setVisible(true);
	kalenderpanel.add(tfZeitraum);
	
	lblMY = new JLabel();
	lblMY.setBounds(68, 325, 144, 30);
	lblMY.setFont(f);
	lblMY.setVisible(true);
	//lblMY.setBorder(BorderFactory.createLineBorder(Color.black));
	kalenderpanel.add(lblMY);
	lblMY.setText(todaydate.getMonth().getDisplayName(TextStyle.FULL, Locale.GERMAN) + " " + todaydate.getYear());
	
	btnNächstes = new JButton();
	btnNächstes.setBounds(221, 325, 50, 30);
	btnNächstes.setFont(f);
	btnNächstes.setVisible(true);
	btnNächstes.setIcon(new ImageIcon(img1));
	kalenderpanel.add(btnNächstes);
	
	btnNächstes.addActionListener((event) -> {
	    if (selectedDays[1] != -1) {
		selKalendertag[1] = true;
	    } else {
		selKalendertag[1] = false;
	    }
	    if (selectedDays[0] != -1) {
		selKalendertag[0] = true;
	    } else {
		selKalendertag[0] = false;
	    }
	    
	    this.remove(kalenderpanel);
	    //kalenderKomponenten = new ArrayList<Kalendertag>();
	    kalenderpanel = new JPanel();
	    kalenderpanel.setBounds(0, 0, 300, 462);
	    kalenderpanel.setLayout(null);
	    kalenderpanel.setVisible(true);
	    try {
		todaydate = LocalDate.of(cYear, cMonat +1, 1);
	    } catch (DateTimeException dtex) {
		cYear = cYear +1;
		cMonat = 0;
		todaydate = LocalDate.of(cYear, cMonat +1, 1);
	    }
	    cMonat = todaydate.getMonthValue();
	    createMonth(0);
	    repaint();
	    this.add(kalenderpanel);
	    tfZeitraum.setText(textEingabe);
	    
	    if (!selectedDates[0].equals("")) {
		String cString = selectedDates[0].substring(selectedDates[0].indexOf("-") +1);
		if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
		    kalenderKomponenten.get(Integer.parseInt(selectedDates[0].substring(0, selectedDates[0].indexOf("-"))) + 
			    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();
		}
	    }
	    
	    if (!selectedDates[1].equals("")) {
		String cString = selectedDates[1].substring(selectedDates[1].indexOf("-") +1);
		if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
		    kalenderKomponenten.get(Integer.parseInt(selectedDates[1].substring(0, selectedDates[1].indexOf("-"))) + 
			    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();;
		}
	    }
	});
	
	btnVorheriges = new JButton();
	btnVorheriges.setBounds(9, 325, 50, 30);
	btnVorheriges.setFont(f);
	btnVorheriges.setVisible(true);
	btnVorheriges.setIcon(new ImageIcon(img2));
	kalenderpanel.add(btnVorheriges);
	
	btnVorheriges.addActionListener((event) -> {
	    this.remove(kalenderpanel);
	    //kalenderKomponenten = new ArrayList<Kalendertag>();
	    kalenderpanel = new JPanel();
	    kalenderpanel.setBounds(0, 0, 300, 462);
	    kalenderpanel.setLayout(null);
	    kalenderpanel.setVisible(true);
	    try {
		todaydate = LocalDate.of(cYear, cMonat -1, 1);
	    } catch (DateTimeException dtex) {
		cYear = cYear -1;
		cMonat = 13;
		todaydate = LocalDate.of(cYear, cMonat -1, 1);
	    }
	    cMonat = todaydate.getMonthValue();
	    createMonth(0);
	    repaint();
	    this.add(kalenderpanel);
	    tfZeitraum.setText(textEingabe);
	    
	    if (!selectedDates[0].equals("")) {
		String cString = selectedDates[0].substring(selectedDates[0].indexOf("-") +1);
		if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
		    kalenderKomponenten.get(Integer.parseInt(selectedDates[0].substring(0, selectedDates[0].indexOf("-"))) + 
			    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();
		}
	    }
	    
	    if (!selectedDates[1].equals("")) {
		String cString = selectedDates[1].substring(selectedDates[1].indexOf("-") +1);
		if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
		    kalenderKomponenten.get(Integer.parseInt(selectedDates[1].substring(0, selectedDates[1].indexOf("-"))) + 
			    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();;
		}
	    }
	});
	
	
	tfZeitraum.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
		    try {
			speichern();
		    } catch (DateTimeException dtex) {
			if (dtex.getMessage().equals("Wrong Number Format") || dtex.getMessage().equals("Cannot ressolve this as an date")) {
			    int id = JOptionPane.showConfirmDialog(null, "Die angegebenen Daten sind möglicherweise nicht korrekt. Möchten Sie fortfahren?");
			    if (id == JOptionPane.YES_OPTION) {
				Kalenderdialog.save(tfZeitraum.getText());
			    }
			} else {
			    dtex.printStackTrace();
			    dtex.getMessage();
			    JOptionPane.showMessageDialog(null, dtex.getMessage());
			}
		    }
		}
	    }
	});
	
	
    }
    
    private void speichern() throws DateTimeException {
	int d1, d2, m1, m2, y1, y2;
	if (tfZeitraum.getText().equals("")) {
	    Kalenderdialog.save("");
	} else {
	    try {
		d1 = Integer.parseInt(tfZeitraum.getText().substring(0, 2));
		m1 = Integer.parseInt(tfZeitraum.getText().substring(3, 5));
		y1 = Integer.parseInt(tfZeitraum.getText().substring(6, 10));
	    } catch (NumberFormatException nfex) {
		throw new DateTimeException("Wrong Number Format");
	    } catch(StringIndexOutOfBoundsException sioobex) {
		throw new DateTimeException("Illegal String length");
	    }
	    try {
		LocalDate test = LocalDate.of(y1, m1, d1);
	    } catch (DateTimeException dtex) {
		throw new DateTimeException("Cannot ressolve this as an date");
	    }
	}
	
	if (tfZeitraum.getText().contains("-")) {
	    try {
		d2 = Integer.parseInt(tfZeitraum.getText().substring(13, 15));
		m2 = Integer.parseInt(tfZeitraum.getText().substring(16, 18));
		y2 = Integer.parseInt(tfZeitraum.getText().substring(19, 23));
	    } catch (NumberFormatException nfex) {
		throw new DateTimeException("Wrong Number Format");
	    } catch(StringIndexOutOfBoundsException sioobex) {
		throw new DateTimeException("Illegal String length");
	    }
	    try {
		LocalDate test = LocalDate.of(y2, m2, d2);
	    } catch (DateTimeException dtex) {
		throw new DateTimeException("Cannot ressolve this as an date");
	    }
	}
	Kalenderdialog.save(tfZeitraum.getText());
    }
    
    public static void updateSelectedKalendertag(int offset, int position) {
	int sDays = 0;
	if (selKalendertag[1]) {
	    sDays++;
	} else {
	    selectedDays[1] = -1;
	    selectedDates[1] = "";
	}
	
	if (selKalendertag[0]) {
	    sDays++;
	} else {
	    selectedDays[0] = -1;
	    selectedDates[0] = "";
	}
	//selectedDays[0] = -1;
	//selectedDays[1] = -1;    
	//selectedDates[0] = "";
	//selectedDates[1] = "";
	for (int i = 0; i < kalenderKomponenten.size(); i++) {
	    if (kalenderKomponenten.get(i).isSelected()) {
		try {
		    selectedDays[sDays] = i - offset;
		    System.out.println("selectedDays[" + sDays + "] = " + selectedDays[sDays]);
		    sDays++;
		} catch (ArrayIndexOutOfBoundsException aioobex) {
		    System.out.println("position: " + position);
		    position = position -1;
		    //System.out.println("isS 0: " + kalenderKomponenten.get(selectedDays[0] + offset).isSelected());
		    //System.out.println("isS 1: " + kalenderKomponenten.get(selectedDays[1] + offset).isSelected());
		    if (selectedDays[0] != position) {
			kalenderKomponenten.get(selectedDays[0] + offset).deselect();
			selectedDays[0] = i - offset;
		    } else {
			kalenderKomponenten.get(selectedDays[1] + offset).deselect();
			selectedDays[1] = i - offset;
		    }
		}
	    }
	}
	System.out.println("sDays: " + sDays);
	
	if (selectedDays[0] != -1) {
	    selectedDates[0] = String.valueOf(selectedDays[0] +1).concat("-").concat(String.valueOf(cMonat)).concat("-").concat(String.valueOf(cYear));
	}
	
	if (selectedDays[1] != -1) {
	    selectedDates[1] = String.valueOf(selectedDays[1] +1).concat("-").concat(String.valueOf(cMonat)).concat("-").concat(String.valueOf(cYear));
	}
	
	setZeitraum();
    }
    
    private static void setZeitraum() {
	if (selectedDays[0] == -1 && selectedDays[1] == -1) {
	    tfZeitraum.setText("");
	} else if (selectedDays[0] != -1 && selectedDays[1] == -1) {
	    tfZeitraum.setText(kalenderKomponenten.get(selectedDays[0]).getLocalDate().format(formatter));
	} else if (selectedDays[1] != -1 && selectedDays[1] != -1) {
	    if (kalenderKomponenten.get(selectedDays[0]).getLocalDate().isAfter(kalenderKomponenten.get(selectedDays[1]).getLocalDate())) {
		tfZeitraum.setText(kalenderKomponenten.get(selectedDays[1]).getLocalDate().format(formatter) + " - " + kalenderKomponenten.get(selectedDays[0]).getLocalDate().format(formatter));
	    } else {
		tfZeitraum.setText(kalenderKomponenten.get(selectedDays[0]).getLocalDate().format(formatter) + " - " + kalenderKomponenten.get(selectedDays[1]).getLocalDate().format(formatter));
	    }
	} else {
	    throw new IllegalArgumentException();
	}
	textEingabe = tfZeitraum.getText();
    }
    
    public static void main(String[] args) {
	JFrame frame = new JFrame();
	frame.setBounds(400, 400, 684, 462);
	frame.setLayout(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
	
	Kalender k = new Kalender();
	k.setLayout(null);
	k.setBounds(0, 0, 684, 462);
	k.setBackground(Color.white);
	
	k.createMonth(0);
	frame.setContentPane(k);
	
	/*frame.addKeyListener(new KeyAdapter() {
	    @Override
	    public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
		    System.out.println("HI NA");
		    k.remove(k.kalenderpanel);
		    kalenderKomponenten = new ArrayList<Kalendertag>();
		    k.kalenderpanel = new JPanel();
		    k.kalenderpanel.setBounds(0, 0, 300, 462);
		    k.kalenderpanel.setLayout(null);
		    k.kalenderpanel.setVisible(true);
		    try {
			k.todaydate = LocalDate.of(cYear, cMonat +1, 1);
		    } catch (DateTimeException dtex) {
			cYear = cYear +1;
			cMonat = 0;
			k.todaydate = LocalDate.of(cYear, cMonat +1, 1);
		    }
		    cMonat = k.todaydate.getMonthValue();
		    k.createMonth(0);
		    k.repaint();
		    k.add(k.kalenderpanel);
		    tfZeitraum.setText(textEingabe);
		    
		    if (!selectedDates[0].equals("")) {
			String cString = selectedDates[0].substring(selectedDates[0].indexOf("-") +1);
			if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
			    kalenderKomponenten.get(Integer.parseInt(selectedDates[0].substring(0, selectedDates[0].indexOf("-"))) + 
				    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();
			}
		    }
		    
		    if (!selectedDates[1].equals("")) {
			String cString = selectedDates[1].substring(selectedDates[1].indexOf("-") +1);
			if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
			    kalenderKomponenten.get(Integer.parseInt(selectedDates[1].substring(0, selectedDates[1].indexOf("-"))) + 
				    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();;
			}
		    }
		}
		
		if (e.getKeyCode() == KeyEvent.VK_LEFT) {
		    System.out.println("HI NA");
		    k.remove(k.kalenderpanel);
		    kalenderKomponenten = new ArrayList<Kalendertag>();
		    k.kalenderpanel = new JPanel();
		    k.kalenderpanel.setBounds(0, 0, 300, 462);
		    k.kalenderpanel.setLayout(null);
		    k.kalenderpanel.setVisible(true);
		    try {
			k.todaydate = LocalDate.of(cYear, cMonat -1, 1);
		    } catch (DateTimeException dtex) {
			cYear = cYear -1;
			cMonat = 13;
			k.todaydate = LocalDate.of(cYear, cMonat -1, 1);
		    }
		    cMonat = k.todaydate.getMonthValue();
		    k.createMonth(0);
		    k.repaint();
		    k.add(k.kalenderpanel);
		    tfZeitraum.setText(textEingabe);
		    
		    if (!selectedDates[0].equals("")) {
			String cString = selectedDates[0].substring(selectedDates[0].indexOf("-") +1);
			if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
			    System.out.println("TRUE");
			    kalenderKomponenten.get(Integer.parseInt(selectedDates[0].substring(0, selectedDates[0].indexOf("-"))) + 
				    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();
			}
		    }
		    
		    if (!selectedDates[1].equals("")) {
			String cString = selectedDates[1].substring(selectedDates[1].indexOf("-") +1);
			if (Integer.parseInt(cString.substring(0, cString.indexOf("-"))) == cMonat && Integer.parseInt(cString.substring(cString.indexOf("-") +1)) == cYear) {
			    kalenderKomponenten.get(Integer.parseInt(selectedDates[1].substring(0, selectedDates[1].indexOf("-"))) + 
				    LocalDate.of(cYear, cMonat, 1).getDayOfWeek().getValue() -2).select();;
			}
		    }
		}
	    }
	});*/
    }

}
