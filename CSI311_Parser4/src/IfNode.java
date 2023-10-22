import java.util.LinkedList;
import java.util.Optional;

public class IfNode extends StatementNode{
    // Variable declarations.
    Optional<Node> condition;
    Optional<BlockNode> statement;

    Optional<StatementNode> nextIf;

    /**
     * Construtor 1.
     * @param conditions
     * @param statements
     * @throws Exception
     */
    public IfNode(Optional<Node> conditions, Optional<BlockNode> statements) throws Exception {
        condition = conditions;
        statement = statements;
        nextIf = Optional.empty();
    }

    /**
     * Constructor 2.
     * @param statements
     * @throws Exception
     */
    public IfNode(Optional<BlockNode> statements) throws Exception {
        condition = Optional.empty();
        statement = statements;
        nextIf = Optional.empty();
    }

    public IfNode(Optional<Node> conditions, Optional<BlockNode> statements, Optional<StatementNode> next){
        condition = conditions;
        statement = statements;
        nextIf = next;
    }

    /**
     * This method outputs the conditions and statements.
     * @return "Statements: " + statement;
     */
    @Override
    public String toString() {
        if(nextIf.isPresent()){
            if(condition.isPresent()){
                return "Conditions: " + condition + " Statements: " + statement + " Next If: " + nextIf.get().toString();
            }
            return "Statements: " + statement + " Next If: " + nextIf.get().toString();
        }
        if (condition.isPresent()){
            return "Conditions: " + condition + " Statements: " + statement;
        }
        return "Statements: " + statement;
    }
}
