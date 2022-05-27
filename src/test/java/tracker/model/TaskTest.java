package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.managers.Managers;
import tracker.managers.TaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Task.FORMATTER;

class TaskTest {

    @Test
    void getString(){
        LocalDateTime startTime = LocalDateTime.now();
        Long duration = 1L;
        LocalDateTime endTime = startTime.plusMinutes(duration);
        TaskManager taskManager = Managers.getDefault();
        Task taskOne = new Task("Name of task one", "Description of task one", startTime, duration);
        taskManager.createTask(taskOne);
        String expected = taskOne.getUid() + ",TASK,Name of task one,NEW,Description of task one," + startTime.format(FORMATTER) + "," + duration + "," + endTime.format(FORMATTER) + ",\n";
        assertEquals(expected,taskOne.toString());
    }
}