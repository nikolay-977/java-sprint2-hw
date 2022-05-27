package tracker;

import tracker.managers.Managers;
import tracker.managers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        // две задачи
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1L;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        taskManager.createTask(taskOne);
        LocalDateTime taskTwoStartTime = taskOneStartTime.plusMinutes(2l);
        long taskTwoDuration = 1L;
        Task taskTwo = new Task("Name of task two", "Description of task two", taskTwoStartTime, taskTwoDuration);
        taskManager.createTask(taskTwo);
        // эпик с тремя подзадачами
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        epicOne.setStartTime(taskTwoStartTime.plusDays(1L));
        int uidEpicOne = taskManager.createTask(epicOne);
        LocalDateTime subtaskOneStartTime = taskTwoStartTime.plusMinutes(2l);
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, uidEpicOne);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        LocalDateTime subtaskThreeStartTime = subtaskTwoStartTime.plusMinutes(2l);
        long subtaskThreeDuration = 1L;
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", subtaskThreeStartTime, subtaskThreeDuration, uidEpicOne);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(uidSubtaskOne, uidSubtaskTwo, uidSubtaskThree)));
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
