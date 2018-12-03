import client.Client;
import client.HackerNewsClient;
import controller.AppControllerV1;
import controller.AppControllerV2;
import controller.AppControllerV3;
import utils.TimeTracker;

public class BenchTestMain {

    public static void main(String[] args) {
        final Client client = new HackerNewsClient();

        final TimeTracker tt = new TimeTracker();

        final AppControllerV1 a1 = new AppControllerV1(client);
        final AppControllerV2 a2 = new AppControllerV2(client);
        final AppControllerV3 a3 = new AppControllerV3(client);

        a1.getTopCommentersForTopStories(30, 10).forEach(System.out::println);
        tt.capture();

        a2.getTopCommentersForTopStories(30, 10).forEach(System.out::println);
        tt.capture();

        a3.getTopCommentersForTopStories(30, 10).forEach(System.out::println);
        tt.capture();

        tt.printTimes();

        /*
        Test #1
        a1: 70756.0 ms
        a2: 69612.0 ms
        a3: 30770.0 ms

        Test #2
        a1: 81114.0 ms
        a2: 78426.0 ms
        a3: 33511.0 ms
         */
    }
}