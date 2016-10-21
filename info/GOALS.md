GOALS
=====

Here is an overview of the goals to be achieved.
[This link](https://olat.server.uni-frankfurt.de/olat/auth/RepositoryEntry/3936681984/CourseNode/94313253446608?2) has all the information.

Milestone 1
===========

Abgabe und Bewertung
--------------------

* Die Abgabefrist für diesen Milestone ist am 9. November um 23:59. Gültig ist der Stand der Versionsverwaltung zu diesem Zeitpunkt.

Grundvoraussetzungen für die Bewertung:
---------------------------------------

* Alle notwendigen Dokumente im Subversion Repository. 
* Die Ergebnisdatei sollte zip oder gz-komprimiert sein(!)
* Autorenschaft(en) der Klassen mit JavaDoc dokumentieren
* Erstellung eines jar-Archivs Ihres Projekts um die Ausführung für Dritte zu erleichtern. 
* Das Projekt sollte über den Aufruf (Beispiel) java -Xmx2g -jar WikiXtractor.jar input.txt output.txt gestartet werden können.
* Erstellung einer Datei "Milestone1_README.txt" im Wurzelverzeichnis Ihres SVN-Repositories in welcher Sie Ihre Abgabe für den Tutor kommentieren. 

Die Beschreibung sollte zumindest enthalten:
+ wie die Berechnung ausgeführt werden kann (Beispielaufruf auf der Konsole)
+ wo die Ergebnisdatei liegt
+ wer woran gearbeitet hat (vor allem in den späteren Milestones von Bedeutung)
+ welche bekannten Probleme ggf. noch bestehen


Die Bewertung der Abgabe ist wie folgt:
---------------------------------------

* Implementierung...
+ (15 Punkte) ... der Klasse "Page" mit allen benötigten Attributen und Methoden
+ (15 Punkte) ... der Klasse "PageFactory" zum Einlesen und Erzeugen der Page-Objekte
+ (15 Punkte) ... der Klasse "LinkExtraction" zum extrahieren der Kategorien-Links unter Verwendung von JSoup
+ (15 Punkte) ... der Klasse "PageExport" zum Export der Daten in eine XML-Datei
+ (10 Punkte) Kommentierung aller Klassen, Methoden und Attribute mit JavaDoc (kurze Beschreibung, Autor(en), Parameter, Rückgabewert)
+ (10 Punkte) Logging von Exceptions und dem Programmfortschritt über log4j auf die Konsole sowie in eine Log-Datei.
+ (20 Punkte) Durchführung der Berechnung anhand der bereitgestellten Eingabedatei. Die Ausgabedatei soll komprimiert (zip) in Subversion eingecheckt sein.

Für die Gewichtung des Milestones in der Gesamtbewertung werden 3 Wochen angesetzt. 
Die Aufgabenstellung ist für alle Gruppenkonstellationen gleich, d.h. es sind hier keine Aufgabenteile optional.