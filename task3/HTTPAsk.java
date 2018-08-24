import java.net.*;
import java.io.*;

//http://localhost:8000/ask?hostname=time.nist.gov&port=13

public class HTTPAsk {
    public static void main(String[] args) throws IOException {
        try {

            ServerSocket welcomeSocket = new ServerSocket(Integer.parseInt(args[0]));

            while (true) {
                String clientSentence;
                StringBuilder sb = new StringBuilder();
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader infromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outtoClient = new DataOutputStream(connectionSocket.getOutputStream());
                connectionSocket.setSoTimeout(5000);
                while ((clientSentence = infromClient.readLine()) != null && clientSentence.length() != 0) {
                    sb.append(clientSentence + "\n");
                }

                String data1;
                String hostname1 = null;
                int port1;
                String[] port = null;
                String badRequest = "HTTP/1.1 400 Bad Request";
                String notFound = "HTTP/1.1 404 Not Found";
                String OK = "HTTP/1.1 200 OK";


                   String[] hostname = sb.toString().split("(?<==)");
                   hostname = hostname[1].split("(?=&)");
                   hostname1 = hostname[0];

                   port = sb.toString().split("(?<==)");
                   port = port[2].split("\\s+|&");

                try {
                    port1 = Integer.parseInt(port[0]);

                    try {
                    String[] data = sb.toString().split("(?<==)");
                    data = data[3].split("\\s");
                    data1 = data[0];
                }catch(java.lang.ArrayIndexOutOfBoundsException ex){
                    data1 = "";
                    }
                    String fromServer = askServer(hostname1, port1, data1);
                    String response = OK + "\r\n" + "Content-Type: text/plain" + "\r\n" + "Content-Length: " + fromServer.length() + "\r\n\r\n";

                    outtoClient.writeBytes(response + fromServer);
                    outtoClient.flush();
                    connectionSocket.close();
                } catch (java.lang.NumberFormatException ex) {
                }
            }
        }catch (java.io.IOException e) { }
    }
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
                sb.append(temp + "\n");
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

                sb.append(temp + "\n");
            }
        } catch(java.net.SocketTimeoutException e){
            return sb.toString();
        }
        clientSocket.close();
        return sb.toString();
    }
}

