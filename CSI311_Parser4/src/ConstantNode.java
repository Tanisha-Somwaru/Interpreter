public class ConstantNode extends Node {
    // Variable declaration.
    Token constantName;

    /**
     * Constructor.
     * @param name
     */
    public ConstantNode(Token name){
        constantName = name;
    }

    /**
     * This method outputs the name of the token and returns it.
     * @return "Constant name: " + constantName
     */
    @Override
    public String toString() {
        return "Constant name: " + constantName;
    }
}
