import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;

public class UnitTests {
    @Test
    public void TestTokenManagerMatch() throws Exception{
        /**
         * // Test 1.
         *         String input1 = "Hello$Me";
         *         Lexer temp1 = new Lexer(input1);
         *         LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
         *         Parser object1 = new Parser(tokens1);
         *
         *         Exception exception = assertThrows(Exception.class, () -> object1.Parse());
         *         assertEquals("Exception!", exception.getMessage());
         *
         *         // Test 2.
         *         String input2 = "HelloWorld";
         */
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

    /**
     * Come back to this test later after Phipps responds back.
     * @throws Exception
     */
    @Test
    public void TestAccessSeparators() throws Exception{
        // Test 1.
        String input1 = "My; Name; Is;";
        Lexer temp1 = new Lexer(input1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());
        Parser test1 = new Parser(tokens1);

        assertFalse(test1.AcceptSeparators());
        assertTrue(test1.AcceptSeparators());
        assertFalse(test1.AcceptSeparators());
        assertTrue(test1.AcceptSeparators());
        assertFalse(test1.AcceptSeparators());
        assertTrue(test1.AcceptSeparators());
    }

    @Test
    public void TestParseFunction() throws Exception{

    }

    @Test
    public void TestParseAction() throws Exception{

    }

    @Test
    public void TestSpecialCase() throws Exception{

    }
}
