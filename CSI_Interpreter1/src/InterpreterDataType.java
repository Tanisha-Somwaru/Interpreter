public class InterpreterDataType {
    // Variable declaration.
    private String value;

    /**
     * Constructor 1.
     * @param initialValue
     */
    public InterpreterDataType(String initialValue){
        value = initialValue;
    }

    /**
     * Constructor 2.
     */
    public InterpreterDataType(){
        value = "";
    }

    /**
     * Helper method.
     * @return value
     */
    public String getValue(){
        return value;
    }
}
