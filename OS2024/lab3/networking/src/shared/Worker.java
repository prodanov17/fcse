package shared;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Worker extends Thread{

    private Socket socket;
    private File logFile;
    private File clientsCountFile;

    private static Lock mutex = new ReentrantLock();

    public Worker(Socket socket, File logFile, File clientsCountFile) {
        this.socket = socket;
        this.logFile = logFile;
        this.clientsCountFile = clientsCountFile;
    }

    private void execute() throws IOException, InterruptedException {
        BufferedWriter logWriter = new BufferedWriter(new FileWriter(this.logFile, true));
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        RandomAccessFile clientCounterRaf = new RandomAccessFile(this.clientsCountFile, "rw");
        Integer currentCounterValue = incrementCounter(clientCounterRaf);

        System.out.println("SERVER: current counter value: " + currentCounterValue);

        String line;

        try{
            while((line = reader.readLine()) != null) {
                System.out.println("SERVER: " + line);
                logWriter.write(line);
                logWriter.newLine();
                logWriter.flush();
            }
        }
        catch(IOException e){
            e.printStackTrace();
        }
        finally {
            logWriter.close();
            reader.close();
            socket.close();
        }
    }

    @Override
    public void run() {
        try {
            execute();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Integer incrementCounter(RandomAccessFile clientCounterRaf) throws IOException {
        mutex.lock();
        Integer currentCounter = 0;

        try{
            currentCounter = clientCounterRaf.readInt();
        }
        catch(IOException e){
            clientCounterRaf.seek(0);
            clientCounterRaf.writeInt(0);
        }

        currentCounter++;

        clientCounterRaf.seek(0);
        clientCounterRaf.writeInt(currentCounter);
        mutex.unlock();

        return currentCounter;
    }
}
