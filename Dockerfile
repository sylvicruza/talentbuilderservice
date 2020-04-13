FROM openjdk:8
ADD target/talentbuilder-1.jar talentbuilder-1.jar
EXPOSE 8085
ENTRYPOINT ["java","-jar","/talentbuilder-1.jar"]