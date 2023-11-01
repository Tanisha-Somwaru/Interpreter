import java.util.LinkedList;

public class ProgramNode extends Node {
    //Variable declarations.
    private LinkedList<FunctionDefinitionNode> functionDefinitionNodes = new LinkedList<>();
    private LinkedList<BlockNode> BEGIN_BlockNodes = new LinkedList<>();
    private LinkedList<BlockNode> END_BlockNodes = new LinkedList<>();
    private LinkedList<BlockNode> other_BlockNodes = new LinkedList<>();

    /**
     * Helper method for ParseFunction.
     *
     * @param functNode
     */
    public void AddFunction(FunctionDefinitionNode functNode) {
        functionDefinitionNodes.add(functNode);
    }

    /**
     * Helper method for ParseAction.
     *
     * @param nodeBlock1
     */
    public void AddBlock1(BlockNode nodeBlock1) {
        BEGIN_BlockNodes.add(nodeBlock1);
    }

    /**
     * Helper method for ParseAction.
     *
     * @param nodeBlock2
     */
    public void AddBlock2(BlockNode nodeBlock2) {
        END_BlockNodes.add(nodeBlock2);
    }

    /**
     * Helper method for ParseAction.
     *
     * @param nodeBlock3
     */
    public void AddBlock3(BlockNode nodeBlock3) {
        other_BlockNodes.add(nodeBlock3);
    }

    /**
     * This method outputs the string value of BEGIN_BlockNodes, functionDefintionNodes,
     * END_BlockNodes, and other_BlockNodes.
     *
     * @return "BEGIN: " + BEGIN_BlockNodes + "\n" + "Function Nodes: " + functionDefinitionNodes +
     * "\n" + "END: " + END_BlockNodes + "\n" + "Other Nodes: " + other_BlockNodes + "\n"
     */
    @Override
    public String toString() {
        return "BEGIN: " + BEGIN_BlockNodes + "\n" + "Function Nodes: " + functionDefinitionNodes + "\n" +
                "END: " + END_BlockNodes + "\n" + "Other Nodes: " + other_BlockNodes.toString() + "\n";
    }
}

