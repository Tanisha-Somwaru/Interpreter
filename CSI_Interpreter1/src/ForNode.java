import java.util.Optional;

public class ForNode extends StatementNode{
    // Variable declaration.
    Optional<Node> initialize;
    Optional<Node> condition;
    Optional<Node> update;
    Optional<BlockNode> statement;

    /**
     * Constructor
     * @param initial
     * @param cond
     * @param updater
     */
    public ForNode(Optional<Node> initial, Optional<Node> cond, Optional<Node> updater, Optional<BlockNode> statements){
        initialize = initial;
        condition = cond;
        update = updater;
        statement = statements;
    }

    /**
     * This method out puts the for loop initializer, the condition, the update, and its statements.
     * @return
     */
    @Override
    public String toString() {
        return "For loop initializer: " + initialize + " For loop condition: " + condition + " For loop update: " + update + " For loop statements: " + statement;
    }
}