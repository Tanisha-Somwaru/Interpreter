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
     * It accepts any number of separators (newline or semicolon) and returns true if it
     * finds at least one.
     * @return
     */
    boolean AcceptSeparators(){
        // Variable declaration.
        boolean foundSeparator = false;

        while (handleToken.MoreTokens()){
            if (handleToken.MatchAndRemove(Token.TokenType.SEPARATOR).isPresent()) {
                foundSeparator = true;
                break;
            }
        }
        return foundSeparator;
    }

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

                        } else if (handleToken.MatchAndRemove(Token.TokenType.WORD).isPresent()){

                            while (!handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES).isPresent()){
                                if (handleToken.MatchAndRemove(Token.TokenType.WORD).isPresent() && handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                                    parameters.add(String.valueOf(handleToken.MatchAndRemove(Token.TokenType.WORD).get()));
                                    FunctionDefinitionNode item2 = new FunctionDefinitionNode(functionName, parameters);
                                    progNode.AddFunction(item2);
                                }
                            }
                            FunctionDefinitionNode item3 = new FunctionDefinitionNode(functionName, parameters);
                            progNode.AddFunction(item3);
                        }
                    }
                }
            return true;
        } else {
            return false;
        }
        /**
         * if (handleToken.MatchAndRemove(Token.TokenType.WORD).isPresent()){
         *                             parameters.add(String.valueOf(handleToken.element()));
         *                             //parameters.add(String.valueOf(handleToken.MatchAndRemove(Token.TokenType.WORD).get()));
         *                             if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES).isPresent()){
         *                                 if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET).isPresent()){
         *                                      // I don't think I put anything in here T_T.
         *                                     //progNode.AddFunction(temp);
         *                                 }
         *                             }
         *                         }z
         */
    }

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

    BlockNode ParseBlock(){
        return new BlockNode();
    }

    Optional<Node> ParseOperation(){
        return Optional.empty();
    }
}