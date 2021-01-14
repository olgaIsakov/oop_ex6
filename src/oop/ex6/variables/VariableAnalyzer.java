package oop.ex6.variables;



import oop.ex6.main.TypeCheck;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;

public class VariableAnalyzer {

    private static final int FIRST = 0;
    private static final int SECOND = 1;
    private static final int EMPTY = 0;
    private static final String EMPTY_SPACE = " ";
    private static final String EQUAL= "=";
    private static final String SEM = ";";
    private static final String COMMA = ",";
    private static final String EMPTY_STRING = "";

    /*List of variables */
    public static HashMap<String,String>  listVariables = new HashMap<String,String>();
    public static List<String>  listFinals= new ArrayList<>();
    public static List<String>  listInit= new ArrayList<>();
    public static List<String>  listDeclared= new ArrayList<>();

    /*Error messages to print */
    private static final String ERROR_FINAL = "ERROR : the variable is final but without initialization  ";
    private static final String ERROR_TYPE = "ERROR : invalid type name ";
    private static final String ERROR_NAME = "ERROR : invalid variable  name ";
    private static final String ERROR_VALUE = "ERROR : invalid variable value ";
    private static final String ERROR_DECLARE = "ERROR : the variable is already declared ";


    /**
     * This method analyzes a line of variable
     * @param line line of variable
     * @throws VariableException
     */
    public static void analyzeLineVariable(String line) throws VariableException {
       boolean isFinal = false ;
       boolean isInit = false ;
       boolean isDeclared = false ;
        if (isFinal(line)) {
            if (!declarationWithInit(line)) {
                throw new VariableException(ERROR_FINAL);
            }isFinal = true;
            line = removeWord(line);

        }
        String type = beginningWord(line);
        if (!listVariables.containsKey(type)){

            if (!MainVariable.isTypeNameValid(type)) {
                throw new VariableException(ERROR_TYPE);
            }
            isDeclared = true;
            line = removeWord(line);
        }
        String names = getLineNames(line) ;
        if (!checkNamesIfDeclared(names,type))throw new VariableException(ERROR_DECLARE) ;
        if (!checkAllNames(names)){
            throw new VariableException(ERROR_NAME) ;
        }
        String values = splitValues(line);
        if (values.length() > EMPTY ){
            if (!checkNamesIfInFinal(names)) throw new VariableException(ERROR_FINAL);
            if (!checkAllValues(values,type,names)) {
                throw new VariableException(ERROR_VALUE);
            }isInit = true;
        }
        addToList(names,type,isFinal ,isInit,isDeclared);

    }

    /**
     * This method returns the type of the variable
     * @param line line of variable
     * @return type
     */
    public static String getType(String line){
        if (isFinal(line)){
            line = removeWord(line);
        } if (MainVariable.isTypeNameValid(beginningWord(line))) {
            return beginningWord(line);
        }return EMPTY_STRING;
    }

    /**
     * This method add to the lists the given variable by condition
     * @param names names in the declaration
     * @param type  of the variable
     * @param  isFinal true if the variable is final , false otherwise
     * @param  isInit true if the variable is initialized , false otherwise
     * @param  isDeclared true if the variable is declared with a value , false otherwise
     *
     */
    public static void addToList(String names, String type, boolean isFinal , boolean isInit, boolean isDeclared){
        String [] varNames = splitLineWithComma(names);
        for (String name : varNames){
            listVariables.put(name,type);
            if (isFinal) listFinals.add(name);
            if (isInit) listInit.add(name);
            if(isDeclared) listDeclared.add(name);
            }

    }
    /**
     * This method returns false if the variable is already saved as final
     * @param line line of variable
     * @return true or false
     */
    public static boolean checkNamesIfInFinal(String line){
        String [] varNames = splitLineWithComma(line);
        for (String name : varNames){
            if (listFinals.contains(name)){
                return false;
            }
        }return true;
    }
    /**
     * This method returns false if the variable is already saved as declared
     * @param names variable names
     * @param type
     * @return true or false
     */
    public static boolean checkNamesIfDeclared(String names , String type ){
        String [] varNames = splitLineWithComma(names);
        for (String name : varNames){
            if (listDeclared.contains(name)&& MainVariable.isTypeNameValid(type)){
                return false;
            }
        }return true;
    }


