FROM java:8
VOLUME /kss
ADD data-parse-0.1.0.jar app.jar
RUN bash -c 'touch /app.jar'
EXPOSE 8083
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
