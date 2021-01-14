package oop.ex6.variables;

import java.util.regex.Pattern;

public class VariablesPattern {
    public final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    public final static String INT_TYPE = "^\\s*[-]?\\d+\\s*$";
    public final static String DOUBLE_TYPE = "^\\s*[-]?\\d*\\.?\\d+\\s*$";
    public final static String STRING_TYPE = "^\\s*\".*?\"s*$";
    public final static String CHAR_TYPE = "^\\s*\'.*?\'s*$";
    public final static String BOOL_TYPE = "^\\s*(true|false)\\s*$";
    public final static String CONDITION = "true|false|(?:-?(?:\\d+(?:\\.\\d*)?)\\s*|(?:\\.\\d+)\\s*])";
    public final static String VAR_NAME = "(?:[a-zA-Z]+\\w*)|(?:_+\\w+)";
    public final static String OPERATORS = "[&&||]+" ;

    public final static Pattern INT_TYPE_PATTERN = Pattern.compile(INT_TYPE);
    public final static Pattern DOUBLE_TYPE_PATTERN = Pattern.compile(DOUBLE_TYPE);
    public final static Pattern STRING_TYPE_PATTERN = Pattern.compile(STRING_TYPE);
    public final static Pattern CHAR_TYPE_PATTERN = Pattern.compile(CHAR_TYPE);
    public final static Pattern BOOL_TYPE_PATTERN = Pattern.compile(BOOL_TYPE);
    public final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);
    public static final Pattern CONDITION_PATTERN = Pattern.compile(CONDITION);
    public static final Pattern NAME_VARIABLE_PATTERN =  Pattern.compile(VAR_NAME);
}
