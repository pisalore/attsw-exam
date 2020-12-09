
FROM openjdk:11

ARG jarFile

COPY /target/$jarFile-jar-with-dependencies.jar /app/app.jar

RUN apt-get update
RUN apt-get install -y libxrender1 libxtst6 libxi6
RUN adduser --system --uid 1000 --disabled-password applicationuser

USER applicationuser

CMD ["sh", "-c" , "java -jar /app/app.jar"]
