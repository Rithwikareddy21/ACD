import java.util.Scanner;

abstract class ASTNode {}

class Num extends ASTNode {
    int value;
    Num(int value) { this.value = value; }
    public String toString() { return Integer.toString(value); }
}

class BinOp extends ASTNode {
    ASTNode left;
    String op;
    ASTNode right;
    BinOp(ASTNode left, String op, ASTNode right) {
        this.left = left;
        this.op = op;
        this.right = right;
    }
    public String toString() { return op; }
}

public class ASTTreeConsole {

    static String input;
    static int index;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter Expression: ");
        input = sc.nextLine().replaceAll("\\s+","");
        index = 0;

        ASTNode ast = parseExpr();
        System.out.println("\nAST (Parenthesized Form): " + toText(ast));

        System.out.println("\nAST Tree Structure:");
        printTree(ast, "", true);
    }

    // Grammar: expr -> term ((+|-) term)*
    static ASTNode parseExpr() {
        ASTNode node = parseTerm();
        while (peek() == '+' || peek() == '-') {
            char op = next();
            ASTNode right = parseTerm();
            node = new BinOp(node, String.valueOf(op), right);
        }
        return node;
    }

    // Grammar: term -> factor ((*|/) factor)*
    static ASTNode parseTerm() {
        ASTNode node = parseFactor();
        while (peek() == '*' || peek() == '/') {
            char op = next();
            ASTNode right = parseFactor();
            node = new BinOp(node, String.valueOf(op), right);
        }
        return node;
    }

    // factor -> NUMBER | '(' expr ')'
    static ASTNode parseFactor() {
        if (peek() == '(') {
            next();
            ASTNode node = parseExpr();
            next();
            return node;
        }
        return new Num(parseNumber());
    }

    static int parseNumber() {
        int num = 0;
        while (Character.isDigit(peek())) {
            num = num * 10 + (next() - '0');
        }
        return num;
    }

    static char peek() {
        if (index < input.length()) return input.charAt(index);
        return '\0';
    }

    static char next() {
        return input.charAt(index++);
    }

    static String toText(ASTNode node) {
        if (node instanceof Num) return node.toString();
        BinOp b = (BinOp) node;
        return "(" + toText(b.left) + " " + b.op + " " + toText(b.right) + ")";
    }

    // -------- Pretty Tree Print --------
    static void printTree(ASTNode node, String prefix, boolean isLast) {
        System.out.print(prefix + (isLast ? "└── " : "├── "));

        if (node instanceof Num) {
            System.out.println(((Num)node).value);
        } else {
            BinOp b = (BinOp)node;
            System.out.println(b.op);
            printTree(b.left, prefix + (isLast ? "    " : "│   "), false);
            printTree(b.right, prefix + (isLast ? "    " : "│   "), true);
        }
    }
}
