package tracker.controllers;

import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tracker.model.Status.*;

public class InMemoryTaskManager implements TaskManager {
    private static Integer nextUid;
    // Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    private HashMap<Integer, Task> taskHashMap;

    public InMemoryTaskManager() {
        this.taskHashMap = new HashMap<>();
        this.nextUid = 0;
    }

    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // Получение списка всех задач.
    @Override
    public HashMap<Integer, Task> getTaskHashMap() {
        return taskHashMap;
    }

    // Удаление всех задач.
    @Override
    public void deleteAllTasks() {
        taskHashMap.clear();
        nextUid = 0;
    }

    // Получение по идентификатору.
    @Override
    public Task getTaskByUid(Integer uid) {
        return taskHashMap.get(uid);
    }

    // Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public Integer createTask(Task task) {
        int uid = ++nextUid;
        task.setUid(uid);
        taskHashMap.put(uid, task);
        return uid;
    }

    // Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
    @Override
    public void update(Task task) {
        if (task instanceof Subtask) {
            updateSubTask((Subtask) task);
        } else if (task instanceof Epic) {
            updateEpic((Epic) task);
        } else {
            updateTask(task);
        }

    }

    private void updateEpic(Epic epic) {
        epic = calculateEpicStatus(epic);
        taskHashMap.put(epic.getUid(), epic);
    }

    private void updateTask(Task task) {
        taskHashMap.put(task.getUid(), task);
    }

    private void updateSubTask(Subtask subtask) {
        taskHashMap.put(subtask.getUid(), subtask);
        Integer epicUid = subtask.getEpicUid();
        Epic epic = (Epic) taskHashMap.get(epicUid);
        calculateEpicStatus(epic);
        taskHashMap.put(epicUid, epic);
    }

    // Удаление по идентификатору.
    @Override
    public void deleteByUid(Integer uid) {
        Task task = getTaskByUid(uid);

        if (task instanceof Subtask) {
            deleteSubtaskByUid((Subtask) task);
        } else if (task instanceof Epic) {
            deleteEpicByUid((Epic) task);
        } else {
            taskHashMap.remove(uid);
        }
    }

    private void deleteSubtaskByUid(Subtask subtask) {
        Integer subtaskUid = subtask.getUid();
        Integer epicUid = subtask.getEpicUid();
        Epic epic = (Epic) taskHashMap.get(epicUid);
        epic.getSubtaskUidSet().remove(subtaskUid);
        taskHashMap.remove(subtask);
        calculateEpicStatus(epic);

    }

    private void deleteEpicByUid(Epic epic) {
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        for (int uid : subtaskUidList) {
            Subtask subtask = (Subtask) getTaskByUid(uid);
            subtask.setEpicUid(null);
        }
        taskHashMap.remove(epic.getUid());
    }

    // Дополнительные методы:
    // Получение списка всех подзадач определённого эпика.
    @Override
    public List<Subtask> getEpicSubtaskList(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        for (int uid : subtaskUidList) {
            subtaskList.add((Subtask) getTaskByUid(uid));
        }
        return subtaskList;
    }

    // Управление статусами осуществляется по следующему правилу:
    // Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    // Для эпиков:
    @Override
    public Epic calculateEpicStatus(Epic epic) {
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        int countSubtask = subtaskUidList.size();
        // если у эпика нет подзадач, то статус должен быть NEW.
        if (countSubtask == 0) {
            epic.setStatus(NEW);
            return epic;
        }
        // если у эпика подзадач есть
        List<Status> statusList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            statusList.add(getTaskByUid(uid).getStatus());
        }

        Map<Status, Integer> statusCount = statusList.stream()
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum));

        if (statusCount.get(IN_PROGRESS) == null) {
            if (statusCount.get(DONE) == null) {
                // если все подзадачи имеют статус NEW, то статус должен быть NEW
                epic.setStatus(NEW);
            } else {
                // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
                epic.setStatus(DONE);
            }
        } else {
            // во всех остальных случаях статус должен быть IN_PROGRESS.
            epic.setStatus(IN_PROGRESS);
        }
        return epic;
    }
}
