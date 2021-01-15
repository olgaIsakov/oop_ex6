package oop.ex6.methods;

import oop.ex6.main.StructureException;
import oop.ex6.variables.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;

/**
 * This class checks if/ while blocks
 */
public class IfWhileMethods {
    private static final int INITIALIZE_COUNTER = 0;
    private static final String START_CONDITION = "(";
    private static final String END_CONDITION = ")";
    final static int EMPTY_BLOCK = 0;
    final static int FIRST = 0;
    final static int SECOND_LINE =1;

    final static String ERROR_CONDITION = "ERROR: problem found in if/while block";
    final static String BLOCK_ERROR = " ERROR : error in block line ";
    final static String INVALID_LINE_ERROR = "ERROR: Invalid line found";
    public static List<String> declarationInit = new ArrayList<>();

    /**
     * A method to check if an if\ while block is valid
     * @param method a method to check
     * @return true if block is legal, false otherwise
     */
    public static List<String> ifWhile(List<String> method) throws ConditionException {

        List<String> block = new ArrayList<>();
        String firstLine = method.get(FIRST) ;
        int startParenthesis = firstLine.indexOf(START_CONDITION);
        int endParenthesis = firstLine.indexOf(END_CONDITION);
        // empty condition
        if (startParenthesis + 1 == endParenthesis) throw new ConditionException(ERROR_CONDITION);
        String sub = firstLine.substring(startParenthesis + 1, endParenthesis);
        if (!Condition.analyzeCondition(sub))
            throw new ConditionException(ERROR_CONDITION);
        int counter = INITIALIZE_COUNTER;
        for (String lineCurr : method) {
            Matcher matchOpen = MethodPatterns.OPEN_PATTERN.matcher(lineCurr);
            Matcher matchClose = MethodPatterns.CLOSE_PATTERN.matcher(lineCurr);
            Matcher matchIllegalOpen = MethodPatterns.ILLEGAL_OPEN_PATTERN.matcher(lineCurr);
            Matcher matchIllegalClose = MethodPatterns.ILLEGAL_CLOSE_PATTERN.matcher(lineCurr);
            if (matchOpen.find()) {
                if (matchIllegalOpen.find()) throw new ConditionException(ERROR_CONDITION);
                else counter++;
            }
            if (matchClose.matches()) {
                if (matchIllegalClose.find()) throw new ConditionException(ERROR_CONDITION);

                else {
                    counter--;
                }
            }
            block.add(lineCurr);
            if (counter == INITIALIZE_COUNTER)
                break;
        }
            if (counter != INITIALIZE_COUNTER) throw new ConditionException(ERROR_CONDITION);
            return block;
        }

    /**
     * a method to find all if/while blocks in the block
      * @param method the method
     * @param i the line we are at in the method check
     * @param nameMethod the method name
     * @return the method line after the block
     * @throws ConditionException invalid condition found
     * @throws VariableException invalid variable found
     * @throws BlockException invalid block found
     * @throws MethodException Invalid method
     * @throws StructureException Invalid block structure
     */
    static int findAllBlocks(List<String> method, int i, String nameMethod)
            throws ConditionException, VariableException, BlockException, MethodException, StructureException {
        List<List<String>> blocks = new ArrayList<>() ;
        List<String> mainBlock= IfWhileMethods.ifWhile(method.subList(i, method.size()));
        blocks.add(mainBlock);
        int startBlock = i+1;
        int endBlock = mainBlock.size()+1;
        int temp = endBlock;
        while (startBlock  < endBlock ){
            Matcher ifWhileMatch = MethodPatterns.IF_WHILE_PATTERN.matcher(method.get(startBlock));
            if (ifWhileMatch.matches()) {
                List<String> innerBlock = IfWhileMethods.ifWhile(method.subList(startBlock, endBlock));
                if (innerBlock.size() == EMPTY_BLOCK)
                    break;
                else{
                    blocks.add(innerBlock);
                    endBlock = startBlock+ innerBlock.size();}
            }
            startBlock ++;
        }
        List<List<String>> newBlocks = innerBlockLines(blocks,method);
        checkInnerBlocks(newBlocks,nameMethod);
        i = temp;
        return i;
    }

