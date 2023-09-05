import java.util.LinkedList;
public class Lexer {
    // Variable declarations.
    private int lineNumber = 1;
    private int position = 1;
    private StringHandler item;
    private LinkedList<Token> tokens = new LinkedList();

    /**
     * Constructor.
     * @param document
     */
    public Lexer(String document) {
        item = new StringHandler(document);
    }

    /**
     * This method breaks the data from the StringHandler class up into a LinkedList of tokens
     * which is returned at the end.
     * @return tokens
     * @throws Exception
     */
    public LinkedList<Token> Lex() throws Exception {
        while (!item.isDone()) {
            if (item.Peek(0) == ' ' || item.Peek(0) == '\t') {
                item.Swallow(1);
                position++;
            } else if (item.Peek(0) == '\n') {
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
            } else {
                throw new Exception("Unrecognizable character!");
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
        return new Token(Token.TokenType.WORD, word, lineNumber, position);
    }

    /**
     * This method takes numbers from zero to nine and one period and turns them into a string number
     * and returns it as new Token.
     * @return new Token(Token.TokenType.NUMBER, number, lineNumber, position)
     */
    public Token ProcessNumber() {
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
}
