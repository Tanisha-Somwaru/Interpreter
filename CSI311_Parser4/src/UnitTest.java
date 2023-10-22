import org.junit.Test;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnitTest {
    @Test
    public void TestParseBlock() throws Exception{
        // Test 1 basic functionality
        String input1 = "BEGIN{}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: [Statements: []]\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: []\n", test1.Parse().toString());

        // Test 2 basic functionality.
        String input2 = "END{print (\"hello\")}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: [Statements: [Function name: Optional[PRINT] Parameters: [Constant name: STRINGLITERAL(hello)]]]\n" +
                "Other Nodes: []\n", test2.Parse().toString());

        // Test 3 with awk program.
        String input3 = "BEGIN { print (\"Don't Panic!\") }";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("BEGIN: [Statements: [Function name: Optional[PRINT] Parameters: [Constant name: STRINGLITERAL(Don't Panic!)]]]\n" + "Function Nodes: []\n" +
                "END: []\n" + "Other Nodes: []\n", test3.Parse().toString());

        // Test 4 with awk program.
        String input4 = "{ x += $5 }\n" + "END { print (\"total bytes: \") x }";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: [Statements: []]\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(x)]" +
                "Expression: Left node, right node, and operations: Name: WORD(x), Optional[Left node and operations: Constant name: NUMBER(5), DOLLAR], " +
                "ADD]]\n", test4.Parse().toString());
    }

    @Test
    public void TestParseBreak() throws Exception{
        // Test 1 basic functionality.
        String input1 = "{break;}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Break node: Break]]\n", test1.Parse().toString());

        // Test 2 awk program.
        String input2 = "{\n" + "num = $1\n" + "for (divisor = 2; divisor * divisor <= num; divisor++) {\n" + "if (num % divisor == 0)\n" + "break\n" + "}\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(num)]Expression: Left node, " +
                "right node, and operations: Name: WORD(num), Optional[Left node and operations: Constant name: NUMBER(1), DOLLAR], EQ, For loop initializer: Optional[Target: " +
                "Optional[Name: WORD(divisor)]Expression: Left node, right node, and operations: Name: WORD(divisor), Optional[Constant name: NUMBER(2)], EQ] For loop condition: " +
                "Optional[Left node, right node, and operations: Left node, right node, and operations: Name: WORD(divisor), Optional[Name: WORD(divisor)], MULTIPLY, " +
                "Optional[Name: WORD(num)], LE] For loop update: Optional[Target: Optional[Name: WORD(divisor)]Expression: Left node and operations: Name: WORD(divisor), " +
                "POSTINC] For loop statements: Optional[Statements: [Conditions: Optional[Left node, right node, and operations: Left node, right node, and operations: " +
                "Name: WORD(num), Optional[Name: WORD(divisor)], MODULO, Optional[Constant name: NUMBER(0)], EQ] Statements: Optional[Statements: " +
                "[Break node: Break]]]]]]\n", test2.Parse().toString());

        // Test 3 awk program.
        String input3 = "{\n" + "num = $1\n" + "for (divisor = 2; divisor * divisor <= num; divisor++) {\n" + "if (num % divisor == 0)\n" +
                "break\n" + "}\n" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(num)]Expression: Left node, " +
                "right node, and operations: Name: WORD(num), Optional[Left node and operations: Constant name: NUMBER(1), DOLLAR], EQ, For loop initializer: " +
                "Optional[Target: Optional[Name: WORD(divisor)]Expression: Left node, right node, and operations: Name: WORD(divisor), Optional[Constant name: " +
                "NUMBER(2)], EQ] For loop condition: Optional[Left node, right node, and operations: Left node, right node, and operations: Name: WORD(divisor), " +
                "Optional[Name: WORD(divisor)], MULTIPLY, Optional[Name: WORD(num)], LE] For loop update: Optional[Target: Optional[Name: WORD(divisor)]Expression: " +
                "Left node and operations: Name: WORD(divisor), POSTINC] For loop statements: Optional[Statements: [Conditions: Optional[Left node, right node, and operations: " +
                "Left node, right node, and operations: Name: WORD(num), Optional[Name: WORD(divisor)], MODULO, Optional[Constant name: NUMBER(0)], EQ] Statements: " +
                "Optional[Statements: [Break node: Break]]]]]]\n", test3.Parse().toString());
    }

    /**
     * This has to be fixed idk what happened
     * @throws Exception
     */
    @Test
    public void TestParseContinue() throws Exception{
        // Test 1 basic functionality.
        String input1 = "{continue;}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Continue node: Continue]]\n", test1.Parse().toString());

        // Test 2 awk program with break.
        String input2 = "BEGIN {\n" + "for (x = 0; x <= 20; x++) {\n" + "if (x == 5)\n" + "continue\n" + "printf (\"%d\", x)\n" + "}\n" + "print( \"\")\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: [Statements: [For loop initializer: Optional[Target: Optional[Name: WORD(x)]Expression: Left node, right node, and operations: Name: WORD(x), " +
                "Optional[Constant name: NUMBER(0)], EQ] For loop condition: Optional[Left node, right node, and operations: Name: WORD(x), Optional[Constant name: NUMBER(20)], LE] " +
                "For loop update: Optional[Target: Optional[Name: WORD(x)]Expression: Left node and operations: Name: WORD(x), POSTINC] For loop statements: Optional[Statements: " +
                "[Conditions: Optional[Left node, right node, and operations: Name: WORD(x), Optional[Constant name: NUMBER(5)], EQ] Statements: Optional[Statements: " +
                "[Continue node: Continue]], Function name: Optional[PRINTF] Parameters: [Constant name: STRINGLITERAL(%d), Name: WORD(x)]]], Function name: Optional[PRINT] " +
                "Parameters: [Constant name: STRINGLITERAL()]]]\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: []\n", test2.Parse().toString());

        // Test 3 awk program with continue.
        String input3 = "BEGIN {\n" + "x = 0\n" + "while(x <= 20) {\n" + "if (x == 5)\n" + "continue\n" + "printf(\"%d\", x)\n" + "}\n" +
                "print (\"\")\n" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("BEGIN: [Statements: [Target: Optional[Name: WORD(x)]Expression: Left node, right node, and operations: Name: WORD(x), " +
                "Optional[Constant name: NUMBER(0)], EQ, Conditions: Optional[Left node, right node, and operations: Name: WORD(x), Optional[Constant name: NUMBER(20)], LE] " +
                "Statements: Optional[Statements: [Conditions: Optional[Left node, right node, and operations: Name: WORD(x), Optional[Constant name: NUMBER(5)], EQ] " +
                "Statements: Optional[Statements: [Continue node: Continue]], Function name: Optional[PRINTF] Parameters: [Constant name: STRINGLITERAL(%d), Name: WORD(x)]]], " +
                "Function name: Optional[PRINT] Parameters: [Constant name: STRINGLITERAL()]]]\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: []\n", test3.Parse().toString());
    }

    @Test
    public void TestParseIf() throws Exception{
        // Test 1 basic functionality.
        String input1 = "{\n" + "if ($3 ==\"\" || $4 == \"\" || $5 == \"\")\n" + "\tprint (\"Some score for the student\",$1,\"is missing\");\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional[Left node, right node, and operations: " +
                "Left node and operations: Constant name: NUMBER(3), DOLLAR, Optional[Left node, right node, and operations: Constant name: STRINGLITERAL(), Optional[Left node, " +
                "right node, and operations: Left node and operations: Constant name: NUMBER(4), DOLLAR, Optional[Left node, right node, and operations: Constant name: " +
                "STRINGLITERAL(), Optional[Left node, right node, and operations: Left node and operations: Constant name: NUMBER(5), DOLLAR, Optional[Constant name: " +
                "STRINGLITERAL()], EQ], OR], EQ], OR], EQ] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Constant name: " +
                "STRINGLITERAL(Some score for the student), Left node and operations: Constant name: NUMBER(1), DOLLAR, Constant name: " +
                "STRINGLITERAL(is missing)]]]]]\n", test1.Parse().toString());

        // Test 2 basic functionality.
        String input2 = "{\n" + "if ($3 >=35 && $4 >= 35 && $5 >= 35)\n" + "\tprint ($0,\"=>\",\"Pass\");\n" + "else\n" + "\tprint ($0,\"=>\",\"Fail\");\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Conditions: Optional[Left node, right node, and operations: " +
                "Left node and operations: Constant name: NUMBER(3), DOLLAR, Optional[Left node, right node, and operations: Constant name: NUMBER(35), " +
                "Optional[Left node, right node, and operations: Left node and operations: Constant name: NUMBER(4), DOLLAR, Optional[Left node, right node, and operations: " +
                "Constant name: NUMBER(35), Optional[Left node, right node, and operations: Left node and operations: Constant name: NUMBER(5), DOLLAR, " +
                "Optional[Constant name: NUMBER(35)], GE], AND], GE], AND], GE] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: " +
                "[Left node and operations: Constant name: NUMBER(0), DOLLAR, Constant name: STRINGLITERAL(=>), Constant name: STRINGLITERAL(Pass)]]] Next If: Statements: " +
                "Optional[Statements: [Function name: Optional[PRINT] Parameters: [Left node and operations: Constant name: NUMBER(0), DOLLAR, Constant name: STRINGLITERAL(=>), " +
                "Constant name: STRINGLITERAL(Fail)]]]]]\n", test2.Parse().toString());

        // Test 3 awk programs.
        String input3 = "{\n" + "total=$3+$4+$5;\n" + "avg=total/3;\n" + "if ( avg >= 90 ) grade=\"A\";\n" + "else if ( avg >= 80) grade =\"B\";\n" +
                "else if (avg >= 70) grade =\"C\";\n" + "else grade=\"D\";\n" + "\n" + "print ($0,\"=>\",grade);\n" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(total)]Expression: Left node, right node, " +
                "and operations: Name: WORD(total), Optional[Left node, right node, and operations: Left node, right node, and operations: Left node and operations: Constant name: " +
                "NUMBER(3), DOLLAR, Optional[Left node and operations: Constant name: NUMBER(4), DOLLAR], ADD, Optional[Left node and operations: Constant name: NUMBER(5), DOLLAR], ADD], " +
                "EQ, Target: Optional[Name: WORD(avg)]Expression: Left node, right node, and operations: Name: WORD(avg), Optional[Left node, right node, and operations: " +
                "Name: WORD(total), Optional[Constant name: NUMBER(3)], DIVIDE], EQ, Conditions: Optional[Left node, right node, and operations: Name: WORD(avg), " +
                "Optional[Constant name: NUMBER(90)], GE] Statements: Optional[Statements: [Target: Optional[Name: WORD(grade)]Expression: Left node, right node, " +
                "and operations: Name: WORD(grade), Optional[Constant name: STRINGLITERAL(A)], EQ]] Next If: Conditions: Optional[Left node, right node, and operations: " +
                "Name: WORD(avg), Optional[Constant name: NUMBER(80)], GE] Statements: Optional[Statements: [Target: Optional[Name: WORD(grade)]Expression: " +
                "Left node, right node, and operations: Name: WORD(grade), Optional[Constant name: STRINGLITERAL(B)], EQ]] Next If: Conditions: Optional[Left node, right node, " +
                "and operations: Name: WORD(avg), Optional[Constant name: NUMBER(70)], GE] Statements: Optional[Statements: [Target: Optional[Name: WORD(grade)]Expression: " +
                "Left node, right node, and operations: Name: WORD(grade), Optional[Constant name: STRINGLITERAL(C)], EQ]] Next If: Statements: Optional[Statements: " +
                "[Target: Optional[Name: WORD(grade)]Expression: Left node, right node, and operations: Name: WORD(grade), Optional[Constant name: STRINGLITERAL(D)], EQ]], " +
                "Function name: Optional[PRINT] Parameters: [Left node and operations: Constant name: NUMBER(0), DOLLAR, Constant name: STRINGLITERAL(=>), " +
                "Name: WORD(grade)]]]\n", test3.Parse().toString());
    }

    @Test
    public void TestParseFor() throws Exception{
        // Test 1 basic functionality of for loop.
        String input1 = "{\n" + "for (i = 1; i <= 3; i++)\n" + "print ($i)\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [For loop initializer: Optional[Target: Optional[Name: WORD(i)]" +
                "Expression: Left node, right node, and operations: Name: WORD(i), Optional[Constant name: NUMBER(1)], EQ] For loop condition: " +
                "Optional[Left node, right node, and operations: Name: WORD(i), Optional[Constant name: NUMBER(3)], LE] For loop update: Optional[Target: Optional[Name: WORD(i)]" +
                "Expression: Left node and operations: Name: WORD(i), POSTINC] For loop statements: Optional[Statements: [Function name: Optional[PRINT] " +
                "Parameters: [Left node and operations: Name: WORD(i), DOLLAR]]]]]\n", test1.Parse().toString());

        // Test 2 basic functionality of for each loop.
        String input2 = "{\n for (x in used)\n print (x)}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [For each condition: Optional[Left node, right node, and operations: " +
                "Name: WORD(x), Optional[Name: WORD(used)], IN] For each statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: " +
                "[Name: WORD(x)]]]]]\n", test2.Parse().toString());

        // Test 3 awk program for each loop.
        String input3 = "END {\n" + "for (x in used) {\n" + "if (length(x) > 10) {\n" + "++num_long_words\n" + "print (x)\n" + "}\n" + "}\n" +
                "print (num_long_words, \"words longer than 10 characters\")\n" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: [Statements: [For each condition: Optional[Left node, right node, and operations: Name: WORD(x), " +
                "Optional[Name: WORD(used)], IN] For each statements: Optional[Statements: [Conditions: Optional[Left node, right node, and operations: Function name: " +
                "Optional[WORD(length)] Parameters: [Name: WORD(x)], Optional[Constant name: NUMBER(10)], GT] Statements: Optional[Statements: [Target: Optional[Name: " +
                "WORD(num_long_words)]Expression: Left node and operations: Name: WORD(num_long_words), PREINC, Function name: Optional[PRINT] Parameters: " +
                "[Name: WORD(x)]]]]], Function name: Optional[PRINT] Parameters: [Name: WORD(num_long_words), Constant name: STRINGLITERAL(words longer than 10 characters)]]]\n" +
                "Other Nodes: []\n", test3.Parse().toString());

        // Test 4 awk program for loop.
        String input4 = "BEGIN {\n" + "sum = 0; for (i = 0; i < 20; ++i) { \n" + "sum += i; if (sum > 50) break; else print (\"Sum =\", sum)\n" +
                "} \n" + "}";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("BEGIN: [Statements: [Target: Optional[Name: WORD(sum)]Expression: Left node, right node, and operations: Name: WORD(sum), Optional[Constant name: " +
                "NUMBER(0)], EQ, For loop initializer: Optional[Target: Optional[Name: WORD(i)]Expression: Left node, right node, and operations: Name: WORD(i), Optional[Constant name: " +
                "NUMBER(0)], EQ] For loop condition: Optional[Left node, right node, and operations: Name: WORD(i), Optional[Constant name: NUMBER(20)], LT] " +
                "For loop update: Optional[Target: Optional[Name: WORD(i)]Expression: Left node and operations: Name: WORD(i), PREINC] For loop statements: Optional[Statements: " +
                "[Target: Optional[Name: WORD(sum)]Expression: Left node, right node, and operations: Name: WORD(sum), Optional[Name: WORD(i)], ADD, Conditions: " +
                "Optional[Left node, right node, and operations: Name: WORD(sum), Optional[Constant name: NUMBER(50)], GT] Statements: Optional[Statements: [Break node: Break]] " +
                "Next If: Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Constant name: STRINGLITERAL(Sum =), Name: WORD(sum)]]]]]]]\n" +
                "Function Nodes: []\n" + "END: []\n" + "Other Nodes: []\n", test4.Parse().toString());
    }

    @Test
    public void TestParseWhile() throws Exception{
        // Test 1 basic functionality.
        String input1 = "{\n" + "i = 1\n" + "while (i <= 3) {\n" + "print ($i)\n" + "i++\n" + "}\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(i)]Expression: " +
                "Left node, right node, and operations: Name: WORD(i), Optional[Constant name: NUMBER(1)], EQ, Conditions: Optional[Left node, right node, and operations: " +
                "Name: WORD(i), Optional[Constant name: NUMBER(3)], LE] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Left node and operations: " +
                "Name: WORD(i), DOLLAR], Target: Optional[Name: WORD(i)]Expression: Left node and operations: Name: WORD(i), POSTINC]]]]\n", test1.Parse().toString());

        // Test 2 awk program.
        String input2 = "{\n" + "i=2; total=0; \n" + "while (i <= NF) {\n" + "total = total + $i;\n" + "i++;\n" + "}\n" + "print (\"Item\", $1, \":\", " +
                "total, \"quantities sold\");\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(i)]Expression: " +
                "Left node, right node, and operations: Name: WORD(i), Optional[Constant name: NUMBER(2)], EQ, Target: Optional[Name: WORD(total)]Expression: " +
                "Left node, right node, and operations: Name: WORD(total), Optional[Constant name: NUMBER(0)], EQ, Conditions: Optional[Left node, right node, and operations: " +
                "Name: WORD(i), Optional[Name: WORD(NF)], LE] Statements: Optional[Statements: [Target: Optional[Name: WORD(total)]Expression: Left node, right node, and operations: " +
                "Name: WORD(total), Optional[Left node, right node, and operations: Name: WORD(total), Optional[Left node and operations: Name: WORD(i), DOLLAR], ADD], EQ, " +
                "Target: Optional[Name: WORD(i)]Expression: Left node and operations: Name: WORD(i), POSTINC]], Function name: Optional[PRINT] Parameters: " +
                "[Constant name: STRINGLITERAL(Item), Left node and operations: Constant name: NUMBER(1), DOLLAR, Constant name: STRINGLITERAL(:), Name: WORD(total), " +
                "Constant name: STRINGLITERAL(quantities sold)]]]\n", test2.Parse().toString());
    }

    @Test
    public void ParseDoWhile() throws Exception{
        // Test 1 basic functionality.
        String input1 = "{\n" + "i = 1\n" + "do {\n" + "print ($0)\n" + "i++\n" + "} while (i <= 10)\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(i)]Expression: Left node, " +
                "right node, and operations: Name: WORD(i), Optional[Constant name: NUMBER(1)], EQ, Conditions: Optional[Left node, right node, and operations: Name: " +
                "WORD(i), Optional[Constant name: NUMBER(10)], LE] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Left node and operations: " +
                "Constant name: NUMBER(0), DOLLAR], Target: Optional[Name: WORD(i)]Expression: Left node and operations: Name: WORD(i), POSTINC]]]]\n", test1.Parse().toString());

        // Test 2 awk program.
        String input2 = "{\n" + "total = i = 0\n" + "do {\n" + "++i\n" + "total += $i\n" + "} while ( total <= 100 )\n" + "print (i, \":\", total) }";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Target: Optional[Name: WORD(total)]Expression: " +
                "Left node, right node, and operations: Name: WORD(total), Optional[Target: Optional[Name: WORD(i)]Expression: Left node, right node, and operations: " +
                "Name: WORD(i), Optional[Constant name: NUMBER(0)], EQ], EQ, Conditions: Optional[Left node, right node, and operations: Name: WORD(total), Optional[Constant name: " +
                "NUMBER(100)], LE] Statements: Optional[Statements: [Target: Optional[Name: WORD(i)]Expression: Left node and operations: Name: WORD(i), PREINC, " +
                "Target: Optional[Name: WORD(total)]Expression: Left node, right node, and operations: Name: WORD(total), Optional[Left node and operations: " +
                "Name: WORD(i), DOLLAR], ADD]], Function name: Optional[PRINT] Parameters: [Name: WORD(i), Constant name: STRINGLITERAL(:), " +
                "Name: WORD(total)]]]\n", test2.Parse().toString());
    }

    @Test
    public void TestParseDelete() throws Exception{
        // Test 1 basic functionality.
        String input1 = "END{\n" + "for (i in frequencies)\n" + "delete frequencies[i]\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: [Statements: [For each condition: Optional[Left node, right node, and operations: Name: WORD(i), " +
                "Optional[Name: WORD(frequencies)], IN] For each statements: Optional[Statements: [Delete array reference: Optional[Name: WORD(frequencies), Name and expression: " +
                "WORD(frequencies), Optional[Name: WORD(i)]]]]]]\n" + "Other Nodes: []\n", test1.Parse().toString());

        // Test 2 awk program.
        String input2 = "{\n" + "delete foo[4]\n" + "if (4 in foo)\n" + "print (\"This will never be printed\")\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Delete array reference: Optional[Name: WORD(foo), " +
                "Name and expression: WORD(foo), Optional[Constant name: NUMBER(4)]], Conditions: Optional[Left node, right node, and operations: Constant name: " +
                "NUMBER(4), Optional[Name: WORD(foo)], IN] Statements: Optional[Statements: [Function name: Optional[PRINT] Parameters: [Constant name: " +
                "STRINGLITERAL(This will never be printed)]]]]]\n", test2.Parse().toString());
    }

    @Test
    public void TestParseReturn() throws Exception{
        // Test 1 basic functionality.
        String input1 = "{\n" + "return 5\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: []\n" + "END: []\n" + "Other Nodes: [Statements: [Return: Optional[Constant name: NUMBER(5)]]]\n", test1.Parse().toString());

        // Test 2 awk program.
        String input2 = "function maxelt(vec, i, ret)\n" + "{\n" + "for (i in vec) {\n" + "if (ret == \"\" || vec[i] > ret)\n" + "ret = vec[i]\n" +
                "}\n" + "return ret\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: [Function name: WORD(maxelt) Parameters: [WORD(vec), WORD(i), WORD(ret)]]\n" + "END: []\n" + "Other Nodes: " +
                "[Statements: [For each condition: Optional[Left node, right node, and operations: Name: WORD(i), Optional[Name: WORD(vec)], IN] For each statements: " +
                "Optional[Statements: [Conditions: Optional[Left node, right node, and operations: Name: WORD(ret), Optional[Left node, right node, and operations: " +
                "Constant name: STRINGLITERAL(), Optional[Left node, right node, and operations: Name: WORD(vec), Name and expression: WORD(vec), Optional[Name: WORD(i)], " +
                "Optional[Name: WORD(ret)], GT], OR], EQ] Statements: Optional[Statements: [Target: Optional[Name: WORD(ret)]Expression: Left node, right node, and operations: " +
                "Name: WORD(ret), Optional[Name: WORD(vec), Name and expression: WORD(vec), Optional[Name: WORD(i)]], EQ]]]], " +
                "Return: Optional[Name: WORD(ret)]]]\n", test2.Parse().toString());
    }

    @Test
    public void ParseFunctionCall() throws Exception{
        // Test 1 basic functionality.
        String input1 = "function rev(str)\n" + "{\n" + "if (str == \"\")\n" + "return \"\"\n" + "\n" + "return (rev(substr(str, 2)) substr(str, 1, 1))\n" + "}";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("BEGIN: []\n" + "Function Nodes: [Function name: WORD(rev) Parameters: [WORD(str)]]\n" + "END: []\n" + "Other Nodes: [Statements: " +
                "[Conditions: Optional[Left node, right node, and operations: Name: WORD(str), Optional[Constant name: STRINGLITERAL()], EQ] Statements: Optional[Statements: " +
                "[Return: Optional[Constant name: STRINGLITERAL()]]], Return: Optional[Left node, right node, and operations: Function name: Optional[WORD(rev)] Parameters: " +
                "[Function name: Optional[WORD(substr)] Parameters: [Name: WORD(str), Constant name: NUMBER(2)]], Optional[Function name: Optional[WORD(substr)] Parameters: " +
                "[Name: WORD(str), Constant name: NUMBER(1), Constant name: NUMBER(1)]], CONCATENATION]]]\n", test1.Parse().toString());

        // Test 2 awk program.
        String input2 = "function find_min(num1, num2){\n" + "if (num1 < num2)\n" + "return num1\n" + "return num2\n" + "}\n" + "function find_max(num1, num2){\n" +
                "if (num1 > num2)\n" + "return num1\n" + "return num2\n" + "}\n" + "function main(num1, num2){\n" + "result = find_min(10, 20)\n" + "print (\"Minimum =\", result)\n" +
                " \n" + "result = find_max(10, 20)\n" + "print (\"Maximum =\", result)\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("BEGIN: []\n" + "Function Nodes: [Function name: WORD(find_min) Parameters: [WORD(num1), WORD(num2)], Function name: WORD(find_max) Parameters: " +
                "[WORD(num1), WORD(num2)], Function name: WORD(main) Parameters: [WORD(num1), WORD(num2)]]\n" + "END: []\n" + "Other Nodes: [Statements: " +
                "[Conditions: Optional[Left node, right node, and operations: Name: WORD(num1), Optional[Name: WORD(num2)], LT] Statements: Optional[Statements: " +
                "[Return: Optional[Name: WORD(num1)]]], Return: Optional[Name: WORD(num2)]], Statements: [Conditions: Optional[Left node, right node, and operations: " +
                "Name: WORD(num1), Optional[Name: WORD(num2)], GT] Statements: Optional[Statements: [Return: Optional[Name: WORD(num1)]]], Return: Optional[Name: WORD(num2)]], " +
                "Statements: [Target: Optional[Name: WORD(result)]Expression: Left node, right node, and operations: Name: WORD(result), Optional[Left node, right node, and operations: " +
                "Function name: Optional[WORD(find_min)] Parameters: [Constant name: NUMBER(10), Constant name: NUMBER(20)], Optional[Function name: Optional[PRINT] Parameters: " +
                "[Constant name: STRINGLITERAL(Minimum =), Name: WORD(result)]], CONCATENATION], EQ, Target: Optional[Name: WORD(result)]Expression: Left node, right node, and " +
                "operations: Name: WORD(result), Optional[Left node, right node, and operations: Function name: Optional[WORD(find_max)] Parameters: [Constant name: NUMBER(10), " +
                "Constant name: NUMBER(20)], Optional[Function name: Optional[PRINT] Parameters: [Constant name: STRINGLITERAL(Maximum =), " +
                "Name: WORD(result)]], CONCATENATION], EQ]]\n", test2.Parse().toString());
    }

    @Test
    public void TestSpecialCases() throws Exception{
        // Test 1.
        String input1 = "{for(x in 10" + "\n" + "return 5";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        Exception exception1 = assertThrows(Exception.class, () -> test1.Parse());
        assertEquals("Incorrect syntax!", exception1.getMessage());

        // Test 2.
        String input2 = "BEGIN{\n" + "if (x == 0)\n" + "print (\"Hello\"" + "\n" + "}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        Exception exception2 = assertThrows(Exception.class, () -> test2.Parse());
        assertEquals("Incorrect syntax!", exception2.getMessage());

        // Test 3.
        String input3 = "{" + "if (condition == cold){" + "\n" + "\n" + "}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        Exception exception3 = assertThrows(Exception.class, () -> test3.Parse());
        assertEquals("Incorrect syntax!", exception3.getMessage());
    }
}
