
import java.util.*;

public class Parser {
    final static int INITIALIZED_COUNTER = 0;
    final static int FIRST_WORD = 0;
    private static final String METHOD_PREFIX = "void";
    private static final String EMPTY_SPACE = " ";
    private static final String OPEN_PARENTHESIS = "{";
    private static final String CLOSE_PARENTHESIS = "}";


    /**
     * this method parse the file into methods
     * @param sJavaLines the given file
     * @return a map -> list of the method lines : list of the method parameters
     */
    public Map<List<String>, List<String>> parseToMethods(String[] sJavaLines) {
        Map<List<String>, List<String>> singleMethods = new HashMap<>();
        int parenthesisCounter = INITIALIZED_COUNTER;
        for (int i = 0; i < sJavaLines.length; i++) {
            if (sJavaLines[i].split(EMPTY_SPACE)[FIRST_WORD].equals(METHOD_PREFIX)){
                List<String> params = new ArrayList<>() ;
                methods.mainMethod.isFirstMethodLineLegal(sJavaLines[i].split(EMPTY_SPACE), params);
                List<String> methodLines = new ArrayList<>();
                parenthesisCounter++;
                methodLines.add(sJavaLines[i]);
                i++;
                while (parenthesisCounter > INITIALIZED_COUNTER) {
                    String[] splitLine = sJavaLines[i].split(EMPTY_SPACE);
                    if (splitLine[sJavaLines.length - 1].endsWith(OPEN_PARENTHESIS))
                        parenthesisCounter++;
                    if (splitLine[sJavaLines.length - 1].endsWith(CLOSE_PARENTHESIS))
                        parenthesisCounter--;
                    methodLines.add(sJavaLines[i]);
                    i++;
                }
                singleMethods.put(methodLines, params);

            }
        }
        return singleMethods;
    }

}
