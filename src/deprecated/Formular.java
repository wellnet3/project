package deprecated;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;

public class Formular {
    
    private static final String path1 = "C:\\Users\\User\\Desktop\\Vorlage Rechnung Praxis1.dotm";
    private static final String path2 = "C:\\Users\\User\\Desktop\\Vorlage Rechnung Privat1.dotm";
    private static String[] runnables;
    private static String[] tabellentext;
    private static int position = 0;
    
    Formular() {
	
    }
    
    public static void createPraxisFormular(String[] optionen, String dateiname) throws IOException, IllegalArgumentException {
	/*
	 * optionen:
	 * 
	 * 1: Firma;
	 * 2: Anrede;
	 * 3: Vorname;
	 * 4: Name;
	 * 5: Anschrift;
	 * 6: Anschrift2;
	 * 7: PLZ;
	 * 8: Ort;
	 * 9: Rechnungsnummer;
	 * 10: Datum;
	 * 11: Text;
	 * 12: Hinweis;
	 * 13: Frist;
	 * 
	 * 14: Kategorie;
	 * 15: Betrag;
	 * 16: Betrag_Mwst;
	 * 17: Gesamt
	 */
	
	if (optionen.length != 17) {
	    throw new IllegalArgumentException();
	}
	
	runnables = new String[16];
	for (int i = 0; i < 10; i++) {
	    runnables[i] = optionen[i];
	}
	runnables[10] = optionen[1];
	runnables[11] = optionen[3];
	runnables[12] = optionen[10];
	runnables[13] = optionen[11];
	runnables[14] = optionen[16];
	runnables[15] = optionen[12];
	
	XWPFDocument docx = new XWPFDocument(new FileInputStream(path1));
	List<XWPFParagraph> paragraphlist = docx.getParagraphs();
	
	//XWPFRun[] runnable = new XWPFRun[13];
	for (int i = 0; i < paragraphlist.size(); i++) {
	    for (int j = 0; j < paragraphlist.get(i).getRuns().size(); j++) {
		System.out.println("paragraph[" + i + "].getRun[" + j + "]: " + paragraphlist.get(i).getRuns().get(j).text());
		if (paragraphlist.get(i).getRuns().get(j).text().contains("«")) {
		    paragraphlist.get(i).removeRun(j);
		    paragraphlist.get(i).getRuns().get(j).setText(runnables[position]);
		    position++;
		}
	    }
	}
	
	List<XWPFTable> tabelle = docx.getTables();
	tabellentext = new String[tabelle.size()];
	for (int i = 0; i < tabelle.size(); i++) {
	    tabellentext[i] = tabelle.get(i).getText();
	    System.out.println("tabelle[i]: " + tabelle.get(i).getText());
	}
	tabelle.get(0).getRow(0).getTableCells().get(0).removeParagraph(0);
	tabelle.get(0).getRow(0).getTableCells().get(0).setText(optionen[13]);
	for (int i = 0; i < 3; i++) {
	    tabelle.get(0).getRow(i).getTableCells().get(1).removeParagraph(0);
	    tabelle.get(0).getRow(i).getTableCells().get(1).setText(optionen[i + 14]);
	}
	docx.setTable(0, tabelle.get(0));
	
	FileOutputStream out = new FileOutputStream(new File("C:\\Users\\User\\Desktop\\" + dateiname + ".docx"));
	docx.write(out);
	docx.close();
	out.close();
    }
    
    public static void createPrivatFormular(String[] optionen, String dateiname) throws IOException, IllegalArgumentException {
	if (optionen.length != 17) {
	    throw new IllegalArgumentException();
	}
	
	runnables = new String[16];
	for (int i = 0; i < 10; i++) {
	    runnables[i] = optionen[i];
	}
	runnables[10] = optionen[1];
	runnables[11] = optionen[3];
	runnables[12] = optionen[10];
	runnables[13] = optionen[11];
	runnables[14] = optionen[16];
	runnables[15] = optionen[12];
	
	XWPFDocument docx = new XWPFDocument(new FileInputStream(path2));
	List<XWPFParagraph> paragraphlist = docx.getParagraphs();
	
	for (int i = 0; i < paragraphlist.size(); i++) {
	    for (int j = 0; j < paragraphlist.get(i).getRuns().size(); j++) {
		System.out.println("paragraph[" + i + "].getRun[" + j + "]: " + paragraphlist.get(i).getRuns().get(j).text());
		if (paragraphlist.get(i).getRuns().get(j).text().contains("«")) {
		    paragraphlist.get(i).removeRun(j);
		    paragraphlist.get(i).getRuns().get(j).setText(runnables[position]);
		    position++;
		}
	    }
	}
	List<XWPFTable> tabelle = docx.getTables();
	tabellentext = new String[tabelle.size()];
	for (int i = 0; i < tabelle.size(); i++) {
	    tabellentext[i] = tabelle.get(i).getText();
	    System.out.println("tabelle[i]: " + tabelle.get(i).getText());
	}
	tabelle.get(0).getRow(0).getTableCells().get(0).removeParagraph(0);
	tabelle.get(0).getRow(0).getTableCells().get(0).setText(optionen[13]);
	for (int i = 0; i < 3; i++) {
	    tabelle.get(0).getRow(i).getTableCells().get(1).removeParagraph(0);
	    tabelle.get(0).getRow(i).getTableCells().get(1).setText(optionen[i + 14]);
	}
	docx.setTable(0, tabelle.get(0));
	
	FileOutputStream out = new FileOutputStream(new File("C:\\Users\\User\\Desktop\\" + dateiname + ".docx"));
	docx.write(out);
	docx.close();
	out.close();
    }
    
    public static void convertToPdf(XWPFDocument doc, String dateiname) throws IOException {
	PdfConverter.getInstance().convert(doc, new FileOutputStream(new File("C:\\Users\\User\\Desktop\\" + dateiname + ".pdf")), PdfOptions.getDefault());
    }

}
