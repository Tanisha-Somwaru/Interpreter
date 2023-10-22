import java.util.LinkedList;
import java.util.Optional;

public class FunctionCallNode extends StatementNode{
    // Variable declarations.
    String functionName;
    LinkedList<Node> parameters;

    /**
     * Constructor 1.
     * @param functName
     * @throws Exception
     */
    public FunctionCallNode(String functName) throws Exception {
        functionName = functName;
        parameters = new LinkedList<>();
    }

    /**
     * Constructor 2.
     * @param functName
     * @param param
     */
    public FunctionCallNode(String functName, LinkedList<Node> param){
        functionName = functName;
        parameters = param;
    }

    /**
     * This method outputs the function name and its parameters.
     * @return "Function name: " + functionName + " Parameters: " + parameters;
     */
    @Override
    public String toString() {
        return "Function name: " + functionName + " Parameters: " + parameters;
    }

}
