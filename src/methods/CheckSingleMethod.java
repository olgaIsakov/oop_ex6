package methods;

import manager.Parser;
import variables.ConditionException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSingleMethod {
    private static final String START_PARAMETERS = "(";
    List<String> globalVars;
    private final static String IF_WHILE = ".*\\s*if|while\\s*\\(\\s*.*\\s*\\)\\s*\\{\\s*.*\\s*\\}$";
    private final static String METHOD_CALL = "[a-zA-Z]\\w*\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
    private final static String SPACES = "\\s+";
    final static String EMPTY_SPACE = " ";
    final static int FIRST = 0;
    final static int SECOND = 1;
    final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);

    public void checkMethods(Map<List<String>, List<String>> allMethods) throws ConditionException {
        globalVars = Parser.getGlobalVars();
        for (List<String> method: allMethods.keySet()){
            String name = mainMethod.getMethodName(method.get(FIRST));
            checkSingleMethod(method, name);
        }
    }

    private void checkSingleMethod(List<String> method, String name) throws ConditionException {
        for (int i = 0 ; i < method.size() ; i++){
            Matcher ifWhileMatch = IF_WHILE_PATTERN.matcher(method.get(i));
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

    private void checkInnerBlocks(List<List<String>> blocks) {
        for (int i= blocks.size() ; i-- > 0 ;){

        }
    }
}
