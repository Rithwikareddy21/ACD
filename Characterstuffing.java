import java.util.Scanner;
public class Characterstuffing {
public static String stuff(String s, String f, String e) { StringBuilder sd = new StringBuilder(f);
for (int i = 0; i < s.length(); i++) {
if (i + f.length() <= s.length() && s.substring(i, i + f.length()).equalsIgnoreCase(f)) { sd.append(e).append(f);
i += f.length() - 1;
} else if (i + e.length() <= s.length() && s.substring(i, i + e.length()).equalsIgnoreCase(e)) {
sd.append(e).append(e);
i += e.length() - 1;
} else {
sd.append(s.charAt(i));
}
}
sd.append(f); return sd.toString();
}
public static String destuff(String s, String f, String e) { StringBuilder dd = new StringBuilder();
s = s.substring(f.length(), s.length() - f.length()); for (int i = 0; i < s.length(); i++) {
if (i + e.length() + f.length() <= s.length() && s.substring(i, i + e.length() + f.length()).equalsIgnoreCase(e + f)) {
dd.append(f);
i += e.length() + f.length() - 1;
} else if (i + e.length() + e.length() <= s.length() && s.substring(i, i + e.length() + e.length()).equalsIgnoreCase(e + e)) {
dd.append(e);
i += e.length() + e.length() - 1;
} else {
dd.append(s.charAt(i));
}
}
return dd.toString();
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in); String f = "FLAG", e = "ESC"; System.out.print("Enter packet: ");
String s = sc.next();
 
String sd = stuff(s, f, e);
String dd = destuff(sd, f, e);
System.out.println("Transmitted Data (Stuffed Data):\n" + sd); System.out.println("Receiver Data (De-Stuffed Data):\n" + dd); System.out.println("Matches Original: " + s.equals(dd));
sc.close();
}
}
