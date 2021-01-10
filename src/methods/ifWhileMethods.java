package methods;

import variables.ConditionException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ifWhileMethods {
    private static final int INITIALIZE_COUNTER = 0;
    private static final String START_CONDITION = "(";
    private static final String END_CONDITION = ")";
    private final static String OPEN = "\\s*[{]\\s*$";
    private final static String CLOSE = "\\s*}\\s*$";
    private final static String ILLEGAL_OPEN = "\\s*[{][{]+\\s*$";
    private final static String ILLEGAL_CLOSE = "\\s*[}][}]+\\s*$";
    private final static String IF_WHILE = "^\\s*+if|while\\s*\\(\\s*";

    private final static String WHILE = "^\\s*+while\\s*\\(\\s*";
    private final static String IF = "^\\s*+if\\s*\\(\\s*";
    final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    final static Pattern OPEN_PATTERN = Pattern.compile(OPEN);
    final static Pattern CLOSE_PATTERN = Pattern.compile(CLOSE);
    final static Pattern ILLEGAL_OPEN_PATTERN = Pattern.compile(ILLEGAL_OPEN);
    final static Pattern ILLEGAL_CLOSE_PATTERN = Pattern.compile(ILLEGAL_CLOSE);
    final static String ERROR_CONDITION = "ERROR: problem found in if/while block";

    /**
     * A method to check if an if\ while block is valid
     * @param method a method to check
     * @return true if block is legal, false otherwise
     */
    public static List<String> ifWhile(List<String> method) throws ConditionException {
        List<String> block = new ArrayList<>();
        for (String line: method){
            Matcher matchIfWhile = IF_WHILE_PATTERN.matcher(line);
            if (matchIfWhile.matches()){
                int startParenthesis = line.indexOf(START_CONDITION);
                int endParenthesis = line.indexOf(END_CONDITION);
                // empty condition
                if (startParenthesis + 1 == endParenthesis) throw new ConditionException(ERROR_CONDITION);
                String sub = line.substring(startParenthesis + 1, endParenthesis);
                if (!variables.Condition.analyzeCondition(sub))
                    throw new ConditionException(ERROR_CONDITION);
                int counter = INITIALIZE_COUNTER;
                for (int i = method.indexOf(line); i< method.size(); i++){
                    Matcher matchOpen = OPEN_PATTERN.matcher(method.get(i));
                    Matcher matchClose = CLOSE_PATTERN.matcher(method.get(i));
                    Matcher matchIllegalOpen = ILLEGAL_OPEN_PATTERN.matcher(method.get(i));
                    if (matchOpen.matches()) {
                        if (matchIllegalOpen.matches()) throw new ConditionException(ERROR_CONDITION);
                        else counter++;
                    }
                    if (matchClose.matches())
                        counter--;
                    block.add(method.get(i));
                    if (counter == INITIALIZE_COUNTER)
                        break;
                }
                if (counter != INITIALIZE_COUNTER) throw new ConditionException(ERROR_CONDITION);
            }
        }
        return block;
    }
}
