package tracker.managers;

import org.junit.jupiter.api.Test;
import tracker.exceptions.IncorrectUidException;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Status.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;

    public TaskManagerTest(T taskManager) {
        this.taskManager = taskManager;
    }

    //    Для подзадач нужно дополнительно проверить наличие эпика
    @Test
    void getEpicSubtaskList() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(epicOne.getUid(), subtaskOne.getEpicUid());
    }

    //    расчёт статуса для эпика
    @Test
    void calculateEpicStatusNew() {
        TaskManager taskManager = Managers.getDefault();
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));

        subtaskOne.setStatus(NEW);
        subtaskTwo.setStatus(NEW);
        subtaskThree.setStatus(NEW);
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(NEW, epicOne.getStatus());
    }

    @Test
    void calculateEpicStatusInProgress() {
        TaskManager taskManager = Managers.getDefault();
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));

        subtaskOne.setStatus(NEW);
        subtaskTwo.setStatus(IN_PROGRESS);
        subtaskThree.setStatus(DONE);
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(IN_PROGRESS, epicOne.getStatus());
    }

    @Test
    void calculateEpicStatusDone() {
        TaskManager taskManager = Managers.getDefault();
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));

        subtaskOne.setStatus(DONE);
        subtaskTwo.setStatus(DONE);
        subtaskThree.setStatus(DONE);
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(DONE, epicOne.getStatus());
    }

    @Test
    void deleteAllTasks() {
        TaskManager taskManager = Managers.getDefault();
        // две задачи
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.createTask(taskOne);
        Task taskTwo = new Task("Name of task two", "Description of task two");
        taskManager.createTask(taskTwo);
        // эпик с тремя подзадачами
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
        taskManager.update(uidEpicOne, epicOne);
        taskManager.deleteAllTasks();
        assertTrue(taskManager.getTaskHashMap().isEmpty());
    }

    @Test
    void getTaskByUid() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(taskOne, taskManager.getTaskHashMap().get(uidTaskOne));
        assertEquals(epicOne, taskManager.getTaskHashMap().get(uidEpicOne));
        assertEquals(subtaskOne, taskManager.getTaskHashMap().get(uidSubtaskOne));
    }

    @Test
    void getTaskByUidWithEmptyTaskHashMap() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidEpicOne, epicOne);
        taskManager.getTaskHashMap().clear();
        assertNull(taskManager.getTaskHashMap().get(uidTaskOne));
        assertNull(taskManager.getTaskHashMap().get(uidEpicOne));
        assertNull(taskManager.getTaskHashMap().get(uidSubtaskOne));
    }

    @Test
    void createTask() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(taskOne, taskManager.getTaskHashMap().get(uidTaskOne));
        assertEquals(epicOne, taskManager.getTaskHashMap().get(uidEpicOne));
        assertEquals(subtaskOne, taskManager.getTaskHashMap().get(uidSubtaskOne));
    }

    @Test
    void update() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Task taskOneUpdated = new Task("Name of task one updated", "Description of task one updated");
        taskOneUpdated.setUid(uidTaskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Epic epicOneUpdated = new Epic("Name of epic one updated", "Description of epic one updated");
        epicOneUpdated.setUid(uidEpicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskOneUpdated = new Subtask("Name of subtask one updated", "Description of subtask one updated", uidEpicOne);
        subtaskOneUpdated.setUid(uidSubtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidTaskOne, taskOneUpdated);
        taskManager.update(uidEpicOne, epicOneUpdated);
        taskManager.update(uidSubtaskOne, subtaskOneUpdated);
        assertEquals(taskOneUpdated, taskManager.getTaskHashMap().get(uidTaskOne));
        assertEquals(epicOneUpdated, taskManager.getTaskHashMap().get(uidEpicOne));
        assertEquals(subtaskOneUpdated, taskManager.getTaskHashMap().get(uidSubtaskOne));
    }

    @Test
    void updateWithEmptyTaskHashMap() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Task taskOneUpdated = new Task("Name of task one updated", "Description of task one updated");
        taskOneUpdated.setUid(uidTaskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Epic epicOneUpdated = new Epic("Name of epic one updated", "Description of epic one updated");
        epicOneUpdated.setUid(uidEpicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskOneUpdated = new Subtask("Name of subtask one updated", "Description of subtask one updated", uidEpicOne);
        subtaskOneUpdated.setUid(uidSubtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.getTaskHashMap().clear();
        final IllegalArgumentException taskException = assertThrows(IllegalArgumentException.class, () -> taskManager.update(uidTaskOne, taskOneUpdated));
        assertEquals("Некорректный uid", taskException.getMessage());
        final IllegalArgumentException epicException = assertThrows(IllegalArgumentException.class, () -> taskManager.update(uidEpicOne, epicOneUpdated));
        assertEquals("Некорректный uid", epicException.getMessage());
        final IllegalArgumentException subTaskException = assertThrows(IllegalArgumentException.class, () -> taskManager.update(uidSubtaskOne, subtaskOneUpdated));
        assertEquals("Некорректный uid", subTaskException.getMessage());
    }

    @Test
    void deleteByUid() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidEpicOne, epicOne);
        taskManager.deleteByUid(taskOne.getUid());
        taskManager.deleteByUid(subtaskOne.getUid());
        taskManager.deleteByUid(epicOne.getUid());
        assertNull(taskManager.getTaskHashMap().get(uidTaskOne));
        assertNull(taskManager.getTaskHashMap().get(uidEpicOne));
        assertNull(taskManager.getTaskHashMap().get(uidSubtaskOne));
    }

    @Test
    void deleteByUidWithEmptyTaskHashMap() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(uidEpicOne, epicOne);
        taskManager.getTaskHashMap().clear();
        final IncorrectUidException taskException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteByUid(uidTaskOne));
        assertEquals("Некорректный uid", taskException.getMessage());
        final IncorrectUidException epicException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteByUid(uidEpicOne));
        assertEquals("Некорректный uid", epicException.getMessage());
        final IncorrectUidException subTaskException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteByUid(uidSubtaskOne));
        assertEquals("Некорректный uid", subTaskException.getMessage());
    }

    @Test
    void history() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        taskManager.getTaskByUid(uidTaskOne);
        assertEquals(1, taskManager.history().size());
    }

    @Test
    void historyWithEmptyTaskHashMap() {
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one");
        int uidTaskOne = taskManager.createTask(taskOne);
        taskManager.getTaskHashMap().clear();
        final IncorrectUidException taskException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteByUid(uidTaskOne));
        assertEquals("Некорректный uid", taskException.getMessage());
        assertEquals(0, taskManager.history().size());
    }
}