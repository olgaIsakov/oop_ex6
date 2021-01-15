package oop.ex6.variables;

/**
 * A condition exception class
 */
public class ConditionException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * the condition exception constructor
     *
     * @param msg the message to print
     */
    public ConditionException(String msg) {
        super(msg);
    }
}
