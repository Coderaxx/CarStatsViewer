AAOS Car Stats Viewer
===========================================
Reads data from the VHAL of AAOS to provide more detailed car stats than the regular trip computer.

currently being tested on Polestar 2

Based on https://github.com/android/car-samples/tree/main/car-lib/CarGearViewerKotlin

## Changelog [DE]:

### 0.15 (08.01.2023)
- Ladekurve hinzugefügt.
- Experimentelles Layout erweitert
- Allgemeine Stabilitätsverbesserungen

### 0.14 (07.01.2023)
- Experimentelle Messwertansicht hinzugefügt

### 0.13 (06.01.2023)
- Achsenbeschriftung im Verbrauchsdiagramm mit sinnvollerer Einteilung
- Diagrammaktualisierung stabilisiert
- Service für die Fahrdatenerfassung ist jetzt (hoffentlich) stabiler und es sollten keine Werte mehr einfrieren
- Diagramme werden bei größeren Distanzen geglättet

### 0.12 (05.01.2023)
- Diagramm für die Geschwindigkeit hinzugefügt.
- Leichte Anpassungen am Logging.

### 0.11 (04.01.2023)
- Debug-Log hinzugefügt

### 0.10 (04.01.2023)
- Verbrauchsdiagramm verbessert

### 0.9 (02.01.2023)
- Experimentelle Verbrauchsanzeige hinzugefügt
- Layout für bessere Leserlichkeit angepasst

### 0.8 (31.12.2022)
- (Hoffentlich) Fix für einfrierende Werte bei Aktivierung der Kamera

### 0.7 (30.12.2022)
- Einstellungsmenü hinzugefügt
- Datenerfassung in Service ausgelagert

### 0.6 (24.12.2022)
- Benachrichtigung mit den aktuellen Fahrdaten
- Kein Neustart der App mehr erforderlich, wenn die Berechtigungen erteilt wurden

### 0.5 (21.12.2022)
- Deutsche Lokalisierung und Styling-Anpassungen
