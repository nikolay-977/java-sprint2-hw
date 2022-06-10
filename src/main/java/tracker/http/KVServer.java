package tracker.http;

import static java.nio.charset.StandardCharsets.UTF_8;
import static tracker.constants.StatusCode.*;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

/**
 * Постман: https://www.getpostman.com/collections/a83b61d9e1c81c10575c
 */
public class KVServer {
    public static final int PORT = 8082;
    private final String apiToken;
    private final HttpServer server;
    private final Map<String, String> data = new HashMap<>();

    public KVServer() throws IOException {
        this.apiToken = generateToken();
        this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
        this.server.createContext("/register", this::register);
        this.server.createContext("/save", this::save);
        this.server.createContext("/load", this::load);
    }

    private void load(HttpExchange exchange) throws IOException {
        try {
            System.out.println("\n/load");
            if (!hasAuth(exchange)) {
                System.out.println("Запрос (load) неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                exchange.sendResponseHeaders(SC_403, 0);
                return;
            }

            switch (exchange.getRequestMethod()) {
                case "GET":
                    String key = exchange.getRequestURI().toString().substring("/load/".length());
                    if (key.startsWith("?API_TOKEN=")) {
                        key = key.substring("?API_TOKEN=".length());
                    }
                    if (key.isEmpty()) {
                        System.out.println("Key для получения значения пустой. key указывается в пути: /load/{key}");
                        exchange.sendResponseHeaders(SC_400, 0);
                        return;
                    }
                    String value = data.get(key);
                    String response = value == null ? "null" : value;
                    System.out.println("Возвращаем значение для ключа " + key);
                    sendText(exchange, response);
                    break;
                default:
                    System.out.println("/load ждёт GET-запрос, а получил: " + exchange.getRequestMethod());
                    exchange.sendResponseHeaders(SC_405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void save(HttpExchange exchange) throws IOException {
        try {
            System.out.println("\n/save");
            if (!hasAuth(exchange)) {
                System.out.println("Запрос неавторизован, нужен параметр в query API_TOKEN со значением апи-ключа");
                exchange.sendResponseHeaders(SC_403, 0);
                return;
            }
            if ("POST".equals(exchange.getRequestMethod())) {
                String key = exchange.getRequestURI().getPath().substring("/save/?API_TOKEN=".length());
                if (key.isEmpty()) {
                    System.out.println("Key для сохранения пустой. key указывается в пути: /save/{key}");
                    exchange.sendResponseHeaders(SC_400, 0);
                    return;
                }
                String value = readText(exchange);
                if (value.isEmpty()) {
                    System.out.println("Value для сохранения пустой. value указывается в теле запроса");
                    exchange.sendResponseHeaders(SC_400, 0);
                    return;
                }
                data.put(key, value);
                System.out.println("Значение для ключа " + key + " успешно обновлено!");
                exchange.sendResponseHeaders(SC_200, 0);
            } else {
                System.out.println("/save ждёт POST-запрос, а получил: " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(SC_405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    private void register(HttpExchange exchange) throws IOException {
        try {
            System.out.println("\n/register");
            if ("GET".equals(exchange.getRequestMethod())) {
                sendText(exchange, apiToken);
            } else {
                System.out.println("/register ждёт GET-запрос, а получил " + exchange.getRequestMethod());
                exchange.sendResponseHeaders(SC_405, 0);
            }
        } finally {
            exchange.close();
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Открой в браузере http://localhost:" + PORT + "/");
        System.out.println("API_TOKEN: " + apiToken);
        server.start();
    }

    public void stop() {
        server.stop(2);
    }

    private String generateToken() {
        return "" + System.currentTimeMillis();
    }

    protected boolean hasAuth(HttpExchange exchange) {
        String rawQuery = exchange.getRequestURI().getRawQuery();
        return rawQuery != null && (rawQuery.contains("API_TOKEN=" + apiToken) || rawQuery.contains("API_TOKEN=DEBUG"));
    }

    protected String readText(HttpExchange httpExchange) throws IOException {
        return new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
    }

    protected void sendText(HttpExchange httpExchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        httpExchange.getResponseHeaders().add("Content-Type", "application/json");
        httpExchange.sendResponseHeaders(SC_200, resp.length);
        httpExchange.getResponseBody().write(resp);
    }
}
