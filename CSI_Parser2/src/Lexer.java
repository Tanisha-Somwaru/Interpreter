import java.util.HashMap;
import java.util.LinkedList;

public class Lexer {
    // Variable declarations.
    private int lineNumber = 1;
    private int position = 1;
    private StringHandler item;
    private LinkedList<Token> tokens = new LinkedList();
    private HashMap<String, Token.TokenType> keywords;
    private HashMap<String, Token.TokenType> twoCharacter = HandlePatternHelper1();
    private HashMap<String, Token.TokenType> oneCharacter = HandlePatternHelper2();

    /**
     * Constructor.
     * @param document
     */
    public Lexer(String document) {
        item = new StringHandler(document);
        keywords = AddWordsHelper();
    }

    /**
     * This method breaks the data from the StringHandler class up into a LinkedList of tokens
     * which is returned at the end.
     * @return tokens
     * @throws Exception
     */
    public LinkedList<Token> Lex() throws Exception {
        while (!item.isDone()) {
            if (item.Peek(0) == '#') {
                while (item.Peek(0) != '\n'){
                    if (item.Peek(0) == '\n') {
                        break;
                    } else {
                        item.Swallow(1);
                        position++;
                    }
                }

            } else if (item.Peek(0) == ' ' || item.Peek(0) == '\t') {
                item.Swallow(1);
                position++;
            } else if (item.Peek(0) == '\n' || item.Peek(0) == ';') {
                Token.TokenType type = Token.TokenType.SEPARATOR;
                tokens.add(new Token(type, lineNumber, position));
                item.Swallow(1);
                lineNumber++;
                position = 1;
            } else if (item.Peek(0) == '\r') {
                item.Swallow(1);
            } else if (item.Peek(0) >= 'a' && item.Peek(0) <= 'z' || item.Peek(0) >= 'A' && item.Peek(0) <= 'Z') {
                tokens.add(ProcessWord());
            } else if (item.Peek(0) >= '0' && item.Peek(0) <= '9' || item.Peek(0) == '.') {
                tokens.add(ProcessNumber());
            } else if (item.Peek(0) == '\"') { // This needs to be edited since it could cause bugs.
                tokens.add(HandleStringLiteral());
            } else if (item.Peek(0) == '`') {
                tokens.add(HandlePattern());
            } else {
                Token temp = ProcessSymbol();

                if (temp == null) {
                    throw new Exception("Unrecognizable character!");
                } else {
                    tokens.add(temp);
                }
            }
        }
        return tokens;
    }

    /**
     * This method takes letters, numbers, and underscores and turns them into a string word
     * and returns it as a new Token.
     * @return new Token(Token.TokenType.WORD, word, lineNumber, position)
     */
    public Token ProcessWord() {
        // Variable declaration.
        String word = "";

        while (!item.isDone()) {
            if (Character.isLetter(item.Peek(0)) || Character.isDigit(item.Peek(0)) || item.Peek(0) == '_') {
                word += item.Peek(0);
                item.Swallow(1);
                position++;
            } else {
                break;
            }
        }
        if (keywords.containsKey(word)){
            return new Token(keywords.get(word), lineNumber, position);
        }
        return new Token(Token.TokenType.WORD, word, lineNumber, position);
    }

    /**
     * This method takes numbers from zero to nine and one period and turns them into a string number
     * and returns it as new Token.
     * @return new Token(Token.TokenType.NUMBER, number, lineNumber, position)
     */
    public Token ProcessNumber() {
        // Variable declaration.
        String number = "";

        while (!item.isDone()){
            if (Character.isDigit(item.Peek(0)) || item.Peek(0) == '.'){
                number += item.Peek(0);
                item.Swallow(1);
                position++;
            } else {
                break;
            }
        }
        return new Token(Token.TokenType.NUMBER, number, lineNumber, position);
    }

