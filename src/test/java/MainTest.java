import model.Result;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MainTest {

    @Test
    public void test1() {
        final List<String> inputList1 = Arrays.asList("a", "a", "a", "b", "b", "c");
        final List<String> inputList2 = Arrays.asList("c", "b", "b", "a", "a", "a");
        final List<String> inputList3 = Arrays.asList("a", "b", "a", "c", "b", "a");
        final List<Result> expected = Arrays.asList(
                new Result("a", 3),
                new Result("b", 2),
                new Result("c", 1));

//        assertEquals(expected, Calc.extract(inputList1));
//        assertEquals(expected, Calc.extract(inputList2));
//        assertEquals(expected, Calc.extract(inputList3));
    }
}