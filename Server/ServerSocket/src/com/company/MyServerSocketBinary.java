package com.company;

import java.io.BufferedInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServerSocketBinary {
    private ServerSocket server;
    public MyServerSocketBinary(String ipAddress) throws Exception {
        if (ipAddress != null && !ipAddress.isEmpty())
            this.server = new ServerSocket(0, 1, InetAddress.getByName(ipAddress));
        else
            this.server = new ServerSocket(0, 1, InetAddress.getLocalHost());
    }
    private void listen() throws Exception {
        MyFileWriter fw = new MyFileWriter();
        //
        // Accept new client connection
        //
        Socket client = this.server.accept();
        String clientAddress = client.getInetAddress().getHostAddress();
        System.out.println("\r\nNew connection from " + clientAddress);
        //
        // Read binary data from client socket and write to file
        //
        BufferedInputStream bis = new BufferedInputStream(client.getInputStream());
        String fileName = "image-" + System.currentTimeMillis() + ".jpg";
        int fileSize = fw.writeFile(fileName, bis);
        bis.close();

        //
        // Close socket connection
        //
        client.close();
        System.out.println("\r\nWrote " + fileSize + " bytes to file " + fileName);
    }
    public InetAddress getSocketAddress() {
        return this.server.getInetAddress();
    }
    public int getPort() {
        return this.server.getLocalPort();
    }


    public static void main(String[] args) throws Exception {
	// write your code here
        MyServerSocketBinary app = new MyServerSocketBinary("192.168.1.11");

        System.out.println("\r\nRunning Server (Image): " +
                "Host=" + app.getSocketAddress().getHostAddress() +
                " Port=" + app.getPort());

        app.listen();

    }
}
