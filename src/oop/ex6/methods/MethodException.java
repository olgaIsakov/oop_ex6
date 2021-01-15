package oop.ex6.methods;

/**
 * An exception class ro method related issues
 */
public class MethodException extends Exception {
    private static final long serialVersionUID = 1L;

    /**
     * a method exception constructor
     * @param errMsg the message to print
     */
    public MethodException(String errMsg){
        super(errMsg);
    }

}
