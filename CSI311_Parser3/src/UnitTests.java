import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class UnitTests {
    @Test
    public void TestPostIncrementAndDecrement() throws Exception{
        // Test 1.
        String input1 = "hello++";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node and operations: Name: WORD(hello), POSTINC]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "tracker--";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node and operations: Name: WORD(tracker), POSTDEC]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "$BobbyBrown++";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node and operations: Left node and operations: Name: WORD(BobbyBrown), POSTINC, DOLLAR]", test3.ParseOperation().toString());
    }

    @Test
    public void TestExponents() throws Exception{
        // Test 1.
        String input1 = "2^2";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(2), Optional[Constant name: NUMBER(2)], EXPONENT]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "4^8^2";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(4), Optional[Left node, right node, and operations: Constant name: NUMBER(8), Optional[Constant name: NUMBER(2)], EXPONENT], EXPONENT]"
        , test2.ParseOperation().toString());

        // Test 3.
        String input3 = "1^2^3^4";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(1), Optional[Left node, right node, and operations: Constant name: NUMBER(2), Optional[Left node, right node, and operations: Constant name: NUMBER(3), " +
                "Optional[Constant name: NUMBER(4)], EXPONENT], EXPONENT], EXPONENT]", test3.ParseOperation().toString());
    }

    @Test
    public void TestFactor() throws Exception{
        // Test 1.
        String input1 = "xy";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Name: WORD(xy)]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "589";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Constant name: NUMBER(589)]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "LifeAlert10";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Name: WORD(LifeAlert10)]", test3.ParseOperation().toString());
    }

    @Test
    public void TestTerm() throws Exception{
        // Test 1.
        String input1 = "15/5";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(15), Optional[Constant name: NUMBER(5)], DIVIDE]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "x%y";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(x), Optional[Name: WORD(y)], MODULO]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "6*5";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(6), Optional[Constant name: NUMBER(5)], MULTIPLY]", test3.ParseOperation().toString());
    }

    @Test
    public void TestExpression() throws Exception{
        // Test 1.
        String input1 = "x + y";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(x), Optional[Name: WORD(y)], ADD]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "5 - y";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(5), Optional[Name: WORD(y)], SUBTRACT]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "25 - 3";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(25), Optional[Constant name: NUMBER(3)], SUBTRACT]", test3.ParseOperation().toString());
    }

    @Test
    public void TestConcatenation() throws Exception{
        // Test 1.
        String input1 = "ab kz";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(ab), Optional[Name: WORD(kz)], CONCATENATION]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "12 13";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(12), Optional[Constant name: NUMBER(13)], CONCATENATION]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "5-y 8+1";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Left node, right node, and operations: Constant name: NUMBER(5), Optional[Name: WORD(y)], SUBTRACT, " +
                "Optional[Left node, right node, and operations: Constant name: NUMBER(8), Optional[Constant name: NUMBER(1)], ADD], CONCATENATION]", test3.ParseOperation().toString());
    }

    @Test
    public void TestBooleanCompare() throws Exception{
        // Test 1.
        String input1 = "IntelliJ > Eclipse";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(IntelliJ), Optional[Name: WORD(Eclipse)], GT]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "5 >= bob";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(5), Optional[Name: WORD(bob)], GE]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "-5 == -5";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node and operations: Left node, right node, and operations: Constant name: NUMBER(5), Optional[Left node and operations: " +
                "Constant name: NUMBER(5), UNARYNEG], EQ, UNARYNEG]", test3.ParseOperation().toString());

        // Test 4.
        String input4 = "7 != 14";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(7), Optional[Constant name: NUMBER(14)], NE]", test4.ParseOperation().toString());

        // Test 5.
        String input5 = "6 < 14";
        Lexer temp5 = new Lexer(input5);
        LinkedList<Token> tokens5 = new LinkedList<>(temp5.Lex());
        Parser test5 = new Parser(tokens5);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(6), Optional[Constant name: NUMBER(14)], LT]", test5.ParseOperation().toString());

        // Test 6.
        String input6 = "6 <= tracker";
        Lexer temp6 = new Lexer(input6);
        LinkedList<Token> tokens6 = new LinkedList<>(temp6.Lex());
        Parser test6 = new Parser(tokens6);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(6), Optional[Name: WORD(tracker)], LE]", test6.ParseOperation().toString());
    }

    @Test
    public void TestMatch() throws Exception{
        // Test 1.
        String input1 = "abc ~ abck";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(abc), Optional[Name: WORD(abck)], MATCH]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "hye !~ hi";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(hye), Optional[Name: WORD(hi)], NOTMATCH]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "crabs12 ~ abs12";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(crabs12), Optional[Name: WORD(abs12)], MATCH]", test3.ParseOperation().toString());
    }

    @Test
    public void TestArrayMembership() throws Exception{
        // Test 1.
        String input1 = "a[1+3] in $1";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(a), Name and expression: WORD(a), " +
                "Optional[Left node, right node, and operations: Constant name: NUMBER(1), Optional[Constant name: NUMBER(3)], ADD], " +
                "Optional[Left node and operations: Constant name: NUMBER(1), DOLLAR], IN]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "help in life";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(help), Optional[Name: WORD(life)], IN]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "cake[j] in oceanFloor";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(cake), Name and expression: WORD(cake), Optional[Name: WORD(j)], " +
                "Optional[Name: WORD(oceanFloor)], IN]", test3.ParseOperation().toString());
    }

    @Test
    public void TestAnd() throws Exception{
        // Test 1.
        String input1 = "hello && bottle";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(hello), Optional[Name: WORD(bottle)], AND]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "10 && fish";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(10), Optional[Name: WORD(fish)], AND]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "15 && 12";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(15), Optional[Constant name: NUMBER(12)], AND]", test3.ParseOperation().toString());
    }

    @Test
    public void TestOr() throws Exception{
        // Test 1.
        String input1 = "pocky || chocolate";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(pocky), Optional[Name: WORD(chocolate)], OR]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "18 || 20";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(18), Optional[Constant name: NUMBER(20)], OR]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "Boba || 58";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Left node, right node, and operations: Name: WORD(Boba), Optional[Constant name: NUMBER(58)], OR]", test3.ParseOperation().toString());
    }

    @Test
    public void TestTernary() throws Exception{
        // Test 1.
        String input1 = "$8 ? chicken : 16";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Expression: Optional[Left node and operations: Constant name: NUMBER(8), DOLLAR]Case 1: Optional[Name: WORD(chicken)]" +
                "Case 2: Optional[Constant name: NUMBER(16)]]", test1.ParseOperation().toString());

        // Test 2.
        String input2 = "(condition) ? True : False";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Expression: Optional[Name: WORD(condition)]Case 1: Optional[Name: WORD(True)]Case 2: Optional[Name: WORD(False)]]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "(num1 > num2) ? num1 : num2";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Expression: Optional[Left node, right node, and operations: Name: WORD(num1), Optional[Name: WORD(num2)], GT]" +
                "Case 1: Optional[Name: WORD(num1)]Case 2: Optional[Name: WORD(num2)]]", test3.ParseOperation().toString());
    }

    @Test
    public void TestAssignment() throws Exception{
        // Test 1.
        String input1 = "a += 6";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertEquals("Optional[Target: Optional[Constant name: NUMBER(6)]Expression: Left node, right node, and operations: Name: WORD(a), Optional[Constant name: NUMBER(6)], ADD]",
                test1.ParseOperation().toString());

        // Test 2.
        String input2 = "$4 /= 25+2";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertEquals("Optional[Target: Optional[Left node, right node, and operations: Constant name: NUMBER(25), Optional[Constant name: NUMBER(2)], " +
                "ADD]Expression: Left node, right node, and operations: Left node and operations: Constant name: NUMBER(4), DOLLAR, " +
                "Optional[Left node, right node, and operations: Constant name: NUMBER(25), Optional[Constant name: NUMBER(2)], ADD], DIVIDE]", test2.ParseOperation().toString());

        // Test 3.
        String input3 = "c -= 4";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertEquals("Optional[Target: Optional[Constant name: NUMBER(4)]Expression: Left node, right node, and operations: Name: WORD(c), " +
                "Optional[Constant name: NUMBER(4)], SUBTRACT]", test3.ParseOperation().toString());

        // Test 4.
        String input4 = "pizza ^= 69";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertEquals("Optional[Target: Optional[Constant name: NUMBER(69)]Expression: Left node, right node, and operations: Name: WORD(pizza), " +
                "Optional[Constant name: NUMBER(69)], EXPONENT]", test4.ParseOperation().toString());

        // Test 5.
        String input5 = "7 %= 10";
        Lexer temp5 = new Lexer(input5);
        LinkedList<Token> tokens5 = new LinkedList<>(temp5.Lex());
        Parser test5 = new Parser(tokens5);

        assertEquals("Optional[Target: Optional[Constant name: NUMBER(10)]Expression: Left node, right node, and operations: Constant name: NUMBER(7), " +
                "Optional[Constant name: NUMBER(10)], MODULO]", test5.ParseOperation().toString());

        // Test 6.
        String input6 = "78 *= 14";
        Lexer temp6 = new Lexer(input6);
        LinkedList<Token> tokens6 = new LinkedList<>(temp6.Lex());
        Parser test6 = new Parser(tokens6);

        assertEquals("Optional[Target: Optional[Constant name: NUMBER(14)]Expression: Left node, right node, and operations: Constant name: NUMBER(78), " +
                "Optional[Constant name: NUMBER(14)], MULTIPLY]", test6.ParseOperation().toString());

        // Test 7.
        String input7 = "67 = 67";
        Lexer temp7 = new Lexer(input7);
        LinkedList<Token> tokens7 = new LinkedList<>(temp7.Lex());
        Parser test7 = new Parser(tokens7);

        assertEquals("Optional[Left node, right node, and operations: Constant name: NUMBER(67), Optional[Constant name: NUMBER(67)], EQ]", test7.ParseOperation().toString());
    }

    @Test
    public void SpecialCases() throws Exception{
        // Test 1.
        String input1 = "== 90";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        Exception exception1 = assertThrows(Exception.class, () -> test1.ParseOperation());
        assertEquals("Incorrect syntax!", exception1.getMessage());

        // Test 2.
        String input2 = "[9] in a";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        Exception exception2 = assertThrows(Exception.class, () -> test2.ParseOperation());
        assertEquals("Incorrect syntax!", exception2.getMessage());

        // Test 3.
        String input3 = "8 < ,";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        Exception exception3 = assertThrows(Exception.class, () -> test3.ParseOperation());
        assertEquals("Incorrect syntax!", exception3.getMessage());
    }
}