FROM ubuntu:latest
LABEL authors="krzysztof"

# ETAP 1: budowanie aplikacji
FROM eclipse-temurin:21-jdk AS builder
WORKDIR /app

# kopiowanie plików konfiguracyjnych Gradle'a (tzw. wrapper – tj. elementy, które
# pozwalają na uruchomienie Gradle'a bez jego instalacji)
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
# gwiazdka sprawia, że Docker nie wyrzuci błędu, jeśli pliku nie będzie
COPY settings.gradle* .

# nadawanie uprawnień do wykonania dla wrappera
RUN chmod +x ./gradlew

# pobieranie zależności (to przyspieszy kolejne budowania dzięki cache Dockera)
RUN ./gradlew dependencies --no-daemon

# kopiowanie kodu źródłowego
COPY src src

# budowanie pliku JAR. Użyta flaga '-x test', pozwala pominąć testy podczas budowania
RUN ./gradlew bootJar -x test --no-daemon

# ETAP 2: obraz uruchomieniowy (Runtime)
FROM eclipse-temurin:21-jre
WORKDIR /app

# kopiowanie, zbudowanego w poprzednim etapie, pliku JAR
COPY --from=builder /app/build/libs/*.jar app.jar

# informacja dla Dockera, że aplikacja nasłuchuje na porcie 8080 (domyślny port)
EXPOSE 8080

# komenda uruchamiająca aplikację
ENTRYPOINT ["java", "-jar", "app.jar"]