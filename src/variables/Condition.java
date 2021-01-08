package variables;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Condition {
    private static final String AND ="&&";
    private static final String OR ="||";
    private static final String OPEN_PAR ="(";
    private static final String CLOSE_PAR =")";

    public static final Pattern CONDITION_PATTERN = Pattern.compile("true|false|(?:-?(?:\\d+(?:\\.\\d*)?)\\s*|(?:\\.\\d+)\\s*])");


    public boolean analyzeCondition(String line) throws ConditionException{
        String[] conditions = splitOperator(line);

        for (String condition : conditions){
            if (!isCondition(condition.trim())){
                throw new ConditionException("");
            }

        }if (!counterOperators(line,conditions.length)){
            throw new ConditionException("");
        }return true;

    }

    public boolean counterOperators(String line , int numConditions ){
        String line1 = replacedAnd(line);
        String line2 = replacedAnd(line1);
        int counter = 0 ;
        for(int i = 0; i<line2.length(); i++) {
            if (line2.charAt(i)== '*') {
                counter++;
            }


        } return  (counter+1 == numConditions);
    }

    public  String replacedAnd(String line){
       return line.replace(AND, "*");
    }
    public  String replacedOr(String line){
        return line.replace(OR, "*");
    }


    public String[] splitOperator(String line){
        return line.split(AND+ OR);
    }
    public boolean isCondition(String condition){
        Matcher matcher = CONDITION_PATTERN.matcher(condition);
        return matcher.matches();
    }



}
