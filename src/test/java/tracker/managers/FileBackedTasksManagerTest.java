package tracker.managers;

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

    //    a. Пустой список задач.
    @Test
    void emptyTaskHashMap() {
        taskManager.save();
        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
        assertTrue(taskManagerFromFile.getAllTasksList().isEmpty());
    }

    //    b. Эпик без подзадач.
    @Test
    void epicWithoutSubtasks() {
        Epic epicWithoutSubtasks = new Epic("Name of epic without subtasks", "Description of epic without subtasks");
        taskManager.addTask(epicWithoutSubtasks);
        taskManager.save();
        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
        assertEquals(Collections.emptyList(), taskManagerFromFile.getSubtaskOfEpic(epicWithoutSubtasks));
    }

    //    c. Пустой список истории.
    @Test
    void emptyHistory() {
        taskManager.save();
        FileBackedTasksManager taskManagerFromFile = FileBackedTasksManager.loadFromFile(new File(FILE_NAME));
        assertTrue(taskManagerFromFile.history().isEmpty());
    }
}