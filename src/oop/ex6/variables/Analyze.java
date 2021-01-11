package oop.ex6.variables;



import oop.ex6.main.Tools;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyze {

    private static final String FINAL = "final";
    private static final String ERROR = "";

    private static final Pattern DECLARATION_PATTERN_WITHOUT_INIT =  Pattern.compile("(?:\\w+\\s+)([a-zA-Z_][a-zA-Z0-9_]*)");
    private static final Pattern NEW_DECLARATION = Pattern.compile("\\s*\\w+\\s+\\w[\\s*,\\w]*\\s*(?:=\\s*.+?)?\\s*;\\s*");
    private static final Pattern ASSIGNMENT =  Pattern.compile("\\s*\\w+\\s*=\\s*.+?\\s*;\\s*");
    private static final Pattern END_OF_LINE = Pattern.compile(";$");
    private static final Pattern FINAL_PATTERN =  Pattern.compile("^final");
    private static final String EMPTY_SPACE = " ";
    private static final String EQUAL= "=";
    private static final String SEM = ";";
    private static final String EMPTY_STRING = "";
    public static HashMap<String,String>  listVariables = new HashMap<String,String>();

    private static final String ERROR_FINAL = "ERROR : the variable is final but without initialization  ";
    private static final String ERROR_TYPE = "ERROR : invalid type name ";
    private static final String ERROR_NAME = "ERROR : invalid variable  name ";
    private static final String ERROR_VALUE = "ERROR : invalid variable value ";

    public static void analyzer(String line) throws VariableException {

        if (isFinal(line)) {
            if (!declarationWithInit(line)) {
                throw new VariableException(ERROR_FINAL);
            }
            line = removeWord(line);

        }
        String type = beginningWord(line);
        if (!listVariables.containsKey(type)){
            if (!oop.ex6.variables.Variable.isTypeNameValid(type)) {
                throw new VariableException(ERROR_TYPE);
            }line = removeWord(line);
        }
        String names = getNames(line) ;
        if (!checkAllNames(names,type)){
            throw new VariableException(ERROR_NAME) ;
        }
        String values = splitValues(line);
        if (values.length() >0){
            if (!checkAllValues(values,type,names)) {
                throw new VariableException(ERROR_VALUE);
            }
        }
        addTolist(names,type);

    }
    public static String getType(String line){
        if (isFinal(line)){
            line = removeWord(line);
        } if (Variable.isTypeNameValid(beginningWord(line))) {
            return beginningWord(line);
        }return EMPTY_STRING;
    }
    public static void addTolist(String names, String type){
        String [] varNames = splitLineWithComma(names);
        for (String name : varNames){
            listVariables.put(name,type);
            }

    }


    public static boolean checkAllNames(String line, String type){
        String [] varNames = splitLineWithComma(line);
        for (String name : varNames){
            if (!oop.ex6.variables.Variable.isNameValid(name)){
                return false;
            }
        }return true;
    }
    public static boolean checkAllValues(String valueLine, String type,String name){
        String [] values = splitLineWithComma(valueLine);
        if (listVariables.containsKey(name)){
            type = listVariables.get(name);
        }
        for (String value : values){
            if (listVariables.containsKey(value)){
                String typeVar = listVariables.get(value);
                if (!type.equals(typeVar) )return false;
            }
            else if (!Tools.checkType(type, value)){
                return false;
            }
        }return true;
    }


    public static String removeSpace(String line) {
        return line.replaceAll("\\s+", "");

    }
    public static String[] splitLineWithComma(String line) {
        line = removeSpace(line);
        return line.split(",");
    }



    public static boolean isFinal(String line) { // we already init and want new value//
        Matcher matcher = FINAL_PATTERN.matcher(line);
        return matcher.find();
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
        return line.contains(EQUAL)&&splitValues(line).length()>0;


    }
    public boolean declarationWithoutInit(String line){
        Matcher matcher = DECLARATION_PATTERN_WITHOUT_INIT.matcher(line);
        return matcher.matches();

    }

    public static String removeWord(String line){ // for new declaration//
       String removeWord = beginningWord(line);
        if (removeWord.equals(line.trim())) return "";
        else    return line.trim().split("\\s", 2)[1];


    }
    public static String beginningWord(String line){
        line= line.trim();// for new declaration//
        String[] arr = line.split(EMPTY_SPACE, 2);
        return arr[0];
    }

    public static String splitValues(String line){ // if we have new value or values
        line = removeSpace(line);
        line = line.replaceAll(SEM, "");
        if (line.contains(EQUAL)) {
            return line.substring(line.lastIndexOf("=")+1);
        }
        return EMPTY_STRING;



    }public static String getNames(String line){
        line = line.replaceAll(SEM, "");
        if (line.contains(EQUAL)){
            return line.substring(0,line.indexOf(EQUAL)).trim();
        }
        return line.trim();


    }
    public static  String [] getName(String line){
        if (isFinal(line)){
            line = removeWord(line);
        } if (Variable.isTypeNameValid(beginningWord(line))){
            line = removeWord(line);
        }return getNames(line).split(",");
    }






}
