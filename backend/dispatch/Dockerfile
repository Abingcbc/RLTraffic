FROM java:8
VOLUME /tmp
COPY target/dispatch-0.0.1-SNAPSHOT.jar dispatch.jar
RUN bash -c "touch /dispatch.jar"
EXPOSE 6627
ENTRYPOINT ["java","-jar","dispatch.jar"]