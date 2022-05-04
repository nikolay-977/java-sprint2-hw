package tracker.model;

import java.util.Objects;

public class Task {
    protected Integer uid;
    protected TaskType type;
    protected String name;
    protected Status status;
    protected String description;

    public Task(String name, String description) {
        this.type = TaskType.TASK;
        this.name = name;
        this.status = Status.NEW;
        this.description = description;
    }

    public Task(Integer uid, String name, Status status, String description) {
        this.uid = uid;
        this.type = TaskType.TASK;
        this.name = name;
        this.status = status;
        this.description = description;
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

    public void setType(TaskType type) {
        this.type = type;
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

    @Override
    public String toString() {
        return uid + "," + type + "," + name + "," + status + "," + description + ",\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Task)) return false;
        Task task = (Task) o;
        return Objects.equals(getName(), task.getName()) && Objects.equals(getDescription(), task.getDescription())
                && Objects.equals(getUid(), task.getUid()) && getStatus() == task.getStatus() && getType() == task.getType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getDescription(), getUid(), getStatus(), getType());
    }
}
