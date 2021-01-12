package oop.ex6.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class Variable {


    private static final List<String> typesList=  List.of("String","int","double","boolean","char");


    /*Patterns*/
    private static final Pattern NAME_VARIABLE_PATTERN =  Pattern.compile("(?:[a-zA-Z]+\\w*)|(?:_+\\w+)");


    public static boolean isNameValid(String varName){
        Matcher matcher = NAME_VARIABLE_PATTERN.matcher(varName);
        return matcher.matches();
    }

    public static boolean isTypeNameValid(String typeName){
        return typesList.contains(typeName);

    }














}
