import java.util.*;
public class BitStuffing {
static String bitStuff(String data) {
StringBuilder stuffed = new StringBuilder(); int count = 0, i = 0;
while (i < data.length()) { char bit = data.charAt(i); stuffed.append(bit);
if (bit == '1') { count++;
} else if (bit == '0' && count == 5) { stuffed.append('0');
count = 0;
} else {
count = 0;
} i++;
}
return stuffed.toString();
}
static String bitDestuff(String data) {
StringBuilder destuffed = new StringBuilder(); int count = 0, i = 0;
while (i < data.length()) { char bit = data.charAt(i); destuffed.append(bit); if (bit == '1') {
count++;
} else if (bit == '0' && count == 5) { i++;
count = 0; continue;
} else {
count = 0;
} i++;
}
return destuffed.toString();
}
static String addFlags(String data) {
return "01111110" + data + "01111110";
}
 
static String removeFlags(String data) { if (data.length() >= 16) {
return data.substring(8, data.length() - 8);
} else {
return "";
}
}
static List<String> frameData(String data, int frameSize) { List<String> frames = new ArrayList<>();
for (int i = 0; i < data.length(); i += frameSize) {
int end = Math.min(i + frameSize, data.length()); frames.add(data.substring(i, end));
}
return frames;
}
public static void main(String[] args) {
Scanner sc = new Scanner(System.in);
System.out.print("Enter the data bits (e.g., 110111...): "); String dataBits = sc.nextLine();
System.out.print("Enter the frame size (e.g., 10): "); int frameSize = sc.nextInt();
List<String> framed = frameData(dataBits, frameSize); List<String> stuffedFrames;
if (frameSize > 8) {
stuffedFrames = new ArrayList<>(); for (String f : framed) {
stuffedFrames.add(bitStuff(f));
}
} else {
stuffedFrames = framed;
}
List<String> flaggedFrames = new ArrayList<>(); for (String f : stuffedFrames) {
flaggedFrames.add(addFlags(f));
}
System.out.println("\n--- Transmitted Frames ---"); for (int i = 0; i < flaggedFrames.size(); i++) {
System.out.println("Frame " + (i + 1) + ": " + flaggedFrames.get(i));
}
StringBuilder receivedData = new StringBuilder(); for (String f : flaggedFrames) {
receivedData.append(removeFlags(f));
}
 
String destuffedData; if (frameSize > 8) {
destuffedData = bitDestuff(receivedData.toString());
} else {
destuffedData = receivedData.toString();
}
System.out.println("\n--- Receiver Side ---"); System.out.println("Destuffed Data: " + destuffedData);
System.out.println("Matches original: " + destuffedData.equals(dataBits));
}
}
