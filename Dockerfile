FROM openjdk:11
ADD target/movie-booking-webapp.jar movie-booking-webapp.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "movie-booking-webapp.jar"]