# ChatBot_Telegram

Ich habe diese Anwendung erstellt, um meine Kompetenz in Spring und TelegramBot zu demonstrieren.
Gibt den aktuellen Wechselkurs von USD/EUR zu UAN von der ukrainischen Zentralbank an.

## Overview
Diese Anwendung erstellt einen Telegram-Bot, der vier Befehle unterstützt: /start, /usd für den Dollarkurs, /eur für den Eurokurs und /help für Hilfe. Bei jeder Anfrage wird eine GET-Anfrage an die entsprechende Quelle gesendet, um die aktuellen Wechselkurse in XML-Format abzurufen. Diese XML-Antwort wird dann geparst und die Daten werden dem Benutzer zurückgegeben. Darüber hinaus wurde Caching implementiert, um nicht bei jeder Anfrage erneut auf das XML zugreifen zu müssen.

## Technologies Used
- Spring Boot
- Gradle
- OkHttp
- XML Parsing
- Scheduling
- Caching
- Logging


