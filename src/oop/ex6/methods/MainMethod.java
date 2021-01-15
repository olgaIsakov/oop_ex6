package oop.ex6.methods;
import oop.ex6.main.*;
import java.util.*;
import java.util.regex.Matcher;

/**
 * This class checks general method related issues
 */
public class MainMethod {
    private static final String START_PARAMETERS = "(";
    private static final String END_PARAMETERS = ")";
    private static final String EMPTY_SPACE = " ";
    private static final int ERROR_FOUND = 1;
    private static final int FIRST = 0;
    final static String FINAL = "final";
    final static String NULL_MARK = "null";

    final static List<String> typeOptions = List.of("int", "String", "double", "boolean", "char");
    static List<String> resKeys = List.of("void", "final", "if", "while", "true", "false");

    private static final String ERROR_PARAM_MSG = "ERROR: illegal parameters called.";
    private static final String ERROR_NAME_MSG = "ERROR: illegal name method.";
    private static final String ERROR_RETURN_MSG = "ERROR: no return statement in method.";

    /**
     * This method checks the first line of a method
     * @param splitLine first line word by word
     * @param params method parameters
     */
    public static List<String> isFirstMethodLineLegal(String[] splitLine, List<String> params) {
        StringBuilder line = new StringBuilder();
        for (String word : splitLine) {
            line.append(word).append(EMPTY_SPACE);
        }
        try {
            int openParenthesis = line.indexOf(START_PARAMETERS);
            int closeParenthesis = line.indexOf(END_PARAMETERS);
            if ((closeParenthesis == openParenthesis +1)) {
                params =  Collections.singletonList(NULL_MARK);
            }
            else{
                String subList = line.substring(openParenthesis+1, closeParenthesis);
                params = new ArrayList<>(Arrays.asList(subList.split(",")));
            }
            //checkMethodParameters(params);

        } catch (Exception e){
            System.out.println(ERROR_FOUND);
        }
        return params;
    }

    /**
     * Checks legal parameters in method
     * @throws MethodException illegal parameter found
     */
    //public static void checkMethodParameters(List<String> parameters) throws MethodException {
            //for (int i=0; i < parameters.size(); i++){
                //if (parameters.get(i).equals(NULL_MARK))
                   // continue;
                //if (parameters.get(i).equals(FINAL)){
                    //if (i==parameters.size()-1 || !typeOptions.contains(parameters.get(i+1))){
                        //throw new MethodException(ERROR_PARAM_MSG);
                    //}
               // }
                //if (typeOptions.contains(parameters.get(i))){
                    //i ++;
                //}
                //else throw new MethodException(ERROR_PARAM_MSG);
            //}
    //}
    /**
     * This method checks if a called method is legal
     * @throws MethodException method called an unexciting method or itself.
     */
    public static void checkMethodCall(String methodLine) throws MethodException {
        if (methodLine.contains("=")) throw new MethodException(ERROR_PARAM_MSG);
        String called = getMethodName(methodLine);
        Map<String, List<String>> mapNameParameters = Parser.getMapNameParams();
        if (!mapNameParameters.containsKey(called))
            throw new MethodException(ERROR_PARAM_MSG);
        else{
            if (!ParamCheck.checkCalledParams(methodLine, mapNameParameters.get(called)))
                throw new MethodException(ERROR_PARAM_MSG);
        }
    }

    /**
     * This method gets the method name
     * @param methodLine the line in the method
     * @return the method name 
     */
    public static String getMethodName(String methodLine) throws MethodException {
        String[] lineWords = methodLine.strip().replaceAll(MethodPatterns.SPACES, EMPTY_SPACE).split(EMPTY_SPACE) ;
        String name = "";
        for (String word: lineWords){
            Matcher paramStart = MethodPatterns.PARAM_START_PATTERN.matcher(word);
            if (paramStart.find()){
                name = word;
                break;
            }
        }
        int endCalled = name.indexOf(START_PARAMETERS);
        name = name.substring(FIRST, endCalled);
        if (resKeys.contains(name) || typeOptions.contains(name) )
            throw new MethodException(ERROR_NAME_MSG) ;
        return name;
    }


    /**
     * This method checks if the method end with a return statement
     * @param method a method
     * @throws MethodException no return statement
     */
    public static void checkReturnStatement(List<String> method) throws MethodException {
        String endLine = method.get(method.size()-2) ;
        Matcher returnMatcher = MethodPatterns.RETURN_PATTERN.matcher(endLine);
        if (!returnMatcher.matches())
            throw new MethodException(ERROR_RETURN_MSG);
    }



}
