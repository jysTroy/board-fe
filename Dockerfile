ARG JAR_PATH=build/libs/.jar
ARG PORT=3000
COPY ${JAR_PATH} app.jar
RUN mkdir uploads

ENV DB_URL=**
ENV DB_PASSWORD=**
ENV DB_USERNAME=**
ENV SPRING_PROFILES_ACTIVE=default,prod

ENTRYPOINT ["java", "-Ddb.password=${DB_PASSWORD}","-Ddb.url=${DB_URL}", "-Ddb.username=${DB_USERNAME}", "-Dfile.path=/uploads", "-Dfile.url=/uploads", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-jar", "app.jar"]

EXPOSE ${PORT}