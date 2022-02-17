FROM openjdk:12

ADD target/friends-managements-spring-boot.jar app.jar

ENTRYPOINT ["java","-jar","app.jar"]

EXPOSE 8080