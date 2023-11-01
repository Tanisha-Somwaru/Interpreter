import java.util.LinkedList;
import java.util.Optional;

public class DoWhileNode extends StatementNode{
    // Variable declarations.
    Optional<Node> conditions;
    Optional<BlockNode> statements;

    /**
     * Constructor.
     * @throws Exception
     */
    public DoWhileNode(Optional<Node> condition, Optional<BlockNode> statement) throws Exception {
        conditions = condition;
        statements = statement;
    }

    /**
     * This method outputs conditions and statements,
     * @return "Conditions: " + conditions + " Statements: " + statements;
     */
    @Override
    public String toString() {
        return "Conditions: " + conditions + " Statements: " + statements;
    }
}
