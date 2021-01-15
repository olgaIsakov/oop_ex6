package oop.ex6.methods;

import oop.ex6.main.Parser;
import oop.ex6.main.StructureException;
import oop.ex6.variables.VariableAnalyzer;
import oop.ex6.variables.ConditionException;

import oop.ex6.variables.VariableException;

import java.util.*;
import java.util.regex.Matcher;

/**
 * This class checks a single method
 */
public class CheckSingleMethod {
    private static final String NULL_MARK = "null";
    private static final String SPACE = " ";
    private static final String EMPTY_STRING = "";
    private static final String COMMA = ",";

    private static final String ERROR_SAME_PARAM_NAME = "Error : same parameters name";
    public static List<String> declarationInit = new ArrayList<>();


    /**
     * This method to check if all the methods are valid
     * @param mapNameLines a map of the name and the lines of the method
     * @throws ConditionException invalid condition found
     * @throws MethodException invalid method
     * @throws VariableException invalid variables
     * @throws StructureException invalid structure
     * @throws BlockException invalid block found
     */
    public static void checkMethods(Map<String, List<String>> mapNameLines)
            throws ConditionException, MethodException, VariableException, StructureException, BlockException {
        for (Map.Entry<String, List<String>> nameAndLines: mapNameLines.entrySet()){
            List<String> method = nameAndLines.getValue();
            String name = nameAndLines.getKey() ;
            checkSingleMethod(method, name);
        }
    }

    /**
     * a method to check the validity of a single method
     * @param method the method to check
     * @param name the method name
     * @throws ConditionException invalid condition found
     * @throws MethodException invalid method
     * @throws VariableException invalid variables
     * @throws StructureException invalid structure
     * @throws BlockException invalid block found
     */
    private static void checkSingleMethod(List<String> method, String name)
            throws ConditionException, MethodException, VariableException, StructureException, BlockException {
        addParamsAsLocalVariables(name);
        for (int i = 1 ; i < method.size() ; i++){
            Matcher ifWhileMatch = MethodPatterns.IF_WHILE_PATTERN.matcher(method.get(i));
            Matcher methodCallMatch = MethodPatterns.METHOD_CALL_PATTERN.matcher(method.get(i));
            Matcher varsMatch = MethodPatterns.VARIABLE_SUFFIX_PATTERN.matcher(method.get(i));
            Matcher returnMatcher = MethodPatterns.RETURN_PATTERN.matcher(method.get(i));

            if (ifWhileMatch.matches()){
                i = IfWhileMethods.findAllBlocks(method, i , name);
            }
            else if (methodCallMatch.find()){
                MainMethod.checkMethodCall(method.get(i));
            }
            else if (varsMatch.find() && !returnMatcher.matches() ){
                VariableAnalyzer.analyzeLineVariable(method.get(i),false);
            }
            MainMethod.checkReturnStatement(method);
        }
    }

    /**
     * This method adds parameters as local variables
     * @param name the method name
     * @throws VariableException Invalid variable
     */
    private static void addParamsAsLocalVariables(String name) throws VariableException {

        List<String> params = Parser.getMapNameParams().get(name);
        List<String> nameParams = new ArrayList<>();
        for(int j =1 ;j<params.size(); j+=2){
            String nameParam = params.get(j).replaceAll(COMMA,EMPTY_STRING);
            if (nameParams.contains(nameParam) ) throw new VariableException(ERROR_SAME_PARAM_NAME);
            else nameParams.add(nameParam);
        }
        for(int i =0 ;i<params.size()-1; i+=2){
            String paramLine = params.get(i) +SPACE +params.get(i + 1);
            if (!paramLine.equals(NULL_MARK)) {
                VariableAnalyzer.analyzeLineVariable(paramLine , true);
                }
           }
    }

}
