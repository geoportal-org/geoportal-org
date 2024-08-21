FROM openjdk:21-jdk-slim

LABEL vendor="Eversis"
LABEL maintainer="Eversis"
LABEL project="GEOSS"
LABEL version="1.0"
LABEL image="geoss-settings"

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
ENV JDK_JAVA_OPTIONS=$JAVA_OPTS
ENV JAVA_TOOL_OPTIONS="-Djava.io.tmpdir=tomcat/temp"

WORKDIR /opt/app

ARG JAR_FILE=./application/build/libs/application-*-boot.jar
COPY ${JAR_FILE} ./application-boot.jar

RUN mkdir -p "tomcat/temp"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "application-boot.jar"]
CMD [""]
