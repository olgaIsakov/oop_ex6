package oop.ex6.main;

import oop.ex6.methods.*;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    final static int INITIALIZED_COUNTER = 0;
    private static final String EMPTY_SPACE = " ";
    private final static String ILLEGAL_OPEN = "\\s*[{][{]+\\s*$";
    private final static String ILLEGAL_CLOSE = "\\s*[}][}]+\\s*$";
    private final static String METHOD = "\\s*void\\s+[a-zA-Z]\\w*\\s*\\(.*\\s*\\)\\s*[{]\\s*$";
    private final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    private final static String OPEN = "\\s*[{]\\s*$";
    private final static String CLOSE = "^\\s*[}]\\s*$";
    private final static String IF_WHILE = "^\\s*+(if|while)\\s*\\(\\s*.*\\s*\\)\\s*[{]\\s*$";
    private final static String RETURN = "^\\s*return\\s*;\\s*$";
    private final static String METHOD_CALL = "[a-zA-Z]\\w*\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
    final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    final static Pattern RETURN_PATTERN = Pattern.compile(RETURN);
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);
    final static Pattern OPEN_PATTERN = Pattern.compile(OPEN);
    final static Pattern CLOSE_PATTERN = Pattern.compile(CLOSE);
    final static Pattern ILLEGAL_OPEN_PATTERN = Pattern.compile(ILLEGAL_OPEN);
    final static Pattern ILLEGAL_CLOSE_PATTERN = Pattern.compile(ILLEGAL_CLOSE);
    final static Pattern METHOD_PATTERN = Pattern.compile(METHOD);
    final static String ERROR_MSG = "ERROR: Illegal method format";
    final static String INVALID_LINE_ERROR = "ERROR: Invalid line found";
    static List<String> globalVars = new ArrayList<>();
    static Map<String, List<String>> mapNameLines = new HashMap<>();
    static Map<String, List<String>> mapNameParams = new HashMap<>();

    /**
     * this method parse the file into methods
     *
     * @param sJavaLines the given file
     * @return a map -> list of the method lines : list of the method parameters
     */
    public static void parseToMethods(String[] sJavaLines) throws MethodException, StructureException {
        for (int i = 0; i < sJavaLines.length; i++) {
            Matcher methodStructure = METHOD_PATTERN.matcher(sJavaLines[i]);
            if (methodStructure.matches()) {
                i = getMethodBlock(sJavaLines, i)-1; //change in dibug
                continue;
            }
            Matcher globalVarsMatcher = VARIABLE_SUFFIX_PATTERN.matcher(sJavaLines[i]);
            //Matcher returnStructure = RETURN_PATTERN.matcher(sJavaLines[i]);
            if  (globalVarsMatcher.find())
                globalVars.add(sJavaLines[i]);
            else{
                Matcher closeStructure = CLOSE_PATTERN.matcher(sJavaLines[i]);
                if (!closeStructure.matches()) throw new StructureException(INVALID_LINE_ERROR);
            }
        }

    }

    /**
     * This method finds a single method block
     * @param sJavaLines the file
     * @param i the current line index
     * @return the index of the last block line
     * @throws MethodException illegal block
     */
    public static int getMethodBlock(String[] sJavaLines, int i) throws MethodException, StructureException {
        String name = mainMethod.getMethodName(sJavaLines[i]);
        Matcher illegalOpen = ILLEGAL_OPEN_PATTERN.matcher(sJavaLines[i]);
        if (illegalOpen.find()) throw new MethodException(ERROR_MSG);
        else {
            int parenthesisCounter = INITIALIZED_COUNTER;
            List<String> params = new ArrayList<>();
            List<String> newParams = mainMethod.isFirstMethodLineLegal(sJavaLines[i].split(EMPTY_SPACE), params);
            List<String> methodLines = new ArrayList<>();
            parenthesisCounter++;
            methodLines.add(sJavaLines[i]);
            i++;
            while (parenthesisCounter != INITIALIZED_COUNTER) {
                // if open parenthesis different from close parenthesis
                if (i ==sJavaLines.length -1)
                    throw new StructureException(ERROR_MSG);
                Matcher openParenthesis = OPEN_PATTERN.matcher(sJavaLines[i]);
                Matcher closeParenthesis = CLOSE_PATTERN.matcher(sJavaLines[i]);
                if (openParenthesis.find()) {
                    if (illegalOpen.find()) throw new MethodException(ERROR_MSG);
                    parenthesisCounter++;
                }
                if (closeParenthesis.matches()) {
                    Matcher illegalClose = ILLEGAL_CLOSE_PATTERN.matcher(sJavaLines[i]);
                    if (illegalClose.matches()) throw new MethodException(ERROR_MSG);
                    parenthesisCounter--;
                }
                methodLines.add(sJavaLines[i]);
                i++;
            }
            mapNameLines.put(name, methodLines);
            mapNameParams.put(name, newParams);
        }
        return i;
    }

    /**
     * this method checks all lines in the file
     * @param sJavaLines the given file
     */
    public void checkAllLines(String[] sJavaLines) throws StructureException {
        List<String> variableList = new ArrayList<>();
        boolean flag = true;
        for (int i = 0; i < sJavaLines.length; i++) {
            Matcher variableMatch = VARIABLE_SUFFIX_PATTERN.matcher(sJavaLines[i]);
            Matcher methodMatch = METHOD_PATTERN.matcher(sJavaLines[i]);
            Matcher ifWhileStructure = IF_WHILE_PATTERN.matcher(sJavaLines[i]);
            Matcher closeStructure = CLOSE_PATTERN.matcher(sJavaLines[i]);
            Matcher returnStructure = RETURN_PATTERN.matcher(sJavaLines[i]);
            Matcher methodCallStructure = METHOD_CALL_PATTERN.matcher(sJavaLines[i]);
            if (!variableMatch.matches() && !methodMatch.matches() && !ifWhileStructure.matches() &&
                    !closeStructure.matches() &&
                    !returnStructure.matches() && !methodCallStructure.matches()) {
                flag = false;
                break;
            }
        }
        if (!flag) throw new StructureException(INVALID_LINE_ERROR);

    }

    /**
     * A getter for the global variables
     * @return the global variables list
     */
    public static List<String> getGlobalVars(){
        return globalVars;
    }

    /**
     * a getter for the name- method lines map
     * @return the map
     */
    public static Map<String, List<String>> getMapNameLines(){
        return mapNameLines;
    }

    /**
     * a getter for the name- parameters map
     * @return the map
     */
    public static Map<String, List<String>> getMapNameParams(){
        return mapNameParams;
    }



}
