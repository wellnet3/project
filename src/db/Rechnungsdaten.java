package db;

@Deprecated
public class Rechnungsdaten {
    
    private final int Rechnungsnummer;
    private String datum;
    private String frist;
    private double betrag;
    private String kategorie;
    private String firma;
    private String anrede;
    private String vorname;
    private String nachname;
    private String anschrift;
    private String anschrift2;
    private String plz;
    private String ort;
    private double km;
    private String text;
    private String eingang;
    private String anmerkung;
    private boolean cmwst;
    private double mwst;
    private String hinweis;
    private double gesamt;
    private double kmpreis;
    
    @Deprecated
    Rechnungsdaten(int Rnummer, String Datum, String Frist, double Betrag, String Kategorie, String Firma, String Anrede, String Vorname, String Nachname,
	     String Anschrift, String Anschrift2, String PLZ, String Ort, double Km, String Text, String Eingang, String Anmerkung, boolean Cmwst, String Hinweis ) {
	Rechnungsnummer = Rnummer;
	datum = Datum;
	frist = Frist;
	betrag = Betrag;
	kategorie = Kategorie;
	firma = Firma;
	anrede = Anrede;
	vorname = Vorname;
	nachname = Nachname;
	anschrift = Anschrift;
	anschrift2 = Anschrift2;
	plz = PLZ;
	ort = Ort;
	km = Km;
	text = Text;
	eingang = Eingang;
	cmwst = Cmwst;
	hinweis = Hinweis;
	
	if (cmwst) {
	    gesamt = betrag * 1.19;
	    mwst = betrag * 0.19;
	} else {
	    gesamt = betrag;
	    mwst = 0;
	}
	
	kmpreis = km * 0.3;
    }
    
    public final int getRechnungsnummer() {
	return Rechnungsnummer;
    }
    
    public String getDatum() {
	return datum;
    }
    
    public String getFrist() {
	return frist;
    }
    
    public double getBetrag() {
	return betrag;
    }
    
    public String getKategorie() {
	return kategorie;
    }
    
    public String getFirma() {
	return firma;
    }
    
    public String getAnrede() {
	return anrede;
    }
    
    public String getVorname() {
	return vorname;
    }
    
    public String getNachname() {
	return nachname;
    }
    
    public String getAnschrift() {
	return anschrift;
    }
    
    public String getAnschrift2() {
	return anschrift2;
    }
    
    public String getPLZ() {
	return plz;
    }
    
    public String getOrt() {
	return ort;
    }
    
    public double getKm() {
	return km;
    }
    
    public double getKmpreis() {
	return kmpreis;
    }
    
    public String getText() {
	return text;
    }
    
    public String getEingang() {
	return eingang;
    }
    
    public String getAnmerkung() {
	return anmerkung;
    }
    
    public boolean getMwst() {
	return cmwst;
    }
    
    public double getMwstBetrag() {
	return mwst;
    }
    
    public double getGesamt() {
	return gesamt;
    }
    
    public String getHinweis() {
	return hinweis;
    }
}
