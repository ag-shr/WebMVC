FROM openjdk:11
ADD target/moviebooking-webapp.jar moviebooking-webapp.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "moviebooking-webapp.jar"] 