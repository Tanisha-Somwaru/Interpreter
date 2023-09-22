public class Token {
    // Variable declarations.
    public enum TokenType {WORD, NUMBER, SEPARATOR, WHILE, IF, DO, FOR, BREAK, CONTINUE, ELSE, RETURN, BEGIN, END,
        PRINT, PRINTF, NEXT, IN, DELETE, GETLINE, EXIT, NEXTFILE, FUNCTION, STRINGLITERAL, LITERAL, OPENCURLYBRACKET, CLOSEDCURLYBRACKET,
        OPENBRACKET, CLOSEDBRACKET, OPENPARENTHESES, CLOSEDPARENTHESES, DOLLAR, TILDE, EQUAL, GREATERTHAN, LESSTHAN, EXCLAMATION,
        PLUS, CARET, MINUS, QUESTION, COLON, ASTERISK, FRONTSLASH, PERCENT, BAR, COMMA, RIGHTSHIFT, GREATEREQUAL, LESSEQUAL,
        INCREMENT, DECREMENT, EQUALS, NOTEQUALS, CARETEQUALS, PERCENTEQUALS, ASTERISKEQUALS, FRONTSLASHEQUALS, PLUSEQUALS, MINUSEQUALS,
        NOTTILDE, AND, OR}
    private String tokenValue;
    private int lineNumber;
    private int charPosition;
    TokenType type;

    /**
     * Constructor.
     * @param type
     */
    public Token (TokenType type){
        this.type = type;
        this.lineNumber = 1;
        this.charPosition = 0;
    }

    /**
     * Constructor.
     * @param tokenType
     * @param lineNum
     * @param position
     */
    public Token(TokenType tokenType, int lineNum, int position){
        type = tokenType;
        lineNumber = lineNum;
        charPosition = position;
    }

    /**
     * Constructor.
     * @param tokenType
     * @param value
     * @param lineNum
     * @param position
     */
    public Token(TokenType tokenType, String value, int lineNum, int position){
        type = tokenType;
        tokenValue = value;
        lineNumber = lineNum;
        charPosition = position;
    }

    /**
     * This method returns the string representation of the TokenType and value of the token
     * which can be either a number or word.
     * @return type
     */
    public String toString(){
        if (tokenValue == null){
            return type.toString();
        }
        return type + "(" + tokenValue + ")";
    }

    /**
     * Helper method for TokenManager.
     */
    public Token.TokenType getTokenType(){
        return type;
    }
}
