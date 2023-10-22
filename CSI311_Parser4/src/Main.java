import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;

public class Main {
    public static void main(String[] args) throws Exception {
        // Variable declarations.
        if (args.length > 0) {
            Path filePath = Paths.get(args[0]);
            String fileInfo = new String(Files.readAllBytes(filePath));
            Lexer file = new Lexer(fileInfo);

            System.out.println(file.Lex());
        }
        // Test 3.
        String input3 = "{" + "if (condition == cold){" + "\n" + "\n" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        System.out.println(test3.Parse());
    }
}
