FROM kimmarinet/ubuntu:1.0.0
ARG JAR_PATH=build/libs/chatboard-0.0.1-SNAPSHOT.jar
ARG PORT=3000
COPY ${JAR_PATH} app.jar
RUN mkdir uploads

ENV DB_URL=**
ENV DB_PASSWORD=**
ENV DB_USERNAME=**
ENV SPRING_PROFILES_ACTIVE=default,prod
ENV DDL=**
ENV KAKAO_APIKEY=**
ENV NAVER_APIKEY=**
ENV NAVER_SECRET=**
ENV MAIL_USERNAME=**
ENV MAIL_PASSWORD=**

ENTRYPOINT ["java", "-Ddb.password=${DB_PASSWORD}","-Ddb.url=${DB_URL}", "-Ddb.username=${DB_USERNAME}", "-Dfile.path=/uploads", "-Dfile.url=/uploads", "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE}", "-Dddl.auto=${DDL}", "-Dchatbot.url=http://localhost:8000", "-Dkakao.apikey=${KAKAO_APIKEY}", "-Dnaver.apikey=${NAVER_APIKEY}", "-Dnaver.secret=${NAVER_SECRET}", "-Dmail.username=${MAIL_USERNAME}", "-Dmail.password=${MAIL_PASSWORD}", "-Dredis.host=localhost", "-Dredis.port=6379" ,"-jar", "app.jar"]

EXPOSE ${PORT}