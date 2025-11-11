import java.util.*;
import java.util.regex.*;

public class threeaddresscoden {

    // Step 1: Tokenize expression
    public static List<String> tokenize(String expr) {
        List<String> tokens = new ArrayList<>();
        Matcher matcher = Pattern.compile("[a-zA-Z_][\\w]*|\\d+|[()+\\-*/]").matcher(expr);

        while (matcher.find()) {
            tokens.add(matcher.group());
        }
        return tokens;
    }

    // Operator precedence
    static final Map<String, Integer> precedence = new HashMap<>() {{
        put("+", 1);
        put("-", 1);
        put("*", 2);
        put("/", 2);
    }};

    // Step 2: Convert Infix → Postfix
    public static List<String> infixToPostfix(List<String> tokens) {
        Stack<String> stack = new Stack<>();
        List<String> output = new ArrayList<>();

        for (String token : tokens) {
            if (token.matches("[a-zA-Z_][\\w]*|\\d+")) { 
                output.add(token);
            }
            else if (token.equals("(")) {
                stack.push(token);
            }
            else if (token.equals(")")) {
                while (!stack.isEmpty() && !stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop(); // remove "("
            }
            else { // operator
                while (!stack.isEmpty() && !stack.peek().equals("(") &&
                        precedence.get(stack.peek()) >= precedence.get(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }
        while (!stack.isEmpty()) output.add(stack.pop());

        return output;
    }

    // Step 3: Generate TAC Structures
    public static void generateTAC(List<String> postfix) {
        Stack<String> stack = new Stack<>();
        List<String[]> quadruples = new ArrayList<>();
        List<String[]> triples = new ArrayList<>();
        int tempVarCount = 1;

        for (String token : postfix) {
            if (token.matches("[a-zA-Z_][\\w]*|\\d+"))
                stack.push(token);
            else {
                String op2 = stack.pop();
                String op1 = stack.pop();
                String temp = "t" + tempVarCount;

                // Quadruple
                quadruples.add(new String[]{token, op1, op2, temp});

                // Triple
                triples.add(new String[]{token, op1, op2});

                stack.push(temp);
                tempVarCount++;
            }
        }

        printQuadruples(quadruples);
        printTriples(triples);
        printIndirectTriples(triples.size());
    }

    public static void printQuadruples(List<String[]> q) {
        System.out.println("\nQuadruples:");
        System.out.printf("%-5s %-3s %-8s %-8s %-6s\n", "Idx", "Op", "Arg1", "Arg2", "Result");
        for (int i = 0; i < q.size(); i++)
            System.out.printf("%-5d %-3s %-8s %-8s %-6s\n", i, q.get(i)[0], q.get(i)[1], q.get(i)[2], q.get(i)[3]);
    }

    public static void printTriples(List<String[]> t) {
        System.out.println("\nTriples:");
        System.out.printf("%-5s %-3s %-8s %-8s\n", "Idx", "Op", "Arg1", "Arg2");
        for (int i = 0; i < t.size(); i++)
            System.out.printf("%-5d %-3s %-8s %-8s\n", i, t.get(i)[0], t.get(i)[1], t.get(i)[2]);
    }

    public static void printIndirectTriples(int size) {
        System.out.println("\nIndirect Triples:");
        System.out.printf("%-5s %-10s\n", "Idx", "PointsTo");
        for (int i = 0; i < size; i++)
            System.out.printf("%-5d %-10d\n", i, i);
    }

    // Main
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter expression: ");
        String expr = sc.nextLine();

        List<String> tokens = tokenize(expr);
        List<String> postfix = infixToPostfix(tokens);

        generateTAC(postfix);
    }
}
