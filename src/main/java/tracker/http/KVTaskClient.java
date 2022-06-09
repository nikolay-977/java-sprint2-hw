package tracker.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
                throw new IllegalStateException("apiKey");
            } else {
                this.apiToken = apiKey;
            }
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Ошибка во время регистрации KV клиента");
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
            throw new IllegalArgumentException(String.format("Значение с ключом %s уже существует.", key));
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
            throw new RuntimeException(e);
        }
    }
} 
