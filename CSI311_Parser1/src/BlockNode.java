import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{
    // Variable declarations.
    private LinkedList<StatementNode> statements = new LinkedList<>();
    private Optional<Node> condition = Optional.empty();

    @Override
    public String toString() {
        return "Statements: " + statements.toString();
    }
}