package org.example;
import java.util.List;
import java.util.Scanner;


public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter the expression: ");
        String expressionText = in.nextLine();
        List<token> lexemes = token_list.lexAnalyze(expressionText);
        token_list lexemeBuffer = new token_list(lexemes);
        System.out.println(token_list.expr(lexemeBuffer));
    }
}