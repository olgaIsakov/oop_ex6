package oop.ex6.main;

import oop.ex6.methods.*;
import oop.ex6.variables.VariablesPattern;

import java.util.*;
import java.util.regex.Matcher;

/**
 * A class to parse the file into methods
 */
public class Parser {

    /*Magic numbers and strings*/
    private final static int INITIALIZED_COUNTER = 0;
    private static final String EMPTY_SPACE = " ";

    /*Error messages to print */
    final static String ERROR_MSG = "ERROR: Illegal method format";
    final static String INVALID_LINE_ERROR = "ERROR: Invalid line found";

    /*Lists of the file lines */
    static List<String> globalVars = new ArrayList<>();
    static Map<String, List<String>> mapNameLines = new HashMap<>();
    static Map<String, List<String>> mapNameParams = new HashMap<>();

    /**
     * this method parse the file into methods
     *
     * @param sJavaLines the given file
     */
    public static void parseToMethods(String[] sJavaLines) throws MethodException, StructureException {
        for (int i = 0; i < sJavaLines.length; i++) {
            Matcher methodStructure = MethodPatterns.METHOD_LINE_PATTERN.matcher(sJavaLines[i]);
            if (methodStructure.matches()) {
                i = getMethodBlock(sJavaLines, i)-1;
                continue;
            }
            Matcher globalVarsMatcher = VariablesPattern.VARIABLE_SUFFIX_PATTERN.matcher(sJavaLines[i]);
            if  (globalVarsMatcher.find())
                globalVars.add(sJavaLines[i]);
            else{
                Matcher closeStructure = MethodPatterns.CLOSE_PATTERN.matcher(sJavaLines[i]);
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
        String name = MainMethod.getMethodName(sJavaLines[i]);
        Matcher illegalOpen = MethodPatterns.ILLEGAL_OPEN_PATTERN.matcher(sJavaLines[i]);
        if (illegalOpen.matches()) throw new MethodException(ERROR_MSG);
        else {
            int parenthesisCounter = INITIALIZED_COUNTER;
            List<String> params = new ArrayList<>();
            List<String> newParams = MainMethod.isFirstMethodLineLegal(sJavaLines[i].split(EMPTY_SPACE), params);
            List<String> methodLines = new ArrayList<>();
            parenthesisCounter++;
            methodLines.add(sJavaLines[i]);
            i++;
            while (parenthesisCounter != INITIALIZED_COUNTER) {
                // if num of open parenthesis different from the num of close parenthesis
                if (i ==sJavaLines.length)
                    throw new StructureException(ERROR_MSG);
                Matcher openParenthesis = MethodPatterns.OPEN_PATTERN.matcher(sJavaLines[i]);
                Matcher closeParenthesis = MethodPatterns.CLOSE_PATTERN.matcher(sJavaLines[i]);
                if (openParenthesis.find()) {
                    if (illegalOpen.find()) throw new MethodException(ERROR_MSG);
                    parenthesisCounter++;
                }
                if (closeParenthesis.matches()) {
                    Matcher illegalClose = MethodPatterns.ILLEGAL_CLOSE_PATTERN.matcher(sJavaLines[i]);
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