    /**
     * This method returns true if all the names to variable in the line are valid
     * @param line variable names
     * @return true or false
     */
    public static boolean checkAllNames(String line){
        String [] varNames = splitLineWithComma(line);
        for (String name : varNames){
            if (!MainVariable.isNameValid(name)){
                return false;
            }
        }return true;
    }

    /**
     * This method returns true if the values is suitable for the given type
     * @param valueLine line of the values only
     * @param type
     * @param  name
     * @return true or false
     */
    public static boolean checkAllValues(String valueLine, String type,String name){
        String [] values = splitLineWithComma(valueLine);
        // if we assigned a variable as a value to another variable ,
        // we take the type of the value and update the type ot the variable we check
        if (listVariables.containsKey(name)){
            type = listVariables.get(name);
        }
        for (String value : values){
            if (listVariables.containsKey(value)){
                String typeVar = listVariables.get(value);
                if (!type.equals(typeVar) )return false;
            }
            else if (!TypeCheck.checkType(type, value)){
                return false;
            }
        }return true;
    }

    /**
     * This method returns true if the variable is final
     * @param line
     * @return true or false
     */
    public static boolean isFinal(String line) {
        Matcher matcher = VariablesPattern.FINAL_PATTERN.matcher(line);
        return matcher.find();
    }
    /**
     * This method returns true if it is new declaration
     * @param line
     * @return true or false
     */
    public  static  boolean declarationWithInit(String line){
        return line.contains(EQUAL)&&splitValues(line).length()>EMPTY ;
    }


    /**
     * This method returns the line of the names of the variable
     * @param line
     * @return String line of names
     */
    public static String getLineNames(String line){
        line = line.replaceAll(SEM, EMPTY_STRING );
        if (line.contains(EQUAL)){
            return line.substring(FIRST ,line.indexOf(EQUAL)).trim();
        }
        return line.trim();
    }

    /**
     * This method returns a list of the names
     * @param line
     * @return String list of names
     */
    public static  String [] getName(String line){
        if (isFinal(line)){
            line = removeWord(line);
        } if (MainVariable.isTypeNameValid(beginningWord(line))){
            line = removeWord(line);
        }return getLineNames(line).split(COMMA);
    }


    /**
     * This method remove spaces from the line
     * @param line
     * @return new line
     */
    public static String removeSpace(String line) {
        return line.replaceAll(VariablesPattern.SPACE, EMPTY_STRING );

    }
    /**
     * This method split the line with comma
     * @param line
     * @return new line
     */
    public static String[] splitLineWithComma(String line) {
        line = removeSpace(line);
        return line.split(COMMA);
    }
    /**
     * This method removes the first word in the line
     * @param line
     * @return new line
     */
    public static String removeWord(String line){ // for new declaration//
       String removeWord = beginningWord(line);
        if (removeWord.equals(line.trim())) return EMPTY_STRING ;
        else    return line.trim().split("\\s", 2)[SECOND ];

    }
    /**
     * This method returns the first word in the line
     * @param line
     * @return word
     */
    public static String beginningWord(String line){
        line= line.trim();// for new declaration//
        String[] arr = line.split(EMPTY_SPACE, 2);
        return arr[FIRST ];
    }
    /**
     * This method splits the values from the line
     * @param line
     * @return line of values
     */
    public static String splitValues(String line){ // if we have new value or values
        line = removeSpace(line);
        line = line.replaceAll(SEM, EMPTY_STRING );
        if (line.contains(EQUAL)) {
            return line.substring(line.lastIndexOf(EQUAL )+1);
        }
        return EMPTY_STRING;
    }









}
