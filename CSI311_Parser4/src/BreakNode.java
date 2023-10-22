import java.util.Optional;

public class BreakNode extends StatementNode{
    /**
     * Constructor.
     */
    public BreakNode(){

    }

    /**
     * This method outputs break.
     * @return "Break node: " + "Break";
     */
    @Override
    public String toString() {
        return "Break node: " + "Break";
    }
}
