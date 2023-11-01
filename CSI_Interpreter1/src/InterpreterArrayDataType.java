import java.util.HashMap;

public class InterpreterArrayDataType extends InterpreterDataType{
    // Variable declaration.
    private HashMap<String, InterpreterDataType> arrayMap;

    /**
     * Constructor.
     */
    public InterpreterArrayDataType(){
        arrayMap = new HashMap<>();
    }

    /**
     * Helper method.
     * @param value
     * @param type
     */
    public void AddArray(String value, InterpreterDataType type){
        arrayMap.put(value, type);
    }

    /**
     * Helper method.
     * @return arrayMap
     */
    public HashMap<String, InterpreterDataType> GetArrayMap(){
        return arrayMap;
    }
}
