package task;

import java.util.HashSet;

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
}
