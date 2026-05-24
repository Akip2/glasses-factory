# La Fabrique de Lunettes

## Prérequis
Java 21
Mosquitto installé et lancé

### Lancer le backend
```bash
java -jar backend.jar
```

Le backend se connecte automatiquement au broker MQTT sur `tcp://localhost:1883`.

### Lancer le frontend
```bash
java -jar frontend.jar
```

## Configuration
La configuration est embarquée dans le jar. Par défaut :
```
mqtt.url=tcp://localhost:1883
mqtt.clientId=backend
capacity=5
```
`capacity` correspond au nombre de lunettes fabriquées simultanément.
Format des messages
Les messages échangés sur le bus MQTT utilisent un format custom :
Commande : `TYPE:QUANTITE;TYPE:QUANTITE`
```
CLAUDE:3;BANANA:2
```
Livraison : `TYPE:SERIAL;TYPE:SERIAL`
```
CLAUDE:CL-AQR65E-C8E11277;BANANA:BA-XY123Z-ABCD1234
```
Types de lunettes disponibles
`CLAUDE`
`CHATGPT`
`BANANA`
`LE_CHAT`
