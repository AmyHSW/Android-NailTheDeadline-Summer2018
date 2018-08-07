package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

public class Task {
    private final String taskId;
    private String taskName;
    private int dueYear;
    private int dueMonth;
    private int dueDay;
    private int dueHour;
    private int dueMinute;
    private int workedOnInMinute;

    private Task(String taskId, String taskName, int dueYear, int dueMonth, int dueDay,
                 int dueHour, int dueMinute, int workedOnInMinute) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.dueYear = dueYear;
        this.dueMonth = dueMonth;
        this.dueDay = dueDay;
        this.dueHour = dueHour;
        this.dueMinute = dueMinute;
        this.workedOnInMinute = workedOnInMinute;
    }

    private Task(String taskName, int dueYear, int dueMonth, int dueDay,
                 int dueHour, int dueMinute) {
        this(UUID.randomUUID().toString(), taskName, dueYear, dueMonth, dueDay,
                dueHour, dueMinute, 0);
    }

    public static Task create(String taskName, int dueYear, int dueMonth, int dueDay,
                              int dueHour, int dueMinute) {
        return new Task(taskName, dueYear, dueMonth, dueDay, dueHour, dueMinute);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(taskId).append(',');
        sb.append(taskName).append(',');
        sb.append(dueYear).append(',').append(dueMonth).append(',').append(dueDay).append(',');
        sb.append(dueHour).append(',').append(dueMinute).append(',');
        sb.append(workedOnInMinute);
        return sb.toString();
    }

    public static Task fromString(String str) {
        String[] tokens = str.split(",");
        return new Task(tokens[0], tokens[1],
                Integer.parseInt(tokens[2]),
                Integer.parseInt(tokens[3]),
                Integer.parseInt(tokens[4]),
                Integer.parseInt(tokens[5]),
                Integer.parseInt(tokens[6]),
                Integer.parseInt(tokens[7]));
    }

    public String getTaskId() {
        return taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public Date getDueDate() {
        return new Date(dueYear - 1900, dueMonth, dueDay);
    }

    public Time getDueTime() {
        return new Time(dueHour, dueMinute, 0);
    }

    public int getWorkedOnInMinute() {
        return workedOnInMinute;
    }
}
