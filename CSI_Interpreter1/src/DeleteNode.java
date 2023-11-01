import java.util.LinkedList;
import java.util.Optional;

public class DeleteNode extends StatementNode{
    // Variable declaration.
    Optional<Node> arrayReference;

    /**
     * Constructor 1.
     * @param reference
     * @throws Exception
     */
    public DeleteNode(Optional<Node> reference) throws Exception {
        arrayReference = reference;
    }

    /**
     * This method outputs the deleted array.
     * @return "Delete array reference: " + arrayReference;
     */
    @Override
    public String toString() {
        return "Delete array reference: " + arrayReference;
    }
}
