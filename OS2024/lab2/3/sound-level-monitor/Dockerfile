FROM openjdk:11-jdk

WORKDIR /app

RUN mkdir /data

RUN touch /data/noisepollution.txt

COPY SoundLevelMonitor.java /app

RUN javac SoundLevelMonitor.java

CMD ["java", "SoundLevelMonitor"]

