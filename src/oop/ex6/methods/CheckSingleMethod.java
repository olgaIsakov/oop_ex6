package oop.ex6.methods;

import oop.ex6.main.Parser;
import oop.ex6.main.StructureException;
import oop.ex6.variables.VariableAnalyzer;
import oop.ex6.variables.ConditionException;

import oop.ex6.variables.VariableException;

import java.util.*;
import java.util.regex.Matcher;

public class CheckSingleMethod {
/*    private final static String IF_WHILE = "^\\s*+(if|while)\\s*\\(\\s*.*\\s*\\)\\s*[{]\\s*$";
    private final static String METHOD_CALL = "[a-zA-Z]\\w*\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
    private final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    private final static String CLOSE = "\\s*}\\s*$";
    private final static String ILLEGAL_CLOSE = "\\s*[}][}]+\\s*$";
    final static Pattern CLOSE_PATTERN = Pattern.compile(CLOSE);
    final static Pattern ILLEGAL_CLOSE_PATTERN = Pattern.compile(ILLEGAL_CLOSE);*/
    final static int EMPTY_BLOCK = 0;
    final static int FIRST = 0;
    final static int INITIALIZE_COUNTER = 0;
    final static int SECOND_LINE =1;
/*    final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);*/
    private static final String NULL_MARK = "null";
    public static List<String> declarationInit = new ArrayList<>();
/*    private final static String RETURN = "^\\s*return\\s*;\\s*$";
    final static Pattern RETURN_PATTERN = Pattern.compile(RETURN);*/


    final static String BLOCK_ERROR = " ERROR : error in block line ";
    final static String INVALID_LINE_ERROR = "ERROR: Invalid line found";

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
      /*  List<String> globalVars = Parser.getGlobalVars();*/
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
                i = ifWhileMethods.findAllBlocks(method, i , name);
            }
            else if (methodCallMatch.find()){
                mainMethod.checkMethodCall(method.get(i));
            }
            else if (varsMatch.find() && !returnMatcher.matches() ){
                VariableAnalyzer.analyzeLineVariable(method.get(i));
            }
            mainMethod.checkReturnStatement(method);
        }
    }

    private static void addParamsAsLocalVariables(String name) throws VariableException {
        List<String> params = Parser.getMapNameParams().get(name);
        for(int i =0 ;i<params.size()-1;i+=2){
            String paramLine = params.get(i) +" " +params.get(i + 1);
            if (!paramLine.equals(NULL_MARK)) {
                    VariableAnalyzer.analyzeLineVariable(paramLine);
                }
           }
    }
/*
    private static int findAllBlocks(List<String> method, int i, String nameMethod)
            throws ConditionException, VariableException, BlockException, MethodException, StructureException {
        List<List<String>> blocks = new ArrayList<>() ;
        List<String> mainBlock= ifWhileMethods.ifWhile(method.subList(i, method.size()));
        blocks.add(mainBlock);
        int startBlock = i+1;
        int endBlock = mainBlock.size()+1;
        int temp = endBlock;
        while (startBlock  < endBlock ){
            Matcher ifWhileMatch = MethodPatterns.IF_WHILE_PATTERN.matcher(method.get(startBlock));
            if (ifWhileMatch.matches()) {
                List<String> innerBlock = ifWhileMethods.ifWhile(method.subList(startBlock, endBlock));
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
    }*/

//    static  List<List<String>> innerBlockLines(List<List<String>> blocks, List<String> method) {
//        List<List<String>> blockList = new ArrayList<>();
//        TreeMap<Integer, Integer> innerIdxMap = new TreeMap<>();
//        for (int i= blocks.size() ; i-- > 0 ;){
//            int firstLineIdx = method.lastIndexOf(blocks.get(i).get(FIRST));
//            int lastLineIdx =  firstLineIdx + blocks.get(i).size() -1 ;
//            innerIdxMap.put(firstLineIdx, lastLineIdx);
//        }
//        innerIdxMap.remove(innerIdxMap.descendingKeySet().last() );
//        int blockCounter = INITIALIZE_COUNTER ;
//        for (List<String> block: blocks){
//            if (blockCounter == blocks.size() -1){
//                blockList.add(block);
//                break;
//            }
//            Map.Entry<Integer, Integer> biggestIdx = innerIdxMap.firstEntry() ;
//            int InnerBegin = block.indexOf(method.get(biggestIdx.getKey()));
//            int InnerLast = InnerBegin + biggestIdx.getValue() - biggestIdx.getKey() ;
//            List<String> newBlock = new ArrayList<>();
//            newBlock.addAll(block.subList(FIRST , InnerBegin)) ;
//            newBlock.addAll(block.subList(InnerLast+1, block.size())) ;
//            blockList.add(newBlock) ;
//            innerIdxMap.remove(biggestIdx.getKey()) ;
//            blockCounter ++;
//        }
//        return blockList ;
//    }




/*    static void checkInnerBlocks(List<List<String>> blocks, String nameMethod)
            throws VariableException, BlockException, MethodException, StructureException {
        for (int i= blocks.size() ; i-- > 0 ;){
            List<String> slicedBlock = blocks.get(i).subList(SECOND_LINE, blocks.get(i).size());
            for(String line : slicedBlock){
                Matcher variableMatch = MethodPatterns.VARIABLE_SUFFIX_PATTERN.matcher(line);
                Matcher callMethodMatch = mainMethod.METHOD_CALL_PATTERN.matcher(line);
                Matcher closeMatcher = MethodPatterns.CLOSE_PATTERN.matcher(line);
                Matcher illegalMatcher = MethodPatterns.ILLEGAL_CLOSE_PATTERN.matcher(line);
                Matcher returnMatcher = MethodPatterns.RETURN_PATTERN.matcher(line);

                if (variableMatch.find() && !returnMatcher.matches()
                        && !callMethodMatch.matches()){
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
                }else if (callMethodMatch.matches())
                    mainMethod.checkMethodCall(line);
                else {
                    if (!((closeMatcher.matches())&& !(illegalMatcher.find()))&&(!returnMatcher.matches())){
                        throw new StructureException(INVALID_LINE_ERROR);

                    }
                }
            }

        }
    }*/
}
