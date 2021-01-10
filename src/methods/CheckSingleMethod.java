package methods;

import manager.Parser;
import manager.StructureException;
import variables.Analyze;
import variables.ConditionException;

import variables.VariableException;

import java.sql.ParameterMetaData;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckSingleMethod {
    private static final String START_PARAMETERS = "(";
    List<String> globalVars;
    private final static String IF_WHILE = ".*\\s*if|while\\s*\\(\\s*.*\\s*\\)\\s*\\{\\s*.*\\s*\\}$";
    private final static String METHOD_CALL = "[a-zA-Z]\\w*\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
    private final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    private final static String CLOSE = "\\s*}\\s*$";
    private final static String ILLEGAL_CLOSE = "\\s*[}][}]+\\s*$";
    final static Pattern CLOSE_PATTERN = Pattern.compile(CLOSE);
    final static Pattern ILLEGAL_CLOSE_PATTERN = Pattern.compile(ILLEGAL_CLOSE);
    final static int TWO_LINES = 2 ;
    final static int EMPTY_BLOCK = 0;
    private final static String SPACES = "\\s+";
    final static String EMPTY_SPACE = " ";
    final static int FIRST = 0;
    final static int SECOND = 1;
    final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);
    static List<String> declarationInit = new ArrayList<>();


    final static String BLOCK_ERROR = " ERROR : error in block line ";
    final static String INVALID_LINE_ERROR = "ERROR: Invalid line found";

    public static void checkMethods(Map<String, List<String>> mapNameLines)
            throws ConditionException, MethodException, VariableException, StructureException, BlockException {
        List<String> globalVars = Parser.getGlobalVars();
        for (Map.Entry<String, List<String>> nameAndLines: mapNameLines.entrySet()){
            List<String> method = nameAndLines.getValue();
            String name = nameAndLines.getKey() ;
            checkSingleMethod(method, name);
        }
    }

    private static void checkSingleMethod(List<String> method, String name)
            throws ConditionException, MethodException, VariableException, StructureException, BlockException {
        for (int i = 0 ; i < method.size() ; i++){
            Matcher ifWhileMatch = IF_WHILE_PATTERN.matcher(method.get(i));
            Matcher methodCallMatch = METHOD_CALL_PATTERN.matcher(method.get(i));
            Matcher varsMatch = VARIABLE_SUFFIX_PATTERN.matcher(method.get(i));
            if (ifWhileMatch.matches()){
                i = findAllBlocks(method, i , name);
            }
            if (methodCallMatch.matches()){
                mainMethod.checkMethodCall(method.get(i),name );
            }
            if (varsMatch.matches()){
                Analyze.analyzer(method.get(i));
            }
            mainMethod.checkReturnStatement(method);
        }
    }

    private static int findAllBlocks(List<String> method, int i, String nameMethod)
            throws ConditionException, VariableException, BlockException, MethodException, StructureException {
        List<List<String>> blocks = new ArrayList<>() ;
        List<String> mainBlock= ifWhileMethods.ifWhile(method.subList(i, method.size()));
        blocks.add(mainBlock);
        int startBlock = i;
        int endBlock = mainBlock.size();
        int temp = endBlock;
        while (endBlock - startBlock  > TWO_LINES ){
            List<String> innerBlock= ifWhileMethods.ifWhile(method.subList(startBlock+1, endBlock-1));
            if (innerBlock.size() == EMPTY_BLOCK  )
                break;
            else blocks.add(innerBlock);
            startBlock ++;
            endBlock --;
        }
        checkInnerBlocks(blocks,nameMethod);
        i = temp;
        return i;
    }

    private static  List<List<String>> innerBlockLines(List<List<String>> blocks,List<String> method){
        List<List<String>> blockList = new ArrayList<>();
        TreeMap<Integer, Integer> innerIdxMap = new TreeMap<>();
        for (int i= blocks.size() ; i-- > 0 ;){
            int firstLineIdx = method.lastIndexOf(blocks.get(i).get(0));
            int lastLineIdx = method.indexOf(blocks.get(i).get(blocks.get(i).size())) ;
            innerIdxMap.put(firstLineIdx, lastLineIdx);
        }
        innerIdxMap.remove(innerIdxMap.descendingKeySet().first());
        for (List<String> block: blocks){
            Map.Entry<Integer, Integer> biggestIdx = innerIdxMap.lastEntry() ;
            int InnerBegin = block.indexOf(method.get(biggestIdx.getKey()));
            int InnerLast = block.indexOf(method.get(biggestIdx.getValue()));
            blockList.add(block.subList(InnerBegin, InnerLast)) ;
            innerIdxMap.remove(biggestIdx.getKey()) ;
        }
        return blockList ;
    }

   void foo(int i){ // [109, 117] ,[111, 116], [113, 115]
        if (i>5){
            i =8;
            while (i %2 ==0){
                i =5;
                while ( i> 5){
                    i =4;
                }
            }
        }
   }

    private static void checkInnerBlocks(List<List<String>> blocks ,String nameMethod)
            throws VariableException, BlockException, MethodException, StructureException {
        int firstLineLastBlock = -1;
        int lastLineLastBlock = -1;
        for (int i= blocks.size() ; i-- > 0 ;){
            for(String line : blocks.get(i)){
                Matcher variableMatch = VARIABLE_SUFFIX_PATTERN.matcher(line);
                Matcher callMethodMatch = mainMethod.METHOD_CALL_PATTERN.matcher(line);
                Matcher closeMatcher = CLOSE_PATTERN.matcher(line);
                Matcher illegalMatcher = ILLEGAL_CLOSE_PATTERN.matcher(line);

                if (variableMatch.matches()){ // its a var//
                    Analyze.analyzer(line);

                    if (Analyze.declarationWithInit(line)) {
                        String[] names = Analyze.getName(line);
                        for (String name : names) {
                            if (name.equals(nameMethod))
                                throw new BlockException(BLOCK_ERROR);
                            if (declarationInit.contains(name))
                                throw new BlockException(BLOCK_ERROR);
                            declarationInit.add(name);
                        }
                    }
                }if (callMethodMatch.matches()){
                    mainMethod.checkMethodCall(line,nameMethod);

                }else {
                    if (!((closeMatcher.matches())&& !(illegalMatcher.matches()))){
                        throw new StructureException(INVALID_LINE_ERROR);

                    }
                }
            }

        }
    }
}
