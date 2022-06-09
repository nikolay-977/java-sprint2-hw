package tracker.managers;

import org.junit.jupiter.api.Test;
import tracker.exceptions.IncorrectUidException;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.HashSet;
import java.util.List;

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
        Epic addedEpic = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpic.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(addedEpic);
        assertEquals(epicOne.getUid(), subtaskOne.getEpicUid());
    }

    //    расчёт статуса для эпика
    @Test
    void calculateEpicStatusNew() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));

        subtaskOne.setStatus(NEW);
        subtaskTwo.setStatus(NEW);
        subtaskThree.setStatus(NEW);
        taskManager.updateEpic(epicOne);
        assertEquals(NEW, epicOne.getStatus());
    }

    @Test
    void calculateEpicStatusInProgress() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(subtaskOne.getUid(), subtaskTwo.getUid(), subtaskThree.getUid())));
        subtaskOne.setStatus(NEW);
        subtaskTwo.setStatus(IN_PROGRESS);
        subtaskThree.setStatus(DONE);
        taskManager.updateEpic(epicOne);
        assertEquals(IN_PROGRESS, epicOne.getStatus());
    }

    @Test
    void calculateEpicStatusDone() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(subtaskOne.getUid(), subtaskTwo.getUid(), subtaskThree.getUid())));

        subtaskOne.setStatus(DONE);
        subtaskTwo.setStatus(DONE);
        subtaskThree.setStatus(DONE);
        taskManager.updateEpic(epicOne);
        assertEquals(DONE, epicOne.getStatus());
    }

    @Test
    void deleteAllTasks() {
        // две задачи
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.addTask(taskOne);
        Task taskTwo = new Task("Name of task two", "Description of task two");
        taskManager.addTask(taskTwo);
        // эпик с тремя подзадачами
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(subtaskOne.getUid(), subtaskTwo.getUid(), subtaskThree.getUid())));

        taskManager.updateEpic(epicOne);
        taskManager.deleteAllTasks();
        taskManager.deleteAllEpics();
        taskManager.deleteAllSubtasks();

        assertTrue(taskManager.getAllTasksList().isEmpty());
        assertTrue(taskManager.getAllEpicsList().isEmpty());
        assertTrue(taskManager.getAllSubtasksList().isEmpty());
    }

    @Test
    void getByUid() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        Task getTaskOne = taskManager.getTaskByUid(addedTaskOne.getUid());
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Epic getEpicOne = taskManager.getEpicByUid(addedEpicOne.getUid());
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask getSubtaskOneSubtaskOne = taskManager.getSubtaskByUid(addedSubtaskOne.getUid());
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(addedTaskOne, getTaskOne);
        assertEquals(addedEpicOne, getEpicOne);
        assertEquals(addedSubtaskOne, getSubtaskOneSubtaskOne);
    }

    @Test
    void getByUidWithEmptyTaskHashMap() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(addedEpicOne);
        taskManager.getAllTasksList().clear();
        taskManager.getAllEpicsList().clear();
        taskManager.getAllSubtasksList().clear();
        final IncorrectUidException taskException = assertThrows(IncorrectUidException.class, () -> taskManager.getTaskByUid(addedTaskOne.getUid()));
        assertEquals("Некорректный uid", taskException.getMessage());
        final IncorrectUidException epicException = assertThrows(IncorrectUidException.class, () -> taskManager.getEpicByUid(addedEpicOne.getUid()));
        assertEquals("Некорректный uid", epicException.getMessage());
        final IncorrectUidException subTaskException = assertThrows(IncorrectUidException.class, () -> taskManager.getSubtaskByUid(addedSubtaskOne.getUid()));
        assertEquals("Некорректный uid", subTaskException.getMessage());
    }

    @Test
    void createTask() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(taskOne, addedTaskOne);
        assertEquals(epicOne, addedEpicOne);
        assertEquals(subtaskOne, addedSubtaskOne);
    }

    @Test
    void update() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        addedTaskOne.setName("Name of task updated");

        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        addedEpicOne.setName("Name of epic updated");

        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        addedSubtaskOne.setName("Name of subtask updated");

        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateTask(addedTaskOne);
        taskManager.updateEpic(addedEpicOne);
        taskManager.updateSubtask(addedSubtaskOne);
        assertEquals("Name of task updated", taskManager.getTaskByUid(addedTaskOne.getUid()).getName());
        assertEquals("Name of epic updated", taskManager.getEpicByUid(addedEpicOne.getUid()).getName());
        assertEquals("Name of subtask updated", taskManager.getSubtaskByUid(addedSubtaskOne.getUid()).getName());
    }

    @Test
    void updateWithEmptyTaskHashMap() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        Task taskOneUpdated = new Task("Name of task one updated", "Description of task one updated");
        taskOneUpdated.setUid(addedTaskOne.getUid());
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Epic epicOneUpdated = new Epic("Name of epic one updated", "Description of epic one updated");
        epicOneUpdated.setUid(addedEpicOne.getUid());
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskOneUpdated = new Subtask("Name of subtask one updated", "Description of subtask one updated", addedEpicOne.getUid());
        subtaskOneUpdated.setUid(addedSubtaskOne.getUid());
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.getAllTasksList().clear();
        taskManager.getAllEpicsList().clear();
        taskManager.getAllSubtasksList().clear();
        final IncorrectUidException taskException = assertThrows(IncorrectUidException.class, () -> taskManager.updateTask(taskOneUpdated));
        assertEquals("Некорректный uid", taskException.getMessage());
        final IncorrectUidException epicException = assertThrows(IncorrectUidException.class, () -> taskManager.updateEpic(epicOneUpdated));
        assertEquals("Некорректный uid", epicException.getMessage());
        final IncorrectUidException subTaskException = assertThrows(IncorrectUidException.class, () -> taskManager.updateSubtask(subtaskOneUpdated));
        assertEquals("Некорректный uid", subTaskException.getMessage());
    }

    @Test
    void deleteByUid() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.addTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(epicOne);
        taskManager.deleteTaskByUid(taskOne.getUid());
        taskManager.deleteSubtaskByUid(subtaskOne.getUid());
        taskManager.deleteEpicByUid(epicOne.getUid());
        assertTrue(taskManager.getAllTasksList().isEmpty());
        assertTrue(taskManager.getAllEpicsList().isEmpty());
        assertTrue(taskManager.getAllSubtasksList().isEmpty());
    }

    @Test
    void deleteByUidWithEmptyTaskHashMap() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(epicOne);
        taskManager.getAllTasksList().clear();
        taskManager.getAllEpicsList().clear();
        taskManager.getAllSubtasksList().clear();
        final IncorrectUidException taskException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteTaskByUid(addedTaskOne.getUid()));
        assertEquals("Некорректный uid", taskException.getMessage());
        final IncorrectUidException epicException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteEpicByUid(addedEpicOne.getUid()));
        assertEquals("Некорректный uid", epicException.getMessage());
        final IncorrectUidException subTaskException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteSubtaskByUid(addedSubtaskOne.getUid()));
        assertEquals("Некорректный uid", subTaskException.getMessage());
    }

    @Test
    void history() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        taskManager.getTaskByUid(addedTaskOne.getUid());
        assertEquals(1, taskManager.history().size());
    }

    @Test
    void historyWithEmptyTaskHashMap() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        Task addedTaskOne = taskManager.addTask(taskOne);
        taskManager.getAllTasksList().clear();
        final IncorrectUidException taskException = assertThrows(IncorrectUidException.class, () -> taskManager.deleteTaskByUid(addedTaskOne.getUid()));
        assertEquals("Некорректный uid", taskException.getMessage());
        assertEquals(0, taskManager.history().size());
    }
}