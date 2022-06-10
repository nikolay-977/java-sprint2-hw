package tracker.managers;

import java.net.URI;

import static tracker.http.KVServer.PORT;

public class Managers {
    public static TaskManager getDefault() {
        return new HTTPTaskManager(URI.create("http://localhost:" + PORT));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
