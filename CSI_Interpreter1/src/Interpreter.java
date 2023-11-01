import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

public class Interpreter {
    // Variable declarations.
    private HashMap<String, InterpreterDataType> globalVariables = new HashMap<>();
    private HashMap<String, FunctionDefinitionNode> functionSource = new HashMap<>();
    private Path filePath;
    private ProgramNode program;
    private String fileName;
    private LineManager manageLine;

    public class LineManager{
        // Variable declaration.
        List<String> lineManager;
        int NR;
        int NF;
        /**
         * Constructor.
         * @param line
         */
        public LineManager(List<String> line){
            lineManager = line;
            NR = 0;
            NF = 0;
        }

        /**
         * This method gets the next line and splits it by looking at the global variables to find the fields separator. It returns
         * true if it split and returns false when there is nothing to split.
         * @return true.
         */
        boolean SplitAndAssign(){
            // Variable declarations.
            if (lineManager.isEmpty()){
                return false;
            } else {
                String FS = globalVariables.get("FS").getValue();
                String[] lineInput = lineManager.get(NF).split(FS);

                for (int i = 0; i < lineInput.length; i++){
                    String spiltResult = "$" + NR;
                    globalVariables.put(spiltResult, new InterpreterDataType(lineInput[i]));
                    NR++;
                }
                NF++;
                return true;
            }
        }
    }

