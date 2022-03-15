package task;

public class Subtask extends Task {
    private Integer epicUid;

    public Subtask(String name, String description, Integer epicUid) {
        super(name, description);
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
        return "Subtask{" +
                "epicUid=" + epicUid +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", uid=" + uid +
                ", status=" + status +
                ", type=" + type +
                '}';
    }
}
