import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;

public class UnitTests {
    @Test
    public void TestTokenManagerMatch() throws Exception{
        // Test 1.
        String input1 = "HelloBobby";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        TokenManager test1 = new TokenManager(tokens1);

        assertTrue(test1.MatchAndRemove(Token.TokenType.WORD).isPresent());

        // Test 2.
        String input2 = "while Crabby (Patty)";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        TokenManager test2 = new TokenManager(tokens2);

        assertTrue(test2.MatchAndRemove(Token.TokenType.WHILE).isPresent());
        assertTrue(test2.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertTrue(test2.MatchAndRemove(Token.TokenType.OPENPARENTHESES).isPresent());
        assertTrue(test2.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertTrue(test2.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES).isPresent());

        // Test 3.
        String input3 = "for if - ?";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        TokenManager test3 = new TokenManager(tokens3);

        assertTrue(test3.MatchAndRemove(Token.TokenType.FOR).isPresent());
        assertTrue(test3.MatchAndRemove(Token.TokenType.IF).isPresent());
        assertTrue(test3.MatchAndRemove(Token.TokenType.MINUS).isPresent());
        assertTrue(test3.MatchAndRemove(Token.TokenType.QUESTION).isPresent());
    }

    @Test
    public void TestTokenManagerMoreTokens() throws Exception{
        // Test 1.
        String input1 = "if [] Rudy";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        TokenManager test1 = new TokenManager(tokens1);

        assertTrue(test1.MatchAndRemove(Token.TokenType.IF).isPresent());
        assertTrue(test1.MoreTokens());
        assertTrue(test1.MatchAndRemove(Token.TokenType.OPENBRACKET).isPresent());
        assertTrue(test1.MoreTokens());
        assertTrue(test1.MatchAndRemove(Token.TokenType.CLOSEDBRACKET).isPresent());
        assertTrue(test1.MoreTokens());
        assertTrue(test1.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertFalse(test1.MoreTokens());

        // Test 2.
        String input2 = "for turtles && biscuits";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        TokenManager test2 = new TokenManager(tokens2);

        assertTrue(test2.MatchAndRemove(Token.TokenType.FOR).isPresent());
        assertTrue(test2.MoreTokens());
        assertTrue(test2.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertTrue(test2.MatchAndRemove(Token.TokenType.AND).isPresent());
        assertTrue(test2.MoreTokens());
        assertTrue(test2.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertFalse(test2.MoreTokens());

        // Test 3.
        String input3 = "123 for Nobles function panda";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        TokenManager test3 = new TokenManager(tokens3);

        assertTrue(test3.MatchAndRemove(Token.TokenType.NUMBER).isPresent());
        assertTrue(test3.MoreTokens());
        assertTrue(test3.MatchAndRemove(Token.TokenType.FOR).isPresent());
        assertTrue(test3.MoreTokens());
        assertTrue(test3.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertTrue(test3.MoreTokens());
        assertTrue(test3.MatchAndRemove(Token.TokenType.FUNCTION).isPresent());
        assertTrue(test3.MoreTokens());
        assertTrue(test3.MatchAndRemove(Token.TokenType.WORD).isPresent());
        assertFalse(test3.MoreTokens());

    }

    @Test
    public void TestAccessSeparators() throws Exception{
        // Test 1.
        String input1 = "function hello(a;, b;)";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertTrue(test1.AcceptSeparators());

        // Test 2.
        String input2 = ";walrus\noscar";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        assertTrue(test2.AcceptSeparators());

        // Test 3.
        String input3 = "function bobbyAlfred(f, y, i, h, l, j;)\n";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        assertTrue(test3.AcceptSeparators());

        // Test 4.
        String input4 = "\nPablo;Patrick\n";
        Lexer temp4 = new Lexer(input4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());
        Parser test4 = new Parser(tokens4);

        assertTrue(test4.AcceptSeparators());
    }

    @Test
    public void TestParseFunction() throws Exception{
        // Test 1.
        String input1 = "function getHelp(why, is, lifeDifficult)";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        ProgramNode node1 = new ProgramNode();
        Parser test1 = new Parser(tokens1);

        assertTrue(test1.ParseFunction(node1));

        // Test 2.
        String input2 = "getMethod(hello, tatter)";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        ProgramNode node2 = new ProgramNode();
        Parser test2 = new Parser(tokens2);

        assertFalse(test2.ParseFunction(node2));

        // Test 1.
        String input3 = "function WowPop()";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        ProgramNode node3 = new ProgramNode();
        Parser test3 = new Parser(tokens3);

        assertTrue(test3.ParseFunction(node3));
    }

    @Test
    public void TestParseAction() throws Exception{
        // Test 1.
        String input = "BEGIN{}";
        Lexer temp1 = new Lexer(input);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        ProgramNode node1 = new ProgramNode();
        Parser test1 = new Parser(tokens1);

        assertTrue(test1.ParseAction(node1));

        // Test 2.
        String input2 = "(b < a){}";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        ProgramNode node2 = new ProgramNode();
        Parser test2 = new Parser(tokens2);

        assertFalse(test2.ParseAction(node2));

        // Test 3.
        String input3 = "END{}";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        ProgramNode node3 = new ProgramNode();
        Parser test3 = new Parser(tokens3);

        assertTrue(test3.ParseAction(node3));
    }

    @Test
    public void TestSpecialCase() throws Exception{
        // Test 1.
        String input1 = "Hello$Me";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        Exception exception1 = assertThrows(Exception.class, () -> test1.Parse());
        assertEquals("Exception!", exception1.getMessage());

        // Test 2.
        String input2 = "&&$";
        Lexer temp2 = new Lexer(input2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());
        Parser test2 = new Parser(tokens2);

        Exception exception2 = assertThrows(Exception.class, () -> test2.Parse());
        assertEquals("Exception!", exception2.getMessage());

        // Test 3.
        String input3 = "function 12Bobby()";
        Lexer temp3 = new Lexer(input3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());
        Parser test3 = new Parser(tokens3);

        Exception exception3 = assertThrows(Exception.class, () -> test3.Parse());
        assertEquals("Exception!", exception3.getMessage());
    }
}