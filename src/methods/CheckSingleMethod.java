package methods;

import manager.Parser;
import variable.FinalVariableException;
import variable.NameVariableException;
import variable.TypeException;
import variables.Analyze;
import variables.ConditionException;
import variables.GrammarException;
import variables.ValueTypeException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSingleMethod {
    List<String> globalVars;
    private final static String IF_WHILE = ".*\\s*if|while\\s*\\(\\s*.*\\s*\\)\\s*\\{\\s*.*\\s*\\}$";
    private final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    List<String> declarationInit = new ArrayList<>();
    private final static String BLOCK_ERROR = "ERROR : there is error in block lines";

    public void checkMethods(Map<List<String>, List<String>> allMethods){
        globalVars = Parser.getGlobalVars();
        for (List<String> method: allMethods.keySet()){
            checkSingleMethod(method);
        }
    }

    private void checkSingleMethod(List<String> method) throws ConditionException {
        for (int i = 0 ; i < method.size() ; i++){
            Matcher ifWhileMatch = IF_WHILE_PATTERN.matcher(method.get(i));
            if (ifWhileMatch.matches()){
                i = findAllBlocks(method, i);
            }

        }
    }

    private int findAllBlocks(List<String> method, int i) throws ConditionException {
        List<List<String>> blocks = new ArrayList<>() ;
        List<String> mainBlock= ifWhileMethods.ifWhile(method.subList(i, method.size()));
        blocks.add(mainBlock);
        int startBlock = i;
        int endBlock = mainBlock.size();
        int temp = endBlock;
        while (endBlock - startBlock  > 2){
            List<String> innerBlock= ifWhileMethods.ifWhile(method.subList(startBlock+1, endBlock-1));
            if (innerBlock.size() == 0 )
                break;
            else blocks.add(innerBlock);
            startBlock ++;
            endBlock --;
        }
        checkInnerBlocks(blocks);
        i = temp;
        return i;
    }

    private void checkInnerBlocks(List<List<String>> blocks) throws NameVariableException, TypeException, ValueTypeException, GrammarException, FinalVariableException, BlockException {
        for (int i= blocks.size() ; i-- > 0 ;){
            for(String line : blocks.get(i)){
                Matcher variableMatch = VARIABLE_SUFFIX_PATTERN.matcher(line);

                Matcher callMethodMatch = mainMethod.METHOD_CALL_PATTERN.matcher(line);
                if (variableMatch.matches()){ // its a var//
                    Analyze.analyzer(line);
                    if (Analyze.declarationWithInit(line)) {
                        String[] names = Analyze.getName(line);
                        for (String name : names) {
                            if (declarationInit.contains(name)) {
                                throw new BlockException(BLOCK_ERROR);
                            }
                            declarationInit.add(name);
                        }
                    }


                }if (callMethodMatch.matches()){

                }
            }

        }
    }
}
