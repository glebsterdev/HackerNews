package model;

import java.util.Objects;

public class Result {
    public final Integer count;
    public final String name;

    public Result(String name, int count) {
        this.name = name;
        this.count = count;
    }

    @Override
    public String toString() {
        return "Result{" +
                "count=" + count +
                ", name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result result = (Result) o;
        return count == result.count &&
                Objects.equals(name, result.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(count, name);
    }

    public String toSimpleString() {
        return count + " " + name;
    }
}
