package methods;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class paramCheck {
    private static final String START_PARAMETERS = "(";
    private static final String END_PARAMETERS = ")";
    private static final String EMPTY_SPACE = " ";
    private static final String NULL_MARK = "null";
    private static final int EMPTY_PARAMS = 0;
    private static final int FIRST_PARAM = 0;
    private final static String ONE_PARAMETER = "\\s*final?\\s+[a-zA-Z]+\\s*";
    private final static String INT_TYPE = "^\\s*[-]?\\d+\\s*$";
    private final static String DOUBLE_TYPE = "^\\s*[-]?\\d*\\.?\\d+\\s*$";
    private final static String STRING_TYPE = "^\\s*\\w*\\s*$";
    private final static String CHAR_TYPE = "^\\s*\\d*\\s*$";
    private final static String BOOL_TYPE = "^\\s*true|false\\s*$";
    final static Pattern ONE_PARAMETER_PATTERN = Pattern.compile(ONE_PARAMETER);
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
    final static List<String> typeOptions = List.of("int", "String", "double", "boolean", "char");

    /**
     * This method checks parameters called by another method
     * @param methodLine the line with the call for a method
     * @param parameters method parameters
     * @return is the method call legal
     */
    public  static boolean  checkCalledParams(String methodLine, List<String> parameters) {
        int paramsStart = methodLine.indexOf(START_PARAMETERS);
        int paramsEnd = methodLine.indexOf(END_PARAMETERS);
        String params = methodLine.substring(paramsStart, paramsEnd) ;
        if (params.length() == EMPTY_PARAMS)
            return parameters.get(FIRST_PARAM).equals(NULL_MARK);
        List<String> types = new ArrayList<>();
        for (String type: parameters){
            if (typeOptions.contains(type))
                types.add(type);
        }
        Matcher oneParam = ONE_PARAMETER_PATTERN.matcher(params);
        if (oneParam.matches())
            return CheckType(types.get(FIRST_PARAM), params);
        else{
            if (params.length() != types.size())
                return false;
            else{
                String[] splitParams = params.split(EMPTY_SPACE);
                for (int i=0; i < types.size() ; i++){
                    if (!CheckType(types.get(i) , splitParams[i]))
                        return false;
                }
                return true;
            }
        }

    }

    /**
     * this method checks if the given parameter is of the wanted type
     * @param wantedType the expected type
     * @param calledParam the given parameter
     * @return true if the type matches, false otherwise
     */
    private static boolean CheckType(String wantedType, String calledParam) {
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
                return doubleMach.matches();
        }
        return false;
    }
}
