import java.util.*;

class ThreeAC {
    String result, arg1, arg2, op;
}

public class ThreeAddressToAssembly {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of 3-address code statements: ");
        int n = sc.nextInt();
        sc.nextLine();

        List<ThreeAC> code = new ArrayList<>();

        System.out.println("Enter each 3-address code statement (e.g., t1 = a + b):");
        for (int i = 0; i < n; i++) {
            System.out.print("Statement " + (i + 1) + ": ");
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            ThreeAC stmt = new ThreeAC();
            stmt.result = parts[0];
            stmt.arg1 = parts[2];

            if (parts.length == 5) {
                stmt.op = parts[3];
                stmt.arg2 = parts[4];
            } else {
                stmt.op = "=";
                stmt.arg2 = "";
            }

            code.add(stmt);
        }

        System.out.println("\nGenerated Assembly Code:\n");

        for (ThreeAC stmt : code) {

            if (stmt.op.equals("+")) {
                System.out.println("MOV R, " + stmt.arg1);
                System.out.println("ADD R, " + stmt.arg2);
                System.out.println("MOV " + stmt.result + ", R");
            }
            else if (stmt.op.equals("-")) {
                System.out.println("MOV R, " + stmt.arg1);
                System.out.println("SUB R, " + stmt.arg2);
                System.out.println("MOV " + stmt.result + ", R");
            }
            else if (stmt.op.equals("*")) {
                System.out.println("MOV R, " + stmt.arg1);
                System.out.println("MUL R, " + stmt.arg2);
                System.out.println("MOV " + stmt.result + ", R");
            }
            else if (stmt.op.equals("/")) {
                System.out.println("MOV R, " + stmt.arg1);
                System.out.println("DIV R, " + stmt.arg2);
                System.out.println("MOV " + stmt.result + ", R");
            }
            else if (stmt.op.equals("=")) {
                System.out.println("MOV " + stmt.result + ", " + stmt.arg1);
            }
        }
    }
}
