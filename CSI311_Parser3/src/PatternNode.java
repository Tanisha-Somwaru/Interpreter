public class PatternNode extends Node {
    // Variable declaration.
    Token patternName;

    /**
     * Constructor.
     * @param name
     */
    public PatternNode(Token name){
        patternName = name;
    }

    /**
     * This method outputs a pattern token name and returns it.
     * @return "Pattern name: " + patternName
     */
    @Override
    public String toString() {
        return "Pattern name: " + patternName;
    }
}
