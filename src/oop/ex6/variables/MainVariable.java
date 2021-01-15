package oop.ex6.variables;

import java.util.List;
import java.util.regex.Matcher;

/**
 * A class for general methods related to variables
 */
public class MainVariable {

    /*The valid types */
    private static final List<String> typesList = List.of("String", "int", "double", "boolean", "char");
    private static final List<String> savedWords = List.of("String", "int", "double", "boolean",
            "char", "if", "while", "void", "final", "true", "false", "return");

    /**
     * This method checks if the name is valid
     * @param varName the name to check
     * @return true if valid false otherwise
     */
    public static boolean isNameValid(String varName) {
        Matcher matcher = VariablesPattern.NAME_VARIABLE_PATTERN.matcher(varName);
        return matcher.matches() && !savedWords.contains(varName);
    }

    /**
     * This method checks if the type is valid
     * @param typeName the type name to check
     * @return true if valid false otherwise
     */
    public static boolean isTypeNameValid(String typeName) {
        return typesList.contains(typeName);

    }


}
