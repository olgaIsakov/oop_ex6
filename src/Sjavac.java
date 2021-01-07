import methods.MethodException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Sjavac {
    private static final String COMMENT_SUFFIX = "\\";

    private static int readFile(String args) throws IOException {
        BufferedReader buffer = new BufferedReader(new FileReader(args));
        try {
            String[] fileLines = getAllLInes(buffer);
            Parser parser = new Parser();
            Map<List<String>, List<String>> methodMap = parser.parseToMethods(fileLines);
        }catch (IOException e){
            System.err.println(e.getMessage());
            return 2;
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
