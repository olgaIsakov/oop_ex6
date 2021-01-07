package variable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Analyze {

    private static final String FINAL = "final";
    private static final String ERROR = "";

    private static final Pattern DECLARATION_PATTERN_WITH_INIT =  Pattern.compile("(_?[a-zA-Z0-9_]+)(\\s+)(([a-zA-Z]?_[a-zA-Z])?([a-zA-Z]*[0-9]*_*)(=[a-zA-Z0-9]*)?[,]?)+((\\s+)(([a-zA-Z]?_[a-zA-Z])?([a-zA-Z]*[0-9]*_*)(=[a-zA-Z0-9]*)?[,]?))*");
    private static final Pattern DECLARATION_PATTERN_WITHOUT_INIT =  Pattern.compile("(?:\\w+\\s+)([a-zA-Z_][a-zA-Z0-9_]*)");
    private static final Pattern NEW_DECLARATION = Pattern.compile("\\s*\\w+\\s+\\w[\\s*,\\w]*\\s*(?:=\\s*.+?)?\\s*;\\s*");
    private static final Pattern ASSIGNMENT =  Pattern.compile("\\s*\\w+\\s*=\\s*.+?\\s*;\\s*");
    private static final Pattern FINAL_PATTERN =  Pattern.compile("^FINAL");
;

    public void analyzer(String line) throws FinalVariableException ,TypeException , NameVariableException{
        if (isFinal(line)){
            if (!declarationWithInit(line)) {
                throw new FinalVariableException(ERROR);

            } line = removeWord(line);

            if (!Variable.isTypeNameValid(beginningWord(line))){
                throw new TypeException(ERROR);

            }line = removeWord(line);
            String names = getNames(line) ;
            if (!checkAllNames(names)){
                throw new NameVariableException(ERROR) ;
            }
            String values = splitValues(line);








        }


    }
    public boolean checkAllNames(String line){
        String [] varNames = splitLineWithComma(line);
        for (String name : varNames){
            if (!Variable.isNameValid(name)){
                return false;
            }
        }return true;
    }



    public String[] splitLine(String line){
        return line.split(" ");


    }
    public String[] splitLineWithComma(String line){
        return line.split(",");


    }

    public boolean isFinal(String line) { // we already init and want new value//
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



    public boolean declarationWithInit(String line){ // if its new , did we init with a value ?//
        return line.contains("=");


    }
    public boolean declarationWithoutInit(String line){
        Matcher matcher = DECLARATION_PATTERN_WITHOUT_INIT.matcher(line);
        return matcher.matches();

    }

    public String removeWord(String line){ // for new declaration//
        return line.substring(line.indexOf(" ")+1);

    }
    public String beginningWord(String line){ // for new declaration//
        String[] arr = line.split(" ", 2);
        return arr[0];
    }

    public String splitValues(String line){ // if we have new value or values
        line = line.replaceAll(";", "");
        return line.substring(line.lastIndexOf("=")+1).trim();


    }public String getNames(String line){
        line = line.replaceAll(";", "");
        if (line.contains("=")){
            line.substring(0,line.indexOf("=")).trim();
        }
        return line.trim();


    }






}