    /**
     * This method splits nested blocks to separate blocks
     * @param blocks the block lists (with nested blocks)
     * @param method the method
     * @return a list with separate blocks
     */
    static  List<List<String>> innerBlockLines(List<List<String>> blocks, List<String> method) {
        List<List<String>> blockList = new ArrayList<>();
        TreeMap<Integer, Integer> innerIdxMap = new TreeMap<>();
        for (int i= blocks.size() ; i-- > 0 ;){
            int firstLineIdx = method.lastIndexOf(blocks.get(i).get(FIRST));
            int lastLineIdx =  firstLineIdx + blocks.get(i).size() -1 ;
            innerIdxMap.put(firstLineIdx, lastLineIdx);
        }
        innerIdxMap.remove(innerIdxMap.descendingKeySet().last() );
        int blockCounter = INITIALIZE_COUNTER ;
        for (List<String> block: blocks){
            if (blockCounter == blocks.size() -1){
                blockList.add(block);
                break;
            }
            Map.Entry<Integer, Integer> biggestIdx = innerIdxMap.firstEntry() ;
            int InnerBegin = block.indexOf(method.get(biggestIdx.getKey()));
            int InnerLast = InnerBegin + biggestIdx.getValue() - biggestIdx.getKey() ;
            List<String> newBlock = new ArrayList<>();
            newBlock.addAll(block.subList(FIRST , InnerBegin)) ;
            newBlock.addAll(block.subList(InnerLast+1, block.size())) ;
            blockList.add(newBlock) ;
            innerIdxMap.remove(biggestIdx.getKey()) ;
            blockCounter ++;
        }
        return blockList ;
    }


    /**
     * a method to check each if/while block (without nested blocks)
     * @param blocks the separate block list
     * @param nameMethod the method name
     * @throws VariableException invalid variable found
     * @throws BlockException invalid block found
     * @throws MethodException Invalid method
     * @throws StructureException Invalid block structure
     */
    static void checkInnerBlocks(List<List<String>> blocks, String nameMethod)
            throws VariableException, BlockException, MethodException, StructureException {
        for (int i= blocks.size() ; i-- > 0 ;){
            List<String> slicedBlock = blocks.get(i).subList(SECOND_LINE, blocks.get(i).size());
            for(String line : slicedBlock){
                Matcher variableMatch = MethodPatterns.VARIABLE_SUFFIX_PATTERN.matcher(line);
                Matcher callMethodMatch = MethodPatterns.METHOD_CALL_PATTERN.matcher(line);
                Matcher closeMatcher = MethodPatterns.CLOSE_PATTERN.matcher(line);
                Matcher illegalMatcher = MethodPatterns.ILLEGAL_CLOSE_PATTERN.matcher(line);
                Matcher returnMatcher = MethodPatterns.RETURN_PATTERN.matcher(line);

                if (variableMatch.find() && !returnMatcher.matches()
                        && !callMethodMatch.matches()){
                    VariableAnalyzer.analyzeLineVariable(line,false);

                    if (VariableAnalyzer.declarationWithInit(line)) {
                        String[] names = VariableAnalyzer.getName(line);
                        for (String name : names) {
                            if (name.equals(nameMethod))
                                throw new BlockException(BLOCK_ERROR);
                            if (declarationInit.contains(name))
                                throw new BlockException(BLOCK_ERROR);
                            declarationInit.add(name);
                        }
                    }
                }else if (callMethodMatch.matches())
                    MainMethod.checkMethodCall(line);
                else {
                    if (!((closeMatcher.matches())&& !(illegalMatcher.find()))&&(!returnMatcher.matches())){
                        throw new StructureException(INVALID_LINE_ERROR);

                    }
                }
            }

        }
    }

}
