package derek.rn.com.company;

import java.io.*;
import java.net.Socket;
import java.net.URL;

public class Fetch {

    public static final boolean DEBUG = true;

    public static void main(String[] args) throws Exception {
        //Grab the URL and the filename from the url
        String fileName = args[0];
        URL url = new URL(fileName);

        //Check if it is an HTTP request
        if (!url.getProtocol().toLowerCase().equals("http")) {
            return;
        }

        //Get the path of file
        String filePath = url.getFile();
        if(DEBUG){
            System.out.println("File path: " + filePath);
        }
        //Get the file of the url
        String requestedFile = filePath.substring(filePath.lastIndexOf('/') + 1, filePath.length());

        if(DEBUG) {
            System.out.println("Requested File: " + requestedFile);
        }
        //Check if the requested file is empty
        //If so, set it to an index.html file
        if(requestedFile.equals("")) {
            requestedFile = "index.html";
        }

        System.out.println("Output will be saved as: " + requestedFile);

        //Start the output and the input streams
        Socket httpSocket = new Socket(url.getHost(), url.getDefaultPort());

        InputStream httpInput = httpSocket.getInputStream();
        DataOutputStream httpOutput = new DataOutputStream(httpSocket.getOutputStream());

        //Set the output stream of the file
//        OutputStream fileOutputStream = new FileOutputStream();

        StringBuilder headerInfo = new StringBuilder();
        headerInfo.append("GET " + filePath + " HTTP/1.1\r\n");
        headerInfo.append("Host: " + url.getHost() + "\r\n");
        headerInfo.append("Connection: close\r\n");

        if(DEBUG) {
            System.out.println(headerInfo.toString());
        }

        //Output to the server
        byte[] stringBytes = headerInfo.toString().getBytes();
        httpOutput.write(stringBytes);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // pull the entire response into a ByteArrayOutputStream
        int ch;
        while ((ch = httpSocket.getInputStream().read()) != -1) {
            out.write(ch);
        }
    }
}
