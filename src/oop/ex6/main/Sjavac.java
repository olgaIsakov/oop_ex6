package oop.ex6.main;

import oop.ex6.methods.*;
import oop.ex6.variables.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sjavac {
    private static final String COMMENT_SUFFIX = "\\";
    private static final String COMMENT = "\\s*//.*+";
    private static final String EMPTY = "\\s*";
    private static final Pattern COMMENT_PATTERN = Pattern.compile(COMMENT);
    private static final Pattern EMPTY_PATTERN = Pattern.compile(EMPTY);
    private static final int IO_ERROR = 2;
    private static final int S_JAVA_ERROR = 1;
    private static final int VALID_FILE = 0;
    private static final int FIRST_ARG = 0;
    private static final int EMPTY_LINE = 0 ;
    private static final int VALID_NUM_OF_ARGS = 1;

    public static void main(String[] args) {
        try {
            checkLegalArgsLength(args);
            BufferedReader buffer = new BufferedReader(new FileReader(args[FIRST_ARG]));
            String[] fileLines = getAllLInes(buffer);
            Parser.parseToMethods(fileLines);
            CheckSingleMethod.checkMethods(Parser.getMapNameLines());
            GlobalVariables.checkGlobalVars(Parser.getGlobalVars());
            System.out.println(VALID_FILE);
        }catch (IOException e){
            System.err.println(e.getMessage());
            System.out.println(IO_ERROR);
        } catch (MethodException | StructureException | ConditionException | VariableException | BlockException e) {
            System.err.println(e.getMessage());
            System.out.println(S_JAVA_ERROR);
        }
    }

    private static void checkLegalArgsLength(String[] args) throws IOException {
        if (args.length != VALID_NUM_OF_ARGS) throw new IOException();
    }


    /**
     * This method converts all lines from the given file to an array.
     * @param buffer buffer reader
     * @return an array with all lines except comment lines and empty lines
     * @throws IOException problem in reading the given file.
     */
    private static String[] getAllLInes(BufferedReader buffer) throws IOException{
        String line;
        List<String> sJavaLines = new ArrayList<>() ;
        while ((line = buffer.readLine()) != null){
            Matcher commentMatch = COMMENT_PATTERN.matcher(line);
            Matcher emptyMatch = EMPTY_PATTERN.matcher(line);
            if (!commentMatch.find() && !(emptyMatch.matches())){
                sJavaLines.add(line);
            }
        }
        return sJavaLines.toArray(new String[0]);
    }
}
