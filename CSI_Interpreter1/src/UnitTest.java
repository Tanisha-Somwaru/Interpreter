import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.*;

public class UnitTest {
    @Test
    public void TestParseFunctionCallChanges() throws Exception {
        // Test 1.
        String input1 = "{\n" + "if (k == $1)\n" + "print RSTART, RLENGTH\n" + "else\n" + "print \"no k\"\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional[Left node, right node, and operations: " +
                "Name: WORD(k), Optional[Left node and operations: Constant name: NUMBER(1), DOLLAR], EQ] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: " +
                "[Name: WORD(RSTART), Name: WORD(RLENGTH)]]] Next If: Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: " +
                "[Constant name: STRINGLITERAL(no k)]]]]]\n", test1.Parse().toString());

        // Test 2.
        String input2 = "function reverse(str)\n" + "{\n" + "if (str == \"\")\n" + "return \"\"\n" + "\n" + "return (reverse(substr(str, 2)) substr(str, 1, 1))\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: [Function name: WORD(reverse) Parameters: [WORD(str)]]\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional" +
                "[Left node, right node, and operations: Name: WORD(str), Optional[Constant name: STRINGLITERAL()], EQ] Statements: Optional[Statements: [Return: Optional[Constant name: " +
                "STRINGLITERAL()]]], Return: Optional[Left node, right node, and operations: Function name: Optional[WORD(reverse)] Parameters: [Function name: Optional[WORD(substr)] " +
                "Parameters: [Name: WORD(str), Constant name: NUMBER(2)]], Optional[Function name: Optional[WORD(substr)] Parameters: [Name: WORD(str), Constant name: NUMBER(1), " +
                "Constant name: NUMBER(1)]], CONCATENATION]]]\n", test2.Parse().toString());

        // Test 3.
        String input3 = "{\n" + "if (getline tmp > 0) {\n" + "print tmp\n" + "print $0\n" + "} else {\n" + "print $0\n" + "}" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional[Function name: Optional[GETLINE] " +
                "Parameters: [Left node, right node, and operations: Name: WORD(tmp), Optional[Constant name: NUMBER(0)], GT]] Statements: Optional[Statements: " +
                "[Function name: Optional[PRINT] Parameters: [Name: WORD(tmp)], Function name: Optional[PRINT] Parameters: [Left node and operations: Constant name: NUMBER(0), DOLLAR]]] " +
                "Next If: Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Left node and operations: Constant name: NUMBER(0), DOLLAR]]]]]\n", test3.Parse().toString());

        // Test 4.
        String input4 = "{\n" + "printf \"%-10s %s\\n\", $1, $2\n" + "}";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Function name: Optional[PRINTF] Parameters: [Constant name: STRINGLITERAL(%-10s %s\\n), " +
                "Left node and operations: Constant name: NUMBER(1), DOLLAR, Left node and operations: Constant name: NUMBER(2), DOLLAR]]]\n", test4.Parse().toString());

        // Test 5.
        String input5 = "BEGIN {\n" + "if (getline date_now <= 0) {\n" + "print \"Can't get system date\" > \"/dev/stderr\"\n" + "exit 1\n" + "}\n" + "}";
        Lexer temp5 = new Lexer(input5);
        LinkedList<Token> tokens5 = new LinkedList<>(temp5.Lex());
        Parser test5 = new Parser(tokens5);

        assertEquals("BEGIN: [Statements: [Conditions: Optional[Function name: Optional[GETLINE] Parameters: [Left node, right node, and operations: Name: WORD(date_now), " +
                "Optional[Constant name: NUMBER(0)], LE]] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Left node, right node, and operations: " +
                "Constant name: STRINGLITERAL(Can't get system date), Optional[Constant name: STRINGLITERAL(/dev/stderr)], GT], Function name: Optional[EXIT] Parameters: " +
                "[Constant name: NUMBER(1)]]]]]\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: []\n", test5.Parse().toString());

        // Test 6,
        String input6 = "{\n" + "if ($1 ~ \"Eva\")\n" + "nextfile;\n" + "print \"Eva\"\n" + "}";
        Lexer temp6 = new Lexer(input6);
        LinkedList<Token> tokens6 = new LinkedList<>(temp6.Lex());
        Parser test6 = new Parser(tokens6);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional[Left node, right node, and operations: Left node and operations: " +
                "Constant name: NUMBER(1), DOLLAR, Optional[Constant name: STRINGLITERAL(Eva)], MATCH] Statements: Optional[Statements: [Function name: Optional[NEXTFILE] Parameters: []]], " +
                "Function name: Optional[PRINT] Parameters: [Constant name: STRINGLITERAL(Eva)]]]\n", test6.Parse().toString());

        // Test 7.
        String input7 = "{\n" + "if (carrot <= $5){\n" + "next;\n" + "}\n" + "printf \"%s\\n\", $5;\n" + "}";
        Lexer temp7 = new Lexer(input7);
        LinkedList<Token> tokens7 = new LinkedList<>(temp7.Lex());
        Parser test7 = new Parser(tokens7);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional[Left node, right node, and operations: Name: WORD(carrot), " +
                "Optional[Left node and operations: Constant name: NUMBER(5), DOLLAR], LE] Statements: Optional[Statements: [Function name: Optional[NEXT] Parameters: []]], Function name: " +
                "Optional[PRINTF] Parameters: [Constant name: STRINGLITERAL(%s\\n), Left node and operations: Constant name: NUMBER(5), DOLLAR]]]\n", test7.Parse().toString());
    }

    @Test
    public void TestSplitAndAssign() throws Exception {
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Path input1 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/Test1SplitAndAssign.txt");
        Interpreter temp1 = new Interpreter(prog1, input1);

        assertTrue(temp1.TestSplitAndAssign());

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Path input2 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/Test2SplitAndAssign.txt");
        Interpreter temp2 = new Interpreter(prog2, input2);

        assertTrue(temp2.TestSplitAndAssign());

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Path input3 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/Test3SplitAndAssign.txt");
        Interpreter temp3 = new Interpreter(prog3, input3);

        assertFalse(temp3.TestSplitAndAssign());
    }

    @Test
    public void TestPrint() throws Exception {
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));

        InterpreterArrayDataType input1 = new InterpreterArrayDataType();
        input1.AddArray("0", new InterpreterDataType("Alice"));
        input1.AddArray("1", new InterpreterDataType("Jasmine"));
        input1.AddArray("2", new InterpreterDataType("Ariel"));

        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", input1);

        String output1 = String.valueOf(test1.TestFunctions("print", parameters1));

        // This outputs the correct statement but doesn't show up with actual: in the assertEquals.
        assertEquals("AliceJasmineAriel", output1);

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        InterpreterArrayDataType input2 = new InterpreterArrayDataType();
        input2.AddArray("0", new InterpreterDataType("1"));
        input2.AddArray("1", new InterpreterDataType("2"));
        input2.AddArray("2", new InterpreterDataType("3"));
        input2.AddArray("3", new InterpreterDataType("4"));

        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", input2);

        // This outputs the correct statement but doesn't show up with actual: in the assertEquals.
        String output2 = test2.TestFunctions("print", parameters2);

        assertEquals("1234", output2);

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog3, Path.of("Nonexistent"));
        InterpreterArrayDataType input3 = new InterpreterArrayDataType();
        input3.AddArray("0", new InterpreterDataType("Halloween"));
        input3.AddArray("1", new InterpreterDataType("Trick"));
        input3.AddArray("2", new InterpreterDataType("Treat"));
        input3.AddArray("3", new InterpreterDataType("Candy"));
        input3.AddArray("4", new InterpreterDataType("Pumpkin"));

        HashMap<String, InterpreterDataType> parameters3 = new HashMap<>();
        parameters3.put("0", input3);

        // This outputs the correct statement but doesn't show up with actual: in the assertEquals.
        String output3 = test3.TestFunctions("print", parameters3);

        assertEquals("HalloweenTrickTreatCandyPumpkin", output3);
    }

    @Test
    public void TestPrintf() throws Exception {
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));

        InterpreterArrayDataType formatSpecifiers1 = new InterpreterArrayDataType();
        formatSpecifiers1.AddArray("0", new InterpreterDataType("%s"));
        formatSpecifiers1.AddArray("1", new InterpreterDataType("%s"));
        formatSpecifiers1.AddArray("2", new InterpreterDataType("%s"));

        InterpreterArrayDataType value1 = new InterpreterArrayDataType();
        value1.AddArray("0", new InterpreterDataType("Wow"));
        value1.AddArray("1", new InterpreterDataType("its"));
        value1.AddArray("2", new InterpreterDataType("cold"));

        InterpreterArrayDataType parameters1 = new InterpreterArrayDataType();
        parameters1.AddArray("0", formatSpecifiers1);
        parameters1.AddArray("1", value1);

        // This outputs the correct statement but doesn't show up with actual: in the assertEquals.
        String output1 = test1.TestFunctions("printf", parameters1.GetArrayMap());

        assertEquals("Wowitscold", output1);

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));

        InterpreterArrayDataType formatSpecifiers2 = new InterpreterArrayDataType();
        formatSpecifiers2.AddArray("0", new InterpreterDataType("%s"));
        formatSpecifiers2.AddArray("1", new InterpreterDataType("%s"));

        InterpreterArrayDataType value2 = new InterpreterArrayDataType();
        value2.AddArray("0", new InterpreterDataType("Macaroni"));
        value2.AddArray("1", new InterpreterDataType("Brownies"));

        InterpreterArrayDataType parameters2 = new InterpreterArrayDataType();
        parameters2.AddArray("0", formatSpecifiers2);
        parameters2.AddArray("1", value2);

        // This outputs the correct statement but doesn't show up with actual: in the assertEquals.
        String output2 = test2.TestFunctions("printf", parameters2.GetArrayMap());

        assertEquals("MacaroniBrownies", output2);

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog3, Path.of("Nonexistent"));

        InterpreterArrayDataType formatSpecifiers3 = new InterpreterArrayDataType();
        formatSpecifiers3.AddArray("0", new InterpreterDataType("%s"));
        formatSpecifiers3.AddArray("1", new InterpreterDataType("%s"));
        formatSpecifiers3.AddArray("2", new InterpreterDataType("%s"));
        formatSpecifiers3.AddArray("3", new InterpreterDataType("%s"));

        InterpreterArrayDataType value3 = new InterpreterArrayDataType();
        value3.AddArray("0", new InterpreterDataType("Hello"));
        value3.AddArray("1", new InterpreterDataType("Can"));
        value3.AddArray("2", new InterpreterDataType("You"));
        value3.AddArray("3", new InterpreterDataType("Hear"));

        InterpreterArrayDataType parameters3 = new InterpreterArrayDataType();
        parameters3.AddArray("0", formatSpecifiers3);
        parameters3.AddArray("1", value3);

        // This outputs the correct statement but doesn't show up with actual: in the assertEquals.
        String output3 = test3.TestFunctions("printf", parameters3.GetArrayMap());

        assertEquals("HelloCanYouHear", output3);
    }

    @Test
    public void TestGetLine() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Path input1 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/TestGetLine1.txt");
        Interpreter temp1 = new Interpreter(prog1, input1);

        assertTrue(temp1.TestSplitAndAssign());

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Path input2 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/TestGetLine2.txt");
        Interpreter temp2 = new Interpreter(prog2, input2);

        assertTrue(temp2.TestSplitAndAssign());

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Path input3 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/TestGetLine3.txt");
        Interpreter temp3 = new Interpreter(prog3, input3);

        assertFalse(temp3.TestSplitAndAssign());
    }

    @Test
    public void TestNext() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Path input1 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/TestNext1.txt");
        Interpreter temp1 = new Interpreter(prog1, input1);

        assertTrue(temp1.TestSplitAndAssign());

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Path input2 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/TestNext2.txt");
        Interpreter temp2 = new Interpreter(prog2, input2);

        assertTrue(temp2.TestSplitAndAssign());

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Path input3 = Paths.get("/Users/somwa/eclipse-workspace/CSI_Interpreter1/TestNext3.txt");
        Interpreter temp3 = new Interpreter(prog3, input3);

        assertFalse(temp3.TestSplitAndAssign());
    }

    @Test
    public void TestGsub() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("Whale"));
        parameters1.put("1", new InterpreterDataType("Bad"));
        parameters1.put("2", new InterpreterDataType("Replacement"));

        String output1 = test1.TestFunctions("gsub", parameters1);

        assertEquals("Replacement", output1);

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("Tanisha"));
        parameters2.put("1", new InterpreterDataType("BadCoder"));
        parameters2.put("2", new InterpreterDataType("GoodCoder"));

        String output2 = test2.TestFunctions("gsub", parameters2);

        assertEquals("GoodCoder", output2);

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog3, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters3 = new HashMap<>();
        parameters3.put("0", new InterpreterDataType("Normal"));
        parameters3.put("1", new InterpreterDataType("Abnormal"));
        parameters3.put("2", new InterpreterDataType("Crazy"));

        String output3 = test3.TestFunctions("gsub", parameters3);

        assertEquals("Crazy", output3);
    }

    @Test
    public void TestMatch() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("Cupid"));
        parameters1.put("1", new InterpreterDataType("Cupid"));

        String output1 = test1.TestFunctions("match", parameters1);

        assertEquals("true", output1);

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("GoodGrade"));
        parameters2.put("1", new InterpreterDataType("GoodGrade"));

        String output2 = test2.TestFunctions("match", parameters2);

        assertEquals("true", output2);

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters3 = new HashMap<>();
        parameters3.put("0", new InterpreterDataType("Doo"));
        parameters3.put("1", new InterpreterDataType("Boo"));

        String output3 = test3.TestFunctions("match", parameters3);

        assertEquals("false", output3);
    }

    @Test
    public void TestSub() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("DontReplace"));
        parameters1.put("1", new InterpreterDataType("Replace"));
        parameters1.put("2", new InterpreterDataType("Replacement"));

        assertEquals("DontReplacement", test1.TestFunctions("sub", parameters1));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("Hello"));
        parameters2.put("1", new InterpreterDataType("lo"));
        parameters2.put("2", new InterpreterDataType("Bye"));

        assertEquals("HelBye", test2.TestFunctions("sub", parameters2));

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog3, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters3 = new HashMap<>();
        parameters3.put("0", new InterpreterDataType("KaeyaAbi"));
        parameters3.put("1", new InterpreterDataType("Abi"));
        parameters3.put("2", new InterpreterDataType("Roxas"));

        assertEquals("KaeyaRoxas", test3.TestFunctions("sub", parameters3));
    }

    @Test
    public void TestIndex() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("Item"));
        parameters1.put("1", new InterpreterDataType("It"));

        assertEquals("1", test1.TestFunctions("index", parameters1));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("Purple"));
        parameters2.put("1", new InterpreterDataType("ple"));

        assertEquals("4", test2.TestFunctions("index", parameters2));

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog3, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters3 = new HashMap<>();
        parameters3.put("0", new InterpreterDataType("One"));
        parameters3.put("1", new InterpreterDataType("ne"));

        assertEquals("2", test3.TestFunctions("index", parameters3));
    }

    @Test
    public void TestLength() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("Five"));

        assertEquals("4", test1.TestFunctions("length", parameters1));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("Bobby"));

        assertEquals("5", test2.TestFunctions("length", parameters2));

        // Test 3.
        ProgramNode prog3 = new ProgramNode();
        Interpreter test3 = new Interpreter(prog3, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters3 = new HashMap<>();
        parameters3.put("0", new InterpreterDataType("TurkeyBeans"));

        assertEquals("11", test3.TestFunctions("length", parameters3));
    }

    /**
     * Come back to this at the end.
     * @throws Exception
     */
    @Test
    public void TestSplit() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));

        InterpreterArrayDataType array1 = new InterpreterArrayDataType();
        InterpreterArrayDataType array2 = new InterpreterArrayDataType();
        InterpreterArrayDataType array3 = new InterpreterArrayDataType();
        array1.AddArray("1", array2);
        array1.AddArray("3", array3);

        InterpreterDataType value1 = new InterpreterDataType("Edward");
        InterpreterDataType value2 = new InterpreterDataType("d");

        array1.AddArray("0", value1);
        array1.AddArray("2", value2);

        assertEquals("2", test1.TestFunctions("split", array1.GetArrayMap()));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));

        InterpreterArrayDataType array4 = new InterpreterArrayDataType();
        array4.AddArray("0", new InterpreterDataType("Spooky Ghost Pumpkins Costumes"));
        array4.AddArray("2", new InterpreterDataType(" "));

        InterpreterArrayDataType value3 = new InterpreterArrayDataType();
        InterpreterArrayDataType value4 = new InterpreterArrayDataType();

        array4.AddArray("1", value3);
        array4.AddArray("3", value4);

        assertEquals("4", test2.TestFunctions("split", array4.GetArrayMap()));
    }

    @Test
    public void TestSubstr() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("Coraline"));
        parameters1.put("1", new InterpreterDataType("0"));
        parameters1.put("2", new InterpreterDataType("5"));

        assertEquals("Coral", test1.TestFunctions("substr", parameters1));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("WowCode"));
        parameters2.put("1", new InterpreterDataType("3"));

        assertEquals("Code", test2.TestFunctions("substr", parameters2));
    }

    @Test
    public void TestToLower() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("COOKIES"));

        assertEquals("cookies", test1.TestFunctions("tolower", parameters1));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("COCOA"));

        assertEquals("cocoa", test2.TestFunctions("tolower", parameters2));
    }

    @Test
    public void TestToUpper() throws Exception{
        // Test 1.
        ProgramNode prog1 = new ProgramNode();
        Interpreter test1 = new Interpreter(prog1, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters1 = new HashMap<>();
        parameters1.put("0", new InterpreterDataType("markers"));

        assertEquals("MARKERS", test1.TestFunctions("toupper", parameters1));

        // Test 2.
        ProgramNode prog2 = new ProgramNode();
        Interpreter test2 = new Interpreter(prog2, Path.of("Nonexistent"));
        HashMap<String, InterpreterDataType> parameters2 = new HashMap<>();
        parameters2.put("0", new InterpreterDataType("cob"));

        assertEquals("COB", test2.TestFunctions("toupper", parameters2));
    }
}
