package tracker.managers;

import org.junit.jupiter.api.Test;
import tracker.model.Task;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryTaskManager taskManager = new InMemoryTaskManager();
    //    a. Пустая история задач.
    @Test
    void emptyHistory() {
        assertEquals(Collections.emptyList(), taskManager.history());
    }

    //    b. Дублирование.
    @Test
    void duplicate() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.addTask(taskOne);
        taskManager.getTaskByUid(taskOne.getUid());
        taskManager.getTaskByUid(taskOne.getUid());
        assertEquals(1, taskManager.history().size());
    }

    //    с. Удаление из истории: начало.
    @Test
    void deleteFromBeginning() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.addTask(taskOne);
        Task taskTwo = new Task("Name of task two", "Description of task two");
        taskManager.addTask(taskTwo);
        Task taskThree = new Task("Name of task three", "Description of task three");
        taskManager.addTask(taskThree);
        taskManager.getTaskByUid(taskOne.getUid());
        taskManager.getTaskByUid(taskTwo.getUid());
        taskManager.getTaskByUid(taskThree.getUid());
        taskManager.getHistoryManager().remove(taskOne.getUid());
        assertEquals(2, taskManager.history().size());
    }

    //    с. Удаление из истории: середина.
    @Test
    void deleteFromMiddle() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.addTask(taskOne);
        Task taskTwo = new Task("Name of task two", "Description of task two");
        taskManager.addTask(taskTwo);
        Task taskThree = new Task("Name of task three", "Description of task three");
        taskManager.addTask(taskThree);
        taskManager.getTaskByUid(taskOne.getUid());
        taskManager.getTaskByUid(taskTwo.getUid());
        taskManager.getTaskByUid(taskThree.getUid());
        taskManager.getHistoryManager().remove(taskTwo.getUid());
        assertEquals(2, taskManager.history().size());
    }

    //    с. Удаление из истории: конец.
    @Test
    void deleteFromEnd() {
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.addTask(taskOne);
        Task taskTwo = new Task("Name of task two", "Description of task two");
        taskManager.addTask(taskTwo);
        Task taskThree = new Task("Name of task three", "Description of task three");
        taskManager.addTask(taskThree);
        taskManager.getTaskByUid(taskOne.getUid());
        taskManager.getTaskByUid(taskTwo.getUid());
        taskManager.getTaskByUid(taskThree.getUid());
        taskManager.getHistoryManager().remove(taskThree.getUid());
        assertEquals(2, taskManager.history().size());
    }
}