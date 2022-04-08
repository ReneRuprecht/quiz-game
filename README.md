# paf-quiz

Eine Gruppenarbeit aus dem Studium

# Zuständigkeit
- JavaFX-Client: Leonard 
- Spring-Boot-Server: René Ruprecht
- Android-Flutter-App (extra): René Ruprecht

# WICHTIG vor dem clonen der Repository
In Git muss die automatische Konvertierung ausgestellt werden, andererseits wird das Script von Docker nicht ausgeführt und gibt einen Fehler für den Server Container aus.

        git config --global core.autocrlf false

Es muss darauf geachtet werden, dass das init.sh script im server/docker Verzeichnis den Zeilenumbruch auf <b>LF statt CRLF</b> hat.


# Was beinhaltet die Repository

In der Repository befindet sich: 
- Desktop Client 
- Server
- Datenbank (Wird per Docker erstellt)
- App.

Der Client wurde mit Java und dem Framework JavaFx entwickelt. Für die Kommunikation über den WebSocket wurde im Client der Stompclient implementiert mithilfe von Java's Framework Spring-Boot Bibliotheken.

Der Server wurde mit Java und dem Framework Spring-Boot entwickelt. Hierbei wurden ähnlich wie beim Client auf die Spring-Boot eigenen Bibliotheken zurückgegriffen.

Als Datenbank wurde eine MySQL Datenbank verwendet in der Version: 5.7.16.

Die App wurde mit Flutter entwickelt und ist derzeit auf einem Pixel 4 mit Android 10 angepasst.



# Hinweise
- Die Android App benötigt eine Server-IP.
Da die App auf dem PC in einem Emulator läuft und eine
eigene IP erhält, ist das Verbinden auf localhost, wie es der 
Client, der Server und die Datenbank machen, nicht möglich. Deshalb ist das Testen ohne
vorher abgeänderte IP <b><u>nicht möglich</u></b>.

-Spielstart:
Für den Start des Spiels müssen dem jeweiligen Spiel zwei Spieler beitreten, damit man den Start-Button drücken kann.
Der erste Spieler tritt bei der Erstellung des Spiels bei und der zweite über das Spielbeitrittsfenster.
In der aktuellen Version unseres Spiels muss man nach Drücken des Start-Buttons einen kurzen Moment von zehn Sekunden warten, damit der Client eine Verbindung zum Server aufgebaut hat.

# App IP
Die App IP-Adresse muss in der Datei constants geändert werden 

        lib/constants.dart

Hier muss die IP vom PC eingetragen werden auf dem die Container ausgeführt werden.

# Dummy Daten

Auf dem Server werden zwei User angelegt.

- username1 mit dem passwort: 1234
- username2 mit dem passwort: 1234

# Ausführen des Servers
Abfolge:
- In den Server Ordner wechseln.
        
        cd server
- Im Terminal den Befehl ausführen:

         docker-compose --env ./docker/.env build --no-cache
- Im Terminal den Befehl ausführen: 

        docker-compose --env ./docker/.env up

Der Server sowie die Datenbank starten nun als Container. Der Server wartet bis
die Datenbank sich initialisiert hat und startet dann die Spring-Boot-Applikation.

## Wie ist der Server erreichbar
- Spring-Boot-Server ist auf localhost:3000 erreichbar.
- Datenbank ist auf localhost:3306 erreichbar.
