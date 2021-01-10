package methods;

import variables.ConditionException;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TestMethod {

    void foo(int i){
        if (i>0){
            int j = 6 ;
            while (i>0){
                int w = 7 ;
                if(i ==5 ){
                    int d = 8 ;
                }
            }

        }
    }
    static List<String> block1 = List.of("if (i>0){", "int j = 6;", "while (i>0){", "int w = 7 ;", "if(i ==5 ){", "int d = 8 ;", "}", "}", "}");
    static List<String> block2 = List.of( "while (i>0){", "int w = 7 ;", "if(i ==5 ){", "int d = 8 ;", "}", "}" );
    static List<String> block3 = List.of( "if(i ==5 ){", "int d = 8 ;", "}");
    static List<List<String>> blocks = List.of(block1,block2,block3);
    static List<String> method = List.of("void foo(int i){","if (i>0){", "int j = 6;", "while (i>0){", "int w = 7 ;", "if(i ==5 ){", "int d = 8 ;", "}", "}", "}","}");


    public static void main(String[] args) throws ConditionException {
        System.out.println(innerBlockLines(blocks,method));
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
}
