FROM eclipse-temurin:17

RUN apt update
RUN apt-get install iproute2 -y

# import identify's root CA cert into Java's cacerts
COPY docker/ca.crt /root/.
RUN keytool -importcert -file /root/ca.crt -alias Identify-CA -cacerts -storepass changeit -noprompt

RUN mkdir /tmp/identify
RUN mkdir /identify
RUN mkdir /identify/config
RUN mkdir /identify/keys
RUN mkdir /identify/ssl
RUN mkdir /identify/ssl/client
RUN mkdir /identify/ssl/server

COPY target/IdentifyAPI-0.0.1-SNAPSHOT.war /identify/.

COPY src/main/resources/application.properties /identify/config/.

COPY docker/runfile.sh /identify/.

RUN chmod +x /identify/runfile.sh

ENTRYPOINT ["/identify/runfile.sh"]