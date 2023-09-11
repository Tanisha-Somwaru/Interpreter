public class StringHandler {
    // Variable declarations.
    private String document;
    private int index;

    /**
     * Constructor.
     * @param doc
     */
    public StringHandler(String doc){
        document = doc;
        index = 0;
    }

    /**
     * This method looks “i” characters ahead and returns that character; doesn’t move the index.
     * @param i
     * @return character
     */
    public char Peek(int i){
        char character = document.charAt(i + index);

        return character;
    }

    /**
     * This method returns a string of the next “i” characters but doesn't move the index.
     * @param i
     * @return stringCharacters
     */
    public String PeekString(int i){
        String stringCharacters = document.substring(index, index + i);

        return stringCharacters;
    }

    /**
     * This method returns the next character and moves the index.
     * @return nextCharacter
     */
    public char GetChar(){
        char nextCharacter = document.charAt(index + 1);
        index++;

        return nextCharacter;
    }

    /**
     * This method moves the index ahead “i” positions.
     * @param i
     */
    public void Swallow(int i){
        index += i;
    }

    /**
     * This method returns true if we are at the end of the document.
     * @return true
     */
    public boolean isDone(){
        if (index >= document.length()){
            return true;
        } else {
            return false;
        }

    }

    /**
     * This method returns the rest of the document as a string.
     * @return remainder
     */
    public String Remainder() {
        return document.substring(index);
    }
}

