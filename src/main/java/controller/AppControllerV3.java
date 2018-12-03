package controller;

import client.Client;
import model.Item;
import model.Result;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

public class AppControllerV3 {
    private final Client client;

    public AppControllerV3(Client client) {
        this.client = client;
    }

    public List<String> getTopCommentersForTopStories(int maxStories, int maxCommenters) {
        return client.getTopStories(maxStories)
                .stream()
                .map(client::getItemAsync)
                .filter(Objects::nonNull)
                .map(completableFuture -> {
                    try {
                        final Item story = completableFuture.get();
                        final List<String> commenters = walkItemTree(story, client)
                                .stream()
                                .map(i -> i.by)
                                .collect(Collectors.toList());
                        return story.toString() + "\n" + commenters.stream()
                                .distinct()
                                .map(i -> new Result(i, Collections.frequency(commenters, i)))
                                .sorted((o1, o2) -> o2.count.compareTo(o1.count))
                                .map(Result::toSimpleString)
                                .limit(maxCommenters)
                                .collect(Collectors.joining("\n")) + "\n";

                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private static List<Item> walkItemTree(Item item, Client client) {
        final CompletableFuture<Item>[] kidsItemFutures = item.kids
                .stream()
                .map(client::getItemAsync)
                .filter(Objects::nonNull)
                .toArray((IntFunction<CompletableFuture<Item>[]>) CompletableFuture[]::new);

        final CompletableFuture<List<Item>> combinedKidsItemFutures = CompletableFuture.allOf(kidsItemFutures)
                .thenApply(aVoid -> Arrays.stream(kidsItemFutures)
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()));

        try {
            final List<Item> kidsItems = combinedKidsItemFutures.get();
            final List<Item> results = new ArrayList<>(kidsItems);
            kidsItems.stream().map(i -> walkItemTree(i, client)).forEach(results::addAll);
            return results;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
