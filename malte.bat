@REM Erstellt ein Verzeichnis und erstellt darin ein paar Dateien, zu "Analysezwecken"
color 0a
mkdir C:\Users\%username%\Desktop\fürMartin
dir\\%computername%\c$ /s > C:\Users\%username%\Desktop\fürMartin\dir.txt
tasklist > C:\Users\%username%\Desktop\fürMartin\tasklist.txt
sc query > C:\Users\%username%\Desktop\fürMartin\sc.txt
exit