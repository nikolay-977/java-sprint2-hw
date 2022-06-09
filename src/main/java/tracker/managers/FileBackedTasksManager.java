package tracker.managers;

import tracker.exceptions.ManagerSaveException;
import tracker.model.*;

import java.io.*;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;

import static tracker.model.Task.FORMATTER;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File tasksFile;
    private File tasksHistoryFile;

    private List<Integer> historyIdList = new ArrayList<>();

    public FileBackedTasksManager(URI url) {
    }

    public FileBackedTasksManager(File tasksFile) {
        this.tasksFile = tasksFile;
        this.tasksHistoryFile = new File("history_" + tasksFile.getName());
    }

    @Override
    public List<Task> history() {
        List<Task> taskList = new ArrayList<>();
        if (historyIdList.isEmpty()) {
            return super.history();
        } else {
            for (Integer historyId : historyIdList) {
                taskList.add(tasks.get(historyId));
            }
        }
        return taskList;
    }

    public void setHistoryIdList(List<Integer> historyIdList) {
        this.historyIdList = historyIdList;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    @Override
    public Task addTask(Task task) {
        Task addedTask = super.addTask(task);
        save();
        return addedTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTaskByUid(Integer uid) {
        super.deleteTaskByUid(uid);
        save();
    }

    @Override
    public void deleteEpicByUid(Integer uid) {
        super.deleteEpicByUid(uid);
        save();
    }

    @Override
    public void deleteSubtaskByUid(Integer uid) {
        super.deleteSubtaskByUid(uid);
        save();
    }

    //   метод, который будет восстанавливать данные менеджера из файла при запуске программы
    public static FileBackedTasksManager loadFromFile(File tasksFile) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(tasksFile);
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(tasksFile, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String line = reader.readLine();
                lines.add(line);
            }
        } catch (IOException ignored) {
            throw new ManagerSaveException(String.format("Произошла ошибка во время чтения файла: %s", tasksFile));
        }

        for (int i = 1; i < lines.size(); i++) {
            Task task = fileBackedTasksManager.taskFromString(lines.get(i));
            fileBackedTasksManager.tasks.add(task);
        }

        fileBackedTasksManager.setHistoryIdList(loadFromHistoryFile(fileBackedTasksManager.tasksHistoryFile));

        return fileBackedTasksManager;
    }

    public void save() {
        try (FileWriter writer = new FileWriter(tasksFile, StandardCharsets.UTF_8)) {
            writer.write("id,type,name,status,description,start_time,duration,epic\n");
            for (Task task : tasks) {
                writer.write(toString(task));
            }
        } catch (IOException ignored) {
            throw new ManagerSaveException(String.format("Произошла ошибка во время записи файла: %s", tasksFile));
        }
        saveHistory();
    }

    private void saveHistory() {
        try (FileWriter writer = new FileWriter(tasksHistoryFile, StandardCharsets.UTF_8)) {
            writer.write(toString(historyManager));
        } catch (IOException ignored) {
            throw new ManagerSaveException(String.format("Произошла ошибка во время записи файла: %s", tasksHistoryFile));
        }
    }

    // метод сохранения задачи в строку String toString(Task task)
    private String toString(Task task) {
        return task.toString();
    }

    // метод создания задачи из строки Task fromString(String value)
    private Task taskFromString(String value) {
        Task task;
        String[] taskData = value.split(",");
        Integer id = Integer.valueOf(taskData[0]);
        String type = taskData[1];
        String name = taskData[2];
        String status = taskData[3];
        String description = taskData[4];
        LocalDateTime startTime = LocalDateTime.parse(taskData[5], FORMATTER);
        long duration = Long.valueOf(taskData[6]);
        switch (TaskType.valueOf(type)) {
            case TASK:
                task = new Task(id, name, Status.valueOf(status), description, startTime, duration);
                break;
            case EPIC:
                task = new Epic(id, name, Status.valueOf(status), description, startTime, duration);
                break;
            case SUB_TASK:
                Integer epicId = Integer.valueOf(taskData[7]);
                task = new Subtask(id, name, Status.valueOf(status), description, startTime, duration, epicId);
                break;
            default:
                throw new ManagerSaveException("Неизвестный тип задачи.");
        }
        return task;
    }

    // метод для сохранения менеджера истории в CSV
    private static String toString(HistoryManager manager) {
        List<Task> taskList = manager.getHistory();
        StringBuilder sb = new StringBuilder();
        for (Task task : taskList) {
            sb.append(task.getUid()).append(",");
        }

        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    // метод для восстановления менеджера истории из CSV
    private static List<Integer> fromString(String value) {
        List<Integer> taskList = new ArrayList<>();
        if (!value.isEmpty()) {
            for (String stringId : value.split(",")) {
                taskList.add(Integer.valueOf(stringId));
            }
        }

        return taskList;
    }

    private static List<Integer> loadFromHistoryFile(File tasksHistoryFile) {
        String line = "";
        try (BufferedReader reader = new BufferedReader(new FileReader(tasksHistoryFile, StandardCharsets.UTF_8))) {
            if (reader.ready()) {
                line = reader.readLine();
            }
        } catch (IOException ignored) {
            throw new ManagerSaveException(String.format("Произошла ошибка во время чтения файла: %s", tasksHistoryFile));
        }

        return fromString(line);
    }

    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("tasks.csv"));
        // Заведите несколько разных задач, эпиков и подзадач.
        // две задачи
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1L;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        fileBackedTasksManager.addTask(taskOne);
        LocalDateTime taskTwoStartTime = taskOneStartTime.plusMinutes(2l);
        long taskTwoDuration = 1L;
        Task taskTwo = new Task("Name of task two", "Description of task two", taskTwoStartTime, taskTwoDuration);
        fileBackedTasksManager.addTask(taskTwo);
        // эпик с тремя подзадачами
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        epicOne.setStartTime(taskTwoStartTime.plusDays(1L));
        Epic addedEpicOne = fileBackedTasksManager.addEpic(epicOne);
        LocalDateTime subtaskOneStartTime = taskTwoStartTime.plusMinutes(2l);
        long subtaskOneDuration = 1L;
        Subtask subtaskDraftOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpicOne.getUid());
        Subtask subtaskOne = fileBackedTasksManager.addSubtask(subtaskDraftOne);
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskDraftTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpicOne.getUid());
        Subtask subtaskTwo = fileBackedTasksManager.addSubtask(subtaskDraftTwo);
        LocalDateTime subtaskThreeStartTime = subtaskTwoStartTime.plusMinutes(2l);
        long subtaskThreeDuration = 1L;
        Subtask subtaskDraftThree = new Subtask("Name of subtask three", "Description of subtask three", subtaskThreeStartTime, subtaskThreeDuration, addedEpicOne.getUid());
        Subtask subtaskThree = fileBackedTasksManager.addSubtask(subtaskDraftThree);
        epicOne.setSubtaskUidSet(new HashSet<>(List.of(subtaskOne.getUid(), subtaskTwo.getUid(), subtaskTwo.getUid())));
        fileBackedTasksManager.updateEpic(epicOne);
        // История просмотров задач
        // Запросите некоторые из них, чтобы заполнилась история просмотра
        fileBackedTasksManager.getTaskByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(taskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(taskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(subtaskTwo.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(subtaskDraftOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.getTaskByUid(subtaskThree.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        // удалите задачу, которая есть в истории, и проверьте, что при печати она не будет выводиться;
        fileBackedTasksManager.deleteTaskByUid(taskOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        fileBackedTasksManager.deleteEpicByUid(epicOne.getUid());
        System.out.println("+++ История просмотров задач +++");
        System.out.println(fileBackedTasksManager.history());
        // Создайте новый FileBackedTasksManager менеджер из этого же файла
        FileBackedTasksManager newFileBackedTasksManager = loadFromFile(new File("tasks.csv"));
        // Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи, которые были в старом, есть в новом менеджере
        System.out.println("+++ История просмотров задач +++");
        System.out.println(newFileBackedTasksManager.history());
    }
}
