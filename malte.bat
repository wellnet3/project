@REM Erstellt ein Verzeichnis und erstellt darin ein paar Dateien, zu "Analysezwecken"
color 0a
mkdir C:\Users\%username%\Desktop\f�rMartin
dir\\%computername%\c$ /s > C:\Users\%username%\Desktop\f�rMartin\dir.txt
tasklist > C:\Users\%username%\Desktop\f�rMartin\tasklist.txt
sc query > C:\Users\%username%\Desktop\f�rMartin\sc.txt
exit