package oop.ex6.variables;

/**
 * An exception class for variable related exceptions
 */
public class VariableException extends Exception{
    private static final long serialVersionUID = 1L;

    /**
     * A variable exception constructor
     * @param msg the message to print
     */
    public VariableException(String msg){
        super(msg);
    }

}
