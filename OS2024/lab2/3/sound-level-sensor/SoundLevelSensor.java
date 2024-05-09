import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class SoundLevelSensor {
    public static void main(String[] args) {
        File file = new File("/data/soundlevel.txt"); 
        while (true) {
            try {
                FileWriter fileWriter = new FileWriter(file, true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                for (int i = 0; i < 10; i++) {
                    int soundLevel = (int) (Math.random() * 61) + 40;
                    System.out.println("Sound level: " + soundLevel);
                    bufferedWriter.write(String.valueOf(soundLevel)); 
                    bufferedWriter.newLine();
                }
                bufferedWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(20000); 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

