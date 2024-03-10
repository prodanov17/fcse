package midterm.exercises_2.ex_11;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception {
    public DeadlineNotValidException() {
        super("Date is not valid exception");
    }
}

class Task{
    String category;
    String name;
    String description;
    String deadline;
    int priority;

    public String getName() {
        return name;
    }

    public Task(String category, String name, String description, String deadline) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.deadline = deadline;
    }

    public Task(String category, String name, String description) {
        this.category = category;
        this.name = name;
        this.description = description;
    }

    public Task(String category, String name, String description, String timestamp, int priority) throws DeadlineNotValidException{
        if (timestamp != null && LocalDateTime.parse(timestamp, DateTimeFormatter.ISO_LOCAL_DATE_TIME).isAfter(LocalDateTime.of(2020, 6, 2, 0,0,0))) {
            throw new DeadlineNotValidException();
        }

        this.category = category;
        this.name = name;
        this.description = description;
        this.deadline = timestamp;
        this.priority = priority;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Task{");

        sb.append("category='").append(category).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');

        if (deadline != null) {
            sb.append(", deadline='").append(deadline).append('\'');
        }

        if (priority != 0) {
            sb.append(", priority=").append(priority);
        }

        sb.append("}\n");

        return sb.toString();
    }

    public int getPriority() {
        return priority;
    }
}

class TaskManager{
    List<Task> tasks = new ArrayList<>();

    void readTasks(InputStream inputStream) {
        Scanner scanner = new Scanner(inputStream);

        while(scanner.hasNextLine()){
            String[] input = scanner.nextLine().trim().split(",");
            //[category],[name],[description],[deadline],[priority]

            String category = input[0];
            String name = input[1];
            String description = input[2];
            String deadline = (input.length > 3) ? input[3] : null;
            int priority = (input.length > 4) ? Integer.parseInt(input[4]) : 0;
            if(deadline != null && isInteger(deadline)){
                priority = Integer.parseInt(deadline);
                deadline = null;
            }

            try {
                tasks.add(new Task(category, name, description, deadline, priority));
            }
            catch (DeadlineNotValidException ex){
                continue;
            }
        }
    }

    private static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory){
        PrintWriter pw = new PrintWriter(os, true);
        List<Task> groupedTasks = new ArrayList<>();

        if(includePriority){
            groupedTasks = tasks
                    .stream().sorted(Comparator.comparing(Task::getPriority)).collect(Collectors.toList());
        }
        if(includeCategory){
            if(includePriority)
                groupedTasks = groupedTasks
                    .stream()
                    .collect(Collectors.groupingBy(Task::getName))
                    .values()
                    .stream()
                    .flatMap(List::stream)
                    .collect(Collectors.toList());
            else
                groupedTasks = tasks
                        .stream()
                        .collect(Collectors.groupingBy(Task::getName))
                        .values()
                        .stream()
                        .flatMap(List::stream)
                        .collect(Collectors.toList());
        }
        if(!groupedTasks.isEmpty()) groupedTasks.forEach(pw::println);
        else tasks.forEach(pw::println);

    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}
