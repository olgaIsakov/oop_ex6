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
    private final static String IF_WHILE = "^\\s*+if|while\\s*\\(\\s*";
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

    /**
     * This method checks all the methods
     * @param mapNameLines a map of the method names and their lines
     * @throws ConditionException wrong condition given
     * @throws MethodException method check failed
     * @throws VariableException variable check failed
     * @throws StructureException invalid file structure
     * @throws BlockException invalid block found
     */
    public static void checkMethods(Map<String, List<String>> mapNameLines)
            throws ConditionException, MethodException, VariableException, StructureException, BlockException {
        List<String> globalVars = Parser.getGlobalVars();
        for (Map.Entry<String, List<String>> nameAndLines: mapNameLines.entrySet()){
            List<String> method = nameAndLines.getValue();
            String name = nameAndLines.getKey() ;
            checkSingleMethod(method, name);
        }
    }

    /**
     * This method checks each method by its components
     * @param method the method to check
     * @param name the method name
     * @throws ConditionException wrong condition given
     * @throws MethodException method check failed
     * @throws VariableException variable check failed
     * @throws StructureException invalid file structure
     * @throws BlockException invalid block found
     */
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

    /**
     * This method splits the method into if/while block from outer to inner
     * @param method the method to check
     * @param i the current index in the method
     * @param nameMethod the method name
     * @return the block list
     * @throws ConditionException wrong condition given
     * @throws MethodException method check failed
     * @throws VariableException variable check failed
     * @throws StructureException invalid file structure
     * @throws BlockException invalid block found
     */
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
        List<List<String>> newBlocks = innerBlockLines(blocks,method);
        checkInnerBlocks(newBlocks,nameMethod);
        i = temp;
        return i;
    }

    /**
     * This method splits each block so there will be no inner blocks
     * @param blocks the blocks
     * @param method the method
     * @return a new block list
     */
    private static  List<List<String>> innerBlockLines(List<List<String>> blocks, List<String> method) {
        List<List<String>> blockList = new ArrayList<>();
        TreeMap<Integer, Integer> innerIdxMap = new TreeMap<>();
        for (int i= blocks.size() ; i-- > 0 ;){
            int firstLineIdx = method.lastIndexOf(blocks.get(i).get(FIRST));
            int lastLineIdx =  firstLineIdx + blocks.get(i).size() -1 ;
            innerIdxMap.put(firstLineIdx, lastLineIdx);
        }
        innerIdxMap.remove(innerIdxMap.descendingKeySet().last() );
        int blockCounter = 0 ;
        for (List<String> block: blocks){
            if (blockCounter == blocks.size() -1){
                blockList.add(block);
                break;
            }
            Map.Entry<Integer, Integer> biggestIdx = innerIdxMap.firstEntry() ;
            int InnerBegin = block.indexOf(method.get(biggestIdx.getKey()));
            int InnerLast = InnerBegin + biggestIdx.getValue() - biggestIdx.getKey() ;
            List<String> newBlock = new ArrayList<>();
            newBlock.addAll(block.subList(FIRST, InnerBegin)) ;
            newBlock.addAll(block.subList(InnerLast+1, block.size())) ;
            blockList.add(newBlock) ;
            innerIdxMap.remove(biggestIdx.getKey()) ;
            blockCounter ++;
        }
        return blockList ;
    }


    /**
     * this method checks each inner block
     * @param blocks the block list
     * @param nameMethod the method name
     * @throws VariableException variable check failed
     * @throws BlockException invalid block found
     * @throws MethodException method check failed
     * @throws StructureException invalid file structure
     */
    private static void checkInnerBlocks(List<List<String>> blocks ,String nameMethod)
            throws VariableException, BlockException, MethodException, StructureException {
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
