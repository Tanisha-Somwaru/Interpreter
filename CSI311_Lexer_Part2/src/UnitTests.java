import org.junit.Test;
import static org.junit.Assert.*;
import java.util.LinkedList;

public class UnitTests {
    @Test
    public void TestHandleStringLiteral() throws Exception {
        // Test 1 variables.
        String test1 = "\"She said, \\\"hello there\\\" and then she left.\"";
        Lexer temp1 = new Lexer(test1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());

        assertEquals("STRINGLITERAL(She said, \"hello there\" and then she left.)", tokens1.get(0).toString());

        // Test 2 variables.
        String test2 = "\"Why is the \\\"sky blue\\\"?\" \"I don't know, \\\"Google\\\" it.\"\n";
        Lexer temp2 = new Lexer(test2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());

        assertEquals("STRINGLITERAL(Why is the \"sky blue\"?)", tokens2.get(0).toString());
        assertEquals("STRINGLITERAL(I don't know, \"Google\" it.)", tokens2.get(1).toString());
        assertEquals("SEPARATOR", tokens2.get(2).toString());

        // Test 3 variables.
        String test3 = "\"Life is very \\\"complicated\\\".\"";
        Lexer temp3 = new Lexer(test3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());

        assertEquals("STRINGLITERAL(Life is very \"complicated\".)", tokens3.get(0).toString());
    }

    @Test
    public void TestHandlePattern() throws Exception {
        // Test 1 variables.
        String test1 = "`Hello` Bob";
        Lexer temp1 = new Lexer(test1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());

        assertEquals("LITERAL(Hello)", tokens1.get(0).toString());
        assertEquals("WORD(Bob)", tokens1.get(1).toString());

        // Test 2 variables.
        String test2 = "`Blue Buffalo Chicken Wings`, embrace the wings";
        Lexer temp2 = new Lexer(test2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());

        assertEquals("LITERAL(Blue Buffalo Chicken Wings)", tokens2.get(0).toString());
        assertEquals("COMMA", tokens2.get(1).toString());
        assertEquals("WORD(embrace)", tokens2.get(2).toString());
        assertEquals("WORD(the)", tokens2.get(3).toString());
        assertEquals("WORD(wings)", tokens2.get(4).toString());

        // Test 3 variables.
        String test3 = "`Boom` `Boom` `Boom` `Clap`";
        Lexer temp3 = new Lexer(test3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());

        assertEquals("LITERAL(Boom)", tokens3.get(0).toString());
        assertEquals("LITERAL(Boom)", tokens3.get(1).toString());
        assertEquals("LITERAL(Boom)", tokens3.get(2).toString());
        assertEquals("LITERAL(Clap)", tokens3.get(3).toString());
    }