    /**
     * This method checks if the input has '\"' at the beginning and end of the input. If it matches the
     * '\"', it creates a new string literal token and returns it.
     * @return new Token(Token.TokenType.STRINGLITERAL, stringLiterals, lineNumber, position)
     */
    public Token HandleStringLiteral(){
        // Variable declaration.
        String stringLiterals = "";

        item.Swallow(1);
        position++;

        while (!item.isDone()){
            if (item.Peek(0) != '"') {
                if (item.Peek(0) == '\\' && item.Peek(1) == '"'){
                    item.Swallow(2);
                    stringLiterals += "\"";

                    while (!item.isDone()) {
                        if (item.Peek(0) == '\\' && item.Peek(1) == '"') {
                            item.Swallow(2);
                            stringLiterals += "\"";
                            break;
                        } else {
                            stringLiterals += item.Peek(0);
                            item.Swallow(1);
                        }
                    }
                } else {
                    stringLiterals += item.Peek(0);
                    item.Swallow(1);
                }
            } else {
                // Moving to set up for the next character.
                item.Swallow(1);
                break;
            }
        }
        return new Token(Token.TokenType.STRINGLITERAL, stringLiterals, lineNumber, position);
    }

    /**
     * This method checks if the input has '`' at the beginning and end of the input. If it matches the '`',
     * it creates a new literal token and returns it.
     * @return new Token(Token.TokenType.LITERAL, literal, lineNumber, position)
     */
    public Token HandlePattern(){
        // Variable declaration.
        String literal = "";

        item.Swallow(1);
        position++;

        while (!item.isDone()){
            if (item.Peek(0) != '`') {
                if (item.Peek(0) == '`'){
                    item.Swallow(1);

                    while (!item.isDone()) {
                        if (item.Peek(0) == '`') {
                            item.Swallow(2);
                            break;
                        } else {
                            literal += item.Peek(0);
                            item.Swallow(1);
                        }
                    }
                } else {
                    literal += item.Peek(0);
                    item.Swallow(1);
                }
            } else {
                // Moving to set up for the next character.
                item.Swallow(1);
                break;
            }
        }
        return new Token(Token.TokenType.LITERAL, literal, lineNumber, position);
    }

    /**
     * This method gets two characters or one character from the input and see
     * if it matches with the two character symbols or one character symbols in the hashmaps.
     * A new token is created and returned based on the symbol matches.
     * @return new Token(twoCharacter.get(character2), lineNumber, position)
     */
    public Token ProcessSymbol(){
        // Variable declarations.
        String character2 = "";
        String character1 = "";

        if (item.Remainder().length() >= 2){
            if (twoCharacter.containsKey(item.PeekString(2))){
                character2 += item.PeekString(2);
                item.Swallow(1);
                position++;
            }

        }

        if (!item.Remainder().isEmpty()){
            if (oneCharacter.containsKey(item.PeekString(1))){
                character1 += item.PeekString(1);
                item.Swallow(1);
                position++;
            } else {
                item.Swallow(1);
                position++;
            }
        }

        if (character1.isEmpty() && character2.isEmpty()){
            return null;
        } else {
            if (twoCharacter.containsKey(character2)){
                return new Token(twoCharacter.get(character2), lineNumber, position);
            }
            return new Token(oneCharacter.get(character1), lineNumber, position);
        }
    }

    /**
     * Helper method for the keywords hashMap.
     * @return addWords
     */
    public HashMap<String, Token.TokenType> AddWordsHelper(){
        // Variable declaration.
        HashMap<String, Token.TokenType> addWords = new HashMap<>();

        addWords.put("while", Token.TokenType.WHILE);
        addWords.put("if", Token.TokenType.IF);
        addWords.put("do", Token.TokenType.DO);
        addWords.put("for", Token.TokenType.FOR);
        addWords.put("break", Token.TokenType.BREAK);
        addWords.put("continue", Token.TokenType.CONTINUE);
        addWords.put("else", Token.TokenType.ELSE);
        addWords.put("return", Token.TokenType.RETURN);
        addWords.put("BEGIN", Token.TokenType.BEGIN);
        addWords.put("END", Token.TokenType.END);
        addWords.put("print", Token.TokenType.PRINT);
        addWords.put("printf", Token.TokenType.PRINTF);
        addWords.put("next", Token.TokenType.NEXT);
        addWords.put("in", Token.TokenType.IN);
        addWords.put("delete", Token.TokenType.DELETE);
        addWords.put("getline", Token.TokenType.GETLINE);
        addWords.put("exit", Token.TokenType.EXIT);
        addWords.put("nextfile", Token.TokenType.NEXTFILE);
        addWords.put("function", Token.TokenType.FUNCTION);

        return addWords;
    }

