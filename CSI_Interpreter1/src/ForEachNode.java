import java.util.Optional;

public class ForEachNode extends StatementNode{
    // Variable declaration.
    Optional<Node> forEachCondition;
    Optional<BlockNode> statement;

    /**
     * Constructor.
     * @param forEach
     * @param statements
     */
    public ForEachNode(Optional<Node> forEach, Optional<BlockNode> statements){
        forEachCondition = forEach;
        statement = statements;
    }

    /**
     * This method outputs the for each condition and the statement block.
     * @return "For each condition one: " + forEachCondition1 + " For each condition two: " + forEachCondition2 + " For each statement: " + statement;
     */
    @Override
    public String toString() {
        return "For each condition: " + forEachCondition + " For each statements: " + statement;
    }
}
