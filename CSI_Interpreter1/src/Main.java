import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
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

        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("Coraline"));
        parameters1.put("1", new InterpreterDataType("0"));
        parameters1.put("2", new InterpreterDataType("5"));

        System.out.println(test1.TestFunctions("substr", parameters1));
        // Add assert statements later.

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("WowCode"));
        parameters2.put("1", new InterpreterDataType("3"));

        System.out.println(test2.TestFunctions("substr", parameters2));
        // Add assert statements later.

    }
}