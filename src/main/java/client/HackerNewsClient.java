package client;

import model.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class HackerNewsClient implements Client {
    private final HttpClient httpClient;

    public HackerNewsClient() {
        httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();
    }

    @Override
    public List<Integer> getTopStories() {
        try {
            final HttpResponse<String> response = httpClient.send(getTopStoriesRequest(), HttpResponse.BodyHandlers.ofString());
            return new JSONArray(response.body())
                    .toList()
                    .stream()
                    .map(i -> (Integer) i)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Integer> getTopStories(int quantity) {
        final List<Integer> stories = getTopStories();
        if (stories.size() >= quantity)
            return stories.subList(0, quantity);
        return stories;
    }

    @Override
    public Item getItem(int id) {
        try {
            return toItem(httpClient.send(getItemRequest(id), HttpResponse.BodyHandlers.ofString()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public CompletableFuture<Item> getItemAsync(int id) {
        try {
            return httpClient.sendAsync(getItemRequest(id), HttpResponse.BodyHandlers.ofString()).thenApply(this::toItem);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private HttpRequest getTopStoriesRequest() {
        return HttpRequest.newBuilder()
                .uri(URI.create("https://hacker-news.firebaseio.com/v0/topstories.json"))
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build();
    }

    private HttpRequest getItemRequest(int id) {
        return HttpRequest.newBuilder()
                .uri(URI.create("https://hacker-news.firebaseio.com/v0/item/" + id + ".json"))
                .version(HttpClient.Version.HTTP_2)
                .GET()
                .build();
    }

    private Item toItem(HttpResponse<String> response) {
        final JSONObject jo = new JSONObject(response.body());

        return new Item(
                (jo.isNull("by")) ? "" : (String) jo.get("by"),
                (jo.isNull("id")) ? -1 : (Integer) jo.get("id"),
                (jo.isNull("kids")) ? new ArrayList<>() : ((JSONArray) jo.get("kids"))
                        .toList()
                        .stream()
                        .map(i -> (Integer) i)
                        .collect(Collectors.toList()),
                (jo.isNull("type")) ? "" : (String) jo.get("type"));
    }
}
