package client;

import model.Item;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface Client {
    List<Integer> getTopStories();

    List<Integer> getTopStories(int quantity);

    Item getItem(int id);

    CompletableFuture<Item> getItemAsync(int id);
}
