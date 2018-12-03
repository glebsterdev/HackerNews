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

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.when;

public class AppControllerV1Test {
    @Mock
    Client client;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Test
    public void test() {
        when(client.getTopStories(1)).thenReturn(Arrays.asList(1234));
        when(client.getItem(1234)).thenReturn(new Item("person1", 1234, Arrays.asList(1235, 1236, 1237), "story"));
        when(client.getItem(1235)).thenReturn(new Item("person2", 1235, new ArrayList<>(), "comment"));
        when(client.getItem(1236)).thenReturn(new Item("person3", 1236, new ArrayList<>(), "comment"));
        when(client.getItem(1237)).thenReturn(new Item("person4", 1237, new ArrayList<>(), "comment"));

        final AppControllerV1 appControllerV1 = new AppControllerV1(client);

        assertEquals(
                Arrays.asList("Item{by='person1', id=1234, kids=[1235, 1236, 1237], type='story'}\n"+
                        "1 person2\n"+
                        "1 person3\n"+
                        "1 person4\n"),
                appControllerV1.getTopCommentersForTopStories(1, 10));
    }
}