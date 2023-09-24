import java.util.LinkedList;
import java.util.Optional;

public class Parser {
    // Variable declaration.
    private TokenManager handleToken;

    /**
     * Constructor.
     * @param tokens
     */
    public Parser(LinkedList<Token> tokens){
        handleToken = new TokenManager(tokens);
    }

    /**
     * This method checks to see if there are more tokens that could make up a function, or
     * an action and returns the progNode if this is true. Otherwise, it throws an exception for
     * any other case.
     * @return progNode
     * @throws Exception
     */
    public ProgramNode Parse() throws Exception {
        // Variable declaration.
        ProgramNode progNode = new ProgramNode();

        while (handleToken.MoreTokens()){
            if (!(ParseFunction(progNode) && ParseAction(progNode))){
                throw new Exception("Exception!");
            }
        }
        return progNode;
    }

    /**
     * This method accepts any number of separators (newline or semicolon) and returns true if it
     * finds at least one.
     * @return true
     */
    boolean AcceptSeparators(){
        // Variable declaration.
        int tracker = 0;

        while (handleToken.Peek(tracker).isPresent()){
            tracker++;

            if (handleToken.Peek(tracker).get().getTokenType().equals(Token.TokenType.SEPARATOR)) {
                handleToken.MatchAndRemove(Token.TokenType.SEPARATOR);
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks if the token types from the input make up a function.
     * It returns true if it is a function and false if it isn't.
     * @param progNode
     * @return true
     */
    boolean ParseFunction(ProgramNode progNode){
        // Variable declarations.
        String functionName;
        LinkedList<String> parameters = new LinkedList<>();

        if (handleToken.MatchAndRemove(Token.TokenType.FUNCTION).isPresent()){
                Optional<Token> temp = handleToken.MatchAndRemove(Token.TokenType.WORD);

                if (temp.isPresent()){
                    functionName = String.valueOf(temp.get());

                    if (handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES).isPresent()){
                        if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES).isPresent()){
                            FunctionDefinitionNode item1 = new FunctionDefinitionNode(functionName, parameters);
                            progNode.AddFunction(item1);

                        } else if (temp.isPresent()){
                            Optional<Token> temp2 = handleToken.MatchAndRemove(Token.TokenType.WORD);
                            parameters.add(String.valueOf(temp2.get()));

                            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){

                                if (temp2.isPresent()){
                                    Optional<Token> temp3 = handleToken.MatchAndRemove(Token.TokenType.WORD);
                                    parameters.add(String.valueOf(temp3.get()));
                                }
                            }
                            if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES).isPresent()){
                                FunctionDefinitionNode item2 = new FunctionDefinitionNode(functionName, parameters);
                                progNode.AddFunction(item2);
                            }
                        }
                    }
                }
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method checks if the token types from the input make up a BEGIN, or END block while
     * also checking if the input isn't either of those. It returns true if the input is a BEGIN or END
     * block, and false if it isn't.
     * @param progNode
     * @return true
     */
    boolean ParseAction(ProgramNode progNode){
        if (handleToken.MatchAndRemove(Token.TokenType.BEGIN).isPresent()){
            if (handleToken.MatchAndRemove(Token.TokenType.OPENCURLYBRACKET).isPresent()){
                if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET).isPresent()){
                    progNode.AddBlock1(ParseBlock());
                }
            }
            return true;
        } else if (handleToken.MatchAndRemove(Token.TokenType.END).isPresent()){
            if (handleToken.MatchAndRemove(Token.TokenType.OPENCURLYBRACKET).isPresent()){
                if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET).isPresent()){
                    progNode.AddBlock2(ParseBlock());
                }
            }
            return true;
        } else {
            ParseOperation();
            progNode.AddBlock3(ParseBlock());

            return false;
        }
    }

    /**
     * This method returns a new BlockNode instance.
     * @return new BlockNode()
     */
    BlockNode ParseBlock(){
        return new BlockNode();
    }

    /**
     * This method returns an empty Optional instance.
     * @return Optional.empty()
     */
    Optional<Node> ParseOperation(){
        return Optional.empty();
    }
}