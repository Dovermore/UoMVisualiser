package Util;

public class ClassNameException extends Exception {
    public ClassNameException() {
        super("Given HTML class number is not correct");
    }

    public ClassNameException(int number) {
        super(String.format("Given HTML class number is %d", number));
    }
}
