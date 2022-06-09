package tracker.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tracker.managers.Managers;
import tracker.model.Epic;
import tracker.model.Subtask;
import tracker.model.Task;
import tracker.http.HttpTaskServer;
import tracker.http.KVServer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static tracker.http.HttpTaskServer.PORT;

public class HttpTaskServerTest {
    URI serverURL = URI.create("http://localhost:" + PORT);
    Gson gson = new Gson();
    HttpTaskServer httpTaskServer;
    KVServer kvServer;
    HttpClient client;

    @BeforeEach
    void start() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
        httpTaskServer = new HttpTaskServer(Managers.getDefault());
        httpTaskServer.start();
        client = HttpClient.newHttpClient();
    }

    @AfterEach
    void stop() {
        if (httpTaskServer != null) {
            httpTaskServer.stop();
        }

        if (kvServer != null) {
            kvServer.stop();
        }
    }

    @Test
    void addTask() throws IOException, InterruptedException {
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1L;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        HttpResponse<String> response = addTask(taskOne);
        assertEquals(200, response.statusCode());
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        Task taskOne = new Task("Name of task one", "Description of task one");
        HttpResponse<String> createTaskResponse = addTask(taskOne);
        Task addedTask = gson.fromJson(createTaskResponse.body(), Task.class);
        String updatedName = "Name of task one updated";
        addedTask.setName(updatedName);
        updateTask(addedTask);
        HttpResponse<String> updateResponse = getTask(addedTask.getUid());
        Task updateTask = gson.fromJson(updateResponse.body(), Task.class);
        assertEquals(200, updateResponse.statusCode());
        assertEquals(updatedName, updateTask.getName());
    }

    @Test
    void getTask() throws IOException, InterruptedException {
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1L;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        HttpResponse<String> createTaskResponse = addTask(taskOne);
        Task addedTask = gson.fromJson(createTaskResponse.body(), Task.class);
        URI url = URI.create(serverURL + "/tasks/task/?id=" + addedTask.getUid());
        HttpRequest getTaskByUidRequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getTaskByUidResponse = client.send(getTaskByUidRequest, HttpResponse.BodyHandlers.ofString());
        Task getTask = gson.fromJson(getTaskByUidResponse.body(), Task.class);
        assertEquals(200, getTaskByUidResponse.statusCode());
        assertEquals(addedTask, getTask);
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        LocalDateTime taskOneStartTime = LocalDateTime.now();
        long taskOneDuration = 1L;
        Task taskOne = new Task("Name of task one", "Description of task one", taskOneStartTime, taskOneDuration);
        HttpResponse<String> createTaskResponse = addTask(taskOne);
        Task addedTask = gson.fromJson(createTaskResponse.body(), Task.class);
        HttpResponse<String> deleteTaskResponse = deleteTask(addedTask.getUid());
        assertEquals(200, deleteTaskResponse.statusCode());
    }

    @Test
    void addSubtask() throws IOException, InterruptedException {
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, null);
        HttpResponse<String> response = addTask(subtaskOne);
        assertEquals(200, response.statusCode());
    }

    @Test
    void updateSubtask() throws IOException, InterruptedException {
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", null);
        HttpResponse<String> createSubtaskResponse = addSubtask(subtaskOne);
        Subtask addedSubtask = gson.fromJson(createSubtaskResponse.body(), Subtask.class);
        String updatedName = "Name of task one updated";
        addedSubtask.setName(updatedName);
        updateSubtask(addedSubtask);
        HttpResponse<String> updateResponse = getSubtask(addedSubtask.getUid());
        Subtask updateSubtask = gson.fromJson(updateResponse.body(), Subtask.class);
        assertEquals(200, updateResponse.statusCode());
        assertEquals(updatedName, updateSubtask.getName());
    }

    @Test
    void getSubtask() throws IOException, InterruptedException {
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, null);
        HttpResponse<String> createSubtaskResponse = addTask(subtaskOne);
        Subtask addedSubtask = gson.fromJson(createSubtaskResponse.body(), Subtask.class);
        URI url = URI.create(serverURL + "/tasks/task/?id=" + addedSubtask.getUid());
        HttpRequest getSubtaskByUidRequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getSubtaskByUidResponse = client.send(getSubtaskByUidRequest, HttpResponse.BodyHandlers.ofString());
        Subtask getSubtask = gson.fromJson(getSubtaskByUidResponse.body(), Subtask.class);
        assertEquals(200, getSubtaskByUidResponse.statusCode());
        assertEquals(addedSubtask, getSubtask);
    }

    @Test
    void deleteSubtask() throws IOException, InterruptedException {
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, null);
        HttpResponse<String> createSubtaskResponse = addSubtask(subtaskOne);
        Subtask addedSubtask = gson.fromJson(createSubtaskResponse.body(), Subtask.class);
        HttpResponse<String> deleteSubtaskResponse = deleteSubtask(addedSubtask.getUid());
        assertEquals(200, deleteSubtaskResponse.statusCode());
    }

    @Test
    void addEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of task", "Description of task");
        HttpResponse<String> response = addEpic(epic);
        assertEquals(200, response.statusCode());
    }

    @Test
    void updateEpic() throws IOException, InterruptedException {
        Epic epicOne = new Epic("Name of epic one", "Description of epic one");
        HttpResponse<String> createEpicResponse = addEpic(epicOne);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        String updatedName = "Name of epic one updated";
        addedEpic.setName(updatedName);
        updateEpic(addedEpic);
        HttpResponse<String> updateResponse = getEpic(addedEpic.getUid());
        Epic updateEpic = gson.fromJson(updateResponse.body(), Epic.class);
        assertEquals(200, updateResponse.statusCode());
        assertEquals(updatedName, updateEpic.getName());
    }

    @Test
    void getEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        HttpResponse<String> getEpicByUidResponse = getEpic(addedEpic.getUid());
        Epic getEpic = gson.fromJson(getEpicByUidResponse.body(), Epic.class);
        assertEquals(200, getEpicByUidResponse.statusCode());
        assertEquals(addedEpic, getEpic);
    }

    @Test
    void deleteEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addTask(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        HttpResponse<String> deleteEpicResponse = deleteTask(addedEpic.getUid());
        assertEquals(200, deleteEpicResponse.statusCode());
    }

    @Test
    void addEpicWithSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskOneResponse = addTask(subtaskOne);
        Subtask addedSubtaskOne = gson.fromJson(createSubtaskOneResponse.body(), Subtask.class);
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskTwoResponse = addTask(subtaskTwo);
        Subtask addedSubtaskTwo = gson.fromJson(createSubtaskTwoResponse.body(), Subtask.class);
        epic.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid())));
        HttpResponse<String> updateResponse = updateEpic(addedEpic);
        assertEquals(200, createEpicResponse.statusCode());
        assertEquals(200, createSubtaskOneResponse.statusCode());
        assertEquals(200, createSubtaskTwoResponse.statusCode());
        assertEquals(200, updateResponse.statusCode());
    }

    @Test
    void getSubtasksOfEpic() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskOneResponse = addSubtask(subtaskOne);
        Subtask addedSubtaskOne = gson.fromJson(createSubtaskOneResponse.body(), Subtask.class);
        subtaskOne.setUid(addedSubtaskOne.getUid());
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskTwoResponse = addSubtask(subtaskTwo);
        Subtask addedSubtaskTwo = gson.fromJson(createSubtaskTwoResponse.body(), Subtask.class);
        subtaskTwo.setUid(addedSubtaskTwo.getUid());
        addedEpic.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid())));
        updateEpic(addedEpic);
        URI url = URI.create(serverURL + "/tasks/subtask/epic/?id=" + addedEpic.getUid());
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getSubtasksOfEpicResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> list = gson.fromJson(getSubtasksOfEpicResponse.body(), new TypeToken<List<Subtask>>() {
        }.getType());
        assertEquals(200, getSubtasksOfEpicResponse.statusCode());
        assertEquals(List.of(subtaskOne, subtaskTwo), list);
    }

    @Test
    void getAllTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskOneResponse = addSubtask(subtaskOne);
        Subtask addedSubtaskOne = gson.fromJson(createSubtaskOneResponse.body(), Subtask.class);
        subtaskOne.setUid(addedSubtaskOne.getUid());
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskTwoResponse = addSubtask(subtaskTwo);
        Subtask addedSubtaskTwo = gson.fromJson(createSubtaskTwoResponse.body(), Subtask.class);
        subtaskTwo.setUid(addedSubtaskTwo.getUid());
        epic.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid())));
        updateEpic(addedEpic);
        HttpResponse<String> updatedEpicResponse = getEpic(addedEpic.getUid());
        Epic updatedEpic = gson.fromJson(updatedEpicResponse.body(), Epic.class);
        URI url = URI.create(serverURL + "/tasks/task/");
        HttpRequest getAllTasksRequest = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getAllTasksResponse = client.send(getAllTasksRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getAllTasksResponse.statusCode());
    }

    @Test
    void deleteAllTasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskOneResponse = addSubtask(subtaskOne);
        Subtask addedSubtaskOne = gson.fromJson(createSubtaskOneResponse.body(), Subtask.class);
        subtaskOne.setUid(addedSubtaskOne.getUid());
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 1L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskTwoResponse = addSubtask(subtaskTwo);
        Subtask addedSubtaskTwo = gson.fromJson(createSubtaskTwoResponse.body(), Subtask.class);
        subtaskTwo.setUid(addedSubtaskTwo.getUid());
        epic.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid())));
        updateEpic(addedEpic);
        URI url = URI.create(serverURL + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> deleteAllTasksResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, deleteAllTasksResponse.statusCode());
        assertTrue(deleteAllTasksResponse.body().isEmpty());
    }

    @Test
    void history() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskOneResponse = addSubtask(subtaskOne);
        Subtask addedSubtaskOne = gson.fromJson(createSubtaskOneResponse.body(), Subtask.class);
        subtaskOne.setUid(addedSubtaskOne.getUid());
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 3L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskTwoResponse = addSubtask(subtaskTwo);
        Subtask addedSubtaskTwo = gson.fromJson(createSubtaskTwoResponse.body(), Subtask.class);
        subtaskTwo.setUid(addedSubtaskTwo.getUid());
        epic.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid())));
        updateEpic(addedEpic);
        getSubtask(addedSubtaskTwo.getUid());
        getEpic(addedEpic.getUid());
        getSubtask(addedSubtaskOne.getUid());
        getSubtask(addedSubtaskOne.getUid());

        URI url = URI.create(serverURL + "/tasks/history/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getHistoryResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> history = gson.fromJson(getHistoryResponse.body(), List.class);
        assertEquals(200, getHistoryResponse.statusCode());
        assertNotNull(history);
    }

    @Test
    void prioritizedTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Name of epic", "Description of epic");
        HttpResponse<String> createEpicResponse = addEpic(epic);
        Epic addedEpic = gson.fromJson(createEpicResponse.body(), Epic.class);
        LocalDateTime subtaskOneStartTime = LocalDateTime.now();
        long subtaskOneDuration = 1L;
        Subtask subtaskOne = new Subtask("Name of subtask one", "Description of subtask one", subtaskOneStartTime, subtaskOneDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskOneResponse = addSubtask(subtaskOne);
        Subtask addedSubtaskOne = gson.fromJson(createSubtaskOneResponse.body(), Subtask.class);
        subtaskOne.setUid(addedSubtaskOne.getUid());
        LocalDateTime subtaskTwoStartTime = subtaskOneStartTime.plusMinutes(2l);
        long subtaskTwoDuration = 3L;
        Subtask subtaskTwo = new Subtask("Name of subtask two", "Description of subtask two", subtaskTwoStartTime, subtaskTwoDuration, addedEpic.getUid());
        HttpResponse<String> createSubtaskTwoResponse = addSubtask(subtaskTwo);
        Subtask addedSubtaskTwo = gson.fromJson(createSubtaskTwoResponse.body(), Subtask.class);
        subtaskTwo.setUid(addedSubtaskTwo.getUid());
        epic.setSubtaskUidSet(new HashSet<>(List.of(addedSubtaskOne.getUid(), addedSubtaskTwo.getUid())));
        updateEpic(addedEpic);
        URI url = URI.create(serverURL + "/tasks/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> getHistoryResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, getHistoryResponse.statusCode());
    }

    private HttpResponse<String> addTask(Task task) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/task/");
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getTask(Integer uid) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/task/?id=" + uid);
        HttpRequest getTaskByUidRequest = HttpRequest.newBuilder().uri(url).GET().build();
        return client.send(getTaskByUidRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> updateTask(Task task) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/task/?id=" + task.getUid());
        String json = gson.toJson(task);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteTask(Integer uid) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/task/?id=" + uid);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> addEpic(Epic epic) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/epic/");
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getEpic(Integer uid) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/epic/?id=" + uid);
        HttpRequest getTaskByUidRequest = HttpRequest.newBuilder().uri(url).GET().build();
        return client.send(getTaskByUidRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> updateEpic(Epic epic) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/epic/?id=" + epic.getUid());
        String json = gson.toJson(epic);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteEpic(Integer uid) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/epic/?id=" + uid);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteAllTask() throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/task/");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> addSubtask(Subtask subtask) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/subtask/");
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> getSubtask(Integer uid) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/subtask/?id=" + uid);
        HttpRequest getTaskByUidRequest = HttpRequest.newBuilder().uri(url).GET().build();
        return client.send(getTaskByUidRequest, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> updateSubtask(Subtask subtask) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/subtask/?id=" + subtask.getUid());
        String json = gson.toJson(subtask);
        final HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> deleteSubtask(Integer uid) throws IOException, InterruptedException {
        URI url = URI.create(serverURL + "/tasks/subtask/?id=" + uid);
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
