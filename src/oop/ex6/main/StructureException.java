package oop.ex6.main;

public class StructureException extends Exception  {
    private static final long serialVersionUID = 1L;
    public StructureException(){
        super();
    }
    public StructureException(String errMsg){
        super(errMsg);
    }
}
