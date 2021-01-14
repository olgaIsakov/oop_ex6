package oop.ex6.variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class Variable {

    /*The valid types */
    private static final List<String> typesList=  List.of("String","int","double","boolean","char");

    /**
     * This method checks if the name is valid
     * @param  varName
     * @return true if valid false otherwise
     */
    public static boolean isNameValid(String varName){
        Matcher matcher = VariablesPattern.NAME_VARIABLE_PATTERN.matcher(varName);
        return matcher.matches();
    }
    /**
     * This method checks if the type is valid
     * @param  typeName
     * @return true if valid false otherwise
     */
    public static boolean isTypeNameValid(String typeName){
        return typesList.contains(typeName);

    }















}
