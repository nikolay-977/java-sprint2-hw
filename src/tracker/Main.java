package tracker;

import tracker.managers.Managers;
import tracker.managers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.Arrays;
import java.util.HashSet;

import static tracker.model.Status.DONE;
import static tracker.model.Status.IN_PROGRESS;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();
        // Создайте 2 задачи,
        Task taskOne = new Task("Name of task one", "Description of task one");
        taskManager.createTask(taskOne);
        Task taskTwo = new Task("Name of task one", "Description of task one");
        taskManager.createTask(taskTwo);
        // один эпик с 2 подзадачами,
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = taskManager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = taskManager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        int uidSubtaskTwo = taskManager.createTask(subtaskTwo);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne, uidSubtaskTwo)));
        taskManager.update(epicOne);
        // История просмотров задач
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        // а другой эпик с 1 подзадачей.
        Epic epicTwo = new Epic("Name of epic two", "Description of epic two");
        int uidEpicTwo = taskManager.createTask(epicTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicTwo);
        int uidSubtaskThree = taskManager.createTask(subtaskThree);
        epicTwo.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskThree)));
        taskManager.update(epicTwo);
        Epic epicThree = new Epic("Name of epic three", "Description of epic three");
        int uidEpicThree = taskManager.createTask(epicThree);
        Subtask subtaskFour = new Subtask("Name of subtask four", "Description of subtask four", uidEpicThree);
        int uidSubtaskFour = taskManager.createTask(subtaskFour);
        epicTwo.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskFour)));
        taskManager.update(epicThree);
        // История просмотров задач
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        // Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        subtaskOne.setStatus(DONE);
        taskManager.update(subtaskOne);
        // История просмотров задач
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        subtaskTwo.setStatus(DONE);
        taskManager.update(subtaskTwo);
        subtaskThree.setStatus(IN_PROGRESS);
        taskManager.update(subtaskThree);
        // История просмотров задач
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        taskManager.deleteByUid(uidSubtaskOne);
        taskManager.deleteByUid(uidSubtaskThree);
        taskManager.deleteByUid(uidEpicOne);
        // История просмотров задач
        System.out.println("+++ История просмотров задач +++");
        System.out.println(taskManager.history());
    }
}
