import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class SoundLevelMonitor {
    public static void main(String[] args) {
        while (true) {
            try {
                File soundLevelFile = new File("/data/soundlevel.txt");
                if (soundLevelFile.exists()) {
                    double sum = 0;
                    int count = 0;
                    BufferedReader reader = new BufferedReader(new FileReader(soundLevelFile));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        int soundLevel = Integer.parseInt(line);
                        sum += soundLevel;
                        count++;
                    }
                    reader.close();

                    double average = count > 0 ? sum / count : 0;

                    String noiseLevel;
                    if (average >= 40 && average < 60) {
                        noiseLevel = "Low";
                    } else if (average >= 60 && average < 80) {
                        noiseLevel = "Medium";
                    } else {
                        noiseLevel = "High";
                    }

                    File noisePollutionFile = new File("/data/noisepollution.txt");
                    BufferedWriter writer = new BufferedWriter(new FileWriter(noisePollutionFile, true));
                    System.out.println(noiseLevel);
                    writer.write(noiseLevel);
                    writer.newLine();
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

