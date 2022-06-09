package tracker;

import tracker.managers.Managers;
import tracker.managers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.http.HttpTaskServer;
import tracker.http.KVServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = null;
        HttpTaskServer httpTaskServer = null;
        KVServer kvServer = null;

        try {
            kvServer = new KVServer();
            kvServer.start();
            taskManager = Managers.getDefault();
            httpTaskServer = new HttpTaskServer(taskManager);
            httpTaskServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // две задачи
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1L;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        taskManager.addTask(taskOne);
        LocalDateTime taskTwoStartTime = taskOneStartTime.plusMinutes(2l);
        long taskTwoDuration = 1L;
        Task taskTwo = new Task("Name of task two", "Description of task two", taskTwoStartTime, taskTwoDuration);
        taskManager.addTask(taskTwo);
        httpTaskServer.stop();
        // эпик с тремя подзадачами
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        epicOne.setStartTime(taskTwoStartTime.plusDays(1L));
        Epic addedEpicOne = taskManager.addEpic(epicOne);
        LocalDateTime subtaskOneStartTime = taskTwoStartTime.plusMinutes(2l);
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpicOne.getUid());
        Subtask addedSubtaskOne = taskManager.addSubtask(subtaskOne);
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpicOne.getUid());
        Subtask addedSubtaskTwo = taskManager.addSubtask(subtaskTwo);
        LocalDateTime subtaskThreeStartTime = subtaskTwoStartTime.plusMinutes(2l);
        long subtaskThreeDuration = 1L;
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", subtaskThreeStartTime, subtaskThreeDuration, addedEpicOne.getUid());
        Subtask addedSubtaskThree = taskManager.addSubtask(subtaskThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid(), addedSubtaskThree.getUid())));
        taskManager.updateEpic(addedEpicOne);
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
        taskManager.getEpicByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getSubtaskByUid(subtaskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getSubtaskByUid(subtaskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.getSubtaskByUid(subtaskThree.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        // удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться;
        taskManager.deleteTaskByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.deleteEpicByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        System.out.println("+++ Список задач в порядке приоритета +++");
        System.out.println(taskManager.getPrioritizedTasks());

        httpTaskServer.stop();
        kvServer.stop();
    }
}
