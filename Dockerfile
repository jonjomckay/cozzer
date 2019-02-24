FROM maven:3-jdk-11-slim AS build

# Install Node.js, Yarn and their dependencies so we can build the frontend
RUN apt-get update && \
    apt-get install -y gnupg2 && \
    curl -sL https://deb.nodesource.com/setup_11.x | bash - && \
    curl -sS https://dl.yarnpkg.com/debian/pubkey.gpg | apt-key add - && \
    echo "deb https://dl.yarnpkg.com/debian/ stable main" | tee /etc/apt/sources.list.d/yarn.list && \
    apt-get update && \
    apt-get install -y nodejs yarn

WORKDIR /usr/src/app
ADD . /usr/src/app
RUN mvn clean package -DskipTests

# Copy the fat JAR from the build container into the run container
FROM openjdk:11-jre-slim
COPY --from=build /usr/src/app/target/cozzer.jar /usr/src/app/target/cozzer.jar
ENTRYPOINT ["java", "-jar", "/usr/src/app/target/cozzer.jar", "prod"]
