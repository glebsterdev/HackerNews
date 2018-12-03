package controller;

import client.Client;
import model.Item;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class AppControllerV3Test {
    @Mock
    Client client;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void test_one_comment_per_person() {
        when(client.getTopStories(1)).thenReturn(Arrays.asList(1234));
        when(client.getItemAsync(1234)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person1", 1234, Arrays.asList(1235, 1236, 1237), "story")));
        when(client.getItemAsync(1235)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person2", 1235, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1236)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 1236, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1237)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 1237, new ArrayList<>(), "comment")));

        final AppControllerV3 appControllerV3 = new AppControllerV3(client);

        assertEquals(
                Arrays.asList("Item{by='person1', id=1234, kids=[1235, 1236, 1237], type='story'}\n" +
                        "1 person2\n" +
                        "1 person3\n" +
                        "1 person4\n"),
                appControllerV3.getTopCommentersForTopStories(1, 10));
    }

    @Test
    public void test_multiple_comments_per_person() {
        when(client.getTopStories(1)).thenReturn(Arrays.asList(1234));
        when(client.getItemAsync(1234)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person1", 1234, Arrays.asList(1235, 1236, 1237, 1238, 1239, 1240), "story")));
        when(client.getItemAsync(1235)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person2", 1235, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1236)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 1236, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1237)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 1237, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1238)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 1238, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1239)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 1239, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1240)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 1240, new ArrayList<>(), "comment")));

        final AppControllerV3 appControllerV3 = new AppControllerV3(client);

        assertEquals(
                Arrays.asList("Item{by='person1', id=1234, kids=[1235, 1236, 1237, 1238, 1239, 1240], type='story'}\n" +
                        "3 person4\n" +
                        "2 person3\n" +
                        "1 person2\n"),
                appControllerV3.getTopCommentersForTopStories(1, 10));
    }

    @Test
    public void test_nested_comments() {
        when(client.getTopStories(1)).thenReturn(Arrays.asList(1234));
        when(client.getItemAsync(1234)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person1", 1234, Arrays.asList(1235, 1236, 1237), "story")));
        when(client.getItemAsync(1235)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person2", 1235, Arrays.asList(2001, 2002, 2003), "comment")));
        when(client.getItemAsync(1236)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 1236, Arrays.asList(3001, 3002), "comment")));
        when(client.getItemAsync(1237)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 1237, Arrays.asList(4001), "comment")));

        when(client.getItemAsync(2001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 2001, new ArrayList<>(), "comment")));
        when(client.getItemAsync(2002)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 2002, new ArrayList<>(), "comment")));
        when(client.getItemAsync(2003)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person5", 2003, new ArrayList<>(), "comment")));

        when(client.getItemAsync(3001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 3001, new ArrayList<>(), "comment")));
        when(client.getItemAsync(3002)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person5", 3002, new ArrayList<>(), "comment")));

        when(client.getItemAsync(4001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person5", 4001, new ArrayList<>(), "comment")));

        final AppControllerV3 appControllerV3 = new AppControllerV3(client);

        assertEquals(
                Arrays.asList("Item{by='person1', id=1234, kids=[1235, 1236, 1237], type='story'}\n" +
                        "3 person4\n" +
                        "3 person5\n" +
                        "2 person3\n" +
                        "1 person2\n"),
                appControllerV3.getTopCommentersForTopStories(1, 10));
    }

    @Test
    public void test_multi_level_nested_comments() {
        when(client.getTopStories(1)).thenReturn(Arrays.asList(1234));
        when(client.getItemAsync(1234)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person1", 1234, Arrays.asList(1235, 1236, 1237), "story")));
        when(client.getItemAsync(1235)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person2", 1235, Arrays.asList(2001, 2002, 2003), "comment")));
        when(client.getItemAsync(1236)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 1236, new ArrayList<>(), "comment")));
        when(client.getItemAsync(1237)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 1237, new ArrayList<>(), "comment")));

        when(client.getItemAsync(2001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 2001, Arrays.asList(3001, 3002), "comment")));
        when(client.getItemAsync(2002)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 2002, new ArrayList<>(), "comment")));
        when(client.getItemAsync(2003)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person5", 2003, new ArrayList<>(), "comment")));

        when(client.getItemAsync(3001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 3001, Arrays.asList(4001), "comment")));
        when(client.getItemAsync(3002)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person5", 3002, new ArrayList<>(), "comment")));

        when(client.getItemAsync(4001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person5", 4001, new ArrayList<>(), "comment")));

        final AppControllerV3 appControllerV3 = new AppControllerV3(client);

        assertEquals(
                Arrays.asList("Item{by='person1', id=1234, kids=[1235, 1236, 1237], type='story'}\n" +
                        "3 person4\n" +
                        "3 person5\n" +
                        "2 person3\n" +
                        "1 person2\n"),
                appControllerV3.getTopCommentersForTopStories(1, 10));
    }

    @Test
    public void test_multiple_stories() {
        when(client.getTopStories(5)).thenReturn(Arrays.asList(1234, 1235, 1236));
        when(client.getTopStories(2)).thenReturn(Arrays.asList(1234, 1235));
        when(client.getItemAsync(1234)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person1", 1234, Arrays.asList(2001), "story")));
        when(client.getItemAsync(1235)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person2", 1235, Arrays.asList(3001), "story")));
        when(client.getItemAsync(1236)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person3", 1236, Arrays.asList(4001), "story")));

        when(client.getItemAsync(2001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 2001, new ArrayList<>(), "comment")));
        when(client.getItemAsync(3001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 3001, new ArrayList<>(), "comment")));
        when(client.getItemAsync(4001)).thenReturn(CompletableFuture.supplyAsync(() -> new Item("person4", 4001, new ArrayList<>(), "comment")));

        final AppControllerV3 appControllerV3 = new AppControllerV3(client);

        assertEquals(Arrays.asList(
                "Item{by='person1', id=1234, kids=[2001], type='story'}\n1 person4\n",
                "Item{by='person2', id=1235, kids=[3001], type='story'}\n1 person4\n",
                "Item{by='person3', id=1236, kids=[4001], type='story'}\n1 person4\n"),
                appControllerV3.getTopCommentersForTopStories(5, 10));

        assertEquals(Arrays.asList(
                "Item{by='person1', id=1234, kids=[2001], type='story'}\n1 person4\n",
                "Item{by='person2', id=1235, kids=[3001], type='story'}\n1 person4\n"),
                appControllerV3.getTopCommentersForTopStories(2, 10));
    }
}