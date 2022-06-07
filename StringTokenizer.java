import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.StringTokenizer;
import java.util.stream.Stream;
/*StringTokenizer(String str)	Раскладывает строку на части, используя в качестве разделителя символы пробела " ", 
табуляции "\t", перевода строки "\n" и возврата каретки "\r"
StringTokenizer(String str, String delim)	Раскладывает строку на части, используя в качестве разделителя строку delim
StringTokenizer(String str, String delim, boolean returnDelims)	
оже что и предыдущий, но если returnDelims установлен в true, разделители также возвращаются в качестве части строки*/
public class Reader {
    public static void main(String[] args) throws IOException {
        Stream<String> lines = Files.lines(Paths.get("D:\\root\\Paata\\file1.txt"));
        lines.forEach(e -> {
            //Hadoop is as yellow as can be
            StringTokenizer st = new StringTokenizer(e, " \t\n\r,.");
            //7
            System.out.println(e);
            System.out.println(st.countTokens());
        });
    }
}
