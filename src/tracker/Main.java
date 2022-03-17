package tracker;

import tracker.controllers.Manager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.Arrays;
import java.util.HashSet;

import static tracker.model.Status.DONE;
import static tracker.model.Status.IN_PROGRESS;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        // Создайте 2 задачи,
        Task taskOne = new Task("Name of task one", "Description of task one");
        manager.createTask(taskOne);
        Task taskTwo = new Task("Name of task one", "Description of task one");
        manager.createTask(taskTwo);
        // один эпик с 2 подзадачами,
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        int uidEpicOne = manager.createTask(epicOne);
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", uidEpicOne);
        int uidSubtaskOne = manager.createTask(subtaskOne);
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", uidEpicOne);
        int uidSubtaskTwo = manager.createTask(subtaskTwo);
        epicOne.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskOne, uidSubtaskTwo)));
        manager.update(epicOne);
        // а другой эпик с 1 подзадачей.
        Epic epicTwo = new Epic("Name of epic two", "Description of epic two");
        int uidEpicTwo = manager.createTask(epicTwo);
        Subtask subtaskThree = new Subtask("Name of subtask three", "Description of subtask three", uidEpicTwo);
        int uidSubtaskThree = manager.createTask(subtaskThree);
        epicTwo.setSubtaskUidSet(new HashSet<>(Arrays.asList(uidSubtaskThree)));
        manager.update(epicTwo);
        // Распечатайте списки эпиков, задач и подзадач, через System.out.println(..)
        System.out.println(manager.getTaskHashMap());
        // Измените статусы созданных объектов, распечатайте. Проверьте, что статус задачи и подзадачи сохранился, а статус эпика рассчитался по статусам подзадач.
        subtaskOne.setStatus(DONE);
        manager.update(subtaskOne);
        subtaskTwo.setStatus(DONE);
        manager.update(subtaskTwo);
        subtaskThree.setStatus(IN_PROGRESS);
        manager.update(subtaskThree);
        System.out.println(manager.getTaskHashMap());
        // И, наконец, попробуйте удалить одну из задач и один из эпиков.
        manager.deleteByUid(uidSubtaskOne);
        System.out.println(manager.getTaskHashMap());
        manager.deleteByUid(uidSubtaskThree);
        System.out.println(manager.getTaskHashMap());
        manager.deleteByUid(uidEpicOne);
        System.out.println(manager.getTaskHashMap());
    }
}
