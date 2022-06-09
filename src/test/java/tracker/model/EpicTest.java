package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.managers.InMemoryTaskManager;
import tracker.managers.TaskManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Status.*;
import static tracker.model.Task.FORMATTER;

class EpicTest {
    TaskManager taskManager = new InMemoryTaskManager();

    //    a. Пустой список подзадач.
    @Test
    public void isEpicEmpty() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        assertEquals(Collections.emptyList(), taskManager.getSubtaskOfEpic(epicOne));
        assertEquals(NEW, epicOne.getStatus());
    }

    //    b. Все подзадачи со статусом NEW.
    @Test
    public void allTasksWithStatusNew() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(3, taskManager.getSubtaskOfEpic(epicOne).size());
        assertEquals(NEW, epicOne.getStatus());
    }

    //    c. Все подзадачи со статусом DONE.
    @Test
    public void allTasksWithStatusDone() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        subtaskOne.setStatus(DONE);
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(DONE);
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(DONE);
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(3, taskManager.getSubtaskOfEpic(epicOne).size());
        assertEquals(DONE, epicOne.getStatus());
    }

    //    d. Подзадачи со статусами NEW и DONE.
    @Test
    public void tasksWithStatusNewAndDone() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        subtaskOne.setStatus(DONE);
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(NEW);
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(DONE);
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(3, taskManager.getSubtaskOfEpic(epicOne).size());
        assertEquals(IN_PROGRESS, epicOne.getStatus());
    }

    //    e. Подзадачи со статусом IN_PROGRESS.
    @Test
    public void allTasksWithStatusInProgress() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        subtaskOne.setStatus(IN_PROGRESS);
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(IN_PROGRESS);
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(IN_PROGRESS);
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(3, taskManager.getSubtaskOfEpic(epicOne).size());
        assertEquals(IN_PROGRESS, epicOne.getStatus());
    }

    @Test
    void getString() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        epicOne.setStartTime(LocalDateTime.now());
        epicOne.setDuration(1l);
        taskManager.addTask(epicOne);
        String expected = epicOne.getUid() + ",EPIC,Name of epic one,NEW,Description of epic one," + epicOne.getStartTime().format(FORMATTER) + ",1,,\n";
        assertEquals(expected, epicOne.toString());
    }

    @Test
    void getEndTime() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", addedEpicOne.getUid());
        subtaskTwo.setStartTime(LocalDateTime.now().plusDays(1L));
        subtaskTwo.setDuration(1l);
        subtaskTwo.setStatus(IN_PROGRESS);
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", addedEpicOne.getUid());
        subtaskThree.setStartTime(LocalDateTime.now().plusDays(2L));
        subtaskThree.setDuration(1l);
        subtaskThree.setStatus(IN_PROGRESS);
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));
        taskManager.updateEpic(addedEpicOne);
        Long expectedDuration = 3l;
        assertEquals(expectedDuration, epicOne.getDuration());
        assertEquals(subtaskOne.getStartTime(), epicOne.getStartTime());
        assertEquals(subtaskThree.getEndTime(), epicOne.getEndTime());
    }
}