package variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IntVariable extends Variable {


    private static final Pattern VALUE_PATTERN =  Pattern.compile("^[0-9]+$");

    public IntVariable(String variableName, String variableType, boolean isFinal, boolean isLocal, boolean isGlobal) {
        super(variableName, variableType, isFinal, isLocal, isGlobal);
    }

    @Override
    public boolean isValueValid(String value) {
        Matcher matcher = VALUE_PATTERN.matcher(value);
        return matcher.matches();
    }
}
