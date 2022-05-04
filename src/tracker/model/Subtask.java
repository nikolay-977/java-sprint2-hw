package tracker.model;

import java.util.Objects;

public class Subtask extends Task {
    private Integer epicUid;

    public Subtask(String name, String description, Integer epicUid) {
        super(name, description);
        this.type = TaskType.SUB_TASK;
        this.epicUid = epicUid;
    }

    public Subtask(Integer uid, String name, Status status, String description, Integer epicUid) {
        super(uid, name, status, description);
        this.type = TaskType.SUB_TASK;
        this.epicUid = epicUid;
    }

    public Integer getEpicUid() {
        return epicUid;
    }

    public void setEpicUid(Integer epicUid) {
        this.epicUid = epicUid;
    }

    @Override
    public String toString() {
        return uid + "," + type + "," + name + "," + status + "," + description + "," + epicUid + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Subtask)) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return Objects.equals(getEpicUid(), subtask.getEpicUid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getEpicUid());
    }
}
