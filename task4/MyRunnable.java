import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class MyRunnable implements Runnable {

    public Socket connectionSocket = new Socket();
    public MyRunnable(Socket connectionSocket){
        this.connectionSocket = connectionSocket;
    }

    public void run() {
        String clientSentence;
        StringBuilder sb = new StringBuilder();
        BufferedReader infromClient = null;
        try {
            infromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            DataOutputStream outtoClient = new DataOutputStream(connectionSocket.getOutputStream());

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
                } catch (java.lang.ArrayIndexOutOfBoundsException ex) {
                    data1 = "";
                }
                String fromServer = askServer(hostname1, port1, data1);
                String response = OK + "\r\n" + "Content-Type: text/plain" + "\r\n" + "Content-Length: " + fromServer.length() + "\r\n\r\n";

                outtoClient.writeBytes(response + fromServer);
                outtoClient.flush();
                connectionSocket.close();
            } catch (java.lang.NumberFormatException ex) {
            } catch (IOException e) {
            }
        }catch(IOException ex){}
    }
public static String askServer(String hostname, int port, String ToServer) throws  IOException {
        if (ToServer == null) {
        return askServer(hostname, port);
        }
        Socket clientSocket = new Socket(hostname,port);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        outToServer.writeBytes(ToServer + "\n");
        String temp = null;
        StringBuilder sb = new StringBuilder("");
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

    public static String askServer(String hostname, int port) throws IOException {

        Socket clientSocket = new Socket(hostname, port);
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String temp;
        StringBuilder sb = new StringBuilder("");
        clientSocket.setSoTimeout(1000);
        try {
            while (((temp = inFromServer.readLine()) != null) && sb.length() <= 10000) {
            sb.append(temp + "\n");
        }
    } catch (java.net.SocketTimeoutException e) {
        return sb.toString();
        }
        clientSocket.close();
        return sb.toString();
    }

}
