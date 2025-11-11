import java.util.*;

class ThreeAC {
    String op, arg1, arg2, result;
}

public class Main {

    // Function to assign registers
    public static String getRegister(String var, Map<String, String> regMap, int[] regCount) {
        if (!regMap.containsKey(var)) {
            regMap.put(var, "R" + regCount[0]);
            regCount[0]++;
        }
        return regMap.get(var);
    }

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

            if (parts.length == 5) {    // expression (t1 = a + b)
                stmt.op = parts[3];
                stmt.arg2 = parts[4];
            } else { // assignment (t1 = a)
                stmt.op = "=";
                stmt.arg2 = "";
            }
            code.add(stmt);
        }

        System.out.println("\nGenerated Assembly Code:\n");

        Map<String, String> regMap = new HashMap<>();
        int[] regCount = {0}; // Using array to modify value inside method

        for (ThreeAC stmt : code) {

            String r1 = getRegister(stmt.arg1, regMap, regCount);
            String r3 = getRegister(stmt.result, regMap, regCount);

            if (stmt.op.equals("+")) {
                String r2 = getRegister(stmt.arg2, regMap, regCount);
                System.out.println("MOV " + r3 + ", " + r1);
                System.out.println("ADD " + r3 + ", " + r2);
            } 
            else if (stmt.op.equals("-")) {
                String r2 = getRegister(stmt.arg2, regMap, regCount);
                System.out.println("MOV " + r3 + ", " + r1);
                System.out.println("SUB " + r3 + ", " + r2);
            } 
            else if (stmt.op.equals("*")) {
                String r2 = getRegister(stmt.arg2, regMap, regCount);
                System.out.println("MOV " + r3 + ", " + r1);
                System.out.println("MUL " + r3 + ", " + r2);
            } 
            else if (stmt.op.equals("/")) {
                String r2 = getRegister(stmt.arg2, regMap, regCount);
                System.out.println("MOV " + r3 + ", " + r1);
                System.out.println("DIV " + r3 + ", " + r2);
            } 
            else if (stmt.op.equals("=")) {
                System.out.println("MOV " + r3 + ", " + r1);
            }
        }
    }
}
