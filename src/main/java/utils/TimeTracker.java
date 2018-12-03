package utils;

import java.util.ArrayList;
import java.util.List;

public class TimeTracker {
    private List<Double> times = new ArrayList<>();

    private double currentRef = System.currentTimeMillis();

    public TimeTracker() {
    }

    public void capture() {
        double t = System.currentTimeMillis();
        times.add(t - currentRef);
        currentRef = t;
    }

    public void printTimes() {
        times.forEach(i -> System.out.println(i + " ms"));
    }
}
