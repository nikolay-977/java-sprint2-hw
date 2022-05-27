package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.managers.Managers;
import tracker.managers.TaskManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Status.*;
import static tracker.model.Task.FORMATTER;

class EpicTest {
    TaskManager taskManager = Managers.getDefault();

    //    a. Пустой список подзадач.
    @Test
    public void isEpicEmpty() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        assertEquals(Collections.emptyList(), taskManager.getEpicSubtaskList(epicOne));
        assertEquals(NEW, epicOne.getStatus());
    }

    //    b. Все подзадачи со статусом NEW.
    @Test
    public void allTasksWithStatusNew() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(3, taskManager.getEpicSubtaskList(epicOne).size());
        assertEquals(NEW, epicOne.getStatus());
    }

    //    c. Все подзадачи со статусом DONE.
    @Test
    public void allTasksWithStatusDone() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        subtaskOne.setStatus(DONE);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(DONE);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(DONE);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(3, taskManager.getEpicSubtaskList(epicOne).size());
        assertEquals(DONE, epicOne.getStatus());
    }

    //    d. Подзадачи со статусами NEW и DONE.
    @Test
    public void tasksWithStatusNewAndDone() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        subtaskOne.setStatus(DONE);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(NEW);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(DONE);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(3, taskManager.getEpicSubtaskList(epicOne).size());
        assertEquals(IN_PROGRESS, epicOne.getStatus());
    }

    //    e. Подзадачи со статусом IN_PROGRESS.
    @Test
    public void allTasksWithStatusInProgress() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        subtaskOne.setStatus(IN_PROGRESS);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(IN_PROGRESS);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(IN_PROGRESS);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
        taskManager.update(uidEpicOne, epicOne);
        assertEquals(3, taskManager.getEpicSubtaskList(epicOne).size());
        assertEquals(IN_PROGRESS, epicOne.getStatus());
    }

    @Test
    void getString() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        epicOne.setStartTime(LocalDateTime.now());
        epicOne.setDuration(1l);
        taskManager.createTask(epicOne);
        String expected = epicOne.getUid() + ",EPIC,Name of epic one,NEW,Description of epic one," + epicOne.getStartTime().format(FORMATTER) + ",1,,\n";
        assertEquals(expected, epicOne.toString());
    }

    @Test
    void getEndTime() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(IN_PROGRESS);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicOne);
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(IN_PROGRESS);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
        taskManager.update(uidEpicOne, epicOne);
        Long expectedDuration = 3l;
        assertEquals(expectedDuration, epicOne.getDuration());
        assertEquals(subtaskOne.getStartTime(), epicOne.getStartTime());
        assertEquals(subtaskThree.getEndTime(), epicOne.getEndTime());
    }
}