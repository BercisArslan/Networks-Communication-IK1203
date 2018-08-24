package tcpclient;
import java.net.*;
import java.io.*;

public class TCPClient {

    
    public static String askServer(String hostname, int port, String ToServer) throws  IOException {
        if (ToServer == null) {
            return askServer(hostname, port);
        }
        //create client socket
        Socket clientSocket = new Socket(hostname,port);
        //create output stream attatched to socket
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        //create input stream attached to socket
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        //send line to server
        outToServer.writeBytes(ToServer + "\n");
        //read line from server
        String temp = null;
        StringBuilder sb = new StringBuilder("");
        //create stopwatch
        clientSocket.setSoTimeout(1000);
        try {
            while (((temp = inFromServer.readLine()) != null) && sb.length() <= 10000) {
                sb.append(temp);
            }
        }catch(java.net.SocketTimeoutException e){
            return sb.toString();
        }
        clientSocket.close();
        return sb.toString();
    }

    public static String askServer(String hostname, int port) throws  IOException {

        Socket clientSocket = new Socket(hostname,port);
        //DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String temp;
        StringBuilder sb = new StringBuilder("");
        clientSocket.setSoTimeout(1000);
        try {
            while (((temp = inFromServer.readLine()) != null) && sb.length() <= 10000) {

                sb.append(temp);
            }
            } catch(java.net.SocketTimeoutException e){
                return sb.toString();
            }
        clientSocket.close();
        return sb.toString();
    }
}