    /**
     * Helper method for twoCharacter hashMap.
     * @return twoChar
     */
    public HashMap<String, Token.TokenType> HandlePatternHelper1(){
        // Variable declaration.
        HashMap<String, Token.TokenType> twoChar = new HashMap<>();

        twoChar.put(">=", Token.TokenType.GREATEREQUAL);
        twoChar.put("++", Token.TokenType.INCREMENT);
        twoChar.put("--", Token.TokenType.DECREMENT);
        twoChar.put("<=", Token.TokenType.LESSEQUAL);
        twoChar.put("==", Token.TokenType.EQUALS);
        twoChar.put("!=", Token.TokenType.NOTEQUALS);
        twoChar.put("^=", Token.TokenType.CARETEQUALS);
        twoChar.put("%=", Token.TokenType.PERCENTEQUALS);
        twoChar.put("*=", Token.TokenType.ASTERISKEQUALS);
        twoChar.put("/=", Token.TokenType.FRONTSLASHEQUALS);
        twoChar.put("+=", Token.TokenType.PLUSEQUALS);
        twoChar.put("-=", Token.TokenType.MINUSEQUALS);
        twoChar.put("!~", Token.TokenType.NOTTILDE);
        twoChar.put("&&", Token.TokenType.AND);
        twoChar.put(">>", Token.TokenType.RIGHTSHIFT);
        twoChar.put("||", Token.TokenType.OR);

        return twoChar;
    }

    /**
     * Helper method for oneCharacter hashMap.
     * @return oneChar
     */
    public HashMap<String, Token.TokenType> HandlePatternHelper2(){
        // Variable declaration.
        HashMap<String, Token.TokenType> oneChar = new HashMap<>();

        oneChar.put("{", Token.TokenType.OPENCURLYBRACKET);
        oneChar.put("}", Token.TokenType.CLOSEDCURLYBRACKET);
        oneChar.put("[", Token.TokenType.OPENBRACKET);
        oneChar.put("]", Token.TokenType.CLOSEDBRACKET);
        oneChar.put("(", Token.TokenType.OPENPARENTHESES);
        oneChar.put(")", Token.TokenType.CLOSEDPARENTHESES);
        oneChar.put("$", Token.TokenType.DOLLAR);
        oneChar.put("~", Token.TokenType.TILDE);
        oneChar.put("=", Token.TokenType.EQUAL);
        oneChar.put("<", Token.TokenType.LESSTHAN);
        oneChar.put(">", Token.TokenType.GREATERTHAN);
        oneChar.put("!", Token.TokenType.EXCLAMATION);
        oneChar.put("+", Token.TokenType.PLUS);
        oneChar.put("^", Token.TokenType.CARET);
        oneChar.put("-", Token.TokenType.MINUS);
        oneChar.put("?", Token.TokenType.QUESTION);
        oneChar.put(":", Token.TokenType.COLON);
        oneChar.put("*", Token.TokenType.ASTERISK);
        oneChar.put("/", Token.TokenType.FRONTSLASH);
        oneChar.put("%", Token.TokenType.PERCENT);
        oneChar.put(";", Token.TokenType.SEPARATOR);
        oneChar.put("\n", Token.TokenType.SEPARATOR);
        oneChar.put("|", Token.TokenType.BAR);
        oneChar.put(",", Token.TokenType.COMMA);

        return oneChar;
    }
}
