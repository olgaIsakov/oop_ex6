package oop.ex6.methods;

/**
 * An exception class for block related exceptions
 */
public class BlockException extends  Exception{
    private static final long serialVersionUID = 1L;
    /**
     * a block exception constructor
     * @param errMsg the message to print
     */
    public BlockException(String errMsg){
        super(errMsg);
    }

}
