/**
 * הוראות שימוש (בעברית, כי נמאס כבר מתיעודים באנגלית)
 * שימו את הקובץ הנוכחי בתיקיית
 * src
 * שלכם בסמוך לחבילה
 * oop.ex6
 * כדי שיהיה אפשר להריץ ושהוא יכיר את התכנית שלכם
 *
 * יש שני שדות סטטים, אחד צריך להכיל את הנתיב לתיקייה עם כל הטסטים שהם נתנו (הקבצי
 * javac)
 * השדה נקרא
 * testDirPath
 * השדה השני צריך להכיל את הנתיב לקובץ סיכום שהם נתנו
 * sjavac_tests.txt
 * זהו השדה
 * exceptedFilePath
 *
 * מה שהטסט עושה זה מריץ את התכנית שלכם על כל הקבצים (היא אמורה להדפיס 0 או 1 בכל פעם)
 * ובסמוך לכל טסט להדפיס את השורה הרלוונטית בקובץ שלהם, כך תוכלו להשוות את הערך שמתקבל
 * ולבדוק שהתכנית שלכם נופלת באמת בגלל הסיבה הזאת
 *
 * שימו לב שיש קבצים שאין להם שורה מתאימה בקובץ ולידם לא יודפס כלום.
 *
 * המלצה שלי- בכל פעם שהתכנית מדפיסה 1 תדפיסו את הסיבה לכך אצלכם וכך יהיה נוח להשוות
 *
 *//*


import oop.ex6.main.Sjavac;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Objects;

public class Test {
    // enter here the path to tests directory
    public static final String testDirPath = "C:\\Users\\olga8\\IdeaProjects\\oop_ex6\\tests\\";
    // enter here the path to sjavac_tests.txt
    public static final String exceptedFilePath = "C:\\Users\\olga8\\IdeaProjects\\oop_ex6\\sjavac_tests.txt";

    public static void main(String[] args) {
        File tesDir = new File(testDirPath);
        try {
            FileReader reader = new FileReader(exceptedFilePath);
            BufferedReader bReader = new BufferedReader(reader);
            HashMap<String, String> filesLines = new HashMap<>();
            String line = "";
            while (line != null) {
                line = bReader.readLine();
                while (line.equals("")) {
                    line = bReader.readLine();
                    if(line == null) {
                        break;
                    }
                }
                if(line == null) {
                    break;
                }
                String fileName = line.substring(0, 13);
                filesLines.put(fileName, line);
            }
            for (File file : Objects.requireNonNull(tesDir.listFiles())) {
                String[] arguments = {file.getAbsolutePath()};
                System.out.println("_________\n" + file.getName());
                Sjavac.main(arguments);
                if (filesLines.containsKey(file.getName())) {
                    System.out.println(filesLines.get(file.getName()));
                }
                System.out.println("_________");
            }
        } catch (Exception ignored) {
        }
    }
}
*/
import oop.ex6.main.Sjavac;
import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    // enter here the path to tests directory
    public static final String testDirPath = "C:\\Users\\olga8\\IdeaProjects\\oop_ex6\\tests\\";
    // enter here the path to sjavac_tests.txt
    public static final String exceptedFilePath = "C:\\Users\\olga8\\IdeaProjects\\oop_ex6\\sjavac_tests.txt";

    public static void main(String[] args) {
        List<String> failedFiles = new LinkedList<>();
        File tesDir = new File(testDirPath);
        try {
            FileReader reader = new FileReader(exceptedFilePath);
            BufferedReader bReader = new BufferedReader(reader);
            HashMap<String, String> filesLines = new HashMap<>();
            String line = "";
            while (line != null) {
                line = bReader.readLine();
                while (line.equals("")) {
                    line = bReader.readLine();
                    if(line == null) {
                        break;
                    }
                }
                if(line == null) {
                    break;
                }
                String fileName = line.substring(0, 13);
                filesLines.put(fileName, line);
            }
            for (File file : Objects.requireNonNull(tesDir.listFiles())) {
                String[] arguments = {file.getAbsolutePath()};
                System.out.println("_________\n" + file.getName());


                PrintStream originalStdout = System.out;
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                System.setOut(new PrintStream(outputStream));
                Sjavac.main(arguments);
                String output = outputStream.toString().trim();
                System.setOut(originalStdout);
                if (filesLines.containsKey(file.getName())) {
                    System.out.println(filesLines.get(file.getName()));
                    Matcher matcher =
                            Pattern.compile("\\.sjava\\s+(\\d)").matcher(filesLines.get(file.getName()));
                    matcher.find();
//                    assert matcher.group(1).equals(output);
                    if (!matcher.group(1).equals(output)) {
                        System.out.println("regex match is: " + matcher.group(1));
                        System.out.println("my output is: " + output);
                        failedFiles.add(filesLines.get(file.getName()) + "\nmy output is: " + output);
//                        assertEquals();
//                        throw new AssertionError();

                    }
                }
                System.out.println("_________");
            }
        } catch (Exception ignored) {
        }
        System.out.println("\n\n");
        failedFiles.forEach(System.out::println);
        if (failedFiles.isEmpty()) {
            System.out.println("All tests passed");
        }
    }
}