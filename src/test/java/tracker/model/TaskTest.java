package tracker.model;

import org.junit.jupiter.api.Test;
import tracker.managers.InMemoryTaskManager;
import tracker.managers.Managers;
import tracker.managers.TaskManager;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.model.Task.FORMATTER;

class TaskTest {
    TaskManager taskManager = new InMemoryTaskManager();

    @Test
    void getString(){
        LocalDateTime startTime = LocalDateTime.now();
        Long duration = 1L;
        LocalDateTime endTime = startTime.plusMinutes(duration);
        Task taskOne = new Task("Name of task one", "Description of task one", startTime, duration);
        taskManager.addTask(taskOne);
        String expected = taskOne.getUid() + ",TASK,Name of task one,NEW,Description of task one," + startTime.format(FORMATTER) + "," + duration + "," + endTime.format(FORMATTER) + ",\n";
        assertEquals(expected,taskOne.toString());
    }
}