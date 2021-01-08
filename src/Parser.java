
import methods.MethodException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    final static int INITIALIZED_COUNTER = 0;
    private static final String EMPTY_SPACE = " ";
    private final static String ILLEGAL_OPEN = "\\s*[{][{]+\\s*$";
    private final static String METHOD = "\\s*void\\s+[a-zA-Z]\\w*\\s*\\(.*\\s*\\)\\s*[{]\\s*$";
    private final static String OPEN = "\\s*[{]\\s*$";
    private final static String CLOSE = "\\s*}\\s*$";
    final static Pattern OPEN_PATTERN = Pattern.compile(OPEN);
    final static Pattern CLOSE_PATTERN = Pattern.compile(CLOSE);
    final static Pattern ILLEGAL_OPEN_PATTERN = Pattern.compile(ILLEGAL_OPEN);
    final static Pattern METHOD_PATTERN = Pattern.compile(METHOD);
    final static String ERROR_MSG = "ERROR: Illegal method format";

    /**
     * this method parse the file into methods
     * @param sJavaLines the given file
     * @return a map -> list of the method lines : list of the method parameters
     */
    public Map<List<String>, List<String>> parseToMethods(String[] sJavaLines) throws MethodException {
        Map<List<String>, List<String>> singleMethods = new HashMap<>();
        int parenthesisCounter = INITIALIZED_COUNTER;
        for (int i = 0; i < sJavaLines.length; i++) {
            Matcher methodStructure = METHOD_PATTERN.matcher(sJavaLines[i]);
            if (methodStructure.matches()) {
                Matcher illegalOpen = ILLEGAL_OPEN_PATTERN.matcher(sJavaLines[i]);
                if (illegalOpen.matches()) throw new MethodException(ERROR_MSG);
                else {
                    List<String> params = new ArrayList<>();
                    methods.mainMethod.isFirstMethodLineLegal(sJavaLines[i].split(EMPTY_SPACE), params);
                    List<String> methodLines = new ArrayList<>();
                    parenthesisCounter++;
                    methodLines.add(sJavaLines[i]);
                    i++;
                    while (parenthesisCounter > INITIALIZED_COUNTER) {
                        Matcher openParenthesis = OPEN_PATTERN.matcher(sJavaLines[i]);
                        Matcher closeParenthesis = CLOSE_PATTERN.matcher(sJavaLines[i]);
                        if (openParenthesis.matches())
                            parenthesisCounter++;
                        if (closeParenthesis.matches())
                            parenthesisCounter--;
                        methodLines.add(sJavaLines[i]);
                        i++;
                    }
                    singleMethods.put(methodLines, params);

                }
            }
        }
        return singleMethods;
    }

}
