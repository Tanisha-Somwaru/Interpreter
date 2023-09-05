import static org.junit.Assert.*;
import org.junit.Test;
import java.util.LinkedList;

public class UnitTests {
    @Test
    public void testWords() throws Exception {
        // Test 1 variables.
        String words1 = "Brisk Velocity Ideas Actions Starbucks Dinosaur";
        Lexer object1 = new Lexer(words1);
        LinkedList<Token> tokens1 = new LinkedList<>(object1.Lex());

        assertEquals("WORD(Brisk)", tokens1.get(0).toString());
        assertEquals("WORD(Velocity)", tokens1.get(1).toString());
        assertEquals("WORD(Ideas)", tokens1.get(2).toString());
        assertEquals("WORD(Actions)", tokens1.get(3).toString());
        assertEquals("WORD(Starbucks)", tokens1.get(4).toString());
        assertEquals("WORD(Dinosaur)", tokens1.get(5).toString());

        // Test 2 variables.
        String words2 = "jUnits Water Fish_Swim Bobby_Brown Peter_Parker";
        Lexer object2 = new Lexer(words2);
        LinkedList<Token> tokens2 = new LinkedList<>(object2.Lex());

        assertEquals("WORD(jUnits)", tokens2.get(0).toString());
        assertEquals("WORD(Water)", tokens2.get(1).toString());
        assertEquals("WORD(Fish_Swim)", tokens2.get(2).toString());
        assertEquals("WORD(Bobby_Brown)", tokens2.get(3).toString());
        assertEquals("WORD(Peter_Parker)", tokens2.get(4).toString());

        // Test 3 variables.
        String words3 = "BUBBLETEA BEAR DOGGIES helloWorld reZero reVamp";
        Lexer object3 = new Lexer(words3);
        LinkedList<Token> tokens3 = new LinkedList<>(object3.Lex());

        assertEquals("WORD(BUBBLETEA)", tokens3.get(0).toString());
        assertEquals("WORD(BEAR)", tokens3.get(1).toString());
        assertEquals("WORD(DOGGIES)", tokens3.get(2).toString());
        assertEquals("WORD(helloWorld)", tokens3.get(3).toString());
        assertEquals("WORD(reZero)", tokens3.get(4).toString());
        assertEquals("WORD(reVamp)", tokens3.get(5).toString());

        // Test 4 variables.
        String word4 = "Mr_Krabs";
        Lexer object4 = new Lexer(word4);
        LinkedList<Token> token4 = new LinkedList<>(object4.Lex());

        assertEquals("WORD(Mr_Krabs)", token4.get(0).toString());
    }

    @Test
    public void testNumbers() throws Exception {
        // Test 1 variables.
        String numbers1 = "1234 0546 9087";
        Lexer objectNum1 = new Lexer(numbers1);
        LinkedList<Token> tokens1 = new LinkedList<>(objectNum1.Lex());

        assertEquals("NUMBER(1234)", tokens1.get(0).toString());
        assertEquals("NUMBER(0546)", tokens1.get(1).toString());
        assertEquals("NUMBER(9087)", tokens1.get(2).toString());

        // Test 2 variables.
        String numbers2 = "12.38 5.43 6.7898 .90 56.78";
        Lexer objectNum2 = new Lexer(numbers2);
        LinkedList<Token> tokens2 = new LinkedList<>(objectNum2.Lex());

        assertEquals("NUMBER(12.38)", tokens2.get(0).toString());
        assertEquals("NUMBER(5.43)", tokens2.get(1).toString());
        assertEquals("NUMBER(6.7898)", tokens2.get(2).toString());
        assertEquals("NUMBER(.90)", tokens2.get(3).toString());
        assertEquals("NUMBER(56.78)", tokens2.get(4).toString());

        // Test 3 variables.
        String empty = " ";
        Lexer objectNum3= new Lexer(empty);
        LinkedList<Token> tokens3 = new LinkedList<>(objectNum3.Lex());

        assertEquals(0, tokens3.size());

        // Test 4 variables.
        String number4 = "6780";
        Lexer objectNum4 = new Lexer(number4);
        LinkedList<Token> token4 = new LinkedList<>(objectNum4.Lex());

        assertEquals("NUMBER(6780)", token4.get(0).toString());
    }

