import java.util.Scanner; public class CRC {
public static String xorOp(String a, String b) { StringBuilder result = new StringBuilder(); for (int i = 1; i < b.length(); i++) {
result.append(a.charAt(i) == b.charAt(i) ? '0' : '1');
}
return result.toString();
}
public static String mod2div(String dividend, String divisor) { int pick = divisor.length();
String tmp = dividend.substring(0, pick); int n = dividend.length();
while (pick < n) {
if (tmp.charAt(0) == '1') {
tmp = xorOp(divisor, tmp) + dividend.charAt(pick);
} else {
tmp = xorOp("0".repeat(pick), tmp) + dividend.charAt(pick);
}
pick++;
}
if (tmp.charAt(0) == '1') { tmp = xorOp(divisor, tmp);
} else {
tmp = xorOp("0".repeat(pick), tmp);
}
return tmp;
}
public static String encodeData(String data, String key) { int keyLen = key.length();
String appendedData = data + "0".repeat(keyLen - 1); String remainder = mod2div(appendedData, key); return data + remainder;
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in); System.out.print("Enter the dataword (binary): "); String data = sc.nextLine();
System.out.print("Enter the divisor (binary): "); String key = sc.nextLine();
String codeword = encodeData(data, key);
System.out.println("Generated Codeword: " + codeword);
 
System.out.print("Enter the received codeword (binary): "); String received = sc.nextLine();
String remainder = mod2div(received, key); if (remainder.contains("1")) {
System.out.println("Error detected in received codeword");
} else {
System.out.println("No error in received codeword");
}
sc.close();
}
}
