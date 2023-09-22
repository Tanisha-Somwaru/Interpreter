import java.util.LinkedList;

public class FunctionDefinitionNode extends Node{
    // Variable declarations.
    private String functionName;
    private LinkedList<String> parameters;
    private LinkedList<StatementNode> statements = new LinkedList<>();

    public FunctionDefinitionNode(String function, LinkedList<String> param){
        functionName = function;
        parameters = param;
    }

    @Override
    public String toString() {
        return "function" + functionName + "(" + parameters.toString() + ")";
    }
}