package tracker.managers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tracker.model.Epic;

import java.io.File;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    private static final String FILE_NAME = "test.csv";

    public FileBackedTasksManagerTest() {
        super(new FileBackedTasksManager(new File(FILE_NAME)));
    }

    @Test
    @DisplayName("Пустой список задач")
    void emptyTaskHashMap() {
        taskManager.save();
        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
        assertTrue(taskManagerFromFile.getAllTasksList().isEmpty());
    }

    @Test
    @DisplayName("Эпик без подзадач")
    void epicWithoutSubtasks() {
        Epic epicWithoutSubtasks = new Epic("Name of epic without subtasks", "Description of epic without subtasks");
        taskManager.addTask(epicWithoutSubtasks);
        taskManager.save();
        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
        assertEquals(Collections.emptyList(), taskManagerFromFile.getSubtaskOfEpic(epicWithoutSubtasks));
    }

    @Test
    @DisplayName("Пустой список истории")
    void emptyHistory() {
        taskManager.save();
        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
        assertTrue(taskManagerFromFile.history().isEmpty());
    }
}