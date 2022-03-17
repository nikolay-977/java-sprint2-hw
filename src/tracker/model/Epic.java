package tracker.model;

import java.util.HashSet;
import java.util.Objects;

public class Epic extends Task {
    private HashSet<Integer> subtaskUidSet;

    public Epic(String name, String description) {
        super(name, description);
        this.type = TaskType.EPIC;
        this.subtaskUidSet = new HashSet<>();
    }

    public HashSet<Integer> getSubtaskUidSet() {
        return subtaskUidSet;
    }

    public void setSubtaskUidSet(HashSet<Integer> subtaskUidSet) {
        this.subtaskUidSet = subtaskUidSet;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subtaskUidSet=" + subtaskUidSet +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epic)) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return getSubtaskUidSet().equals(epic.getSubtaskUidSet());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getSubtaskUidSet());
    }
}
