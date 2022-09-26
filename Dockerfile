FROM eclipse-temurin:17-jdk-jammy
EXPOSE 8080
COPY build/libs/\*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]