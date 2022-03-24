package tracker.controllers;

import tracker.model.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getDefaultHistory();
}