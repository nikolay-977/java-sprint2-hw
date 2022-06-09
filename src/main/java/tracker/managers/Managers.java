package tracker.managers;

import java.net.URI;

import static tracker.http.KVServer.PORT;

public class Managers {
    public static TaskManager getDefault() {
        URI url = URI.create("http://localhost:" + PORT);
        return new HTTPTaskManager(url);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
