package tracker.managers;

import com.google.gson.Gson;
import tracker.http.KVTaskClient;

import java.io.IOException;
import java.net.URI;

public class HTTPTaskManager extends FileBackedTasksManager {
    private final KVTaskClient client;
    private String apiKey;

    public HTTPTaskManager(URI url) {
        super(url);
        this.client = new KVTaskClient(url);
        client.register();
        this.apiKey = client.getApiToken();
    }

    @Override
    public void save() {
        Gson gson = new Gson();
        StateManager stateManager = new StateManager();
        stateManager.setTasks(getAllTasksList());
        stateManager.setEpics(getAllEpicsList());
        stateManager.setSubtasks(getAllSubtasksList());
        stateManager.setListHistory(history());
        stateManager.setPrioritizedTasks(getPrioritizedTasks());
        String state = gson.toJson(stateManager);

        try {
            client.save(apiKey, state);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

