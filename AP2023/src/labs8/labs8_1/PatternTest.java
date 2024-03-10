package labs8.labs8_1;

import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

class Song{
    String title;
    String artist;

    public Song(String title, String author){
        this.title = title;
        this.artist = author;
    }

    @Override
    public String toString() {
        return "Song{" +
                "title=" + title +
                ", artist=" + artist + "}";
    }
}

interface State{
    void pressPlay();
    void pressStop();
}

abstract class PlayerState implements State{
    MP3Player player;

    public PlayerState(MP3Player player) {
        this.player = player;
    }
}

class PlayingState extends PlayerState{
    public PlayingState(MP3Player player) {
        super(player);
    }

    @Override
    public void pressPlay() {
        //ERROR STATE
        System.out.println("Song is already playing");
    }

    @Override
    public void pressStop() {
        System.out.printf("Song %d is paused\n", player.currentlyPlaying);
        player.state = new StoppedState(player);
    }
}
class StoppedState extends PlayerState {
    int repeatCounter = 0;
    public StoppedState(MP3Player player) {
        super(player);
    }

    @Override
    public void pressPlay() {
        System.out.printf("Song %d is playing\n", player.currentlyPlaying);
        repeatCounter = 0;
        player.state = new PlayingState(player);
    }

    @Override
    public void pressStop() {
        if(repeatCounter > 0){
            System.out.println("Songs are already stopped");
            return;
        }
        System.out.println("Songs are stopped");
        player.currentlyPlaying = 0;
        repeatCounter++;
    }
}
class MP3Player{
    List<Song> songs;
    int currentlyPlaying;
    PlayerState state = new StoppedState(this);

    MP3Player(List<Song> list){
        songs = list;
        currentlyPlaying = 0;
    }

    void pressPlay(){
        state.pressPlay();
    }
    void pressStop(){
        state.pressStop();
    }
    void pressFWD(){
        System.out.println("Forward...");
        state = new StoppedState(this);
        currentlyPlaying = (currentlyPlaying+1)%(songs.size());
    }
    void pressREW(){
        System.out.println("Reward...");
        if(songs.size() == 1) return;
        state = new StoppedState(this);
        currentlyPlaying--;
        if(currentlyPlaying < 0) currentlyPlaying = songs.size()-1;
    }

    void printCurrentSong(){
        System.out.println(songs.get(currentlyPlaying));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("MP3Player{currentSong = ")
                .append(currentlyPlaying)
                .append(", songList = [");

        StringJoiner joiner = new StringJoiner(", ");

        songs.forEach(e -> joiner.add(e.toString()));

        sb.append(joiner.toString())
                .append("]}");

        return sb.toString();
    }

}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde