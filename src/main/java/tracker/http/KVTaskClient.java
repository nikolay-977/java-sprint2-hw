package tracker.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.MessageFormat;

public class KVTaskClient {
    private final HttpClient client = HttpClient.newHttpClient();
    private final URI url;
    private String apiToken;

    public KVTaskClient(URI url) {
        this.url = url;
    }

    public String getApiToken() {
        return apiToken;
    }

    public void register() {
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url + "/register")).GET().build();
        try {
            String apiKey = client.send(request, HttpResponse.BodyHandlers.ofString()).body();
            if (apiKey == null) {
                throw new IllegalStateException("Получено значение null для apiKey");
            } else {
                this.apiToken = apiKey;
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(MessageFormat.format("Ошибка во время регистрации KV клиента {0}", e.getMessage()));
        }
    }

    public void save(String key, String gson) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/save/" + key + "?API_TOKEN=" + apiToken))
                .POST(HttpRequest.BodyPublishers.ofString(gson))
                .build();
        try {
            client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(MessageFormat.format("Значение с ключом {0} уже существует. {1}", key, e.getMessage()));
        }
    }

    public String load(String key) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url + "/load/" + key + "?API_TOKEN=" + apiToken))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 400) {
                System.out.println("Ответ от сервера не соответствует ожидаемому.");
                return null;
            } else {
                return response.body();
            }

        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(MessageFormat.format("Ошибка во время получение данных KV клиентом {0}", e.getMessage()));
        }
    }
} 
