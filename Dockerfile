FROM java:8

COPY target/ai-face-0.0.1-SNAPSHOT.jar /app.jar
COPY sdk /sdk
ENV TimeZone Asia/Shanghai

RUN bash -c  'sh /sdk/deployLibraries.sh'

EXPOSE 9002
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -jar /app.jar ${EXT_CONFIG}"]

