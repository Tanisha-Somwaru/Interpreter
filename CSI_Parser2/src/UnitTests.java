import org.junit.Test;
import java.util.LinkedList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnitTests {
    @Test
    public void TestParseBottomLevel() throws Exception {
        // Test 1.
        String input1 = "-5";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node and operations: Constant name: NUMBER(5), UNARYNEG]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "(++KingdomHearts)";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node and operations: Name: WORD(KingdomHearts), PREINC]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "((!Hello))";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node and operations: Name: WORD(Hello), NOT]", test3.ParseOperation().toString());

        // Test 4.
        String input4 = "(\"Theres a \\\"leak\\\" in the boat\")";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("Optional[Constant name: STRINGLITERAL(Theres a \"leak\" in the boat)]", test4.ParseOperation().toString());
    }

    @Test
    public void TestParseLValue() throws Exception {
        // Test 1.
        String input1 = "$7";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node and operations: Constant name: NUMBER(7), DOLLAR]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "++$b";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node and operations: Left node and operations: Name: WORD(b), DOLLAR, PREINC]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "++a[--b]";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node and operations: Name: WORD(a), Name and expression: WORD(a), Optional[Left node and operations: Name: WORD(b), PREDEC], PREINC]", test3.ParseOperation().toString());

        // Test 4.
        String input4 = "++(MyGoodNess)";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("Optional[Left node and operations: Name: WORD(MyGoodNess), PREINC]", test4.ParseOperation().toString());
    }

    @Test
    public void TestToStringOperation() throws Exception {
        // Test 1.
        String input1 = "--530$";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node and operations: Constant name: NUMBER(530), PREDEC]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "((++a))";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node and operations: Name: WORD(a), PREINC]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "!PunkyBop";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node and operations: Name: WORD(PunkyBop), NOT]", test3.ParseOperation().toString());
    }

    @Test
    public void TestToStringConstant() throws Exception {
        // Test 1.
        String input1 = "\"Tanisha \\\"struggles with code too much\\\" it sucks\"";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Constant name: STRINGLITERAL(Tanisha \"struggles with code too much\" it sucks)]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "350987653930";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Constant name: NUMBER(350987653930)]", test2.ParseOperation().toString());
    }

    @Test
    public void TestToStringPattern() throws Exception {
        // Test 1.
        String input1 = "`[abc]`";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Pattern name: LITERAL([abc])]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "`[101010]`";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Pattern name: LITERAL([101010])]", test2.ParseOperation().toString());
    }

    @Test
    public void TestToStringVariableReference() throws Exception {
        // Test 1,
        String input1 = "abcdefghijkBAAAAA";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Name: WORD(abcdefghijkBAAAAA)]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "cow[31]";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Name: WORD(cow), Name and expression: WORD(cow), Optional[Constant name: NUMBER(31)]]", test2.ParseOperation().toString());
    }

    @Test
    public void TestSpecialCases() throws Exception {
        // Test 1.
        String input1 = "((BYE)";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        Exception exception1 = assertThrows(Exception.class, () -> test1.ParseBottomLevel());
        assertEquals("Incorrect syntax!", exception1.getMessage());

        // Test 2.
        String input2 = "y[boo[[12]]";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        Exception exception2 = assertThrows(Exception.class, () -> test2.ParseLValue());
        assertEquals("Incorrect syntax!", exception2.getMessage());

        // Test 3.
        String input3 = "((W)";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        Exception exception3 = assertThrows(Exception.class, () -> test3.ParseBottomLevel());
        assertEquals("Incorrect syntax!", exception3.getMessage());

        // Test 4.
        String input4 = "--[MyGoodNess]";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        Exception exception4 = assertThrows(Exception.class, () -> test4.ParseBottomLevel());
        assertEquals("Incorrect syntax!", exception4.getMessage());
    }
}