    @Test
    public void TestProcessSymbol() throws Exception {
        // Test 1 variables.
        String test1 = "Test$%^&&*(!";
        Lexer temp1 = new Lexer(test1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());

        assertEquals("WORD(Test)", tokens1.get(0).toString());
        assertEquals("DOLLAR", tokens1.get(1).toString());
        assertEquals("PERCENT", tokens1.get(2).toString());
        assertEquals("CARET", tokens1.get(3).toString());
        assertEquals("AND", tokens1.get(4).toString());
        assertEquals("ASTERISK", tokens1.get(5).toString());
        assertEquals("OPENPARENTHESES", tokens1.get(6).toString());
        assertEquals("EXCLAMATION", tokens1.get(7).toString());

        // Test 2 variables.
        String test2 = "20,000||RedRow~~\n";
        Lexer temp2 = new Lexer(test2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());

        assertEquals("NUMBER(20)", tokens2.get(0).toString());
        assertEquals("COMMA", tokens2.get(1).toString());
        assertEquals("NUMBER(000)", tokens2.get(2).toString());
        assertEquals("OR", tokens2.get(3).toString());
        assertEquals("WORD(RedRow)", tokens2.get(4).toString());
        assertEquals("TILDE", tokens2.get(5).toString());
        assertEquals("TILDE", tokens2.get(6).toString());
        assertEquals("SEPARATOR", tokens2.get(7).toString());

        // Test 3 variables.
        String test3 = "{ } [ ] ( ) $ ~ = < > !  + ^ - ?  : * / % ; \n | ,";
        Lexer temp3 = new Lexer(test3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());

        assertEquals("OPENCURLYBRACKET", tokens3.get(0).toString());
        assertEquals("CLOSEDCURLYBRACKET", tokens3.get(1).toString());
        assertEquals("OPENBRACKET", tokens3.get(2).toString());
        assertEquals("CLOSEDBRACKET", tokens3.get(3).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(4).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(5).toString());
        assertEquals("DOLLAR", tokens3.get(6).toString());
        assertEquals("TILDE", tokens3.get(7).toString());
        assertEquals("EQUAL", tokens3.get(8).toString());
        assertEquals("LESSTHAN", tokens3.get(9).toString());
        assertEquals("GREATERTHAN", tokens3.get(10).toString());
        assertEquals("EXCLAMATION", tokens3.get(11).toString());
        assertEquals("PLUS", tokens3.get(12).toString());
        assertEquals("CARET", tokens3.get(13).toString());
        assertEquals("MINUS", tokens3.get(14).toString());
        assertEquals("QUESTION", tokens3.get(15).toString());
        assertEquals("COLON", tokens3.get(16).toString());
        assertEquals("ASTERISK", tokens3.get(17).toString());
        assertEquals("FRONTSLASH", tokens3.get(18).toString());
        assertEquals("PERCENT", tokens3.get(19).toString());
        assertEquals("SEPARATOR", tokens3.get(20).toString());
        assertEquals("SEPARATOR", tokens3.get(21).toString());
        assertEquals("BAR", tokens3.get(22).toString());
        assertEquals("COMMA", tokens3.get(23).toString());

        // Test 4 variables.
        String test4 = ">=  ++  --  <=  ==  !=  ^=  %=  *=  /=  +=  -=  !~   &&   >>   ||";
        Lexer temp4 = new Lexer(test4);
        LinkedList<Token> tokens4 = new LinkedList<>(temp4.Lex());

        assertEquals("GREATEREQUAL", tokens4.get(0).toString());
        assertEquals("INCREMENT", tokens4.get(1).toString());
        assertEquals("DECREMENT", tokens4.get(2).toString());
        assertEquals("LESSEQUAL", tokens4.get(3).toString());
        assertEquals("EQUALS", tokens4.get(4).toString());
        assertEquals("NOTEQUALS", tokens4.get(5).toString());
        assertEquals("CARETEQUALS", tokens4.get(6).toString());
        assertEquals("PERCENTEQUALS", tokens4.get(7).toString());
        assertEquals("ASTERISKEQUALS", tokens4.get(8).toString());
        assertEquals("FRONTSLASHEQUALS", tokens4.get(9).toString());
        assertEquals("PLUSEQUALS", tokens4.get(10).toString());
        assertEquals("MINUSEQUALS", tokens4.get(11).toString());
        assertEquals("NOTTILDE", tokens4.get(12).toString());
        assertEquals("AND", tokens4.get(13).toString());
        assertEquals("RIGHTSHIFT", tokens4.get(14).toString());
        assertEquals("OR", tokens4.get(15).toString());
    }

