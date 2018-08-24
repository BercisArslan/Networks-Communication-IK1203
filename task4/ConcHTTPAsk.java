import java.net.*;
import java.io.*;

//http://localhost:8000/ask?hostname=time.nist.gov&port=13

public class ConcHTTPAsk {
    public static void main(String[] args) throws IOException {
        try {
            ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));
            while (true) {
                Socket connectionSocket = welcomeSocket.accept();
                MyRunnable mr = new MyRunnable(connectionSocket);
                new Thread(mr).start();
            }
        } catch (IOException ex) {
        }
    }
}