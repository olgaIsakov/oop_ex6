package methods;

import manager.Parser;
import manager.StructureException;
import variables.Analyze;
import variables.ConditionException;
import variables.VariableException;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;




import manager.Parser;
import manager.StructureException;
import variables.Analyze;
import variables.ConditionException;

import variables.VariableException;

import java.sql.ParameterMetaData;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class TestMethod {




    static List<String> block1 = List.of("if (true){", "int j = 6;", "while (5){", "int w = 7 ;", "if(false){", "int d = 8 ;", "}", "}", "}");
    static List<String> block2 = List.of( "while (i>0){", "int w = 7 ;", "if(i ==5 ){", "int d = 8 ;", "}", "}" );
    static List<String> block3 = List.of( "if(i ==5 ){", "int d = 8 ;", "}");
    static List<List<String>> blocks = List.of(block1,block2,block3);
    static List<String> method = List.of("void foo(int i){","if (true){", "int j = 6;", "while (5){", "int w = 7 ;", "if(false){", "int d = 8 ;", "}", "}", "}","return;","}");
    static String[] methodToArray = {"void foo(){","if (true){", "int j = 6;", "while (5){", "int w = 7 ;", "if(false){", "int d = 8 ;", "}", "}", "}","return;","}"};

        private static final String START_PARAMETERS = "(";
        List<String> globalVars;
        private final static String IF_WHILE = "^\\s*+(if|while)\\s*\\(\\s*.*\\s*\\)\\s*[{]\\s*$";
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
        static List<String> methodNames = List.of("fooo", "ddd");
        final static String ERROR_PARAM_MSG = "bla bla";
        static Map<String, List<String>> mapNameParams = new HashMap<>();
        final static String BLOCK_ERROR = " ERROR : error in block line ";
        final static String INVALID_LINE_ERROR = "ERROR: Invalid line found";
        static List<String> parammmmms = List.of("int","num,", "String", "name") ;

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
            for (int i = 1 ; i < method.size() ; i++){
                Matcher ifWhileMatch = IF_WHILE_PATTERN.matcher(method.get(i));
                Matcher methodCallMatch = METHOD_CALL_PATTERN.matcher(method.get(i));
                Matcher varsMatch = VARIABLE_SUFFIX_PATTERN.matcher(method.get(i));
                if (ifWhileMatch.matches()){
                    i = findAllBlocks(method, i , name);
                }
                else if (methodCallMatch.matches()){
                    mainMethod.checkMethodCall(method.get(i),name );
                }
                else if (varsMatch.matches()){
                    Analyze.analyzer(method.get(i));
                }

            }mainMethod.checkReturnStatement(method);
        }

        private static int findAllBlocks(List<String> method, int i, String nameMethod)
                throws ConditionException, VariableException, BlockException, MethodException, StructureException {
            List<List<String>> blocks = new ArrayList<>() ;
            List<String> mainBlock= ifWhileMethods.ifWhile(method.subList(i, method.size()));
            blocks.add(mainBlock);
            int startBlock = i+1;
            int endBlock = mainBlock.size()+1;
            int temp = endBlock;
            while (startBlock  < endBlock ){
                Matcher ifWhileMatch = IF_WHILE_PATTERN.matcher(method.get(startBlock));
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
        }

        private static  List<List<String>> innerBlockLines(List<List<String>> blocks, List<String> method) {
            List<List<String>> blockList = new ArrayList<>();
            TreeMap<Integer, Integer> innerIdxMap = new TreeMap<>();
            for (int i= blocks.size() ; i-- > 0 ;){
                int firstLineIdx = method.lastIndexOf(blocks.get(i).get(0));
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
                newBlock.addAll(block.subList(0, InnerBegin)) ;
                newBlock.addAll(block.subList(InnerLast+1, block.size())) ;
                blockList.add(newBlock) ;
                innerIdxMap.remove(biggestIdx.getKey()) ;
                blockCounter ++;
            }
            return blockList ;
        }




        private static void checkInnerBlocks(List<List<String>> blocks ,String nameMethod)
                throws VariableException, BlockException, MethodException, StructureException {
            for (int i= blocks.size() ; i-- > 0 ;){
                List<String> slicedBlock = blocks.get(i).subList(1,blocks.get(i).size());
                for(String line : slicedBlock){
                    Matcher variableMatch = VARIABLE_SUFFIX_PATTERN.matcher(line);
                    Matcher callMethodMatch = mainMethod.METHOD_CALL_PATTERN.matcher(line);
                    Matcher closeMatcher = CLOSE_PATTERN.matcher(line);
                    Matcher illegalMatcher = ILLEGAL_CLOSE_PATTERN.matcher(line);

                    if (variableMatch.find()){ // its a var//
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

                    }else if (callMethodMatch.matches()){
                        mainMethod.checkMethodCall(line,nameMethod);

                    }else {
                        if (!((closeMatcher.matches())&& !(illegalMatcher.matches()))){
                            throw new StructureException(INVALID_LINE_ERROR);

                        }
                    }
                }

            }

    }

static String line = "final int d = 8 ;";
    public static void main(String[] args) throws ConditionException, StructureException, BlockException, VariableException, MethodException {
       //manager.Parser.parseToMethods(methodToArray);
       checkMethodCall("fooo(1, 2);", "olga");


    }
//לא להעתיק אותה לקבוץ המקורי
    public static void checkMethodCall(String methodLine, String methodName) throws MethodException {
        mapNameParams.put("fooo", parammmmms);
        String called = mainMethod.getMethodName(methodLine);
        if (!methodNames.contains(called) || called.equals(methodName))
            throw new MethodException(ERROR_PARAM_MSG);
        else{
            //Map<String, List<String>> mapNameParams = manager.Parser.getMapNameParams();
            if (!paramCheck.checkCalledParams(methodLine, mapNameParams.get(called)))
                throw new MethodException(ERROR_PARAM_MSG);
        }
    }








}
