import java.util.Optional;

public class VariableReferenceNode extends Node{
    // Variable declarations.
    Token name;
    Optional<Node> indexExpression = Optional.empty();

    /**
     * Constructor 1.
     * @param name
     * @param indexExpress
     */
    public VariableReferenceNode(Token name, Optional<Node> indexExpress){
        this.name = name;
        indexExpression = indexExpress;
    }

    /**
     * Constructor 2.
     * @param name
     */
    public VariableReferenceNode(Token name){
        this.name = name;
    }

    /**
     * This method outputs the name and expression at the index or only the name,
     * depending on if there is an expression at the index or not before returning it.
     * @return "Name: " + name
     */
    @Override
    public String toString() {
        if (indexExpression.isPresent()){
            return "Name: " + name + ", " + "Name and expression: " + name + ", " + indexExpression;
        }
        return "Name: " + name;
    }
}
