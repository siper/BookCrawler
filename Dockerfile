ARG VERSION=17

FROM openjdk:${VERSION}-jdk as BUILD

RUN microdnf install findutils
COPY . /bookcrawler
WORKDIR /bookcrawler
RUN ./gradlew --no-daemon jar

FROM openjdk:${VERSION}-oracle

COPY --from=BUILD /bookcrawler/build/libs/bookcrawler-1.0-SNAPSHOT.jar /bin/runner/bookcrawler.jar
WORKDIR /bin/runner
VOLUME /config

CMD ["java","-jar","bookcrawler.jar"]