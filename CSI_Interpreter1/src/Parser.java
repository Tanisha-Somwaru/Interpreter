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
            AcceptSeparators();
            if (!(ParseFunction(progNode))){
                if (!(ParseAction(progNode))){
                    throw new Exception("Exception!");
                }
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

            if (handleToken.MatchAndRemove(Token.TokenType.SEPARATOR).isPresent()) {
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
            progNode.AddBlock1(ParseBlock());
            return true;
        } else if (handleToken.MatchAndRemove(Token.TokenType.END).isPresent()){
            progNode.AddBlock2(ParseBlock());
            return true;
        } else if (handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES).isPresent()){
            Optional<Node> condition = ParseOperation();
            BlockNode temp1 = new BlockNode(condition);

            if (handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES).isPresent()){
                progNode.AddBlock3(temp1);
            }
            return true;
        } else {
            BlockNode statement = ParseBlock();
            BlockNode block = new BlockNode(statement.GetStatements());
            progNode.AddBlock3(block);

            return true;
        }
    }

    /**
     * This method finds single and multi-line statements inside the curly brackets by calling ParseStatement(). Multi-lined statements
     * are being checked for until there are no more multi=lined statements. The result of ParseStatement() gets added to statements and
     * returned as BlockNode object.
     * @return statements
     */
    BlockNode ParseBlock() throws Exception{
        // Variable declaration.
        BlockNode statements = new BlockNode();

        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACKET) {
            handleToken.MatchAndRemove(Token.TokenType.OPENCURLYBRACKET);
            Optional<StatementNode> temp1 = ParseStatement();

            if (temp1.isEmpty()){
                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDCURLYBRACKET) {
                    handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET);
                    return statements;
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
            statements.AddStatements(temp1.get());
            AcceptSeparators();

            temp1 = ParseStatement();
            AcceptSeparators();
            while (temp1.isPresent()){
                AcceptSeparators();
                statements.AddStatements(temp1.get());
                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDCURLYBRACKET) {
                    handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET);
                    return statements;
                }
                temp1 = ParseStatement();
            }
            AcceptSeparators();

            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDCURLYBRACKET) {
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET);
                return statements;
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else {
            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENCURLYBRACKET){
                handleToken.MatchAndRemove(Token.TokenType.OPENCURLYBRACKET);
                AcceptSeparators();
                Optional<StatementNode> temp2 = ParseStatement();

                if (temp2.isPresent()){
                    if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDCURLYBRACKET){
                        handleToken.MatchAndRemove(Token.TokenType.CLOSEDCURLYBRACKET);
                        statements.AddStatements(temp2.get());
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                }
            }else{
                Optional<StatementNode> temp1 = ParseStatement();
                if(temp1.isPresent()){
                    statements.AddStatements(temp1.get());
                }
            }
        }

        return statements;
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
                OperationNode nodeTemp1 = new OperationNode(temp5.get(), OperationNode.Operations.PREINC);
                return Optional.of(new AssignmentNode(temp5, nodeTemp1));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.DECREMENT){
            handleToken.MatchAndRemove(Token.TokenType.DECREMENT);
            Optional<Node> temp6 = ParseOperation();

            if (temp6.isPresent()){
                OperationNode nodeTemp2 = new OperationNode(temp6.get(), OperationNode.Operations.PREDEC);
                return Optional.of(new AssignmentNode(temp6, nodeTemp2));
            } else {
                throw new Exception("Incorrect syntax!");
            }
        } else {
            Optional<StatementNode> node1 = ParseFunctionCall();

            if (node1.isPresent()){
                return Optional.of(node1.get());
            }
            return PostIncrementAndPostDecrement();
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
     * This method checks for the statement types and calls their designated parse method
     * before returning it as an object of the specified statement node class.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseStatement() throws Exception{
        AcceptSeparators();
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.IF || handleToken.Peek(0).get().getTokenType() == Token.TokenType.ELSE){
            if (handleToken.MoreTokens()){
                handleToken.MatchAndRemove(Token.TokenType.ELSE);
                Optional<StatementNode> nodeTemp = ParseIf();

                return nodeTemp;
            }
            Optional<StatementNode> node1 = ParseIf();
            return node1;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CONTINUE){
            Optional<StatementNode> node2 = ParseContinue();
            return node2;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.BREAK){
            Optional<StatementNode> node3 = ParseBreak();
            return node3;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.FOR){
            Optional<StatementNode> node4 = ParseFor();
            return node4;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.DELETE){
            Optional<StatementNode> node5 = ParseDelete();
            return node5;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.WHILE){
            Optional<StatementNode> node6 = ParseWhile();
            return node6;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.DO){
            Optional<StatementNode> node7 = ParseDoWhile();
            return node7;
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.RETURN){
            Optional<StatementNode> node8 = ParseReturn();
            return node8;
        } else {
            Optional<Node> temp = ParseOperation();
            if (temp.isPresent()){
                if (temp.get() instanceof FunctionCallNode){
                    return Optional.of((StatementNode)(temp.get()));
                } else if (temp.get() instanceof AssignmentNode){
                    return Optional.of((StatementNode)(temp.get()));
                }
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for the continue statement token and removes it before returning
     * a ContinueNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseContinue() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CONTINUE){
            handleToken.MatchAndRemove(Token.TokenType.CONTINUE);
            ContinueNode continueNode = new ContinueNode();

            return Optional.of(continueNode);
        }
        return Optional.empty();
    }

    /**
     * This method checks for the break statement token and removes it before returning a
     * BreakNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseBreak() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.BREAK){
            handleToken.MatchAndRemove(Token.TokenType.BREAK);
            BreakNode breakNode = new BreakNode();

            return Optional.of(breakNode);
        }
        return Optional.empty();
    }

    /**
     * This method checks for if statement, else-if statement, and else statement. The conditions of the
     * if and else-if statements are found calling ParseOperation(). The statement blocks are found by calling
     * ParseBlock(). The result is then returned as a new IfNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseIf() throws Exception{
        AcceptSeparators();
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.IF){
            handleToken.MatchAndRemove(Token.TokenType.IF);

            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENPARENTHESES){
                handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES);
                Optional<Node> condition1 = ParseOperation();

                if (condition1.isPresent()){
                    if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                        handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);

                        Optional<BlockNode> statements1 = Optional.of(ParseBlock());

                        AcceptSeparators();
                        if(!handleToken.MoreTokens()){
                            IfNode node1 = new IfNode(condition1, statements1);
                            return Optional.of(node1);
                        }

                        AcceptSeparators();
                        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.ELSE) {
                            handleToken.MatchAndRemove(Token.TokenType.ELSE);
                            Optional<StatementNode> nextIfNode = ParseIf();

                            if(nextIfNode.isPresent()){
                                AcceptSeparators();
                                IfNode node1 = new IfNode(condition1, statements1, nextIfNode);
                                return Optional.of(node1);
                            } else {
                                Optional<BlockNode> elseStatements = Optional.of(ParseBlock());
                                IfNode elseNode = new IfNode(Optional.empty(), elseStatements);
                                IfNode node2 = new IfNode(condition1, statements1, Optional.of(elseNode));
                                return Optional.of(node2);
                            }
                        } else {
                            IfNode node1 = new IfNode(condition1, statements1);
                            return Optional.of(node1);
                        }
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for a for statement and a for in statement type. ParseOperation() is called to
     * find the components of the loop condition for a for statement loop and a for in statement loop. The result
     * is returned as a ForNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseFor() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.FOR){
            handleToken.MatchAndRemove(Token.TokenType.FOR);
            AcceptSeparators();

            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENPARENTHESES){
                handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES);
                Optional<Node> initialize = ParseOperation();
                ///AcceptSeparators();

                if (initialize.isPresent()){
                    AcceptSeparators();
                    if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                        handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                        AcceptSeparators();
                        Optional<BlockNode> statements1 = Optional.of(ParseBlock());
                        ForEachNode node2 = new ForEachNode(initialize, statements1);

                        return Optional.of(node2);
                    } else {
                        AcceptSeparators();
                        Optional<Node> condition2 = ParseOperation();

                        if (condition2.isPresent()){
                            AcceptSeparators();
                            Optional<Node> update = ParseOperation();

                            if (update.isPresent()){
                                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                                    handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                                    Optional<BlockNode> statements2 = Optional.of(ParseBlock());
                                    ForNode node1 = new ForNode(initialize, condition2, update, statements2);

                                    return Optional.of(node1);
                                }
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
        return Optional.empty();
    }

    /**
     * This method checks for a while loop statement and finds its condition by calling ParseOperation(). The
     * result is returned as new WhileNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseWhile() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.WHILE){
            handleToken.MatchAndRemove(Token.TokenType.WHILE);

            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENPARENTHESES){
                handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES);
                Optional<Node> condition = ParseOperation();

                if (condition.isPresent()){
                    if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                        handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);

                        AcceptSeparators();
                        Optional<BlockNode> statements = Optional.of(ParseBlock());
                        WhileNode node = new WhileNode(condition, statements);
                        return Optional.of(node);
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for a do statement and finds the statement block in it by calling ParseBlock(). Then,
     * the while statement is checked and its condition is found by calling ParseOperation(). The result is
     * returned as a DoWhileNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseDoWhile() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.DO){
            handleToken.MatchAndRemove(Token.TokenType.DO);
            Optional<BlockNode> statements = Optional.of(ParseBlock());

            if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.WHILE) {
                handleToken.MatchAndRemove(Token.TokenType.WHILE);

                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.OPENPARENTHESES){
                    handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES);
                    Optional<Node> condition = ParseOperation();

                    if (condition.isPresent()){
                        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                            handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);

                            if (AcceptSeparators()){
                                DoWhileNode node = new DoWhileNode(condition, statements);
                                return Optional.of(node);
                            }
                        }
                    } else {
                        throw new Exception("Incorrect syntax!");
                    }
                } else {
                    throw new Exception("Incorrect syntax!");
                }
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for a delete statement and then calls ParseLValue() to find the array that's to be
     * deleted. The result is returned as a DeleteNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseDelete() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.DELETE){
            handleToken.MatchAndRemove(Token.TokenType.DELETE);
            Optional<Node> array = ParseLValue();

            if (array.isPresent()){
                DeleteNode node = new DeleteNode(array);
                return Optional.of(node);
            } else {
                throw new Exception("Incorrect syntax!");
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for a return statement and finds the condition the statement is attached to by calling
     * ParseOperation(). The result is returned as a ReturnNode object.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseReturn() throws Exception{
        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.RETURN){
            handleToken.MatchAndRemove(Token.TokenType.RETURN);
            Optional<Node> temp1 = ParseOperation();

            if (temp1.isPresent()){
                if (AcceptSeparators()){
                    ReturnNode node = new ReturnNode(temp1);
                    return Optional.of(node);
                }
            } else {
                throw new Exception("Incorrect syntax!");
            }
        }
        return Optional.empty();
    }

    /**
     * This method checks for default function call statement types and custom function calls types. A word token is found
     * to get the function name for custom function calls with MatchAndRemove(), and it's used to get the function call of default
     * function call types. The condition of the function calls are found by calling ParseOperation(), the result is returned
     * as a FunctionCallNode.
     * @return Optional.empty
     * @throws Exception
     */
    Optional<StatementNode> ParseFunctionCall() throws Exception{
        // Variable declarations.
        String functionName = "";
        LinkedList<Node> parameters = new LinkedList<>();

        if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.PRINT){
            functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.PRINT));
            Optional<Node> parameter1 = ParseOperation();
            if (parameter1.isEmpty()){
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                FunctionCallNode node2 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node2);
            }
            parameters.add(parameter1.get());

            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                parameter1 = ParseOperation();
                if (parameter1.isEmpty()){
                    throw new Exception("Incorrect syntax");
                } else {
                    parameters.add(parameter1.get());
                }
            }
            FunctionCallNode node3 = new FunctionCallNode(functionName, parameters);
            return Optional.of(node3);

        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.PRINTF){
            functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.PRINTF));
            Optional<Node> parameter2 = ParseOperation();
            if (parameter2.isEmpty()){
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                FunctionCallNode node4 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node4);
            }
            parameters.add(parameter2.get());

            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                parameter2 = ParseOperation();
                if (parameter2.isEmpty()){
                    throw new Exception("Incorrect syntax");
                } else {
                    parameters.add(parameter2.get());
                }
            }
            FunctionCallNode node5 = new FunctionCallNode(functionName, parameters);
            return Optional.of(node5);
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.GETLINE){
            functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.GETLINE));
            Optional<Node> parameter3 = ParseOperation();
            if (parameter3.isEmpty()){
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                FunctionCallNode node6 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node6);
            }
            parameters.add(parameter3.get());

            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                parameter3 = ParseOperation();
                if (parameter3.isEmpty()){
                    throw new Exception("Incorrect syntax");
                } else {
                    parameters.add(parameter3.get());
                }
            }
            if (handleToken.MoreTokens()){
                FunctionCallNode node7 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node7);
            }
            FunctionCallNode node8 = new FunctionCallNode(functionName, parameters);
            return Optional.of(node8);
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.EXIT){
            functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.EXIT));
            Optional<Node> parameter4 = ParseOperation();
            if (parameter4.isEmpty()){
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                FunctionCallNode node8 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node8);
            }
            parameters.add(parameter4.get());

            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                parameter4 = ParseOperation();
                if (parameter4.isEmpty()){
                    throw new Exception("Incorrect Syntax");
                } else {
                    parameters.add(parameter4.get());
                }
            }
            FunctionCallNode node9 = new FunctionCallNode(functionName, parameters);
            return Optional.of(node9);
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.NEXTFILE){
            functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.NEXTFILE));
            Optional<Node> parameter5 = ParseOperation();
            if (parameter5.isEmpty()){
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                FunctionCallNode node10 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node10);
            }
            parameters.add(parameter5.get());

            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                parameter5 = ParseOperation();
                if (parameter5.isEmpty()){
                    throw new Exception("Incorrect Syntax");
                } else {
                    parameters.add(parameter5.get());
                }
            }
            FunctionCallNode node11 = new FunctionCallNode(functionName, parameters);
            return Optional.of(node11);
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.NEXT){
            functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.NEXT));
            Optional<Node> parameter6 = ParseOperation();
            if (parameter6.isEmpty()){
                handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                FunctionCallNode node12 = new FunctionCallNode(functionName, parameters);
                return Optional.of(node12);
            }
            parameters.add(parameter6.get());

            while (handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                parameter6 = ParseOperation();
                if (parameter6.isEmpty()){
                    throw new Exception("Incorrect Syntax");
                } else {
                    parameters.add(parameter6.get());
                }
            }
            FunctionCallNode node13 = new FunctionCallNode(functionName, parameters);
            return Optional.of(node13);
        } else if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.WORD){
            if (handleToken.Peek(1).get().getTokenType() == Token.TokenType.OPENPARENTHESES){
                functionName = String.valueOf(handleToken.MatchAndRemove(Token.TokenType.WORD));
                handleToken.MatchAndRemove(Token.TokenType.OPENPARENTHESES);

                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                    handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                    AcceptSeparators();
                    FunctionCallNode node13 = new FunctionCallNode(functionName);
                    return Optional.of(node13);
                }
                Optional<Node> temp7 = ParseOperation();

                while (handleToken.MoreTokens() && handleToken.MatchAndRemove(Token.TokenType.COMMA).isPresent()){
                    if (temp7.isPresent()) {
                        parameters.add(temp7.get());
                        temp7 = ParseOperation();
                    }
                }
                if (temp7.isPresent()) {
                    parameters.add(temp7.get());
                }
                if (handleToken.Peek(0).get().getTokenType() == Token.TokenType.CLOSEDPARENTHESES){
                    handleToken.MatchAndRemove(Token.TokenType.CLOSEDPARENTHESES);
                    AcceptSeparators();
                    FunctionCallNode node12 = new FunctionCallNode(functionName, parameters);
                    return Optional.of(node12);
                } else {
                    throw new Exception("Incorrect syntax!");
                }
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
                OperationNode operation1 = new OperationNode(tempValue1.get(), OperationNode.Operations.POSTINC);
                return Optional.of(new AssignmentNode(tempValue1, operation1));

            } else if (handleToken.MatchAndRemove(Token.TokenType.DECREMENT).isPresent()){
                OperationNode operation2 = new OperationNode(tempValue1.get(), OperationNode.Operations.POSTDEC);
                return Optional.of(new AssignmentNode(tempValue1, operation2));
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
                }
                return temp1;
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
                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.EXPONENT, Optional.of(temp2.get()))));
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
                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.MODULO, Optional.of(temp3.get()))));
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
                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.MULTIPLY, Optional.of(temp4.get()))));
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
                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.DIVIDE, Optional.of(temp5.get()))));
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
                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.ADD, Optional.of(temp6.get()))));
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
                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.SUBTRACT, Optional.of(temp7.get()))));
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

                        return Optional.of(new AssignmentNode(Optional.of(temp1.get()), new OperationNode(temp1.get(), OperationNode.Operations.EQ, Optional.of(temp8.get()))));
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
