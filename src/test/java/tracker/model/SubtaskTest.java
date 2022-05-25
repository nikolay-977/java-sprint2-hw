package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.managers.Managers;
import tracker.managers.TaskManager;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Task.FORMATTER;

class SubtaskTest extends TaskTest {

    @Test
    void subtaskWithEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int expectedUidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", expectedUidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne)));
        taskManager.update(epicOne.getUid(), epicOne);
        Integer actualUidEpicOne = ((Subtask) taskManager.getTaskHashMap().get(uidSubtaskOne)).getEpicUid();
        assertEquals(expectedUidEpicOne, actualUidEpicOne);
    }

    @Test
    void getString() {
        TaskManager taskManager = Managers.getDefault();
        LocalDateTime startTime = LocalDateTime.now();
        Long duration = 1l;
        LocalDateTime endTime = startTime.plusMinutes(duration);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", startTime, duration, null);
        taskManager.createTask(subtaskOne);
        String expected = subtaskOne.getUid() + ",SUB_TASK,Name of subtask one,NEW,Description of subtask one," + startTime.format(FORMATTER) + "," + duration + "," + endTime.format(FORMATTER) + ",null\n";
        assertEquals(expected, subtaskOne.toString());
    }
}