package tracker.http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import tracker.managers.TaskManager;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.*;

public class HttpTaskServer {
    private final TaskManager manager;
    private final HttpServer server;
    public static final int PORT = 8081;
    private Gson gson = new Gson();

    public HttpTaskServer(TaskManager httpManager) throws IOException {
        this.manager = httpManager;
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.server.createContext("/tasks/task", taskHandler("/tasks/task/"));
        this.server.createContext("/tasks/epic", epicHandler("/tasks/epic/"));
        this.server.createContext("/tasks/subtask", subtaskHandler("/tasks/subtask/"));
        this.server.createContext("/tasks/subtask/epic", subtasksOfEpicHandler("/tasks/subtask/epic/?id="));
        this.server.createContext("/tasks/history", historyHandler("/tasks/history/"));
        this.server.createContext("/tasks", prioritizedHandler("/tasks/"));
    }

    private HttpHandler taskHandler(String request) {
        return (h) -> {
            try {
                String id;
                switch (h.getRequestMethod()) {
                    case "GET":
                        id = h.getRequestURI().toString().substring(request.length());
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        if (id.isEmpty()) {
                            String response = gson.toJson(manager.getAllTasksList(), new TypeToken<List<Task>>() {
                            }.getType());
                            sendText(h, response);
                        } else {
                            Task task = manager.getTaskByUid(Integer.parseInt(id));
                            String response = gson.toJson(task, Task.class);
                            sendText(h, response);
                        }
                        break;
                    case "POST":
                        id = h.getRequestURI().toString().substring(request.length());
                        String value = readText(h);
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        Task task = gson.fromJson(value, Task.class);
                        if (id.isEmpty()) {
                            Task addedTask = manager.addTask(task);
                            String response = gson.toJson(addedTask, Task.class);
                            sendText(h, response);
                        } else {
                            int uid = Integer.parseInt(id);
                            task.setUid(uid);
                            manager.updateTask(task);
                            String response = gson.toJson(task, Task.class);
                            sendText(h, response);
                        }
                        break;
                    case "DELETE":
                        id = h.getRequestURI().toString().substring(request.length());
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        if (id.isEmpty()) {
                            manager.deleteAllTasks();
                            sendText(h, "");
                        } else {
                            manager.deleteTaskByUid(Integer.parseInt(id));
                            sendText(h, "");
                        }
                        break;
                    default:
                        System.out.println("Неизвестный тип запроса: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                h.close();
            }
        };
    }

    private HttpHandler epicHandler(String request) {
        return (h) -> {
            try {
                String id;
                switch (h.getRequestMethod()) {
                    case "GET":
                        id = h.getRequestURI().toString().substring(request.length());
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        if (id.isEmpty()) {
                            String response = gson.toJson(manager.getAllTasksList(), new TypeToken<List<Task>>() {
                            }.getType());
                            sendText(h, response);
                        } else {
                            Epic epic = manager.getEpicByUid(Integer.parseInt(id));
                            String response = gson.toJson(epic, Epic.class);
                            sendText(h, response);
                        }
                        break;
                    case "POST":
                        id = h.getRequestURI().toString().substring(request.length());
                        String value = readText(h);
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        Epic epic = gson.fromJson(value, Epic.class);
                        if (id.isEmpty()) {
                            Epic addedEpic = manager.addEpic(epic);
                            String response = gson.toJson(addedEpic, Epic.class);
                            sendText(h, response);
                        } else {
                            int uid = Integer.parseInt(id);
                            epic.setUid(uid);
                            manager.updateEpic(epic);
                            String response = gson.toJson(epic, Epic.class);
                            sendText(h, response);
                        }
                        break;
                    case "DELETE":
                        id = h.getRequestURI().toString().substring(request.length());
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        if (id.isEmpty()) {
                            manager.deleteAllEpics();
                        } else {
                            manager.deleteEpicByUid(Integer.parseInt(id));
                        }
                        sendText(h, "");
                        break;
                    default:
                        System.out.println("Неизвестный тип запроса: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                h.close();
            }
        };
    }

    private HttpHandler subtaskHandler(String request) {
        return (h) -> {
            try {
                String id;
                switch (h.getRequestMethod()) {
                    case "GET":
                        id = h.getRequestURI().toString().substring(request.length());
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        if (id.isEmpty()) {
                            String response = gson.toJson(manager.getAllTasksList(), new TypeToken<List<Task>>() {
                            }.getType());
                            sendText(h, response);
                        } else {
                            Subtask subtask = manager.getSubtaskByUid(Integer.parseInt(id));
                            String response = gson.toJson(subtask, Subtask.class);
                            sendText(h, response);
                        }
                        break;
                    case "POST":
                        id = h.getRequestURI().toString().substring(request.length());
                        String value = readText(h);
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        Subtask subtask = gson.fromJson(value, Subtask.class);
                        if (id.isEmpty()) {
                            Subtask addedSubtask = manager.addSubtask(subtask);
                            String response = gson.toJson(addedSubtask, Subtask.class);
                            sendText(h, response);
                        } else {
                            int uid = Integer.parseInt(id);
                            subtask.setUid(uid);
                            manager.updateSubtask(subtask);
                            String response = gson.toJson(subtask, Subtask.class);
                            sendText(h, response);
                        }
                        break;
                    case "DELETE":
                        id = h.getRequestURI().toString().substring(request.length());
                        if (id.startsWith("?id=")) {
                            id = id.substring("?id=".length());
                        }
                        if (id.isEmpty()) {
                            manager.deleteAllSubtasks();
                            sendText(h, "");
                        } else {
                            manager.deleteSubtaskByUid(Integer.parseInt(id));
                            sendText(h, "");
                        }
                        break;
                    default:
                        System.out.println("Неизвестный тип запроса: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                h.close();
            }
        };
    }

    private HttpHandler subtasksOfEpicHandler(String request) {
        return (h) -> {
            try {
                switch (h.getRequestMethod()) {
                    case "GET":
                        String id = h.getRequestURI().toString().substring(request.length());
                        int uid = Integer.parseInt(id);
                        Epic epic = manager.getEpicByUid(uid);
                        List<Subtask> subtasks = manager.getSubtaskOfEpic(epic);
                        String response = gson.toJson(subtasks, new TypeToken<List<Subtask>>() {
                        }.getType());
                        sendText(h, response);
                        break;
                    default:
                        System.out.println("Неизвестный тип запроса: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                h.close();
            }
        };
    }

    private HttpHandler historyHandler(String request) {
        return (h) -> {
            try {
                switch (h.getRequestMethod()) {
                    case "GET":
                        List<Task> taskList = manager.history();
                        String response = gson.toJson(taskList, new TypeToken<List<Task>>() {
                        }.getType());
                        sendText(h, response);
                        break;
                    default:
                        System.out.println("Неизвестный тип запроса: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                h.close();
            }
        };
    }

    private HttpHandler prioritizedHandler(String request) {
        return (h) -> {
            try {
                switch (h.getRequestMethod()) {
                    case "GET":
                        Set<Task> taskList = manager.getPrioritizedTasks();
                        String response = gson.toJson(taskList, new TypeToken<Set<Task>>() {
                        }.getType());
                        sendText(h, response);
                        break;
                    default:
                        System.out.println("Неизвестный тип запроса: " + h.getRequestMethod());
                        h.sendResponseHeaders(405, 0);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                h.close();
            }
        };
    }

    protected String readText(HttpExchange h) throws IOException {
        List<Byte> listBytes = new ArrayList<Byte>();
        int bodyBytes;
        while ((bodyBytes = h.getRequestBody().read()) != -1) {
            listBytes.add((byte) bodyBytes);
        }
        byte[] bytes = new byte[listBytes.size()];
        Iterator<Byte> iterator = listBytes.iterator();
        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = iterator.next();
        }
        return new String(bytes, "UTF-8");
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        byte[] resp = text.getBytes("UTF-8");
        h.getResponseHeaders().add("Content-Type", "application/json");
        h.sendResponseHeaders(200, resp.length);
        h.getResponseBody().write(resp);
    }

    public void start() {
        server.start();
        System.out.println("Запущен сервер HTTPTaskServer на порту " + PORT);
    }

    public void stop() {
        server.stop(1);
        System.out.println("Остановлен сервер HTTPTaskServer на порту " + PORT);
    }
}
