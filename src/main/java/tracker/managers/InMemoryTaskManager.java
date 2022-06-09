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
    protected List<Task> tasks = new ArrayList<>();
    protected List<Epic> epics = new ArrayList<>();
    protected List<Subtask> subtasks = new ArrayList<>();
    protected HistoryManager historyManager = new InMemoryHistoryManager();

    public InMemoryTaskManager(){}

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


    // Методы для каждого из типа задач(Задача/Эпик/Подзадача):
    // Получение списка всех задач.
    @Override
    public List<Task> getAllTasksList() {
        return tasks;
    }

    @Override
    public List<Epic> getAllEpicsList() {
        return epics;
    }

    @Override
    public List<Subtask> getAllSubtasksList() {
        return subtasks;
    }

    // Удаление всех задач.
    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
    }

    // Получение по идентификатору.
    @Override
    public Task getTaskByUid(Integer uid) {
        Map<Integer, Task> tasksMap = tasks.stream().collect(Collectors.toMap(Task::getUid, Function.identity()));
        if (!tasksMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        }
        Task task = tasksMap.get(uid);
        historyManager.add(task);
        return task;
    }

    @Override
    public Epic getEpicByUid(Integer uid) {
        Map<Integer, Epic> epicsMap = epics.stream().collect(Collectors.toMap(Epic::getUid, Function.identity()));
        if (!epicsMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        } else {
            Epic epic = epicsMap.get(uid);
            historyManager.add(epic);
            return epic;
        }
    }

    @Override
    public Subtask getSubtaskByUid(Integer uid) {
        Map<Integer, Subtask> subtasksMap = subtasks.stream().collect(Collectors.toMap(Subtask::getUid, Function.identity()));
        if (!subtasksMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        } else {
            Subtask subtask = subtasksMap.get(uid);
            historyManager.add(subtask);
            return subtask;
        }
    }

    // Создание. Сам объект должен передаваться в качестве параметра.
    @Override
    public Task addTask(Task task) {
        prioritizedTasks.add(task);
        if (validateIntersections()) {
            prioritizedTasks.remove(task);
            throw new IntersectionException("Пересечение задач");
        } else {
            int uid = ++nextUid;
            task.setUid(uid);
            tasks.add(task);
            return task;
        }
    }

    @Override
    public Epic addEpic(Epic epic) {
        prioritizedTasks.add(epic);
        if (validateIntersections()) {
            prioritizedTasks.remove(epic);
            throw new IntersectionException("Пересечение задач");
        } else {
            int uid = ++nextUid;
            epic.setUid(uid);
            epics.add(epic);
            return epic;
        }
    }

    @Override
    public Subtask addSubtask(Subtask subtask) {
        prioritizedTasks.add(subtask);
        if (validateIntersections()) {
            prioritizedTasks.remove(subtask);
            throw new IntersectionException("Пересечение задач");
        } else {
            int uid = ++nextUid;
            subtask.setUid(uid);
            subtasks.add(subtask);
            return subtask;
        }
    }

    // Обновление. Новая версия объекта с верным идентификатором передаются в виде параметра.
    @Override
    public void updateTask(Task task) {
        Map<Integer, Task> tasksMap = tasks.stream().collect(Collectors.toMap(Task::getUid, Function.identity()));
        if (!tasksMap.containsKey(task.getUid())) {
            throw new IncorrectUidException("Некорректный uid");
        } else {
            prioritizedTasks.add(task);
            if (validateIntersections()) {
                prioritizedTasks.remove(task);
            } else {
                int indexOfItem = -1;

                for (Task taskItem : tasks) {
                    if (taskItem.getUid().equals(taskItem.getUid())) {
                        indexOfItem = tasks.indexOf(taskItem);
                    }
                }

                tasks.remove(indexOfItem);
                tasks.add(task);
            }
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        Map<Integer, Epic> epicsMap = epics.stream().collect(Collectors.toMap(Epic::getUid, Function.identity()));
        if (!epicsMap.containsKey(epic.getUid())) {
            throw new IncorrectUidException("Некорректный uid");
        } else {
            epic = calculateEpicStatus(epic);

            int indexOfItem = -1;

            for (Epic epicItem : epics) {
                if (epicItem.getUid().equals(epic.getUid())) {
                    indexOfItem = epics.indexOf(epicItem);
                }
            }

            this.epics.remove(indexOfItem);
            this.epics.add(epic);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        Map<Integer, Subtask> subtasksMap = subtasks.stream().collect(Collectors.toMap(Subtask::getUid, Function.identity()));
        if (!subtasksMap.containsKey(subtask.getUid())) {
            throw new IncorrectUidException("Некорректный uid");
        } else {
            prioritizedTasks.add(subtask);
            if (validateIntersections()) {
                prioritizedTasks.remove(subtask);
                throw new IntersectionException("Пересечение задач");
            } else {
                Map<Integer, Subtask> subtaskMap = subtasks.stream().collect(Collectors.toMap(Subtask::getUid, Function.identity()));
                int indexOfSubtaskItem = -1;

                for (Subtask subtaskItem : subtasks) {
                    if (subtaskItem.getUid().equals(subtask.getUid())) {
                        indexOfSubtaskItem = subtasks.indexOf(subtaskItem);
                    }
                }

                subtasks.remove(indexOfSubtaskItem);
                subtasks.add(subtask);

                Map<Integer, Epic> epicsMap = epics.stream().collect(Collectors.toMap(Epic::getUid, Function.identity()));

                Integer epicUid = subtask.getEpicUid();
                Epic epic = epicsMap.get(epicUid);

                if (epic != null) {
                    calculateEpicStatus(epic);

                    int indexOfEpicItem = -1;

                    for (Epic epicItem : epics) {
                        if (epicItem.getUid().equals(epic.getUid())) {
                            indexOfEpicItem = epics.indexOf(epicItem);
                        }
                    }

                    epics.remove(indexOfEpicItem);
                    epics.add(epic);
                }
            }
        }
    }

    // Удаление по идентификатору.
    @Override
    public void deleteTaskByUid(Integer uid) {
        Map<Integer, Task> tasksMap = tasks.stream().collect(Collectors.toMap(Task::getUid, Function.identity()));
        if (!tasksMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        }
        removeTaskByUid(uid);
    }

    @Override
    public void deleteEpicByUid(Integer uid) {
        Map<Integer, Epic> epicsMap = epics.stream().collect(Collectors.toMap(Epic::getUid, Function.identity()));
        if (!epicsMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        } else {
            Epic epic = epicsMap.get(uid);
            Set<Integer> subtaskUidList = epic.getSubtaskUidSet();
            for (Integer subTaskUid : subtaskUidList) {
                removeSubtaskByUid(subTaskUid);
            }
            removeEpicByUid(epic.getUid());
        }
    }

    @Override
    public void deleteSubtaskByUid(Integer uid) {
        Map<Integer, Subtask> subtasksMap = subtasks.stream().collect(Collectors.toMap(Subtask::getUid, Function.identity()));
        if (!subtasksMap.containsKey(uid)) {
            throw new IncorrectUidException("Некорректный uid");
        }
        Map<Integer, Epic> epicsMap = epics.stream().collect(Collectors.toMap(Epic::getUid, Function.identity()));
        Subtask subtask = getSubtaskByUid(uid);
        Epic epic = epicsMap.get(subtask.getEpicUid());
        if (epic != null) {
            epic.getSubtaskUidSet().remove(subtask);
            calculateEpicStatus(epic);
        }
        removeSubtaskByUid(subtask.getUid());
    }

    // Дополнительные методы:
    // Получение списка всех подзадач определённого эпика.
    @Override
    public List<Subtask> getSubtaskOfEpic(Epic epic) {
        List<Subtask> subtaskList = new ArrayList<>();
        Set<Integer> subtaskUidList = epic.getSubtaskUidSet();
        for (int uid : subtaskUidList) {
            subtaskList.add(getSubtaskByUid(uid));
        }
        return subtaskList;
    }

    // Управление статусами осуществляется по следующему правилу:
    // Менеджер сам не выбирает статус для задачи. Информация о нём приходит менеджеру вместе с информацией о самой задаче. По этим данным в одних случаях он будет сохранять статус, в других будет рассчитывать.
    // Для эпиков:
    @Override
    public Epic calculateEpicStatus(Epic epic) {
        Set<Integer> subtaskUidList = epic.getSubtaskUidSet();
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
            statusList.add(getSubtaskByUid(uid).getStatus());
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
        Set<Integer> subtaskUidList = epic.getSubtaskUidSet();
        List<Long> durationList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            durationList.add(getSubtaskByUid(uid).getDuration());
        }

        epic.setDuration(durationList.stream().mapToLong(i -> i).sum());
        return epic;
    }

    private Epic setStartTime(Epic epic) {
        Set<Integer> subtaskUidList = epic.getSubtaskUidSet();
        List<LocalDateTime> startTimeList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            startTimeList.add(getSubtaskByUid(uid).getStartTime());
        }

        try {
            LocalDateTime startTime = startTimeList.stream().min(LocalDateTime::compareTo).get();
            epic.setStartTime(startTime);
        } catch (NoSuchElementException e) {
            System.out.println("Коллекция startTimeList пустая");
        }
        return epic;
    }

    private Epic setEndTime(Epic epic) {
        Set<Integer> subtaskUidList = epic.getSubtaskUidSet();
        List<LocalDateTime> startTimeList = new ArrayList<>();

        for (Integer uid : subtaskUidList) {
            startTimeList.add(getSubtaskByUid(uid).getEndTime());
        }

        try {
            LocalDateTime startTime = startTimeList.stream().max(LocalDateTime::compareTo).get();
            epic.setEndTime(startTime);
        } catch (NoSuchElementException e) {
            System.out.println("Коллекция startTimeList пустая");
        }
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

    private void removeTaskByUid(Integer uid) {
        Map<Integer, Task> tasksMap = tasks.stream().collect(Collectors.toMap(Task::getUid, Function.identity()));
        Task task = tasksMap.get(uid);
        prioritizedTasks.remove(task);
        tasks.remove(task);
        historyManager.remove(uid);
    }

    private void removeEpicByUid(Integer uid) {
        Map<Integer, Epic> subtaskMap = epics.stream().collect(Collectors.toMap(Epic::getUid, Function.identity()));
        Epic epic = subtaskMap.get(uid);
        prioritizedTasks.remove(epic);
        epics.remove(epic);
        historyManager.remove(uid);
    }

    private void removeSubtaskByUid(Integer uid) {
        Map<Integer, Subtask> subtaskMap = subtasks.stream().collect(Collectors.toMap(Subtask::getUid, Function.identity()));
        Subtask subtask = subtaskMap.get(uid);
        prioritizedTasks.remove(subtask);
        subtasks.remove(subtask);
        historyManager.remove(uid);
    }


    protected void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    protected void setEpics(List<Epic> epics) {
        this.epics = epics;
    }

    protected void setSubtasks(List<Subtask> subtasks) {
        this.subtasks = subtasks;
    }

    protected void setPrioritizedTasks(TreeSet<Task> prioritizedTasks) {
        this.prioritizedTasks = prioritizedTasks;
    }
}
