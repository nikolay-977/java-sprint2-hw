package tracker;

import tracker.managers.Managers;
import tracker.managers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        // две задачи
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1l;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        taskManager.createTask(taskOne);
        LocalDateTime taskTwoStartTime = taskOneStartTime.plusMinutes(2l);
        long taskTwoDuration = 1l;
        Task taskTwo = new Task("Name of task two", "Description of task two", taskTwoStartTime, taskTwoDuration);
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
        taskManager.update(epicOne.getUid(), epicOne);
        // История просмотров задач
        // Запросите созданные задачи несколько раз в разном порядке
        taskManager.getTaskByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(taskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(taskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(subtaskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(subtaskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getTaskByUid(subtaskThree.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        // удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться;
        taskManager.deleteByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.deleteByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        System.out.println("+++ Список задач в порядке приоритета +++");
        System.out.println(taskManager.getPrioritizedTasks());
    }
}