    @Test
    public void TestAwkFiles() throws Exception {
        // Test 1 variables.
        String test1 = "function endfile(file)\n" + "{\n" + "if (! no_print && count_only)\n" + "if (do_filenames)\n" +
                "print file \"\\\":\\\"\" fcount\n" + "else\n" + "print fcount\n" +
                "\n" + "total += fcount\n" + "}";
        Lexer temp1 = new Lexer(test1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());

        assertEquals("FUNCTION", tokens1.get(0).toString());
        assertEquals("WORD(endfile)", tokens1.get(1).toString());
        assertEquals("OPENPARENTHESES", tokens1.get(2).toString());
        assertEquals("WORD(file)", tokens1.get(3).toString());
        assertEquals("CLOSEDPARENTHESES", tokens1.get(4).toString());
        assertEquals("SEPARATOR", tokens1.get(5).toString());
        assertEquals("OPENCURLYBRACKET", tokens1.get(6).toString());
        assertEquals("SEPARATOR", tokens1.get(7).toString());
        assertEquals("IF", tokens1.get(8).toString());
        assertEquals("OPENPARENTHESES", tokens1.get(9).toString());
        assertEquals("EXCLAMATION", tokens1.get(10).toString());
        assertEquals("WORD(no_print)", tokens1.get(11).toString());
        assertEquals("AND", tokens1.get(12).toString());
        assertEquals("WORD(count_only)", tokens1.get(13).toString());
        assertEquals("CLOSEDPARENTHESES", tokens1.get(14).toString());
        assertEquals("SEPARATOR", tokens1.get(15).toString());
        assertEquals("IF", tokens1.get(16).toString());
        assertEquals("OPENPARENTHESES", tokens1.get(17).toString());
        assertEquals("WORD(do_filenames)", tokens1.get(18).toString());
        assertEquals("CLOSEDPARENTHESES", tokens1.get(19).toString());
        assertEquals("SEPARATOR", tokens1.get(20).toString());
        assertEquals("PRINT", tokens1.get(21).toString());
        assertEquals("WORD(file)", tokens1.get(22).toString());
        assertEquals("STRINGLITERAL(\":\")", tokens1.get(23).toString());
        assertEquals("WORD(fcount)", tokens1.get(24).toString());
        assertEquals("SEPARATOR", tokens1.get(25).toString());
        assertEquals("ELSE", tokens1.get(26).toString());
        assertEquals("SEPARATOR", tokens1.get(27).toString());
        assertEquals("PRINT", tokens1.get(28).toString());
        assertEquals("WORD(fcount)", tokens1.get(29).toString());
        assertEquals("SEPARATOR", tokens1.get(30).toString());
        assertEquals("SEPARATOR", tokens1.get(31).toString());
        assertEquals("WORD(total)", tokens1.get(32).toString());
        assertEquals("PLUSEQUALS", tokens1.get(33).toString());
        assertEquals("WORD(fcount)", tokens1.get(34).toString());
        assertEquals("SEPARATOR", tokens1.get(35).toString());
        assertEquals("CLOSEDCURLYBRACKET", tokens1.get(36).toString());

        // Test 2 variables.
        String test2 = "function beginfile(junk)\n" + "{\n" + "fcount = 0\n" + "}";
        Lexer temp2 = new Lexer(test2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());

        assertEquals("FUNCTION", tokens2.get(0).toString());
        assertEquals("WORD(beginfile)", tokens2.get(1).toString());
        assertEquals("OPENPARENTHESES", tokens2.get(2).toString());
        assertEquals("WORD(junk)", tokens2.get(3).toString());
        assertEquals("CLOSEDPARENTHESES", tokens2.get(4).toString());
        assertEquals("SEPARATOR", tokens2.get(5).toString());
        assertEquals("OPENCURLYBRACKET", tokens2.get(6).toString());
        assertEquals("SEPARATOR", tokens2.get(7).toString());
        assertEquals("WORD(fcount)", tokens2.get(8).toString());
        assertEquals("EQUAL", tokens2.get(9).toString());
        assertEquals("NUMBER(0)", tokens2.get(10).toString());
        assertEquals("SEPARATOR", tokens2.get(11).toString());
        assertEquals("CLOSEDCURLYBRACKET", tokens2.get(12).toString());

        // Test 3 variables.
        String test3 = "{\n" + "matches = ($0 ~ pattern)\n" + "if (invert)\n" +
                "matches = ! matches\n" + "\n" + "fcount += matches    # 1 or 0 \n" +
                "\n" + "if (! matches)\n" + "next\n" + "\n" + "if (no_print && ! count_only)\n" +
                "nextfile\n" + "\n" + "if (filenames_only && ! count_only) {\n" + "print FILENAME\n" + "nextfile\n" +
                "}\n" + "\n" + "if (do_filenames && ! count_only)\n" + "print FILENAME \"\\\":\\\"\" $0\n" +
                "else if (! count_only)\n" + "print\n" + "}";
        Lexer temp3 = new Lexer(test3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());

        assertEquals("OPENCURLYBRACKET", tokens3.get(0).toString());
        assertEquals("SEPARATOR", tokens3.get(1).toString());
        assertEquals("WORD(matches)", tokens3.get(2).toString());
        assertEquals("EQUAL", tokens3.get(3).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(4).toString());
        assertEquals("DOLLAR", tokens3.get(5).toString());
        assertEquals("NUMBER(0)", tokens3.get(6).toString());
        assertEquals("TILDE", tokens3.get(7).toString());
        assertEquals("WORD(pattern)", tokens3.get(8).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(9).toString());
        assertEquals("SEPARATOR", tokens3.get(10).toString());
        assertEquals("IF", tokens3.get(11).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(12).toString());
        assertEquals("WORD(invert)", tokens3.get(13).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(14).toString());
        assertEquals("SEPARATOR", tokens3.get(15).toString());
        assertEquals("WORD(matches)", tokens3.get(16).toString());
        assertEquals("EQUAL", tokens3.get(17).toString());
        assertEquals("EXCLAMATION", tokens3.get(18).toString());
        assertEquals("WORD(matches)", tokens3.get(19).toString());
        assertEquals("SEPARATOR", tokens3.get(20).toString());
        assertEquals("SEPARATOR", tokens3.get(21).toString());
        assertEquals("WORD(fcount)", tokens3.get(22).toString());
        assertEquals("PLUSEQUALS", tokens3.get(23).toString());
        assertEquals("WORD(matches)", tokens3.get(24).toString());
        assertEquals("SEPARATOR", tokens3.get(25).toString());
        assertEquals("SEPARATOR", tokens3.get(26).toString());
        assertEquals("IF", tokens3.get(27).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(28).toString());
        assertEquals("EXCLAMATION", tokens3.get(29).toString());
        assertEquals("WORD(matches)", tokens3.get(30).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(31).toString());
        assertEquals("SEPARATOR", tokens3.get(32).toString());
        assertEquals("NEXT", tokens3.get(33).toString());
        assertEquals("SEPARATOR", tokens3.get(34).toString());
        assertEquals("SEPARATOR", tokens3.get(35).toString());
        assertEquals("IF", tokens3.get(36).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(37).toString());
        assertEquals("WORD(no_print)", tokens3.get(38).toString());
        assertEquals("AND", tokens3.get(39).toString());
        assertEquals("EXCLAMATION", tokens3.get(40).toString());
        assertEquals("WORD(count_only)", tokens3.get(41).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(42).toString());
        assertEquals("SEPARATOR", tokens3.get(43).toString());
        assertEquals("NEXTFILE", tokens3.get(44).toString());
        assertEquals("SEPARATOR", tokens3.get(45).toString());
        assertEquals("SEPARATOR", tokens3.get(46).toString());
        assertEquals("IF", tokens3.get(47).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(48).toString());
        assertEquals("WORD(filenames_only)", tokens3.get(49).toString());
        assertEquals("AND", tokens3.get(50).toString());
        assertEquals("EXCLAMATION", tokens3.get(51).toString());
        assertEquals("WORD(count_only)", tokens3.get(52).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(53).toString());
        assertEquals("OPENCURLYBRACKET", tokens3.get(54).toString());
        assertEquals("SEPARATOR", tokens3.get(55).toString());
        assertEquals("PRINT", tokens3.get(56).toString());
        assertEquals("WORD(FILENAME)", tokens3.get(57).toString());
        assertEquals("SEPARATOR", tokens3.get(58).toString());
        assertEquals("NEXTFILE", tokens3.get(59).toString());
        assertEquals("SEPARATOR", tokens3.get(60).toString());
        assertEquals("CLOSEDCURLYBRACKET", tokens3.get(61).toString());
        assertEquals("SEPARATOR", tokens3.get(62).toString());
        assertEquals("SEPARATOR", tokens3.get(63).toString());
        assertEquals("IF", tokens3.get(64).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(65).toString());
        assertEquals("WORD(do_filenames)", tokens3.get(66).toString());
        assertEquals("AND", tokens3.get(67).toString());
        assertEquals("EXCLAMATION", tokens3.get(68).toString());
        assertEquals("WORD(count_only)", tokens3.get(69).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(70).toString());
        assertEquals("SEPARATOR", tokens3.get(71).toString());
        assertEquals("PRINT", tokens3.get(72).toString());
        assertEquals("WORD(FILENAME)", tokens3.get(73).toString());
        assertEquals("STRINGLITERAL(\":\")", tokens3.get(74).toString());
        assertEquals("DOLLAR", tokens3.get(75).toString());
        assertEquals("NUMBER(0)", tokens3.get(76).toString());
        assertEquals("SEPARATOR", tokens3.get(77).toString());
        assertEquals("ELSE", tokens3.get(78).toString());
        assertEquals("IF", tokens3.get(79).toString());
        assertEquals("OPENPARENTHESES", tokens3.get(80).toString());
        assertEquals("EXCLAMATION", tokens3.get(81).toString());
        assertEquals("WORD(count_only)", tokens3.get(82).toString());
        assertEquals("CLOSEDPARENTHESES", tokens3.get(83).toString());
        assertEquals("SEPARATOR", tokens3.get(84).toString());
        assertEquals("PRINT", tokens3.get(85).toString());
        assertEquals("SEPARATOR", tokens3.get(86).toString());
        assertEquals("CLOSEDCURLYBRACKET", tokens3.get(87).toString());
    }

    @Test(expected = Exception.class)
    public void TestSpecialCases() throws Exception {
        // Test 1 variables.
        String test1 = "ERROR404@@@@";
        Lexer temp1 = new Lexer(test1);
        LinkedList<Token> tokens1 = new LinkedList<>(temp1.Lex());

        assertEquals("Unrecognizable character!", tokens1.get(0).toString());

        // Test 2 variables.
        String test2 = "568468''''&@&&";
        Lexer temp2 = new Lexer(test2);
        LinkedList<Token> tokens2 = new LinkedList<>(temp2.Lex());

        assertEquals("Unrecognizable character!", tokens2.get(0).toString());

        // Test 3 variables.
        String test3 = "&&&&&@&@&@'";
        Lexer temp3 = new Lexer(test3);
        LinkedList<Token> tokens3 = new LinkedList<>(temp3.Lex());

        assertEquals("Unrecognizable character!", tokens3.get(0).toString());

    }
}