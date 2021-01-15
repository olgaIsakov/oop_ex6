package oop.ex6.methods;

import java.util.regex.Pattern;

/**
 * A class for method related regexes and patterns
 */
public class MethodPatterns {
    public final static String IF_WHILE = "^\\s*+(if|while)\\s*\\(\\s*.*\\s*\\)\\s*[{]\\s*$";
    public final static String METHOD_CALL = "\\s*[a-zA-Z]\\w*\\s*\\(\\s*.*\\s*\\)\\s*;\\s*$";
    public final static String RETURN = "^\\s*return\\s*;\\s*$";
    public final static String VARIABLE_SUFFIX = "\\s*;\\s*$";
    public final static String CLOSE = "\\s*}\\s*$";
    public final static String ILLEGAL_CLOSE = "\\s*[}][}]+\\s*$";
    public final static String OPEN = "\\s*[{]\\s*$";
    public final static String ILLEGAL_OPEN = "\\s*[{][{]+\\s*$";
    public final static String SPACES = "(\\s+|\\t+)";
    public final static String PARAM_START = "\\s*\\(\\s*";
    public final static String METHOD = "\\s*void\\s+[a-zA-Z]\\w*\\s*\\(.*\\s*\\)\\s*[{]\\s*$";

    public final static Pattern CLOSE_PATTERN = Pattern.compile(CLOSE);
    public final static Pattern ILLEGAL_CLOSE_PATTERN = Pattern.compile(ILLEGAL_CLOSE);
    public final static Pattern METHOD_CALL_PATTERN = Pattern.compile(METHOD_CALL);
    public final static Pattern IF_WHILE_PATTERN = Pattern.compile(IF_WHILE);
    public final static Pattern VARIABLE_SUFFIX_PATTERN = Pattern.compile(VARIABLE_SUFFIX);
    public final static Pattern RETURN_PATTERN = Pattern.compile(RETURN);
    public final static Pattern OPEN_PATTERN = Pattern.compile(OPEN);
    public final static Pattern ILLEGAL_OPEN_PATTERN = Pattern.compile(ILLEGAL_OPEN);
    public final static Pattern METHOD_LINE_PATTERN = Pattern.compile(METHOD);
}
