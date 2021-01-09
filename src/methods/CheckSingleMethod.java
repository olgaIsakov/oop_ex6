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
    private final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    private final static String SPACES = "\\s+";
    final static String EMPTY_SPACE = " ";
    final static int FIRST = 0;
    final static int SECOND = 1;
    final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);

    public static void checkMethods(Map<String, List<String>> mapNameLines) throws ConditionException, MethodException {
        globalVars = Parser.getGlobalVars();
        for (Map.Entry<String, List<String>> nameAndLines: mapNameLines.entrySet()){
            List<String> method = nameAndLines.getValue();
            String name = nameAndLines.getKey() ;
            checkSingleMethod(method, name);
        }
    }

    private void checkSingleMethod(List<String> method, String name) throws ConditionException, MethodException {
        for (int i = 0 ; i < method.size() ; i++){
            Matcher ifWhileMatch = IF_WHILE_PATTERN.matcher(method.get(i));
            Matcher methodCallMatch = METHOD_CALL_PATTERN.matcher(method.get(i));
            Matcher varsMatch = VARIABLE_SUFFIX_PATTERN.matcher(method.get(i));
            if (ifWhileMatch.matches()){
                i = findAllBlocks(method, i);
            }
            if (methodCallMatch.matches()){
                mainMethod.checkMethodCall(method.get(i),name );
            }
            if (varsMatch.matches()){

            }
            mainMethod.checkReturnStatement(method);
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
