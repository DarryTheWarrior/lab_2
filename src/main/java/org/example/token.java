package org.example;

/**
 * tokens which can be used
 */
enum tokenType {
    LEFT_BRACKET, RIGHT_BRACKET,
    OP_PLUS, OP_MINUS, OP_MUL, OP_DIV,
    NUMBER, NAME, COMMA,
    EOF;
}
/**
 * Container class
 */

public class token {
    tokenType type;
    String value;


    /**
     * Constructor - creating a new object with specific values
     * @param type token which can be used
     * @param value processed token
     */
    public token(tokenType type, String value) {
        this.type = type;
        this.value = value;
    }
    /**
     * Constructor - creating a new object with specific values
     * @param type token which can be used
     * @param value processed token
     */
    public token(tokenType type, Character value) {
        this.type = type;
        this.value = value.toString();
    }

    /**
     * Display method
     * @return line with information
     */
    public String toString() {
        return "token{" + "type=" + type + ", value='" + value + '\'' + '}';
    }

}