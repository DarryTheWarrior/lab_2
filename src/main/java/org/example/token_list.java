package org.example;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Container class with list of tokens
 */
public class token_list {
    private int pos;
    public List<token> token;

    static HashMap<String, Function> functionMap = Function.getFunctionMap();

    /**
     * Constructor - creating a new object with specific values
     *
     * @param token a list of expression characters
     */
    public token_list(List<token> token) {
        this.token = token;
    }

    /**
     * method for moving to the next position
     *
     * @return next element of the list
     */
    public token next() {
        return token.get(pos++);
    }

    /**
     * method for changing position to the previous
     */
    public void back() {
        pos--;
    }

    /**
     * method for getting position
     * @return current position
     */
    public int getPos() {
        return pos;
    }

    /**
     * Input expression analysing method
     *
     * @param expText line with expression
     * @return character list
     */
    public static List<token> lexAnalyze(String expText) {
        ArrayList<token> token = new ArrayList<>();
        int pos = 0;
        while (pos < expText.length()) {
            char c = expText.charAt(pos);
            switch (c) {
                case '(':
                    token.add(new token(tokenType.LEFT_BRACKET, c));
                    pos++;
                    continue;
                case ')':
                    token.add(new token(tokenType.RIGHT_BRACKET, c));
                    pos++;
                    continue;
                case '+':
                    token.add(new token(tokenType.OP_PLUS, c));
                    pos++;
                    continue;
                case '-':
                    token.add(new token(tokenType.OP_MINUS, c));
                    pos++;
                    continue;
                case '*':
                    token.add(new token(tokenType.OP_MUL, c));
                    pos++;
                    continue;
                case '/':
                    token.add(new token(tokenType.OP_DIV, c));
                    pos++;
                    continue;
                case ',':
                    token.add(new token(tokenType.COMMA, c));
                    pos++;
                    continue;
                default:
                    if (c <= '9' && c >= '0') {
                        StringBuilder sb = new StringBuilder();
                        do {
                            sb.append(c);
                            pos++;
                            if (pos >= expText.length()) {
                                break;
                            }
                            c = expText.charAt(pos);
                        } while (c <= '9' && c >= '0');
                        token.add(new token(tokenType.NUMBER, sb.toString()));
                    } else {
                        if (c != ' ') {
                            if (c >= 'a' && c <= 'z'
                                    || c >= 'A' && c <= 'Z') {
                                StringBuilder sb = new StringBuilder();
                                do {
                                    sb.append(c);
                                    pos++;
                                    if (pos >= expText.length()) {
                                        break;
                                    }
                                    c = expText.charAt(pos);
                                } while (c >= 'a' && c <= 'z'
                                        || c >= 'A' && c <= 'Z');

                                if (functionMap.containsKey(sb.toString())) {
                                    token.add(new token(tokenType.NAME, sb.toString()));
                                } else {
                                    throw new RuntimeException("Unexpected character: " + c);
                                }
                            }
                        } else {
                            pos++;
                        }
                    }
            }
        }
        token.add(new token(tokenType.EOF, ""));
        return token;
    }

    /**
     * Method for finding the value of an expression
     *
     * @param token0 a list of expression characters
     * @return expression value
     */
    public static int expr(token_list token0) {
        token token1 = token0.next();
        if (token1.type == tokenType.EOF) {
            return 0;
        } else {
            token0.back();
            return plusminus(token0);
        }
    }

    /**
     * Method for finding the result between adjacent numbers
     *
     * @param token0 a list of expression characters
     * @return calculation of an expression between adjacent numbers according to the token between them
     */
    public static int plusminus(token_list token0) {
        int value = multdiv(token0);
        while (true) {
            token token1 = token0.next();
            switch (token1.type) {
                case OP_PLUS:
                    value += multdiv(token0);
                    break;
                case OP_MINUS:
                    value -= multdiv(token0);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                    token0.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + token1.value
                            + " at position: " + token0.getPos());
            }
        }
    }

    /**
     * Method for finding the result between adjacent numbers
     *
     * @param token0 a list of expression characters
     * @return calculation of an expression between adjacent numbers according to the token between them
     */
    public static int multdiv(token_list token0) {
        int value = factor(token0);
        while (true) {
            token token1 = token0.next();
            switch (token1.type) {
                case OP_MUL:
                    value *= factor(token0);
                    break;
                case OP_DIV:
                    value /= factor(token0);
                    break;
                case EOF:
                case RIGHT_BRACKET:
                case COMMA:
                case OP_PLUS:
                case OP_MINUS:
                    token0.back();
                    return value;
                default:
                    throw new RuntimeException("Unexpected token: " + token1.value
                            + " at position: " + token0.getPos());
            }
        }
    }

    /**
     * Recursive descent method
     *
     * @param token0 a list of expression characters
     * @return calculation of an expression between adjacent numbers according to the token between them
     */
    public static int factor(token_list token0) {
        token token1 = token0.next();
        switch (token1.type) {
            case NAME:
                token0.back();
                return func(token0);
            case OP_MINUS:
                int value = factor(token0);
                return -value;
            case NUMBER:
                return Integer.parseInt(token1.value);
            case LEFT_BRACKET:
                value = plusminus(token0);
                token1 = token0.next();
                if (token1.type != tokenType.RIGHT_BRACKET) {
                    throw new RuntimeException("Unexpected token: " + token1.value
                            + " at position: " + token0.getPos());
                }
                return value;
            default:
                throw new RuntimeException("Unexpected token: " + token1.value
                        + " at position: " + token0.getPos());
        }
    }
    /**
     * Recursive descent method
     *
     * @param token_list a list of expression characters
     * @return calculation of a function with given name and arguments
     */
    public static int func(token_list token_list) {
        String name = token_list.next().value;
        token token = token_list.next();

        if (token.type != tokenType.LEFT_BRACKET) {
            throw new RuntimeException("Wrong function call syntax at " + token.value);
        }

        ArrayList<Integer> args = new ArrayList<>();

        token = token_list.next();
        if (token.type != tokenType.RIGHT_BRACKET) {
            token_list.back();
            do {
                args.add(expr(token_list));
                token = token_list.next();

                if (token.type != tokenType.COMMA && token.type != tokenType.RIGHT_BRACKET) {
                    throw new RuntimeException("Wrong function call syntax at " + token.value);
                }

            } while (token.type == tokenType.COMMA);
        }
        return functionMap.get(name).apply(args);
    }
}
