FROM eclipse-temurin:25 AS jre-build
LABEL authors="Mika Herrmann"

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules java.base,java.net.http,java.logging \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

# Transfer JRE to small release image
FROM debian:buster-slim
ENV JAVA_HOME=/opt/java/openjdk
ENV PATH="${JAVA_HOME}/bin:${PATH}"
COPY --from=jre-build /javaruntime $JAVA_HOME

RUN mkdir /opt/H4A-CalDAV
COPY calDavProvider/build/libs/H4AllCalDav.jar /opt/H4A-CalDAV
CMD ["java", "-jar", "/opt/H4A-CalDAV/H4AllCalDav.jar"]