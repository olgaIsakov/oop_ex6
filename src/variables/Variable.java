package variables;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public abstract class Variable {
    private final String variableName ;
    private final String variableType ;

    private final boolean isFinal ;
    private final boolean isInit ;
    private final boolean isLocal ;
    private final boolean isGlobal ;

    private static final List<String> typesList=  new ArrayList<String>();
    private static final List<String> globalVariables = new ArrayList<String>();
    private static final List<String> initializedVariables = new ArrayList<String>();



    /*Patterns*/
    private static final Pattern NAME_VARIABLE_PATTERN =  Pattern.compile("(?:[a-zA-Z]+\\w*)|(?:_+\\w+)");
    private static final Pattern DECLARED_WITH_INIT =  Pattern.compile("\\s*\\w+\\s*=\\s*.+?\\s*;\\s*");////
    private static final Pattern DECLARED_WITHOUT_INIT =  Pattern.compile("\\s*\\w+\\s*\\s*;\\s*");//////

    public Variable(String variableName, String variableType, boolean isFinal, boolean isLocal, boolean isGlobal,boolean isInit) {
        this.variableName = variableName;
        this.variableType = variableType;
        this.isFinal = isFinal;
        this.isLocal = isLocal;
        this.isGlobal = isGlobal;
        this.isInit = isInit ;

        typesList.add("String");
        typesList.add("int");
        typesList.add("double");
        typesList.add("boolean");
        typesList.add("char");


    }



    public static boolean isNameValid(String varName){
        Matcher matcher = NAME_VARIABLE_PATTERN.matcher(varName);
        return matcher.matches();
    }
    private boolean isNameValidGlobal(String varName){
        if (isVariableGlobal()){
            return !globalVariables.contains(varName);
        }
        return true;
    }

    public static boolean isTypeNameValid(String typeName){
        return typesList.contains(typeName);

    }
    public String getVariableName (){
        return this.variableName;
    }
    public String getVariableType (){
        return this.variableType;
    }
    public boolean isVariableFinal(){
        return this.isFinal;
    }
    public boolean isVariableInitialized(){
        return this.isInit;
    }
    public boolean isVariableGlobal(){
        return this.isGlobal;
    }

    private boolean finalAndInitialized(){
        if (isVariableFinal()){
            if (!(isVariableInitialized())){
                return false;//exception//
            }
        }
        return true;
    }

    public abstract boolean isValueValid(String value);










}
