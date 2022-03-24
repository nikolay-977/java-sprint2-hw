package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.*;

public interface TaskManager {
    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // Получение списка всех задач.
    HashMap<Integer, Task> getTaskHashMap();

    // Удаление всех задач.
    void deleteAllTasks();

    // Получение по идентификатору.
    Task getTaskByUid(Integer uid);

    // Создание. Сам объект должен передаваться в качестве параметра.
    Integer createTask(Task task);

    // Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
    void update(Task task);

    // Удаление по идентификатору.
    public void deleteByUid(Integer uid);

    // Дополнительные методы:
    // Получение списка всех подзадач определённого эпика.
    public List<Subtask> getEpicSubtaskList(Epic epic);

    // Управление статусами осуществляется по следующему правилу:
    // Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    // Для эпиков:
    Epic calculateEpicStatus(Epic epic);
}
