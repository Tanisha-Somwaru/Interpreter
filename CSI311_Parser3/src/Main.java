import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws Exception {
        // Variable declarations.
        if (args.length > 0) {
            Path filePath = Paths.get(args[0]);
            String fileInfo = new String(Files.readAllBytes(filePath));
            Lexer file = new Lexer(fileInfo);

            System.out.println(file.Lex());
        }
    }
}
