package com.company;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class FileServer {
    static final int LISTENING_PORT = 3210;


    public static void main(String[] args) {

        File directory;        // The directory from which the server
        //    gets the files that it serves.

        ServerSocket listener; // Listens for connection requests.

        Socket connection;     // A socket for communicating with a client.
        MyFileReader fileReader;

      /* Check that there is a command-line argument.
         If not, print a usage message and end. */

//        if (args.length == 0) {
//            System.out.println("Usage:  java FileServer <directory>");
//            return;
//        }
        //D:\MSCS\OS\OS_PROJECT\Server_Memory\

      /* Get the directory name from the command line, and make
         it into a file object.  Check that the file exists and
         is in fact a directory. */

        directory = new File("D:\\MSCS\\OS\\OS_PROJECT\\Server_Memory\\");
        if ( ! directory.exists() ) {
            System.out.println("Specified directory does not exist.");
            return;
        }
        if (! directory.isDirectory() ) {
            System.out.println("The specified file is not a directory.");
            return;
        }

      /* Listen for connection requests from clients.  For
         each connection, call the handleConnection() method
         to process it.  The server runs until the program
         is terminated, for example by a CONTROL-C. */

        try {
            listener = new ServerSocket(LISTENING_PORT);
            System.out.println("Listening on port " + LISTENING_PORT);
            while (true) {
                connection = listener.accept();
                String clientAddress = connection.getInetAddress().getHostAddress();
                System.out.println("a connection accepted from"+clientAddress);
                handleConnection(directory,connection);
            }
        }
        catch (Exception e) {
            System.out.println("Server shut down unexpectedly.");
            System.out.println("Error:  " + e);
            return;
        }

    } // end main()


    /**
     * This method processes process the connection with one client.
     * It creates streams for communicating with the client,
     * reads a command from the client, and carries out that
     * command.  The connection is also logged to standard output.
     * An output beginning with ERROR indicates that a network
     * error occurred.  A line beginning with OK means that
     * there was no network error, but does not imply that the
     * command from the client was a legal command.
     */
    private static void handleConnection(File directory, Socket connection) {
        Scanner incoming;       // For reading data from the client.
        PrintWriter outgoing;   // For transmitting data to the client.
        String command = "Command not read";
        try {
            incoming = new Scanner( connection.getInputStream() );
            outgoing = new PrintWriter( connection.getOutputStream() );
            command = incoming.nextLine();
            System.out.println(command);
            if (command.equals("index")) {
                sendIndex(directory, outgoing);
            }
            else if (command.startsWith("get")){
                String fileName = command.substring(3).trim();
                //sendFile(fileName, directory, outgoing);
                sendFile(fileName, directory, outgoing,connection);
            }else if (command.startsWith("Post")){
                String fileName = command.substring(4).trim();
                //sendFile(fileName, directory, outgoing);
                uploadFile(fileName, directory, outgoing,connection);
            }else if (command.startsWith("del")){
                String fileName = command.substring(3).trim();
                //sendFile(fileName, directory, outgoing);
                deleteFile(fileName, directory, outgoing,connection);
            }
            else {
                outgoing.println("unsupported command");
                outgoing.flush();
            }
            System.out.println("OK    " + connection.getInetAddress()
                    + " " + command);
        }
        catch (Exception e) {
            System.out.println("ERROR " + connection.getInetAddress()
                    + " " + command + " " + e);
        }
        finally {
            try {
                connection.close();
            }
            catch (IOException e) {
            }
        }
    }

    /**
     * This is called by the run() method in response to an "index" command
     * from the client.  Send the list of files in the server's directory.
     */
    private static void sendIndex(File directory, PrintWriter outgoing) throws Exception {
        String[] fileList = directory.list();
        for (int i = 0; i < fileList.length; i++)
            outgoing.println(fileList[i]);
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error while transmitting data.");
    }

    /**
     * This is called by the run() command in response to "get <fileName>"
     * command from the client.  If the file doesn't exist, send the message "error".
     * Otherwise, send the message "ok" followed by the contents of the file.
     */
    private static void sendFile(String fileName, File directory, PrintWriter outgoing, Socket connection) throws Exception {
        File file = new File(directory,fileName);

        if ( (! file.exists()) || file.isDirectory() ) {
            // (Note:  Don't try to send a directory, which
            // shouldn't be there anyway.)
            outgoing.println("error");
        }
        else {
            // Read file from disk
            //
            MyFileReader fileReader = new MyFileReader();
            byte[] data = fileReader.readFile(directory.getAbsolutePath()+fileName);
            for (byte i : data) {
                connection.getOutputStream().write(i);
            }
            System.out.println("\r\nSent " + data.length + " bytes to server.");
            //outgoing.println("ok");
//            BufferedReader fileIn = new BufferedReader( new FileReader(file) );
//            while (true) {
//                // Read and send lines from the file until
//                // an end-of-file is encountered.
//                String line = fileIn.readLine();
//                if (line == null)
//                    break;
//                outgoing.println(line);
//            }
        }
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error while transmitting data.");
    }
    private static void uploadFile(String fileName, File directory, PrintWriter outgoing, Socket connection) throws Exception {
        File file = new File(directory,fileName);
        MyFileWriter fw = new MyFileWriter();
        if ( (file.exists()) || file.isDirectory() ) {
            // (Note:  Don't try to send a directory, which
            // shouldn't be there anyway.)
            outgoing.println("error");
        }
        else {
            // Read file from disk
            //
            BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
            //String fileName = "image-" + System.currentTimeMillis() + ".jpg";

            int fileSize = fw.writeFile(directory.getAbsolutePath()+"/"+fileName, bis);
            bis.close();

            //
            // Close socket connection
            //
            connection.close();
            System.out.println("\r\nWrote " + fileSize + " bytes to file " + fileName);
            //System.out.println("\r\nSent " + data.length + " bytes to server.");
            //outgoing.println("ok");
//            BufferedReader fileIn = new BufferedReader( new FileReader(file) );
//            while (true) {
//                // Read and send lines from the file until
//                // an end-of-file is encountered.
//                String line = fileIn.readLine();
//                if (line == null)
//                    break;
//                outgoing.println(line);
//            }
        }
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error while transmitting data.");
    }
    private static void deleteFile(String fileName, File directory, PrintWriter outgoing, Socket connection) throws Exception {
        File file = new File(directory,fileName);

        if ( (! file.exists()) || file.isDirectory() ) {
            // (Note:  Don't try to send a directory, which
            // shouldn't be there anyway.)
            outgoing.println("error");
        }
        else {
            // Read file from disk
            //
            //MyFileReader fileReader = new MyFileReader();
            file.delete();
            outgoing.println("deleted");
            System.out.println("\r\ndelete "  + " bytes to file " + fileName);
            //System.out.println("\r\nSent " + data.length + " bytes to server.");
            //outgoing.println("ok");
//            BufferedReader fileIn = new BufferedReader( new FileReader(file) );
//            while (true) {
//                // Read and send lines from the file until
//                // an end-of-file is encountered.
//                String line = fileIn.readLine();
//                if (line == null)
//                    break;
//                outgoing.println(line);
//            }
        }
        outgoing.flush();
        outgoing.close();
        if (outgoing.checkError())
            throw new Exception("Error while transmitting data.");
    }

}

