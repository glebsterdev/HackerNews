import client.HackerNewsClient;
import controller.AppControllerV3;

public class Main {
    public static void main(String[] args) {
        final AppControllerV3 appController = new AppControllerV3(new HackerNewsClient());
        appController.getTopCommentersForTopStories(30, 10).forEach(System.out::println);
    }
}