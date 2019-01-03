package db;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

public class UpdateManager {
    
    private static final String urlCheck = "https://raw.githubusercontent.com/wellnet3/project/master/about.txt";
    private static final String urlJar = "https://github.com/wellnet3/project/raw/master/buchhaltung_test.jar";
    private static final String urlJar2 = "https://raw.githubusercontent.com/wellnet3/project/master/buchhaltung_test.jar";
    private static final String urlJar3 = "https://github.com/wellnet3/project/archive/master.zip";
    private static final String programmname = "buchhaltung_test.jar";
    private static String gitversion;
    private static String link;
    
    public static void update(String cversion) throws IOException, URISyntaxException, InterruptedException {
	URL url = new URL(urlCheck);
	BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	gitversion = in.readLine();
	System.out.println(gitversion);
	link = in.readLine();
	System.out.println(link);
	in.close();
	
	double version = Double.parseDouble(gitversion.substring(9));
	System.out.println(version);
	if (version > Double.parseDouble(cversion)) {
	    String thisFile = UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	    //Runtime.getRuntime().exec("cmd.exe /c start " + thisFile + "UpdateManager.jar");
	    //JOptionPane.showMessageDialog(null, "auszuführender Path: " + thisFile + "UpdateManager.jar");
	    //JOptionPane.showMessageDialog(null, "Ein Update ist verfügbar, es kann unter " + "\n " + link + "\n" +  " heruntergeladen werden (einfach die alte Datei ersetzen)");
	    if (JOptionPane.showInputDialog(null, "Ein Update ist verfügbar, es kann unter " + "\n " + "hier heruntergeladen werden (einfach die alte Datei ersetzen). Zum Öffnen einfach auf 'OK' klicken", link).equals(link)) {
		Desktop desktop = Desktop.getDesktop();
		desktop.browse(new URI(link));
	    }
	    //TimeUnit.SECONDS.sleep(3);
	    //removeOldVersion();
	    //TimeUnit.SECONDS.sleep(3);
	    //downloadNewVersion();
	    
	}
	
    }
    
    @Deprecated
    private static void downloadNewVersion() throws IOException, URISyntaxException {
	URL url = new URL(urlJar);
	FileOutputStream fos = new FileOutputStream(UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath() + programmname);
	BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
	ArrayList<Byte> bytes = new ArrayList<Byte>();
	int b;
	while ((b = in.read()) != -1) {
	    bytes.add((byte) b);
	}
	byte[] bs = new byte[bytes.size()];
	for (int i = 0; i < bs.length; i++) {
	    bs[i] = bytes.get(i);
	}
	fos.write(bs);
	fos.close();
    }
    
    @Deprecated
    private static void removeOldVersion() throws IOException, URISyntaxException {
	String OldFile = UpdateManager.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	String batchFile = OldFile + "remove.bat";
	OldFile = OldFile.concat(programmname);
	System.out.println("thisfile: " + OldFile);
	System.out.println("batchfile: " + batchFile);
	Runtime.getRuntime().exec("cmd.exe /c start " + batchFile + " " + OldFile);
    }
    
    public static void main(String[] args) {
	try {
	    //update(null);
	    //downloadNewVersion();
	    update("0.0");
	} catch (IOException ioex) {
	    ioex.printStackTrace();
	    ioex.getMessage();
	} catch (URISyntaxException urisynex) {
	    urisynex.printStackTrace();
	    urisynex.getMessage();
	} catch (InterruptedException iex) {
	    
	}
    }

}
