import java.util.Optional;

public class TernaryNode extends Node{
    // Variable declarations.
    Optional<Node> expression = Optional.empty();
    Optional<Node> case1 = Optional.empty();
    Optional<Node> case2 = Optional.empty();

    /**
     * Constructor.
     * @param express
     * @param case1
     * @param case2
     */
    public TernaryNode(Optional<Node> express, Optional<Node> case1, Optional<Node> case2){
        expression = express;
        this.case1 = case1;
        this.case2 = case2;
    }

    /**
     * This method outputs the expression, case 1, and case 2.
     * @return "Expression: " + expression + "Case 1: " + case1 + "Case 2: " + case2
     */
    @Override
    public String toString() {
        return "Expression: " + expression + "Case 1: " + case1 + "Case 2: " + case2;
    }
}
