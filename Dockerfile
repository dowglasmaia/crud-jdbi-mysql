# ---------- Build stage ----------
FROM maven:3-amazoncorretto-21-alpine AS build

WORKDIR /maia-jdbi-crud

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean install -DskipTests -Dsonar.skip=true

# ---------- Runtime stage ----------
FROM amazoncorretto:21-alpine-jdk

LABEL maintainer="Dowglas Maia"

ENV SPRING_LOGGING_LEVEL=INFO
ENV PORT=8080
ENV JAVA_TOOL_OPTIONS="-javaagent:/app/dd-java-agent.jar"

WORKDIR /app

# Timezone
RUN apk --no-cache add tzdata curl \
    && cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime \
    && echo "America/Sao_Paulo" > /etc/timezone \
    && apk del tzdata

# Baixa e adiciona o agente Datadog
ADD https://dtdg.co/latest-java-tracer dd-java-agent.jar

# Copia o .jar
COPY --from=build /maia-jdbi-crud/target/*.jar ./app.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", \
    "-noverify", \
    "-Dfile.encoding=UTF-8", \
    "-Dlogging.level.root=${SPRING_LOGGING_LEVEL}", \
    "-jar", "app.jar", \
    "--server.port=${PORT}"]