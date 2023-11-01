import java.util.Optional;

public class OperationNode extends Node {
    // Variable declarations.
    Node left;
    Optional<Node> right = Optional.empty();
    public enum Operations{EQ, NE, LT, LE, GT, GE, AND, OR, NOT, MATCH, NOTMATCH, DOLLAR, PREINC, POSTINC, PREDEC, POSTDEC, UNARYPOS,
        UNARYNEG, IN, EXPONENT, ADD, SUBTRACT, MULTIPLY, DIVIDE, MODULO, CONCATENATION}
    Operations operand;

    /**
     * Constructor 1.
     * @param operation
     */
    public OperationNode(Node left, Operations operation){
        this.left = left;
        operand = operation;
    }

    /**
     * Constructor 2.
     * @param operation
     * @param left
     * @param right
     */
    public OperationNode(Node left, Operations operation, Optional<Node> right){
        this.left = left;
        operand = operation;
        this.right = right;
    }

    public OperationNode.Operations getOperation(){
        return operand;
    }

    /**
     * This method outputs the left Node, right Optional<Node>, and operation and returns it.
     * It also outputs only the left node and operation, depending on if the right Optional<Node>
     * is there.
     * @return "Left node and operations: " + left + ", " + operand
     */
    @Override
    public String toString() {
        if (right.isPresent()){
            return "Left node, right node, and operations: " +
                    left + ", " + right + ", " + operand;
        }
        return "Left node and operations: " + left + ", " + operand;
    }
}
