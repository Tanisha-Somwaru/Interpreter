import java.util.LinkedList;
import java.util.Optional;

public class BlockNode extends Node{
    // Variable declarations.
    private LinkedList<StatementNode> statements = new LinkedList<>();
    private Optional<Node> conditions = Optional.empty();

    /**
     * Constructor 1.
     */
    public BlockNode(){

    }

    /**
     * Constructor 2.
     * @param condition
     */
    public BlockNode(Optional<Node> condition){
        conditions = condition;
    }

    /**
     * Constructor 3.
     * @param statement
     */
    public BlockNode(LinkedList<StatementNode> statement){
        statements = statement;
    }

    /**
     * Helper method to add the statements into the LinkedList.
     * @param statement
     */
    public void AddStatements(StatementNode statement){
        statements.add(statement);
    }

    /**
     * Helper method to get the StatementNode value of the ParseBlock() call.
     * @return statements
     */
    public LinkedList<StatementNode> GetStatements() {
        return statements;
    }

    /**
     * This method outputs the string value of the statements and conditions.
     * @return "Statements: " + statements.toString()
     */
    @Override
    public String toString() {
        if (conditions.isPresent()){
            return "Conditions: " + conditions + " Statements: " + statements.toString();
        }
        return "Statements: " + statements.toString();
    }
}
