package labs7.labs7_3;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

class Scheduler<T>{
    Map<Date, T> schedule;

    public Scheduler(){
        schedule = new HashMap<>();
    }

    public void add(Date d, T t){
        schedule.put(d, t);
    }

    public boolean remove(Date d){
       if(!schedule.containsKey(d)) return false;

       schedule.remove(d);
       return true;
    }

    public T next(){
        Date now = new Date();
        Date key = schedule.keySet().stream().filter(date -> date.after(now)).sorted().findFirst().orElse(null);
        return (key != null) ? schedule.get(key) : null;
    }

    public T last(){
        Date now = new Date();
        Date key = schedule.keySet().stream()
                .filter(date -> date.before(now))
                .max(Comparator.naturalOrder())
                .orElse(null);

        return (key != null) ? schedule.get(key) : null;
    }

    public ArrayList<T> getAll(Date begin, Date end){
        return schedule
                .entrySet()
                .stream()
                .filter(e-> e.getKey().before(end) && e.getKey().after(begin))
                .map(HashMap.Entry::getValue)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public T getFirst(){
        Date key = schedule.keySet().stream().sorted().collect(Collectors.toList()).get(0);
        return schedule.get(key);
    }

    public T getLast() {
        Optional<Date> lastKey = schedule.keySet().stream()
                .max(Date::compareTo);

        return lastKey.map(schedule::get).orElse(null);
    }
}

public class SchedulerTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if ( k == 0 ) {
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime()-7200000), jin.next());
            scheduler.add(new Date(now.getTime()-3600000), jin.next());
            scheduler.add(new Date(now.getTime()-14400000), jin.next());
            scheduler.add(new Date(now.getTime()+7200000), jin.next());
            scheduler.add(new Date(now.getTime()+14400000), jin.next());
            scheduler.add(new Date(now.getTime()+3600000), jin.next());
            scheduler.add(new Date(now.getTime()+18000000), jin.next());
            System.out.println(scheduler.getFirst());
            System.out.println(scheduler.getLast());
        }
        if ( k == 3 ) { //test Scheduler with String
            Scheduler<String> scheduler = new Scheduler<String>();
            Date now = new Date();
            scheduler.add(new Date(now.getTime()-7200000), jin.next());
            scheduler.add(new Date(now.getTime()-3600000), jin.next());
            scheduler.add(new Date(now.getTime()-14400000), jin.next());
            scheduler.add(new Date(now.getTime()+7200000), jin.next());
            scheduler.add(new Date(now.getTime()+14400000), jin.next());
            scheduler.add(new Date(now.getTime()+3600000), jin.next());
            scheduler.add(new Date(now.getTime()+18000000), jin.next());
            System.out.println(scheduler.next());
            System.out.println(scheduler.last());
            ArrayList<String> res = scheduler.getAll(new Date(now.getTime()-10000000), new Date(now.getTime()+17000000));
            Collections.sort(res);
            for ( String t : res ) {
                System.out.print(t+" , ");
            }
        }
        if ( k == 4 ) {//test Scheduler with ints complex
            Scheduler<Integer> scheduler = new Scheduler<Integer>();
            int counter = 0;
            ArrayList<Date> to_remove = new ArrayList<Date>();

            while ( jin.hasNextLong() ) {
                Date d = new Date(jin.nextLong());
                int i = jin.nextInt();
                if ( (counter&7) == 0 ) {
                    to_remove.add(d);
                }
                scheduler.add(d,i);
                ++counter;
            }
            jin.next();

            while ( jin.hasNextLong() ) {
                Date l = new Date(jin.nextLong());
                Date h = new Date(jin.nextLong());
                ArrayList<Integer> res = scheduler.getAll(l,h);
                Collections.sort(res);

                String low = l.toString(), high = h.toString();
                low = low.replace("UTC", "GMT");
                high = high.replace("UTC", "GMT");


                System.out.println(low+" <: "+print(res)+" >: "+high);
            }
            System.out.println("test");
            ArrayList<Integer> res = scheduler.getAll(new Date(0),new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
            for ( Date d : to_remove ) {
                scheduler.remove(d);
            }
            res = scheduler.getAll(new Date(0),new Date(Long.MAX_VALUE));
            Collections.sort(res);
            System.out.println(print(res));
        }
    }

    private static <T> String print(ArrayList<T> res) {
        if ( res == null || res.size() == 0 ) return "NONE";
        StringBuffer sb = new StringBuffer();
        for ( T t : res ) {
            sb.append(t+" , ");
        }
        return sb.substring(0, sb.length()-3);
    }


}