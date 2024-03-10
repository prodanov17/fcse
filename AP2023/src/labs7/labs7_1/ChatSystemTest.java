package labs7.labs7_1;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

class NoSuchRoomException extends Exception{

    public NoSuchRoomException(String roomName) {
        super(roomName + " does not exist");
    }
}
class NoSuchUserException extends Exception{

    public NoSuchUserException(String username) {
        super(username + " does not exist");
    }
}


class ChatRoom{
    String roomName;
    Set<String> users;

    public ChatRoom(String name){
        this.roomName = name;
        this.users = new HashSet<>();
    }

    public void addUser(String username){
        users.add(username);
    }

    public void removeUser(String username){
        users.remove(username);
    }

    public boolean hasUser(String username){
        return users.contains(username);
    }

    public int numUsers(){
        return users.size();
    }

    public String getRoomName(){
        return this.roomName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(roomName).append("\n");
        if (users.isEmpty()) sb.append("EMPTY\n");
        users.stream().sorted().forEach(e->sb.append(e).append("\n"));

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(roomName, chatRoom.roomName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomName);
    }
}

class ChatSystem{
    Map<String, ChatRoom> roomList = new TreeMap<>();
    Set<String> registeredUsers = new HashSet<>();
    public ChatSystem(){}

    public void addRoom(String roomName){
        roomList.put(roomName, new ChatRoom(roomName));
    }

    public ChatRoom getRoom(String roomName){
        return roomList.get(roomName);
    }

    public void removeRoom(String roomName){
        roomList.remove(roomName);
    }

    public void register(String username){
        registeredUsers.add(username);

        roomList.values().stream().min(Comparator.comparingInt(ChatRoom::numUsers).thenComparing(ChatRoom::getRoomName)).ifPresent(e->e.addUser(username));
    }

    public void registerAndJoin(String userName, String roomName){
        registeredUsers.add(userName);
        roomList.get(roomName).addUser(userName);
    }

    public void joinRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException{
        if(!roomList.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        if(!registeredUsers.contains(username)) throw new NoSuchUserException(username);
        roomList.get(roomName).addUser(username);
    }

    public void leaveRoom(String username, String roomName) throws NoSuchRoomException, NoSuchUserException{
        if(!roomList.containsKey(roomName)) throw new NoSuchRoomException(roomName);
        if(!registeredUsers.contains(username)) throw new NoSuchUserException(username);
        roomList.get(roomName).removeUser(username);
    }

    public void followFriend(String username, String friend_username) throws NoSuchUserException {
        if(!registeredUsers.contains(username)) throw new NoSuchUserException(username);
        if(!registeredUsers.contains(friend_username)) throw new NoSuchUserException(friend_username);

        List<ChatRoom> arrayList = new ArrayList<>(roomList.values());

        arrayList.stream().filter(e->e.hasUser(friend_username)).forEach(e->e.addUser(username));
    }
}

public class ChatSystemTest {
    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchRoomException {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            ChatRoom cr = new ChatRoom(jin.next());
            int n = jin.nextInt();
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr.addUser(jin.next());
                if ( k == 1 ) cr.removeUser(jin.next());
                if ( k == 2 ) System.out.println(cr.hasUser(jin.next()));
            }
            if(cr.numUsers() != 5)
                System.out.println("");
            System.out.println(cr.toString());
            n = jin.nextInt();
            if ( n == 0 ) return;
            ChatRoom cr2 = new ChatRoom(jin.next());
            for ( int i = 0 ; i < n ; ++i ) {
                k = jin.nextInt();
                if ( k == 0 ) cr2.addUser(jin.next());
                if ( k == 1 ) cr2.removeUser(jin.next());
                if ( k == 2 ) cr2.hasUser(jin.next());
            }
            System.out.println(cr2.toString());
        }
        if ( k == 1 ) {
            ChatSystem cs = new ChatSystem();
            Method mts[] = cs.getClass().getMethods();
            while ( true ) {
                String cmd = jin.next();
                if ( cmd.equals("stop") ) break;
                if ( cmd.equals("print") ) {
                    System.out.println(cs.getRoom(jin.next())+"\n");continue;
                }
                for ( Method m : mts ) {
                    if ( m.getName().equals(cmd) ) {
                        String params[] = new String[m.getParameterTypes().length];
                        for ( int i = 0 ; i < params.length ; ++i ) params[i] = jin.next();
                        m.invoke(cs,(Object[])params);
                    }
                }
            }
        }
    }
}
