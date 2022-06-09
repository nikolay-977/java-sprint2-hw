package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.managers.InMemoryTaskManager;
import tracker.managers.TaskManager;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Task.FORMATTER;

class SubtaskTest extends TaskTest {
    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void subtaskWithEpic() {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", addedEpicOne.getUid());
        subtaskOne.setStartTime(LocalDateTime.now());
        subtaskOne.setDuration(1l);
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        addedEpicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid())));
        taskManager.updateEpic(addedEpicOne);
        assertEquals(addedSubtaskOne.getEpicUid(), addedEpicOne.getUid());
    }

    @Test
    void getString() {
        LocalDateTime startTime = LocalDateTime.now();
        Long duration = 1L;
        LocalDateTime endTime = startTime.plusMinutes(duration);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", startTime, duration, null);
        taskManager.addSubtask(subtaskOne);
        String expected = subtaskOne.getUid() + ",SUB_TASK,Name of subtask one,NEW,Description of subtask one," + startTime.format(FORMATTER) + "," + duration + "," + endTime.format(FORMATTER) + ",null\n";
        assertEquals(expected, subtaskOne.toString());
    }
}