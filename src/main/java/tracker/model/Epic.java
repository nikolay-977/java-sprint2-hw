package tracker.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;

public class Epic extends Task {
    private LocalDateTime endTime;
    private HashSet<Integer> subtaskUidSet;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subtaskUidSet = new HashSet<>();
    }

    public Epic(Integer uid, String name, Status status, String description) {
        super(uid, name, status, description);
        this.type = TaskType.EPIC;
        this.subtaskUidSet = new HashSet<>();
    }

    public Epic(Integer uid, String name, Status status, String description, LocalDateTime startTime, Long duration) {
        super(uid, name, status, description, startTime, duration);
        this.type = TaskType.EPIC;
        this.subtaskUidSet = new HashSet<>();
    }

    public HashSet<Integer> getSubtaskUidSet() {
        return subtaskUidSet;
    }

    public void setSubtaskUidSet(HashSet<Integer> subtaskUidSet) {
        this.subtaskUidSet = subtaskUidSet;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
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
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(getEndTime(), epic.getEndTime()) && Objects.equals(getSubtaskUidSet(), epic.getSubtaskUidSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEndTime(), getSubtaskUidSet());
    }
}