    @Test
    public void testWordsAndNumbers() throws Exception {
        // Test 1 variables.
        String wordsNum1 = "5Below Cookie18 34JumpStreet Mr_Biscker3";
        Lexer object1 = new Lexer(wordsNum1);
        LinkedList<Token> tokens1 = new LinkedList<>(object1.Lex());

        assertEquals("NUMBER(5)", tokens1.get(0).toString());
        assertEquals("WORD(Below)", tokens1.get(1).toString());
        assertEquals("WORD(Cookie18)", tokens1.get(2).toString());
        assertEquals("NUMBER(34)", tokens1.get(3).toString());
        assertEquals("WORD(JumpStreet)", tokens1.get(4).toString());
        assertEquals("WORD(Mr_Biscker3)", tokens1.get(5).toString());


        // Test 2 variables.
        String wordNum2 = "2004today MooBa34 CocoaDay_24";
        Lexer object2 = new Lexer(wordNum2);
        LinkedList<Token> tokens2 = new LinkedList<>(object2.Lex());

        assertEquals("NUMBER(2004)", tokens2.get(0).toString());
        assertEquals("WORD(today)", tokens2.get(1).toString());
        assertEquals("WORD(MooBa34)", tokens2.get(2).toString());
        assertEquals("WORD(CocoaDay_24)", tokens2.get(3).toString());

        // Test 3 variables.
        String wordNum3 = "Wizard101 LuckyCharms 1800Law1010 12321GoodJob";
        Lexer object3 = new Lexer(wordNum3);
        LinkedList<Token> tokens3 = new LinkedList<>(object3.Lex());

        assertEquals("WORD(Wizard101)", tokens3.get(0).toString());
        assertEquals("WORD(LuckyCharms)", tokens3.get(1).toString());
        assertEquals("NUMBER(1800)", tokens3.get(2).toString());
        assertEquals("WORD(Law1010)", tokens3.get(3).toString());
        assertEquals("NUMBER(12321)", tokens3.get(4).toString());
        assertEquals("WORD(GoodJob)", tokens3.get(5).toString());

        // Test 4 variables.
        String wordNum4 = "Mr_Noodle_10";
        Lexer object4 = new Lexer(wordNum4);
        LinkedList<Token> token4 = new LinkedList<>(object4.Lex());

        assertEquals("WORD(Mr_Noodle_10)", token4.get(0).toString());
    }

    @Test(expected = Exception.class)
    public void testSpecialCase() throws Exception {
        // Test 1 variables.
        String wordNum1 = "5Goodbye\rUnicorns456\rSnickers\t1289Watson\t";
        Lexer object1 = new Lexer(wordNum1);
        LinkedList<Token> tokens1 = new LinkedList<>(object1.Lex());

        assertEquals("NUMBER(5)", tokens1.get(0).toString());
        assertEquals("WORD(Goodbye)", tokens1.get(1).toString());
        assertEquals("WORD(Unicorns456)", tokens1.get(2).toString());
        assertEquals("WORD(Snickers)", tokens1.get(3).toString());
        assertEquals("NUMBER(1289)", tokens1.get(4).toString());
        assertEquals("WORD(Watson)", tokens1.get(5).toString());

        // Test 2 variables.
        String wordNum2 = "HelloWorld\nComputerScienceIsTheBest\nMike_Myers\nSuperSaiyan25\n";
        Lexer object2 = new Lexer(wordNum2);
        LinkedList<Token> tokens2 = new LinkedList<>(object2.Lex());

        assertEquals("WORD(HelloWorld)", tokens2.get(0).toString());
        assertEquals("SEPARATOR", tokens2.get(1).toString());
        assertEquals("WORD(ComputerScienceIsTheBest)", tokens2.get(2).toString());
        assertEquals("SEPARATOR", tokens2.get(3).toString());
        assertEquals("WORD(Mike_Myers)", tokens2.get(4).toString());
        assertEquals("SEPARATOR", tokens2.get(5).toString());
        assertEquals("WORD(SuperSaiyan25)", tokens2.get(6).toString());
        assertEquals("SEPARATOR", tokens2.get(7).toString());

        // Test 3 variables.
        String wordNum3 = "GummyWorms\n";
        Lexer object3 = new Lexer(wordNum3);
        LinkedList<Token> tokens3 = new LinkedList<>(object3.Lex());

        assertEquals("WORD(GummyWorms)", tokens3.get(0).toString());
        assertEquals("SEPARATOR", tokens3.get(1).toString());

        // Test 4 variables for unrecognized characters.
        String words = "Hello,-&*Martha !@#$%^&*() Y(U)BLAH*&*";
        Lexer object4 = new Lexer(words);
        LinkedList<Token> tokens4 = new LinkedList<>(object4.Lex());

        assertEquals("Unrecognizable characters!", tokens4.get(0).toString());
        assertEquals("Unrecognizable characters!", tokens4.get(1).toString());
        assertEquals("Unrecognizable characters!", tokens4.get(2).toString());
    }
}