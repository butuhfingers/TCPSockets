package derek.rn.com.company;

import java.io.*;
import java.net.Socket;
import java.net.URL;

public class Fetch {

    public static final boolean DEBUG = true;

    public static void main(String[] args) throws Exception {
        //Check if args[0] is set
        //If not set a default
        //Grab the URL and the filename from the url
        String urlPath;
        urlPath = args.length > 0 ? args[0] : "http://cs.rocky.edu/~bennera/";
        URL url = new URL(urlPath);

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
        OutputStream httpOutput = httpSocket.getOutputStream();

        StringBuilder headerInfo = new StringBuilder();
        headerInfo.append("GET " + filePath + " HTTP/1.1\r\n");
        headerInfo.append("Host: " + url.getHost() + "\r\n");
        headerInfo.append("Connection: close\r\n");
        headerInfo.append("\r\n");

        if(DEBUG) {
            System.out.println(headerInfo.toString());
        }

        //Output to the server
        byte[] stringBytes = headerInfo.toString().getBytes();
        httpOutput.write(stringBytes);

        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        //Pull it into the byte array output stream
        int character;
        while((character = httpSocket.getInputStream().read()) != -1) {
            byteOutput.write(character);
        }

        //Convert bytes to a string
        byte[] response = byteOutput.toByteArray();
        String stringOutput = new String(response);
        int index = stringOutput.indexOf("\r\n\r\n");

        //Display our response header
        String header = new String(response, 0, index);
        System.out.println(header);

        while(response[index] == '\r' || response[index] == '\n'){
            index++;
        }


        //Set the output stream of the file
        File newFile = new File(requestedFile);
        newFile.createNewFile();
        OutputStream fileOutputStream = new FileOutputStream(newFile);
        //Go through the file and write to it
        fileOutputStream.write(response, index, (response.length - index));

        fileOutputStream.close();
        httpSocket.close();
        httpInput.close();
        httpOutput.close();
    }
}
