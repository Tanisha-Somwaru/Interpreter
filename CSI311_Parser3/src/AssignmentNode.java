import java.util.Optional;

public class AssignmentNode extends Node {
    // Variable declarations.
    Optional<Node> target = Optional.empty();
    OperationNode expression;

    /**
     * Constructor.
     * @param target
     * @param express
     */
    public AssignmentNode(Optional<Node> target, OperationNode express){
        this.target = target;
        expression = express;
    }

    /**
     * This ouputs the target and expression
     * @return "Target: " + target + "Expression: " + expression
     */
    @Override
    public String toString() {
        return "Target: " + target + "Expression: " + expression;
    }
}
