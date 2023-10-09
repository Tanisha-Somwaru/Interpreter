import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{
    // Variable declarations.
    private LinkedList<StatementNode> statements = new LinkedList<>();
    private Optional<Node> condition = Optional.empty();

    /**
     * This method outputs the string value of the statements.
     * @return "Statements: " + statements.toString()
     */
    @Override
    public String toString() {
        return "Statements: " + statements.toString();
    }
}
