package tracker.managers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.List;
import java.util.Set;

public class StateManager {
    private List<Task> tasks;
    private List<Epic> epics;
    private List<Subtask> subtasks;
    private Set<Task> prioritizedTasks;

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public List<Epic> getEpics() {
        return epics;
    }

    public void setEpics(List<Epic> epics) {
        this.epics = epics;
    }

    public List<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    public void setPrioritizedTasks(Set<Task> prioritizedTasks) {
        this.prioritizedTasks = prioritizedTasks;
    }

    public List<Task> getListHistory() {
        return listHistory;
    }

    public void setListHistory(List<Task> listHistory) {
        this.listHistory = listHistory;
    }

    private List<Task> listHistory;
}
