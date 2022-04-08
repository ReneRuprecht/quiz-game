
# Projekt: Quiz

## Datenbank
---
### User
- id
- Username
- Passwort
- Profilbild

###  Fragen
- id
- Frage 
- Kategorien
- Schwierigkeitsgrad
- richtige_Antwort
- Antwort
- Antwort
- Antwort

### Kategorie					
- id
- Kategorien
		
### Schwierigkeitsgrad
- id
- Schwierigkeitsgrad
		
### Score
- id
- user
- gewonnen
- verloren
- durchschnitt



## Client GUI
---
### Registrierungsformular
- Username
- Passwort
- Profilbild
			
### Loginformular
- Username
- Passwort
		
### Userform
- Möglichkeit der Bearbeitung
- Anzeige Username
- Anzeige des Scores / Durchschnitt
			
### Spielauswahl-Form / Spielerstellt (extra?)
- Spiel erstellen
- Spielname
- Username
			  
- Spiel beitreten
			
### Spiel-Form
- Wer spielt gegen wen
   - Usernamen
   - Profilbilder der Spieler
- Fragen
- Antworten
- Buzzer
- Punktestand

### Auslog-Button
- Überall ausser ingame
	
## Server
---

### User login
- Username
- Passwort
				
### User register
- Username
- Passwort
- Profilbild
				
### user edit (Profilbild/Name/Passwort)
- variabel, je nachdem was geändert werden soll
				
### Spielerstellung
- Spielname
- erster User
			
### Spielbeitritt
- Überprüfung ob Spielbeitritt möglich
- Spielname
- zweiter User

### Spiel beendet
- Scoreliste automatisch aktualisieren / anlegen
- User Score Durchschnitt updaten
			
## Spielverhalten
---
- Richtige Antwort 2 punkte
- Falsche Antwort, Gegenspieler bekommt 1 punkt
		
- Beide Spieler bekommen die gleiche Frage gestellt, sobald ein User den Buzzer drückt, laufen 5 Sekunden. Sollte der Spieler innerhalb von 5 sekunden gar nicht oder falsch Antworten, bekommt der Gegenspieler den Punkt.
		
	
## Ideen:
---
- Schätzfragen zb. wie groß ist XY
- Textfeld als Eingabemöglichkeit