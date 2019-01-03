package deprecated;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class PLZ {
    
    private String path = "C:\\Users\\User\\Desktop\\PLZ.txt";
    private ArrayList<String> plzliste;
    private ArrayList<String> ortsliste;
    
    PLZ() {
	
    }
    
    PLZ(String pfad) {
	path = pfad;
    }
    
    public String getPLZ(String ort) {
	for (int i = 0; i < ortsliste.size(); i++) {
	    if (ortsliste.get(i).equals(ort)) {
		return plzliste.get(i);
	    }
	}
	return null;
    }
    
    public String getOrt(String PLZ) {
	for (int i = 0; i < plzliste.size(); i++) {
	    if (plzliste.get(i).equals(PLZ)) {
		return ortsliste.get(i);
	    }
	}
	return null;
    }
    
    public void einlesen() throws IOException {
	plzliste = new ArrayList<String>();
	ortsliste = new ArrayList<String>();
	String zeile;
	BufferedReader in = new BufferedReader(new FileReader(path));
	while ((zeile = in.readLine()) != null) {
	    if (zeile.charAt(5) != '-') {
		plzliste.add(zeile.substring(0, zeile.indexOf(" ")));
		ortsliste.add(zeile.substring(zeile.indexOf(" ") +1));
		System.out.println("plzliste: " + plzliste.get(plzliste.size() -1));
		System.out.println("ortsliste: " + ortsliste.get(ortsliste.size() -1));
	    } else {
		for (int i = 0; i < Integer.parseInt(zeile.substring(zeile.indexOf("-") +1, zeile.indexOf("-") +6)) - Integer.parseInt(zeile.substring(0, 5)) +1; i++) {
		    plzliste.add(String.valueOf(Integer.parseInt(zeile.substring(0, 5)) +i));
		    ortsliste.add(zeile.substring(zeile.indexOf(" ") +1));
		    System.out.println("plzliste: " + plzliste.get(plzliste.size() -1));
		    System.out.println("ortsliste: " + ortsliste.get(ortsliste.size() -1));
		}
	    }
	}
	in.close();
    }

}
