package variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BooleanVariable extends Variable{
    private static final Pattern VALUE_PATTERN =  Pattern.compile("temp!!!");


    public BooleanVariable(String variableName, String variableType, boolean isFinal, boolean isLocal, boolean isGlobal) {
        super(variableName, variableType, isFinal, isLocal, isGlobal);
    }

    @Override
    public boolean isValueValid(String value) {
        Matcher matcher = VALUE_PATTERN.matcher(value);
        return matcher.matches();
    }
}
