package ryey.easer.commons;

import java.util.Arrays;

public class IllegalArgumentTypeException extends IllegalArgumentException {
    public IllegalArgumentTypeException(String s) {
        super(s);
    }

    public IllegalArgumentTypeException(Class received, Class expected) {
        super(String.format("Illegal type :: expected <%s> encountered <%s>", expected, received));
    }

    public IllegalArgumentTypeException(Class received, Class[] expected) {
        super(String.format("Illegal type :: expected <%s> encountered <%s>", Arrays.toString(expected), received));
    }
}
