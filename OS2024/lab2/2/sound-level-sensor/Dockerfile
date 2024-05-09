FROM openjdk:11-jdk

WORKDIR /app

RUN mkdir /data

RUN touch /data/soundlevel.txt

COPY SoundLevelSensor.java /app

RUN javac SoundLevelSensor.java

CMD ["java", "SoundLevelSensor"]
