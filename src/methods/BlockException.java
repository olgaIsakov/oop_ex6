package methods;

public class BlockException extends  Exception{
    private static final long serialVersionUID = 1L;
    public BlockException(){
        super();
    }
    public BlockException(String errMsg){
        super(errMsg);
    }

}
