# p2p_udp

Das vorliegende Programm soll die Anforderungen der Aufgabe 2 der Programmierübung Kommunikationssysteme
WS17/18 (Peer-To-Peer Paradigma) erfüllen.

## Allgemein

Die Klasse 'Peer' verlangt zur Ausführung zwei Argumente:
* Einen Namen
* Eine Port Nummer (Sollte nicht bereits vergeben sein)

In dem darauf folgenden kann der Nutzter auswählen, ob und wie viele
Artist / Title Tupel er dem Peer als initialen Datensatz mitgeben will.      

Sind mehrere Peers im 'Netzwerk' (in diesem Fall einfach nur Localhost), so werden sie
selbstständig ihre Daten austauschen. Den aktuellen Status eines Peers (in Datentechnischer
hinsicht) kann sich der Nutzer jederzeit Ausgeben lassen.

