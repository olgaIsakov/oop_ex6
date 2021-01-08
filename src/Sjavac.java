import methods.MethodException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Sjavac {
    private static final String COMMENT_SUFFIX = "\\";
    private static final int IO_ERROR = 2;
    private static final int S_JAVA_ERROR = 1;
    private static final int INITIALIZE_COUNTER = 0;
    private final static String IMPORT = "\\s*import|package\\s*.*$";
    private final static String METHOD = "\\s*void\\s+[a-zA-Z]\\w*\\s*\\(.*\\s*\\)\\s*[{]\\s*$";
    final static Pattern METHOD_PATTERN = Pattern.compile(METHOD);
    final static Pattern IMPORT_PATTERN = Pattern.compile(IMPORT);
    final static String ERROR_MSG = "ERROR: Illegal file format";

    private static int readFile(String args) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(args));
        try {
            String[] fileLines = getAllLInes(buffer);
            checkImport(fileLines);
            Parser parser = new Parser();
            Map<List<String>, List<String>> methodMap = parser.parseToMethods(fileLines);
        }catch (IOException e){
            System.err.println(e.getMessage());
            return IO_ERROR;
        } catch (MethodException e) {
            System.err.println(e.getMessage());
            return S_JAVA_ERROR;
        }

    }

    /**
     * This method checks there is no import in the beginning of the file
     * @param fileLines the file
     * @throws MethodException import found
     */
    private static void checkImport(String[] fileLines) throws MethodException {
        int i = INITIALIZE_COUNTER;
        Matcher methodStructure = METHOD_PATTERN.matcher(fileLines[i]);
        while (!methodStructure.matches() || i < fileLines.length ){
            Matcher importStructure = IMPORT_PATTERN.matcher(fileLines[i]);
            if (importStructure.matches()) throw new MethodException(ERROR_MSG);
            i ++;
        }
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
            if (!line.startsWith(COMMENT_SUFFIX)){
                sJavaLines.add(line);
            }
        }
        return sJavaLines.toArray(new String[0]);
    }
}
