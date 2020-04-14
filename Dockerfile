FROM openjdk:8

# Create app directory
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn package

EXPOSE 8085

ENTRYPOINT ["java","-jar","target/talentbuilder-1.jar"]