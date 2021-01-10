package variables;



import manager.Tools;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyze {

    private static final String FINAL = "final";
    private static final String ERROR = "";

    private static final Pattern DECLARATION_PATTERN_WITH_INIT =  Pattern.compile("(_?[a-zA-Z0-9_]+)(\\s+)(([a-zA-Z]?_[a-zA-Z])?([a-zA-Z]*[0-9]*_*)(=[a-zA-Z0-9]*)?[,]?)+((\\s+)(([a-zA-Z]?_[a-zA-Z])?([a-zA-Z]*[0-9]*_*)(=[a-zA-Z0-9]*)?[,]?))*");
    private static final Pattern DECLARATION_PATTERN_WITHOUT_INIT =  Pattern.compile("(?:\\w+\\s+)([a-zA-Z_][a-zA-Z0-9_]*)");
    private static final Pattern NEW_DECLARATION = Pattern.compile("\\s*\\w+\\s+\\w[\\s*,\\w]*\\s*(?:=\\s*.+?)?\\s*;\\s*");
    private static final Pattern ASSIGNMENT =  Pattern.compile("\\s*\\w+\\s*=\\s*.+?\\s*;\\s*");
    private static final Pattern END_OF_LINE = Pattern.compile(";$");
    private static final Pattern FINAL_PATTERN =  Pattern.compile("^FINAL");
    private static final String START_PARAMETERS = "(";
    private static final String END_PARAMETERS = ")";
    private static final String EMPTY_SPACE = " ";
    private static final String NULL_MARK = "null";

    private static final String OPEN = "{";
    private static final String CLOSE = "}";
    private static final String EQUAL= "=";
    private static final String SEM = ";";
    static HashMap<String,String>  listVariables = new HashMap<String,String>();


    private static final String ERROR_GRAMMAR = "ERROR : no ; at the end of declaration  ";
    private static final String ERROR_FINAL = "ERROR : the variable is final but without initialization  ";
    private static final String ERROR_TYPE = "ERROR : invalid type name ";
    private static final String ERROR_NAME = "ERROR : invalid variable  name ";
    private static final String ERROR_VALUE = "ERROR : invalid variable value ";

    public static void analyzer(String line) throws VariableException {
        Matcher matcher = END_OF_LINE.matcher(line);
        if(!matcher.matches()) {
            throw new VariableException(ERROR_GRAMMAR);

        }if (isFinal(line)) {
            if (!declarationWithInit(line)) {
                throw new VariableException(ERROR_FINAL);
            }
            line = removeWord(line);
        }if (!variables.Variable.isTypeNameValid(beginningWord(line))){
            throw new VariableException(ERROR_TYPE);
        }String type = beginningWord(line);
        line = removeWord(line);
        String names = getNames(line) ;
        if (!checkAllNames(names,type)){
            throw new VariableException(ERROR_NAME) ;
        }
        String values = splitValues(line);
        if (values.length() >0){
            if (!checkAllValues(values,type)) {
                throw new VariableException(ERROR_VALUE);
            }
        }

    }

    public static boolean checkAllNames(String line, String type){
        String [] varNames = splitLineWithComma(line);
        for (String name : varNames){
            if (!variables.Variable.isNameValid(name)){
                return false;
            }listVariables.put(name,type);
        }return true;
    }
    public static boolean checkAllValues(String valueLine, String type){
        String [] values = splitLineWithComma(valueLine);
        for (String value : values){
            if (!Tools.checkType(type, value)){
                return false;
            }
        }return true;
    }
/*    public static boolean isValueValid(String type, String value){
        switch(type){
            case (STRING):
                return isValueValidString(value);
            case (INT):
                return isValueValidInt(value);
            case (DOUBLE):
                return isValueValidDouble(value);
            case(CHAR):
                return isValueValidChar(value);
            case (BOOLEAN):
                return isValueValidBoolean(value);

        }
        return false;
    }

    public static boolean isValueValidInt(String value){
        if (listVariables.containsKey(value)){
            return listVariables.get(value).equals(INT);
        }
        Matcher matcher = INT_PATTERN.matcher(value);
        return matcher.matches();

    }
    public static boolean isValueValidDouble(String value){
        if (listVariables.containsKey(value)){
            return listVariables.get(value).equals(DOUBLE);
        }
        Matcher matcher = DOUBLE_PATTERN.matcher(value);
        return matcher.matches();

    }
    public static boolean isValueValidString(String value){
        if (listVariables.containsKey(value)){
            return listVariables.get(value).equals(STRING);
        }
        Matcher matcher = STRING_PATTERN.matcher(value);
        return matcher.matches();

    }
    public static boolean isValueValidChar(String value){
        if (listVariables.containsKey(value)){
            return listVariables.get(value).equals(CHAR);
        }
        Matcher matcher = CHAR_PATTERN.matcher(value);
        return matcher.matches();


    }
    public static boolean isValueValidBoolean(String value){
        if (listVariables.containsKey(value)){
            return listVariables.get(value).equals(BOOLEAN);
        }
        Matcher matcher = BOOLEAN_PATTERN.matcher(value);
        return matcher.matches();
    }

    }*/

    public String[] splitLine(String line){
        return line.split(" ");


    }
    public static String[] splitLineWithComma(String line) {
        return line.split(",");
    }



    public static boolean isFinal(String line) { // we already init and want new value//
        Matcher matcher = FINAL_PATTERN.matcher(line);
        return matcher.matches();
    }

    public boolean isNewDeclaration(String line) {
        Matcher matcher = NEW_DECLARATION.matcher(line);
        return matcher.matches();
    }
    public boolean isAssignment(String line) { // we already init and want new value//
        Matcher matcher = ASSIGNMENT.matcher(line);
        return matcher.matches();
    }



    public  static  boolean declarationWithInit(String line){ // if its new , did we init with a value ?//
        return line.contains(EQUAL);


    }
    public boolean declarationWithoutInit(String line){
        Matcher matcher = DECLARATION_PATTERN_WITHOUT_INIT.matcher(line);
        return matcher.matches();

    }

    public static String removeWord(String line){ // for new declaration//
        return line.substring(line.indexOf(EMPTY_SPACE)+1);

    }
    public static String beginningWord(String line){ // for new declaration//
        String[] arr = line.split(EMPTY_SPACE, 2);
        return arr[0];
    }

    public static String splitValues(String line){ // if we have new value or values
        line = line.replaceAll(SEM, "");
        return line.substring(line.lastIndexOf("=")+1).trim();


    }public static String getNames(String line){
        line = line.replaceAll(SEM, "");
        if (line.contains(EQUAL)){
            line.substring(0,line.indexOf(EQUAL)).trim();
        }
        return line.trim();


    }
    public static  String [] getName(String line){
        if (isFinal(line)){
            removeWord(line);
        } if (declarationWithInit(line)){
            removeWord(line);
        }return getNames(line).split(",");
    }






}
