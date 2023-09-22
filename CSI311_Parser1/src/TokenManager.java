import java.util.LinkedList;
import java.util.Optional;

public class TokenManager extends LinkedList<Token> {
    // Variable declaration.
    private LinkedList<Token> tokens;

    /**
     * Constructor.
     * @param tokens
     */
    public TokenManager(LinkedList<Token> tokens){
        this.tokens = tokens;
    }

    /**
     * Peek “j” tokens ahead and return the token if we aren’t past the end of the token list.
     * @param j
     * @return
     */
    public Optional<Token> Peek(int j){
        for (int i = 0; i < tokens.size(); i++){
            if (tokens.get(i + j) != null){
                return Optional.of(tokens.get(i + j));
            }
        }
        return Optional.empty();
    }

    /**
     * returns true if the token list is not empty
     * @return
     */
    boolean MoreTokens(){
        if (!tokens.isEmpty()){
            return true;
        } else {
            return false;
        }
    }

    /**
     * looks at the head of the list. If the token type of the head is the same
     * as what was passed in, remove that token from the list and return it.
     * In all other cases, returns Optional.Empty().
     * @param t
     * @return
     */
    Optional<Token> MatchAndRemove(Token.TokenType t){
        // Variable declaration.
        Token head = tokens.getFirst();

        if (head.getTokenType() == t){
            tokens.remove();
            return Optional.of(head);
        } else {
            return Optional.empty();
        }
    }
}
