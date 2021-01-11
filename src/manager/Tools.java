package manager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    private final static String INT_TYPE = "^\\s*[-]?\\d+\\s*$";
    private final static String DOUBLE_TYPE = "^\\s*[-]?\\d*\\.?\\d+\\s*$";
    private final static String STRING_TYPE = "^\\s*\\.*?\\s*$";
    private final static String CHAR_TYPE = "^\\s*\\d*\\s*$";
    private final static String BOOL_TYPE = "^\\s*true|false\\s*$";
    final static Pattern INT_TYPE_PATTERN = Pattern.compile(INT_TYPE);
    final static Pattern DOUBLE_TYPE_PATTERN = Pattern.compile(DOUBLE_TYPE);
    final static Pattern STRING_TYPE_PATTERN = Pattern.compile(STRING_TYPE);
    final static Pattern CHAR_TYPE_PATTERN = Pattern.compile(CHAR_TYPE);
    final static Pattern BOOL_TYPE_PATTERN = Pattern.compile(BOOL_TYPE);
    final static String INT = "int";
    final static String STRING = "String";
    final static String DOUBLE = "double";
    final static String BOOLEAN = "boolean";
    final static String CHAR = "char";
    /**
     * this method checks if the given parameter is of the wanted type
     * @param wantedType the expected type
     * @param calledParam the given parameter
     * @return true if the type matches, false otherwise
     */

    public static boolean checkType(String wantedType, String calledParam) {
        Matcher intMach = INT_TYPE_PATTERN.matcher(calledParam);
        Matcher stringMach = STRING_TYPE_PATTERN.matcher(calledParam);
        Matcher doubleMach = DOUBLE_TYPE_PATTERN.matcher(calledParam);
        Matcher charMach = CHAR_TYPE_PATTERN.matcher(calledParam);
        Matcher boolMach = BOOL_TYPE_PATTERN.matcher(calledParam);
        switch (wantedType){
            case INT:
                return intMach.matches();
            case STRING:
                return stringMach.matches();
            case BOOLEAN:
                return (intMach.matches() || doubleMach.matches() || boolMach.matches());
            case CHAR:
                return charMach.matches();
            case DOUBLE:
                return (intMach.matches() || doubleMach.matches());
        }
        return false;
    }
}
