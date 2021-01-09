package methods;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class mainMethod {
    private static final String START_PARAMETERS = "(";
    private static final String END_PARAMETERS = ")";
    private static final String EMPTY_SPACE = " ";
    private static final String OPEN_PARENTHESIS = "{";
    private static final int ERROR_FOUND = 1;
    final static int SECOND_WORD = 1;
    final static int FIRST_LINE = 0;
    final static int BEGINNING = 0;
    private final static String METHOD_NAME = "[a-zA-Z]\\w*";
    private final static String RETURN = ".*\\s*return\\s*;\\s*\\}$";
    private final static String METHOD_CALL = "[a-zA-Z]\\w*\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
    final static Pattern METHOD_PATTERN = Pattern.compile(METHOD_NAME);
    final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    final static Pattern RETURN_PATTERN = Pattern.compile(RETURN);
    final static String FINAL = "final";
    final static String NULL_MARK = "null";
    final static List<String> typeOptions = List.of("int", "String", "double", "boolean", "char");
    static List<String> resKeys = List.of("void", "final", "if", "while", "true", "false");
    private static Map<List<String>, List<String>> allMethods;
    private static String ERROR_PARAM_MSG = "ERROR: illegal parameters called.";
    private static String ERROR_RETURN_MSG = "ERROR: no return statement in method.";
    static List<String> methodNames = new ArrayList<>() ;

    /**
     * This method checks the first line of a method
     * @param splitLine first line word by word
     * @param params method parameters
     */
    public static void isFirstMethodLineLegal(String[] splitLine, List<String> params) {
        StringBuilder line = new StringBuilder();
        for (String word : splitLine) {
            line.append(word).append(EMPTY_SPACE);
        }
        try {
            int openParenthesis = splitLine[SECOND_WORD].indexOf(START_PARAMETERS);
            String name = splitLine[SECOND_WORD].substring(BEGINNING, openParenthesis);
            Matcher match = METHOD_PATTERN.matcher(name);
            if (!match.matches() || !line.toString().endsWith(OPEN_PARENTHESIS) ||
                    resKeys.contains(name) || typeOptions.contains(name))
                throw new MethodException() ;
            methodNames.add(name);
            int closeParenthesis = splitLine[splitLine.length - 2].indexOf(END_PARAMETERS);
            //check no parameters
            if ((closeParenthesis == openParenthesis +1)) {
                params =  Collections.singletonList(NULL_MARK);
            }
            else{
                String subList = line.substring(openParenthesis, closeParenthesis);
                params = new ArrayList<>(Arrays.asList(subList.split(EMPTY_SPACE)));
            }
            checkMethodParameters(params);
        } catch (Exception e){
            System.out.println(ERROR_FOUND);
        }
    }

    /**
     * Checks legal parameters in method
     * @throws MethodException illegal parameter found
     */
    public static void checkMethodParameters(List<String> parameters) throws MethodException {

            for (int i=0; i < parameters.size(); i++){
                if (parameters.get(i) == null)
                    continue;
                if (parameters.get(i).equals(FINAL)){
                    if (i==parameters.size()-1 || !typeOptions.contains(parameters.get(i+1))){
                        throw new MethodException(ERROR_PARAM_MSG);
                    }
                }
                else if (!typeOptions.contains(parameters.get(i)))
                    throw new MethodException(ERROR_PARAM_MSG);
            }
    }

    /**
     * This method checks if a called method is legal
     * @throws MethodException method called an unexciting method or itself.
     */
    public static void checkMethodCalls() throws MethodException {
        for (List<String> method: allMethods.keySet()){
            Matcher nameMatch = METHOD_CALL_PATTERN.matcher(method.get(FIRST_LINE));
            String name = nameMatch.toMatchResult().group();
            for (int i=1; i< method.size(); i++){
                Matcher methodCall = METHOD_CALL_PATTERN.matcher(method.get(i));
                if (methodCall.matches()){
                    String called = methodCall.toMatchResult().group();
                    if (!methodNames.contains(called) || called.equals(name))
                        throw new MethodException(ERROR_PARAM_MSG);
                    else{
                        if (!paramCheck.checkCalledParams(method.get(i), allMethods.get(method)))
                            throw new MethodException(ERROR_PARAM_MSG);
                    }
                }
            }
        }
    }
    /**
     * This method checks if a called method is legal
     * @throws MethodException method called an unexciting method or itself.
     */
    public static void checkMethodCall(String methodLine) throws MethodException {

    }


    /**
     * This method checks if the method end with a return statement
     * @param method a method
     * @throws MethodException no return statement
     */
    public static void checkReturnStatement(List<String> method) throws MethodException {
        StringBuilder newStr = new StringBuilder();
        for (String line: method){
            newStr.append(line).append("\n");
        }
        Matcher methodCall = METHOD_CALL_PATTERN.matcher(newStr.toString());
        if (!methodCall.matches())
            throw new MethodException(ERROR_RETURN_MSG);
    }



}
