package tracker.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Task {
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    protected Integer uid;
    protected TaskType type;
    protected String name;
    protected Status status;
    protected String description;
    protected LocalDateTime startTime;
    protected long duration;

    public Task(String name, String description) {
        this.type = TaskType.TASK;
        this.name = name;
        this.status = Status.NEW;
        this.description = description;
        this.startTime = LocalDateTime.now();
        this.duration = 0l;
    }

    public Task(String name, String description, LocalDateTime startTime, long duration) {
        this.type = TaskType.TASK;
        this.name = name;
        this.status = Status.NEW;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(Integer uid, String name, Status status, String description) {
        this.uid = uid;
        this.type = TaskType.TASK;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(Integer uid, String name, Status status, String description, LocalDateTime startTime, Long duration) {
        this.uid = uid;
        this.type = TaskType.TASK;
        this.name = name;
        this.status = status;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public TaskType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plusMinutes(duration);
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        return uid + "," + type + "," + name + "," + status + "," + description + "," +
                ((getStartTime() != null) ? getStartTime().format(FORMATTER) : "") + "," + getDuration() + "," +
                ((getEndTime() != null) ? getEndTime().format(FORMATTER) : "") + ",\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription())
                && Objects.equals(getUid(), task.getUid())
                && getStatus() == task.getStatus()
                && getType() == task.getType()
                && getStartTime() == task.getStartTime()
                && getDuration() == task.getDuration();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getUid(), getStatus(), getType());
    }
}
