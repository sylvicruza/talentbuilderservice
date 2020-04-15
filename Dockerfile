FROM maven:3.5.2-jdk-8-alpine 

# Create app directory
RUN mkdir -p /usr/src/app

WORKDIR /usr/src/app

COPY . /usr/src/app

RUN mvn package

EXPOSE 8180

ENTRYPOINT ["java","-jar","target/talentbuilder-1.jar"]