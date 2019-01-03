package deprecated;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

public class Profil {
    
    private ArrayList<String[]> profile;
    private String path = "C:\\Users\\User\\Desktop\\profile.txt";
    private ArrayList<String> eingaben;
    
    Profil() {
	
    }
    
    Profil(String pfad) {
	path = pfad;
    }
    
    Profil(String Firma, String Anrede, String Vorname, String Nachname, String Anschrift, String Anschrift2, String PLZ, String Ort) throws IOException {
	eingaben = new ArrayList<String>();
	String zeile;
	BufferedReader in = new BufferedReader(new FileReader(path));
	while ((zeile = in.readLine()) != null) {
	    eingaben.add(zeile);
	}
	in.close();
	BufferedWriter out = new BufferedWriter(new FileWriter(path));
	for (int i = 0; i < eingaben.size(); i++) {
	    out.write(eingaben.get(i));
	}
	out.write(Firma.concat(",").concat(Anrede).concat(",").concat(Vorname).concat(",").concat(Nachname).concat(",").concat(Anschrift).concat(",")
		.concat(Anschrift2).concat(",").concat(PLZ).concat(",").concat(Ort));
	out.close();
    }
    
    public void einlesen() throws IOException {
	profile = new ArrayList<String[]>();
	BufferedReader in = new BufferedReader(new FileReader(path));
	String zeile;
	while ((zeile = in.readLine()) != null) {
	    String[] s = new String[8];
	    for (int i = 0; i < 8; i++) {
		try {
		    s[i] = zeile.substring(0, zeile.indexOf(","));
		    zeile = zeile.substring(zeile.indexOf(",") +1);
		} catch (StringIndexOutOfBoundsException sioobex) {
		    s[i] = zeile;
		}
		System.out.println("s[i]: " + s[i]);
	    }
	    profile.add(s);
	}
	in.close();
    }
    
    public ArrayList<String[]> vervollständigen(String eigenschaft, String option) {
	ArrayList<String[]> cprofile = new ArrayList<String[]>();
	try {
	    einlesen();
	} catch (IOException ioex) {
	    ioex.printStackTrace();
	    ioex.getMessage();
	    JOptionPane.showMessageDialog(null, "Profile konnten nicht geladen werden");
	}
	if (eigenschaft.equals("firma")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[0].equals(option) || profile.get(i)[0].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("anrede")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[1].equals(option) || profile.get(i)[1].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("vorname")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[2].equals(option) || profile.get(i)[2].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("nachname")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[3].equals(option) || profile.get(i)[3].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("anschrift")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[4].equals(option) || profile.get(i)[4].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("anschrift2")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[5].equals(option) || profile.get(i)[5].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("PLZ")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[6].equals(option) || profile.get(i)[6].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	} else if (eigenschaft.equals("ort")) {
	    for (int i = 0; i < profile.size(); i++) {
		if (profile.get(i)[7].equals(option) || profile.get(i)[7].startsWith(option)) {
		    cprofile.add(profile.get(i));
		}
	    }
	    return cprofile;
	}
	return null;
    }

}
