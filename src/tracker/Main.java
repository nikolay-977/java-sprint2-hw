package tracker;

import tracker.managers.Managers;
import tracker.managers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.Arrays;
import java.util.HashSet;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        // две задачи
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.createTask(taskOne);
        Task taskTwo = new Task("Name of task two", "Description of task two");
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
        taskManager.update(epicOne);
        // эпик без подзадач
        Epic epicTwo = new Epic("Name of epic two", "Description of epic two");
        taskManager.createTask(epicTwo);
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
        taskManager.getTaskByUid(epicTwo.getUid());
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
        taskManager.deleteByUid(epicTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        taskManager.deleteByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
    }
}
