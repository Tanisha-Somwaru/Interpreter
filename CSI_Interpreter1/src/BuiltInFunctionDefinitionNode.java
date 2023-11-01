import java.util.HashMap;
import java.util.function.Function;

public class BuiltInFunctionDefinitionNode extends FunctionDefinitionNode{
    // Variable declaration.
    private boolean variadic;
    private Function<HashMap<String, InterpreterDataType>, String> execute;

    /**
     * Constructor.
     */
    public BuiltInFunctionDefinitionNode(Function<HashMap<String, InterpreterDataType>, String> executes, boolean varic) {
        execute = executes;
        variadic = varic;
    }

    /**
     * This method gets the execute lambda function and runs its code.
     * @param parameters
     * @return execute.apply(parameters)
     */
    public String FunctionExecute(HashMap<String, InterpreterDataType> parameters){
        return execute.apply(parameters);
    }
}
