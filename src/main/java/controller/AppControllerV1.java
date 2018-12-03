package controller;

import client.Client;
import model.Item;
import model.Result;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AppControllerV1 {
    private final Client client;

    public AppControllerV1(Client client) {
        this.client = client;
    }

    public List<String> getTopCommentersForTopStories(int maxStories, int maxCommenters) {
        return client.getTopStories(maxStories)
                .stream()
                .map(client::getItem)
                .filter(Objects::nonNull)
                .map(item -> getCommentStats(item, maxCommenters))
                .collect(Collectors.toList());
    }

    private String getCommentStats(Item story, int maxCommenters) {
        final List<String> commenterList = walkItemTree(story)
                .stream()
                .map(i -> i.by)
                .collect(Collectors.toList());
        return story.toString() + "\n" + commenterList
                .stream()
                .distinct()
                .map(i -> new Result(i, Collections.frequency(commenterList, i)))
                .sorted((o1, o2) -> o2.count.compareTo(o1.count))
                .map(Result::toSimpleString)
                .limit(maxCommenters)
                .collect(Collectors.joining("\n")) + "\n";
    }

    private List<Item> walkItemTree(Item item) {
        final List<Item> kidsItems = item.kids
                .stream()
                .map(client::getItem)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        kidsItems.addAll(kidsItems
                .stream()
                .map(this::walkItemTree)
                .flatMap(List::stream)
                .collect(Collectors.toList()));
        return kidsItems;
    }
}
