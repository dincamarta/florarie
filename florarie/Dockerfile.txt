FROM eclipse-temurin:21-jdk
WORKDIR /app

COPY pom.xml .
COPY .mvn .mvn
COPY mvnw mvnw

RUN chmod +x mvnw && sed -i 's/\r$//' mvnw
RUN ./mvnw -q -DskipTests dependency:go-offline

COPY src src
RUN ./mvnw -q -DskipTests package

EXPOSE 8080
CMD ["java","-jar","target/florarie-0.0.1-SNAPSHOT.jar"]
