package variables;

import java.util.ArrayList;
import java.util.List;

public class GlobalVariables {
    final static String GLOBAL_ERROR = "ERROR: global name initialized more than once.";

    public static void checkGlobalVars(List<String> globalVars) throws VariableException {
        List<String> globalNames = new ArrayList<>();
        for (String line: globalVars){
            Analyze.analyzer(line);
            String[] nameArray = Analyze.getName(line);
            for (String name: nameArray){
                if (globalNames.contains(name)) throw new VariableException(GLOBAL_ERROR) ;
                else globalNames.add(name) ;
            }
        }
    }

}
