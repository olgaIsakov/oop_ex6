package oop.ex6.main;

/**
 * An exception class to check structure related exceptions
 */
public class StructureException extends Exception  {
    private static final long serialVersionUID = 1L;

    /**
     * A structure exceptions constructor
     * @param errMsg the message to print
     */
    public StructureException(String errMsg){
        super(errMsg);
    }
}
