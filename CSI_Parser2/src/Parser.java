import jdk.dynalink.Operation;

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
    boolean ParseAction(ProgramNode progNode) throws Exception {
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
     * This method returns a call to ParseBottomLevel().
     * @return ParseBottomLevel()
     */
    public Optional<Node> ParseOperation() throws Exception {
         return ParseBottomLevel();
    }

    /**
     * This method creates a ConstantNode, PatternNode, or OperationNode and returns it.
     * The Nodes are created based on token type being passed in.
     * @return ParseLValue()
     * @throws Exception
     */
    Optional<Node> ParseBottomLevel() throws Exception {
        // Variable declarations.
        Optional<Token> value;
        ConstantNode constant;
        PatternNode pattern;

        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.STRINGLITERAL){
            value = handleToken.MatchAndRemove(Token.TokenType.STRINGLITERAL);
            constant = new ConstantNode(value.get());

            return Optional.of(constant);

        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.NUMBER){
            value = handleToken.MatchAndRemove(Token.TokenType.NUMBER);
            constant = new ConstantNode(value.get());

            return Optional.of(constant);

        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.LITERAL){
            value = handleToken.MatchAndRemove(Token.TokenType.LITERAL);
            pattern = new PatternNode(value.get());

            return Optional.of(pattern);

        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENPARENTHESES){
            handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES);
            Optional<Node> temp1 = ParseOperation();

            if (temp1.isPresent()){
                if (handleToken.MoreTokens() && handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                    handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                    return temp1;
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.EXCLAMATION){
            handleToken.MatchAndRemove(Token.TokenType.EXCLAMATION);
            Optional<Node> temp2 = ParseOperation();

            if (temp2.isPresent()){
                return Optional.of(new OperationNode(temp2.get(), OperationNode.Operations.NOT));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.MINUS){
            handleToken.MatchAndRemove(Token.TokenType.MINUS);
            Optional<Node> temp3 = ParseOperation();

            if (temp3.isPresent()){
                return Optional.of(new OperationNode(temp3.get(), OperationNode.Operations.UNARYNEG));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.PLUS){
            handleToken.MatchAndRemove(Token.TokenType.PLUS);
            Optional<Node> temp4 = ParseOperation();

            if (temp4.isPresent()){
                return Optional.of(new OperationNode(temp4.get(), OperationNode.Operations.UNARYPOS));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.INCREMENT){
            handleToken.MatchAndRemove(Token.TokenType.INCREMENT);
            Optional<Node> temp5 = ParseOperation();

            if (temp5.isPresent()){
                return Optional.of(new OperationNode(temp5.get(), OperationNode.Operations.PREINC));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.DECREMENT){
            handleToken.MatchAndRemove(Token.TokenType.DECREMENT);
            Optional<Node> temp6 = ParseOperation();

            if (temp6.isPresent()){
                return Optional.of(new OperationNode(temp6.get(), OperationNode.Operations.PREDEC));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        }
        return ParseLValue();
    }

    /**
     * This method creates a VariableReferenceNode depending on the token
     * type passed in and returns it.
     * @return Optional.empty()
     * @throws Exception
     */
    Optional<Node> ParseLValue() throws Exception {
        // Variable declaration.
        Optional<Token> value;

        if (handleToken.MatchAndRemove(Token.TokenType.DOLLAR).isPresent()){
            Optional<Node> temp1 = ParseOperation();

            if (temp1.isPresent()){
                return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.DOLLAR));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.WORD){
            value = handleToken.MatchAndRemove(Token.TokenType.WORD);

            if (handleToken.MoreTokens() && handleToken.MatchAndRemove(Token.TokenType.OPENBRACKET).isPresent()){
                Optional<Node> temp2 = ParseOperation();

                if (temp2.isPresent()){
                    if (handleToken.MoreTokens() && handleToken.MatchAndRemove(Token.TokenType.CLOSEDBRACKET).isPresent()){
                        if (value.isPresent()){
                            return Optional.of(new VariableReferenceNode(value.get(), Optional.of(temp2.get())));
                        }
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else {
                return Optional.of(new VariableReferenceNode(value.get()));
            }
        } else {
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.WORD){
                value = handleToken.MatchAndRemove(Token.TokenType.WORD);

                return Optional.of(new VariableReferenceNode(value.get()));
            }
        }
        return Optional.empty();
    }
}
