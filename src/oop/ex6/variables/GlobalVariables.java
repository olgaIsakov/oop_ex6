package oop.ex6.variables;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to deal with global variables
 */
public class GlobalVariables {
    /*Error massage to print*/
    final static String GLOBAL_ERROR = "ERROR: global name initialized more than once.";

    /*List initialize*/
    public static List<String> globalNames = new ArrayList<>();

    /**
     * This method checks the validness of the global variables
     *
     * @param globalVars List of the global variables
     */
    public static void checkGlobalVars(List<String> globalVars) throws VariableException {
        for (String line : globalVars) {
            VariableAnalyzer.analyzeLineVariable(line, false);
            String[] nameArray = VariableAnalyzer.getName(line);
            for (String name : nameArray) {
                if (VariableAnalyzer.getType(line).isEmpty()) {
                    if (!globalNames.contains(name)) throw new VariableException(GLOBAL_ERROR);
                } else {
                    if (globalNames.contains(name)) throw new VariableException(GLOBAL_ERROR);
                    else globalNames.add(name);
                }
            }
        }
    }

}