    /**
     * Constructor.
     * @param prog
     * @param paths
     * @throws Exception
     */
    public Interpreter(ProgramNode prog, Path paths) throws Exception {
        program = prog;
        filePath = paths;
        List<String> contents;

        if (Files.exists(filePath)){
            contents = Files.readAllLines(filePath);
            manageLine = new LineManager(contents);
            String filename1 = paths.getFileName().toString();
            InterpreterDataType fileType = new InterpreterDataType(filename1);

            globalVariables.put("FILENAME", fileType);
            globalVariables.put("FS", new InterpreterDataType(" "));
            globalVariables.put("OFMT", new InterpreterDataType("%.6g"));
            globalVariables.put("ORS", new InterpreterDataType("\n"));
        } else {
            contents = new ArrayList<>();
            manageLine = new LineManager(contents);
        }


        /*
         * This lambda function returns a string finalValue and gets the parameters of the toupper keyword. The
         * parameter is the string that is being upper-cased.
         */
        String keyWord1 = "toupper";
        Function<HashMap<String, InterpreterDataType>, String> execute1 = (parameters)->{
            InterpreterDataType type = parameters.get("0");
            String value = type.getValue();
            String finalValue = value.toUpperCase();

            return finalValue;
        };
        BuiltInFunctionDefinitionNode toUpper = new BuiltInFunctionDefinitionNode(execute1, false);
        functionSource.put(keyWord1, toUpper);

        /*
         * This lambda function returns a string finalValue and  gets the parameters of the tolower keyword. The parameter
         * is the string that is being lower-cased.
         */
        String keyWord2 = "tolower";
        Function<HashMap<String, InterpreterDataType>, String> execute2 = (parameters)->{
            InterpreterDataType type = parameters.get("0");
            String value = type.getValue();
            String finalValue = value.toLowerCase();

            return finalValue;
        };
        BuiltInFunctionDefinitionNode toLower = new BuiltInFunctionDefinitionNode(execute2, false);
        functionSource.put(keyWord2, toLower);

        /*
         * This lambda function returns finalValue1 or finalValue2 depending on how many parameters are in the substr
         * keyword. The first parameter is the string itself and the second and third parameters is the substring length combined
         * if there is a third parameter. The second parameter is typically the substring length.
         */
        String keyWord3 = "substr";
        Function<HashMap<String, InterpreterDataType>, String> execute3 = (parameters)->{
            String value1;
            String value2;
            String value3;
            InterpreterDataType type1;
            InterpreterDataType type2;
            InterpreterDataType type3;

            if (parameters.containsKey("2")){
                type1 = parameters.get("0");
                type2 = parameters.get("1");
                type3 = parameters.get("2");
                value1 = type1.getValue();
                value2 = type2.getValue();
                value3 = type3.getValue();
                String finalValue1 = value1.substring(Integer.parseInt(value2), Integer.parseInt(value2) + Integer.parseInt(value3));

                return finalValue1;
            } else {
                type1 = parameters.get("0");
                type2 = parameters.get("1");
                value1 = type1.getValue();
                value2 = type2.getValue();
                String finalValue2 = value1.substring(Integer.parseInt(value2));

                return finalValue2;
            }
        };
        BuiltInFunctionDefinitionNode subStr = new BuiltInFunctionDefinitionNode(execute3, false);
        functionSource.put(keyWord3, subStr);

        /*
         * This lambda function returns the length of the split string array. It has four parameters; parameter one is
         * the InterpreterDataType, parameter two is the InterpreterArrayDataType, parameter three is an InterpreterDataType,
         * and the fourth parameter is an InterpreterArrayDataType. The third and fourth parameter are optional since one of them
         * keeps track of a field separator if there is one and the fourth one is an array to keep a count of the field
         * separators if there are any.
         */
        String keyWord4 = "split";
        Function<HashMap<String, InterpreterDataType>, String> execute4 = (parameters)->{
            InterpreterDataType type1 = parameters.get("0");
            InterpreterArrayDataType type2 = (InterpreterArrayDataType) parameters.get("1");
            InterpreterDataType type3 = parameters.get("2");
            InterpreterArrayDataType type4 = (InterpreterArrayDataType) parameters.get("3");
            String value1 = type1.getValue();
            String value3 = type3.getValue();
            String[] split = value1.split(value3);

            for (int i = 0; i < split.length; i++){
                type2.AddArray(String.valueOf(i + 1), new InterpreterDataType(split[i]));
                type4.AddArray(String.valueOf(i + 1), new InterpreterDataType(value3));
            }
            return String.valueOf(split.length);
        };
        BuiltInFunctionDefinitionNode split = new BuiltInFunctionDefinitionNode(execute4, false);
        functionSource.put(keyWord4, split);

        /*
         * This lambda function returns a finalValue string and gets the first parameter of the length keyword.
         * The value represents the length and the length() method is called to get the actual length.
         */
        String keyWord5 = "length";
        Function<HashMap<String, InterpreterDataType>, String> execute5 = (parameters)->{
            InterpreterDataType type = parameters.get("0");
            String value = type.getValue();
            String finalValue = String.valueOf(value.length());

            return finalValue;
        };
        BuiltInFunctionDefinitionNode length = new BuiltInFunctionDefinitionNode(execute5, false);
        functionSource.put(keyWord5, length);

        /*
         * This lambda function returns a finalValue string and gets the two parameters for the index keyword. The
         * index of the first is represented by the second value. The index is found using the indexOf() method.
         */
        String keyWord6 = "index";
        Function<HashMap<String, InterpreterDataType>, String> execute6 = (parameters)->{
            InterpreterDataType type1 = parameters.get("0");
            InterpreterDataType type2 = parameters.get("1");
            String value1 = type1.getValue();
            String value2 = type2.getValue();
            String finalValue = String.valueOf(value1.indexOf(value2) + 1);

            return finalValue;
        };
        BuiltInFunctionDefinitionNode index = new BuiltInFunctionDefinitionNode(execute6, false);
        functionSource.put(keyWord6, index);

        /*
         * This lambda function returns a finalValue string and gets the three parameters for the gsub keyword. The first
         * value is being completely replaced with the second value by using the replaceAll() method.
         */
        String keyWord7 = "gsub";
        Function<HashMap<String, InterpreterDataType>, String> execute7 = (parameters)->{
            InterpreterDataType type1 = parameters.get("0");
            InterpreterDataType type2 = parameters.get("1");
            InterpreterDataType type3 = parameters.get("2");
            String value1 = type1.getValue();
            String value2 = type2.getValue();
            String value3 = type3.getValue();
            String finalValue = value3.replaceAll(value1, value2);

            return finalValue;
        };
        BuiltInFunctionDefinitionNode gSub = new BuiltInFunctionDefinitionNode(execute7, false);
        functionSource.put(keyWord7, gSub);

        /*
         * This lambda function returns a finalValue string and gets the three parameters for the sub keyword, and
         * it replaces the third value with the second value using the replace() method.
         */
        String keyWord8 = "sub";
        Function<HashMap<String, InterpreterDataType>, String> execute8 = (parameters)->{
            InterpreterDataType type1 = parameters.get("0");//Target
            InterpreterDataType type2 = parameters.get("1");//Reg
            InterpreterDataType type3 = parameters.get("2");//Replace
            String value1 = type1.getValue();
            String value2 = type2.getValue();
            String value3 = type3.getValue();
            String finalValue = value1.replaceFirst(value2, value3);

            return finalValue;
        };
        BuiltInFunctionDefinitionNode sub = new BuiltInFunctionDefinitionNode(execute8, false);
        functionSource.put(keyWord8, sub);

        /*
         * This lambda function returns a finalValue string. It gets the two parameters of the match keyword, and sees
         * if the parameters are the same by using the matches method.
         */
        String keyWord9 = "match";
        Function<HashMap<String, InterpreterDataType>, String> execute9 = (parameters)->{
            InterpreterDataType type1 = parameters.get("0");
            InterpreterDataType type2 = parameters.get("1");
            String value1 = type1.getValue();
            String value2 = type2.getValue();
            String finalValue = String.valueOf(Pattern.matches(value2, value1));

            return finalValue;
        };
        BuiltInFunctionDefinitionNode match = new BuiltInFunctionDefinitionNode(execute9, false);
        functionSource.put(keyWord9, match);

        /*
         * This lambda function returns a string called value which has the parameter of the getline statement keyword.
         */
        String keyWord10 = "getline";
        Function<HashMap<String, InterpreterDataType>, String> execute10 = (parameters)->{
            String value = String.valueOf(manageLine.SplitAndAssign());
            return value;
        };
        BuiltInFunctionDefinitionNode getLine = new BuiltInFunctionDefinitionNode(execute10, false);
        functionSource.put(keyWord10, getLine);

        /*
         * This lambda function returns a string called value which has the parameter of the next statement keyword.
         */
        String keyWord11 = "next";
        Function<HashMap<String, InterpreterDataType>, String> execute11 = (parameters)->{
            String value = String.valueOf(manageLine.SplitAndAssign());
            return value;
        };
        BuiltInFunctionDefinitionNode next = new BuiltInFunctionDefinitionNode(execute11, false);
        functionSource.put(keyWord11, next);

        /*
         * This lambda function returns an empty string and prints out the print statement parameters.
         */
        String keyWord12 = "print";
        Function<HashMap<String, InterpreterDataType>, String> execute12 = (parameters)->{
            int tracker = 0;
            String value = "";
            InterpreterArrayDataType type = (InterpreterArrayDataType) parameters.get(String.valueOf(tracker));

            for (int i = 0; i < type.GetArrayMap().size(); i++){
                value += type.GetArrayMap().get(String.valueOf(i)).getValue();
                tracker++;
            }
            System.out.print(value);
            return "";
        };
        BuiltInFunctionDefinitionNode print = new BuiltInFunctionDefinitionNode(execute12, true);
        functionSource.put(keyWord12, print);

        /*
         * This lambda function returns an empty string and prints out the printf statement parameters.
         */
        String keyWord13 = "printf";
        Function<HashMap<String, InterpreterDataType>, String> execute13 = (parameters)->{
            InterpreterArrayDataType type1 = (InterpreterArrayDataType) parameters.get("0");
            InterpreterArrayDataType type2 = (InterpreterArrayDataType) parameters.get("1");
            String value1;
            String value2;
            String finalValue1 = "";
            String finalValue2 = "";

            for (int i = 0; i < type1.GetArrayMap().size(); i++){
                value1 = type1.GetArrayMap().get(String.valueOf(i)).getValue();
                finalValue1 = value1;
                value2 = type2.GetArrayMap().get(String.valueOf(i)).getValue();
                finalValue2 += value2;
            }
            System.out.printf(finalValue1, finalValue2);
            return "";
        };
        BuiltInFunctionDefinitionNode printf = new BuiltInFunctionDefinitionNode(execute13, true);
        functionSource.put(keyWord13, printf);
    }

    /**
     * Helper method for testing SplitAndAssign.
     * @throws Exception
     */
    public boolean TestSplitAndAssign() throws Exception {
        return manageLine.SplitAndAssign();
    }

    /**
     * Helper method for testing built-in functions.
     * @param keyWord
     * @param functionParameters
     * @return
     */
    public String TestFunctions(String keyWord, HashMap<String, InterpreterDataType> functionParameters){
        BuiltInFunctionDefinitionNode test = (BuiltInFunctionDefinitionNode) functionSource.get(keyWord);
        return test.FunctionExecute(functionParameters);
    }

}
