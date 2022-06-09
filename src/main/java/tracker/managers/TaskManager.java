package tracker.managers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.*;

public interface TaskManager {
    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // Получение списка всех задач.
    List<Task> getAllTasksList();

    List<Epic> getAllEpicsList();

    List<Subtask> getAllSubtasksList();

    // Удаление всех задач.
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // Получение по идентификатору.
    Task getTaskByUid(Integer uid);

    Epic getEpicByUid(Integer uid);

    Subtask getSubtaskByUid(Integer uid);

    // Создание. Сам объект должен передаваться в качестве параметра.
    Task addTask(Task task);

    Epic addEpic(Epic epic);

    Subtask addSubtask(Subtask subtask);

    // Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
    void updateTask(Task task);

    void updateEpic(Epic task);

    void updateSubtask(Subtask task);

    // Удаление по идентификатору.
    void deleteTaskByUid(Integer uid);

    void deleteEpicByUid(Integer uid);

    void deleteSubtaskByUid(Integer uid);

    // Дополнительные методы:
    // Получение списка всех подзадач определённого эпика.
    List<Subtask> getSubtaskOfEpic(Epic epic);

    // Управление статусами осуществляется по следующему правилу:
    // Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    // Для эпиков:
    Epic calculateEpicStatus(Epic epic);

    // История просмотров задач
    List<Task> history();

    // Список задач в порядке приоритета
    Set<Task> getPrioritizedTasks();

    // Проверьте пересечения
    boolean validateIntersections();
}
