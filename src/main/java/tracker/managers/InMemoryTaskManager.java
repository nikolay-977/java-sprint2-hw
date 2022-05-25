package tracker.managers;

import tracker.exceptions.IncorrectUidException;
import tracker.exceptions.IntersectionException;
import tracker.model.Epic;
import tracker.model.Status;
import tracker.model.Subtask;
import tracker.model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static tracker.model.Status.*;

public class InMemoryTaskManager implements TaskManager {
    private static Integer nextUid = 0;
    // Возможность хранить задачи всех типов. Для этого вам нужно выбрать подходящую коллекцию.
    protected final HashMap<Integer, Task> taskHashMap = new HashMap<>();
    protected final HistoryManager historyManager = new InMemoryHistoryManager();

    public HistoryManager getHistoryManager() {
        return historyManager;
    }

    private TreeSet<Task> prioritizedTasks = new TreeSet<>(new Comparator<Task>() {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getStartTime() == null && task2.getStartTime() == null) {
                return 0;
            }
            if (task1.getStartTime() == null && task2.getStartTime() != null) {
                return 1;
            }
            if (task1.getStartTime() != null && task2.getStartTime() == null) {
                return -1;
            }
            return task1.getStartTime().compareTo(task2.getStartTime());
        }
    });

    private void updateEpic(Integer epicUid, Epic epic) {
        epic = calculateEpicStatus(epic);
        taskHashMap.put(epicUid, epic);
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
    }

    // Получение по идентификатору.
    @Override
    public Task getTaskByUid(Integer uid) {
        if (!taskHashMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        }
        Task task = taskHashMap.get(uid);
        historyManager.add(task);
        return task;
    }

    // Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public Integer createTask(Task task) {
        prioritizedTasks.add(task);
        if (validateIntersections()) {
            prioritizedTasks.remove(task);
            throw new IntersectionException("Пересечение задач");
        } else {
            int uid = ++nextUid;
            task.setUid(uid);
            taskHashMap.put(uid, task);
            return uid;
        }
    }

    // Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
    @Override
    public void update(Integer uid, Task task) {
        if (!taskHashMap.containsKey(uid)) {
            throw new IllegalArgumentException("Некорректный uid");
        }
        if (task instanceof Subtask) {
            updateSubTask(uid, (Subtask) task);
        } else if (task instanceof Epic) {
            updateEpic(uid, (Epic) task);
        } else {
            updateTask(uid, task);
        }
    }

    // Удаление по идентификатору.
    @Override
    public void deleteByUid(Integer uid) {
        if (!taskHashMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        }
        Task task = getTaskByUid(uid);
        if (task instanceof Subtask) {
            deleteSubtaskByUid((Subtask) task);
        } else if (task instanceof Epic) {
            deleteEpicByUid((Epic) task);
        } else {
            taskHashMap.remove(uid);
            historyManager.remove(uid);
        }
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
            epic.setDuration(0l);
            return epic;
        }
        // если у эпика подзадачи есть
        List<Status> statusList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            statusList.add(getTaskByUid(uid).getStatus());
        }

        Map<Status, Integer> statusCount = statusList.stream()
                .collect(Collectors.toMap(Function.identity(), v -> 1, Integer::sum));
        int statusCountSize = statusCount.size();

        if (statusCountSize == 1) {
            // если все подзадачи имеют статус NEW, то статус должен быть NEW
            // если все подзадачи имеют статус DONE, то и эпик считается завершённым — со статусом DONE.
            epic.setStatus(statusList.get(0));
        } else {
            // во всех остальных случаях статус должен быть IN_PROGRESS.
            epic.setStatus(IN_PROGRESS);
        }

        epic = calculateEpicDuration(epic);
        epic = setStartTime(epic);
        epic = setEndTime(epic);
        return epic;
    }

    private Epic calculateEpicDuration(Epic epic) {
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        List<Long> durationList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            durationList.add(getTaskByUid(uid).getDuration());
        }

        epic.setDuration(durationList.stream().mapToLong(i -> i).sum());
        return epic;
    }

    private Epic setStartTime(Epic epic) {
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        List<LocalDateTime> startTimeList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            startTimeList.add(getTaskByUid(uid).getStartTime());
        }

        LocalDateTime startTime = startTimeList.stream().min(LocalDateTime::compareTo).get();
        epic.setStartTime(startTime);

        return epic;
    }

    private Epic setEndTime(Epic epic) {
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        List<LocalDateTime> startTimeList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            startTimeList.add(getTaskByUid(uid).getEndTime());
        }

        LocalDateTime startTime = startTimeList.stream().max(LocalDateTime::compareTo).get();
        epic.setEndTime(startTime);

        return epic;
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public Set<Task> getPrioritizedTasks() {
        return prioritizedTasks;
    }

    @Override
    public boolean validateIntersections() {
        boolean result = false;
        Task previousTask = null;
        for (Task task : prioritizedTasks) {
            if (previousTask == null) {
                previousTask = task;
                continue;
            }
            if (previousTask.getEndTime() == null || task.getStartTime() == null) {
                continue;
            }
            if (previousTask.getEndTime().isAfter(task.getStartTime())) {
                result = true;
                break;
            }
            previousTask = task;
        }
        return result;
    }

    private void updateTask(Integer tasUid, Task task) {
        prioritizedTasks.add(task);
        if (validateIntersections()) {
            prioritizedTasks.remove(task);
        } else {
            taskHashMap.put(tasUid, task);
        }
    }

    private void updateSubTask(Integer subTaskUid, Subtask subtask) {
        prioritizedTasks.add(subtask);
        if (validateIntersections()) {
            prioritizedTasks.remove(subtask);
            throw new IntersectionException("Пересечение задач");
        } else {
            taskHashMap.put(subTaskUid, subtask);
            Integer epicUid = subtask.getEpicUid();
            Epic epic = (Epic) taskHashMap.get(epicUid);
            calculateEpicStatus(epic);
            taskHashMap.put(epicUid, epic);
        }
    }

    private void deleteSubtaskByUid(Subtask subtask) {
        Epic epic = (Epic) taskHashMap.get(subtask.getEpicUid());
        Integer subtaskUid = subtask.getUid();
        epic.getSubtaskUidSet().remove(subtaskUid);
        removeTaskByUid(subtaskUid);
        calculateEpicStatus(epic);
    }

    private void deleteEpicByUid(Epic epic) {
        HashSet<Integer> subtaskUidList = epic.getSubtaskUidSet();
        for (Integer uid : subtaskUidList) {
            removeTaskByUid(uid);
        }
        removeTaskByUid(epic.getUid());
    }

    private void removeTaskByUid(Integer uid) {
        prioritizedTasks.remove(getTaskHashMap().get(uid));
        taskHashMap.remove(uid);
        historyManager.remove(uid);
    }
}
