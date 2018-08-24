import java.net.*;
import java.io.*;


public class HTTPEcho {
    public static void main(String[] args) throws IOException {
        //welcome socket
        ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));

    try {
        while (true) {
            String clientSentence;
            StringBuilder sb = new StringBuilder();
            Socket connectionSocket = welcomeSocket.accept();
            //create input stream attached to socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            //create output stream attached to socket
            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            //read inline from socket
            connectionSocket.setSoTimeout(5000);
            try {

                while ((clientSentence = inFromClient.readLine()) != null && clientSentence.length() != 0) {
                    sb.append(clientSentence + "\n");
                }
            }catch(java.net.SocketTimeoutException k){ }

            String response = "HTTP/1.1 200 OK"+ "\r\n"+ "Content-Type: text/plain" + "\r\n" + "Content-Length: " + sb.length() + "\r\n\r\n";
            outToClient.writeBytes(response + sb.toString()+ "\n");
            outToClient.flush();
            connectionSocket.close();
        }
    }catch(java.io.IOException e){ }
    }
}

