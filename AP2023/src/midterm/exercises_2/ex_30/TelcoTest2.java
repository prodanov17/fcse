//package midterm.exercises_2.ex_30;

import java.util.*;

interface State{
    void answer(long timestamp);
    void end(long timestamp);
    void resume(long timestamp);
    void hold(long timestamp);
}

abstract class CallState implements State{
    Call call;

    public CallState(Call call) {
        this.call = call;
    }
}

class CallStartedState extends CallState{
    public CallStartedState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        call.callStarted = timestamp;
        call.state = new InProgressCallState(call);
    }

    @Override
    public void end(long timestamp) {
        call.callStarted = timestamp;
        call.callEnded = timestamp;
        call.state = new EndedCallState(call);
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }
}
class InProgressCallState extends CallState{
    public InProgressCallState(Call call) {
        super(call);
    }


    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.callEnded = timestamp;
        call.state = new EndedCallState(call);
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        call.holdStarted = timestamp;
        call.state = new PausedCallState(call);
    }
}
class PausedCallState extends CallState{
    public PausedCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        call.totalTimeInHold += (timestamp - call.holdStarted);
        call.callEnded = timestamp;
        call.state = new EndedCallState(call);
    }

    @Override
    public void resume(long timestamp) {
        call.totalTimeInHold += (timestamp - call.holdStarted);
        call.state = new InProgressCallState(call);
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }
}

class EndedCallState extends CallState{
    public EndedCallState(Call call) {
        super(call);
    }

    @Override
    public void answer(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void end(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void resume(long timestamp) {
        throw new RuntimeException();
    }

    @Override
    public void hold(long timestamp) {
        throw new RuntimeException();
    }
}

class Call{
    String uuid;
    String dialer;
    String receiver;


    long timestamp;
    CallState state = new CallStartedState(this);
    long callStarted;
    long callEnded;
    long totalTimeInHold = 0;
    long holdStarted = -1;

    public String getUuid() {
        return uuid;
    }
    public long getTimestamp() {
        return callEnded - callStarted - totalTimeInHold;
    }

    public Call(String uuid, String dialer, String receiver, long timestamp) {
        this.uuid = uuid;
        this.dialer = dialer;
        this.receiver = receiver;
        this.timestamp = timestamp;
    }

    private char determineReceiverDialer(String phoneNumber){
        return phoneNumber.equals(dialer) ? 'D' : 'R';
    }

    public void answer(long timestamp){
        state.answer(timestamp);
    }
    public void end(long timestamp){
        state.end(timestamp);
    }
    public void resume(long timestamp){
        state.resume(timestamp);
    }
    public void hold(long timestamp){
        state.hold(timestamp);
    }

    public void printByNumber(String phoneNumber) {
        String duration = DurationConverter.convert(callEnded - callStarted-totalTimeInHold);
        System.out.printf("%c %s %d %d %s%s\n",
                determineReceiverDialer(phoneNumber),
                determineReceiverDialer(phoneNumber) == 'R' ? dialer : receiver,
                callStarted,
                callEnded,
                (callStarted == callEnded) ? "MISSED CALL " : "",
                duration
                );
    }

    public void printCall(){
        System.out.printf("%s <-> %s : %s\n",
                dialer,
                receiver,
                DurationConverter.convert(callEnded-callStarted-totalTimeInHold)
                );
    }
}

class TelcoApp{
    Map<String, List<Call>> callsByNumber = new HashMap<>();
    Map<String, Call> callsByUuid = new HashMap<>();

    public TelcoApp() {}

    void addCall(String uuid, String dialer, String receiver, long timestamp){
        Call call = new Call(uuid, dialer, receiver, timestamp);

        callsByNumber.putIfAbsent(dialer, new ArrayList<>());
        callsByNumber.putIfAbsent(receiver, new ArrayList<>());

        callsByNumber.get(dialer).add(call);
        callsByNumber.get(receiver).add(call);

        callsByUuid.put(uuid, call);
    }

    void updateCall(String uuid, long timestamp, String action){
        if(action.equals("ANSWER")){
            callsByUuid.get(uuid).answer(timestamp);
        }
        else if(action.equals("END")){
            callsByUuid.get(uuid).end(timestamp);
        }
        else if(action.equals("HOLD")){
            callsByUuid.get(uuid).hold(timestamp);
        }
        else{
            callsByUuid.get(uuid).resume(timestamp);
        }
    }

    void printChronologicalReport(String phoneNumber){
        List<Call> calls = callsByNumber.get(phoneNumber);

        calls.forEach(e->e.printByNumber(phoneNumber));
    }

    void printReportByDuration(String phoneNumber){
        List<Call> calls = callsByNumber.get(phoneNumber);

        calls.stream().sorted(Comparator.comparingLong(Call::getTimestamp).reversed()).forEach(e->e.printByNumber(phoneNumber));
    }

    void printCallsDuration(){
        callsByUuid
                .values()
                .stream()
                .sorted(Comparator.comparingLong(Call::getTimestamp).reversed())
                .forEach(Call::printCall);
    }

}

class DurationConverter {
    public static String convert(long duration) {
        long minutes = duration / 60;
        duration %= 60;
        return String.format("%02d:%02d", minutes, duration);
    }
}


public class TelcoTest2 {
    public static void main(String[] args) {
        TelcoApp app = new TelcoApp();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            String command = parts[0];

            if (command.equals("addCall")) {
                String uuid = parts[1];
                String dialer = parts[2];
                String receiver = parts[3];
                long timestamp = Long.parseLong(parts[4]);
                app.addCall(uuid, dialer, receiver, timestamp);
            } else if (command.equals("updateCall")) {
                String uuid = parts[1];
                long timestamp = Long.parseLong(parts[2]);
                String action = parts[3];
                app.updateCall(uuid, timestamp, action);
            } else if (command.equals("printChronologicalReport")) {
                String phoneNumber = parts[1];
                app.printChronologicalReport(phoneNumber);
            } else if (command.equals("printReportByDuration")) {
                String phoneNumber = parts[1];
                app.printReportByDuration(phoneNumber);
            } else {
                app.printCallsDuration();
            }
        }

    }
}
