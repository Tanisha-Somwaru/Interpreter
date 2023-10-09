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
        return Assignment();
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
        return PostIncrementAndPostDecrement();
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
            Optional<Node> temp1 = ParseBottomLevel();

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

    /**
     * This method returns a new OperationNode, depending on if an increment or decrement token was passed in
     * to see if it is a post increment or decrement operation.
     * @return tempValue1
     * @throws Exception
     */
    Optional<Node> PostIncrementAndPostDecrement() throws Exception{
        Optional<Node> tempValue1 = ParseLValue();

        if (tempValue1.isPresent()){
            if (handleToken.MatchAndRemove(Token.TokenType.INCREMENT).isPresent()){
                return Optional.of(new OperationNode(tempValue1.get(), OperationNode.Operations.POSTINC));
            } else if (handleToken.MatchAndRemove(Token.TokenType.DECREMENT).isPresent()){
                return Optional.of(new OperationNode(tempValue1.get(), OperationNode.Operations.POSTDEC));
            }
        }
        return tempValue1;
    }

    /**
     * This method returns a new OperationNode, depending on if a caret token was passed in to see if it
     * is an exponential operation.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> Exponents() throws Exception{
        Optional<Node> temp1 = ParseBottomLevel();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CARET){
                handleToken.MatchAndRemove(Token.TokenType.CARET);

                if (temp1.isPresent()){
                    Optional<Node> temp2 = ParseOperation();

                    if (temp2.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.EXPONENT, Optional.of(temp2.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }

    /**
     * This method returns a variable or a number within brackets.
     * @return ParseBottomLevel
     * @throws Exception
     */
    Optional<Node> Factor() throws Exception{
        Optional<Node> checkExponents = Exponents();

        if (checkExponents.isPresent()){
            return checkExponents;
        }
        return ParseBottomLevel();
    }

    /**
     * This method multiples, divides, and performs division with a remainder before returning
     * it as a new OperationNode.
     * @return left
     * @throws Exception
     */
    Optional<Node> Term() throws Exception{
        Optional<Node> left = Factor();

        do{
            Optional<Token> value = handleToken.MatchAndRemove(Token.TokenType.ASTERISK);
            OperationNode.Operations operation = OperationNode.Operations.MULTIPLY;

            if (value.isEmpty()){
                value = handleToken.MatchAndRemove(Token.TokenType.FRONTSLASH);

                if (value.isPresent()){
                    operation = OperationNode.Operations.DIVIDE;
                }
            }

            if (value.isEmpty()){
                value = handleToken.MatchAndRemove(Token.TokenType.PERCENT);

                if (value.isPresent()){
                    operation = OperationNode.Operations.MODULO;
                }
            }

            if (value.isEmpty()){
                return left;
            }
            Optional<Node> right = Factor();
            left = Optional.of(new OperationNode(left.get(), operation, Optional.of(right.get())));
        }
        while (true);
    }

    /**
     * This method creates an expression with addition or subtraction before returning it
     * as a new OperationNode.
     * @return left
     * @throws Exception
     */
    Optional<Node> Expression() throws Exception{
        Optional<Node> left = Term();

        do {
            Optional<Token> value = handleToken.MatchAndRemove(Token.TokenType.PLUS);
            OperationNode.Operations operation = OperationNode.Operations.ADD;

            if (value.isEmpty()){
                value = handleToken.MatchAndRemove(Token.TokenType.MINUS);

                if (value.isPresent()){
                    operation = OperationNode.Operations.SUBTRACT;
                }
            }

            if (value.isEmpty()){
                return left;
            }
            Optional<Node> right = Term();
            left = Optional.of(new OperationNode(left.get(), operation, Optional.of(right.get())));
        }
        while(true);
    }

    /**
     * This method calls the Expression method to make both expressions before checking if they're present.
     * If present, a new OperationNode is created with the expressions and concatenation operation and returns it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> Concatenation() throws Exception{
        Optional<Node> temp1 = Expression();

        if (handleToken.MoreTokens()){
            if (temp1.isPresent()){
                Optional<Node> temp2 = Expression();

                if (temp2.isPresent()){
                    return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.CONCATENATION, Optional.of(temp2.get())));
                } else {
                    return temp1;
                }
            } else {
                throw new Exception("Incorrect syntax!");
            }
        }
        return temp1;
    }

    /**
     * This method calls Concatenation for the first expression and ParseOperation for the second expression. It checks if the expressions
     * are boolean comparison token types, and calls ParseOperation as the second expression for every case. It makes a new OperationNode
     * with the expressions and its designated operation type and returns it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> BooleanCompare() throws Exception{
        Optional<Node> temp1 = Concatenation();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.LESSTHAN){
                handleToken.MatchAndRemove(Token.TokenType.LESSTHAN);

                if (temp1.isPresent()){
                    Optional<Node> temp2 = ParseOperation();

                    if (temp2.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.LT, Optional.of(temp2.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }

            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.LESSEQUAL){
                handleToken.MatchAndRemove(Token.TokenType.LESSEQUAL);

                if (temp1.isPresent()){
                    Optional<Node> temp3 = ParseOperation();

                    if (temp3.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.LE, Optional.of(temp3.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }

            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.NOTEQUALS){
                handleToken.MatchAndRemove(Token.TokenType.NOTEQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp4 = ParseOperation();

                    if (temp4.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.NE, Optional.of(temp4.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }

            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.EQUALS){
                handleToken.MatchAndRemove(Token.TokenType.EQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp5 = ParseOperation();

                    if (temp5.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.EQ, Optional.of(temp5.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }

            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.GREATERTHAN){
                handleToken.MatchAndRemove(Token.TokenType.GREATERTHAN);

                if (temp1.isPresent()){
                    Optional<Node> temp6 = ParseOperation();

                    if (temp6.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.GT, Optional.of(temp6.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.GREATEREQUAL){
                handleToken.MatchAndRemove(Token.TokenType.GREATEREQUAL);

                if (temp1.isPresent()){
                    Optional<Node> temp7 = ParseOperation();

                    if (temp7.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.GE, Optional.of(temp7.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }

    /**
     * This method calls BooleanCompare for the first expression and calls ParseOperation for the second expression. It
     * checks for the match token types and calls ParseOperation for the second expression in every case. It makes a new OperationNode
     * with it's designated operation type and returns it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> Match() throws Exception{
        Optional<Node> temp1 = BooleanCompare();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.TILDE){
                handleToken.MatchAndRemove(Token.TokenType.TILDE);

                if (temp1.isPresent()){
                    Optional<Node> temp2 = ParseOperation();

                    if (temp2.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.MATCH, Optional.of(temp2.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.NOTTILDE){
                handleToken.MatchAndRemove(Token.TokenType.NOTTILDE);

                if (temp1.isPresent()){
                    Optional<Node> temp3 = ParseOperation();

                    if (temp3.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.NOTMATCH, Optional.of(temp3.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }

    /**
     * This method calls Match and calls ParseLValue for the second expression. It checks for the in token type and calls
     * ParseOperation for the second expression for this case. It makes a new OperationNode with the expressions and its
     * designated operation type.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> ArrayMembership() throws Exception{
        Optional<Node> temp1 = Match();

        if (handleToken.MoreTokens()){
            if (temp1.isPresent()){
                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.IN){
                    handleToken.MatchAndRemove(Token.TokenType.IN);

                    Optional<Node> temp2 = ParseLValue();

                    if (temp2.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.IN, Optional.of(temp2.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                }
            } else {
                throw new Exception("Incorrect syntax!");
            }
        }
        return temp1;
    }

    /**
     * This method calls ArrayMembership for the first expression and calls ParseOperation for the second expression. It checks
     * for and token type while calling ParseOperation to the second expression for each case. It makes a new OperationNode
     * with the expressions and its designated operation type before returning it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> And() throws Exception{
        Optional<Node> temp1 = ArrayMembership();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.AND){
                handleToken.MatchAndRemove(Token.TokenType.AND);

                if (temp1.isPresent()){
                    Optional<Node> temp2 = ParseOperation();

                    if (temp2.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.AND, Optional.of(temp2.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }

    /**
     * This method calls And for the first expression and calls ParseOperation for the second expression. It checks
     * for an or token type while calling ParseOperation to the second expression for each case. It makes a new OperationNode
     * with the expressions and its designated operation type before returning it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> Or() throws Exception{
        Optional<Node> temp1 = And();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OR) {
                handleToken.MatchAndRemove(Token.TokenType.OR);

                if (temp1.isPresent()) {
                    Optional<Node> temp3 = ParseOperation();

                    if (temp3.isPresent()) {
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.OR, Optional.of(temp3.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }

    /**
     * This method calls Or for the first expression and calls ParseOperation for the second expression. It checks
     * for ternary token types while calling ParseOperation to the second expression for each case. It makes a new OperationNode
     * with the expressions and its designated operation type before returning it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> Ternary() throws Exception{
        Optional<Node> temp1 = Or();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.QUESTION){
                handleToken.MatchAndRemove(Token.TokenType.QUESTION);

                if (temp1.isPresent()){
                    Optional<Node> temp4 = ParseOperation();

                    if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.COLON){
                        handleToken.MatchAndRemove(Token.TokenType.COLON);

                        if (temp4.isPresent()){
                            Optional<Node> temp5 = ParseOperation();

                            if (temp5.isPresent()){
                                return Optional.of(new TernaryNode(Optional.of(temp1.get()), Optional.of(temp4.get()), Optional.of(temp5.get())));
                            } else {
                                throw new Exception("Incorrect syntax!");
                            }
                        } else {
                            throw new Exception("Incorrect syntax!");
                        }
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }

    /**
     * This method calls Ternary for the first expression and calls ParseOperation for the second expression. It checks
     * for assignment token types while calling ParseOperation to the second expression for each case. It makes a new OperationNode
     * with the expressions and its designated operation type before returning it.
     * @return temp1
     * @throws Exception
     */
    Optional<Node> Assignment() throws Exception{
        Optional<Node> temp1 = Ternary();

        if (handleToken.MoreTokens()){
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CARETEQUALS){
                handleToken.MatchAndRemove(Token.TokenType.CARETEQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp2 = ParseOperation();

                    if (temp2.isPresent()){
                        return Optional.of(new AssignmentNode(Optional.of(temp2.get()), new OperationNode(temp1.get(), OperationNode.Operations.EXPONENT, Optional.of(temp2.get()))));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.PERCENTEQUALS){
                handleToken.MatchAndRemove(Token.TokenType.PERCENTEQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp3 = ParseOperation();

                    if (temp3.isPresent()){
                        return Optional.of(new AssignmentNode(Optional.of(temp3.get()), new OperationNode(temp1.get(), OperationNode.Operations.MODULO, Optional.of(temp3.get()))));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.ASTERISKEQUALS){
                handleToken.MatchAndRemove(Token.TokenType.ASTERISKEQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp4 = ParseOperation();

                    if (temp4.isPresent()){
                        return Optional.of(new AssignmentNode(Optional.of(temp4.get()), new OperationNode(temp1.get(), OperationNode.Operations.MULTIPLY, Optional.of(temp4.get()))));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.FRONTSLASHEQUALS){
                handleToken.MatchAndRemove(Token.TokenType.FRONTSLASHEQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp5 = ParseOperation();

                    if (temp5.isPresent()){
                        return Optional.of(new AssignmentNode(Optional.of(temp5.get()), new OperationNode(temp1.get(), OperationNode.Operations.DIVIDE, Optional.of(temp5.get()))));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.PLUSEQUALS){
                handleToken.MatchAndRemove(Token.TokenType.PLUSEQUALS);

                if (temp1.isPresent()){
                    Optional<Node> temp6 = ParseOperation();

                    if (temp6.isPresent()){
                        return Optional.of(new AssignmentNode(Optional.of(temp6.get()), new OperationNode(temp1.get(), OperationNode.Operations.ADD, Optional.of(temp6.get()))));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.MINUSEQUALS) {
                handleToken.MatchAndRemove(Token.TokenType.MINUSEQUALS);

                if (temp1.isPresent()) {
                    Optional<Node> temp7 = ParseOperation();

                    if (temp7.isPresent()) {
                        return Optional.of(new AssignmentNode(Optional.of(temp7.get()), new OperationNode(temp1.get(), OperationNode.Operations.SUBTRACT, Optional.of(temp7.get()))));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.EQUAL){
                handleToken.MatchAndRemove(Token.TokenType.EQUAL);

                if (temp1.isPresent()){
                    Optional<Node> temp8 = ParseOperation();

                    if (temp8.isPresent()){
                        return Optional.of(new OperationNode(temp1.get(), OperationNode.Operations.EQ, Optional.of(temp8.get())));
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return temp1;
    }
}

