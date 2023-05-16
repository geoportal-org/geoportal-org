FROM openjdk:17.0.2-jdk-slim

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-contents"

ENV TZ=UTC
ENV LANG=en_GB.UTF-8 LANGUAGE=en_GB:en
ENV JAVA_OPTS="${JAVA_OPTS} \
-server \
-Dfile.encoding=UTF-8 \
-Dsun.jnu.encoding=UTF-8 \
-Duser.country=GB \
-Duser.language=en \
-Duser.timezone=GMT \
"
ENV JDK_JAVA_OPTIONS=""
ENV JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=tomcat/temp"

WORKDIR /opt/app

ARG JAR_FILE=./build/libs/geoss-contents-boot.jar
COPY ${JAR_FILE} ./application-boot.jar

RUN mkdir -p "tomcat/temp"

#KCI
COPY ./certs /opt/app/certs/
RUN keytool -importcert -file ./certs/server.crt -noprompt -alias server -storepass changeit -keystore $JAVA_HOME/lib/security/cacerts

EXPOSE 8081

ENTRYPOINT exec java $JAVA_OPTS -jar ./application-boot.jar
