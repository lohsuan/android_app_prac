import java.io.*;
public class BufferedReaderDemo {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("type anything: ");
        String text = bufferedReader.readLine();
        System.out.println("your input: " + text);
    }
}