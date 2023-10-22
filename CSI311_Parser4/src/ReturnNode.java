import java.util.LinkedList;
import java.util.Optional;

public class ReturnNode extends StatementNode{
    // Variable declaration.
    Optional<Node> parameter;

    /**
     * Constructor.
     * @param param
     * @throws Exception
     */
    public ReturnNode(Optional<Node> param) throws Exception {
        parameter = param;
    }

    /**
     * This method outputs the parameters.
     * @return "Parameters: " + parameter;
     */
    @Override
    public String toString() {
        return "Return: " + parameter;
    }
}
