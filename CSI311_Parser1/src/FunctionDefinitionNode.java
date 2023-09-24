import java.util.LinkedList;

public class FunctionDefinitionNode extends Node{
    // Variable declarations.
    private String functionName;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statements = new LinkedList<>();

    /**
     * Constructor.
     * @param function
     * @param param
     */
    public FunctionDefinitionNode(String function, LinkedList<String> param){
        functionName = function;
        parameters = param;
    }

    /**
     * This method outputs the name of the function and its parameters.
     * @return "Function name: " + functionName + " Parameters: " + parameters
     */
    @Override
    public String toString() {
        return "Function name: " + functionName + " Parameters: " + parameters;
    }
}