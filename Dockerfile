# ---------- Build stage ----------
FROM maven:3-amazoncorretto-21-alpine AS build
WORKDIR /maia-jdbi-crud
COPY pom.xml .
RUN mvn dependency:resolve
COPY src ./src
RUN mvn clean install -DskipTests -Dsonar.skip=true

# ---------- Runtime stage ----------
FROM amazoncorretto:21-alpine-jdk
LABEL maintainer="Dowglas Maia"

ENV SPRING_LOGGING_LEVEL=INFO
ENV PORT=8080
WORKDIR /usr/src/app

# Timezone
RUN apk --no-cache add tzdata \
    && cp /usr/share/zoneinfo/America/Sao_Paulo /etc/localtime \
    && echo "America/Sao_Paulo" > /etc/timezone \
    && apk del tzdata

# Copia o .jar
COPY --from=build /maia-jdbi-crud/target/*.jar /usr/src/app/maia-jdbi-crud-api.jar

EXPOSE ${PORT}

ENTRYPOINT ["java", \
            "-noverify", \
            "-Dfile.encoding=UTF-8", \
            "-Dlogging.level.root=${SPRING_LOGGING_LEVEL}", \
            "-jar", "/usr/src/app/maia-jdbi-crud-api.jar", \
            "--server.port=${PORT}"]
