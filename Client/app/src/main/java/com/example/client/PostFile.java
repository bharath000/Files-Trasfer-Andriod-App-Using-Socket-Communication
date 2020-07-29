package com.example.client;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class PostFile {
    static class Post extends AsyncTask<Void, Void, String> {
        private String dstAddress, serverResponse = "",filename;
        private int dstPort, filesize;
        private InputStream inputStream;
        private Socket clientSocket = null;
        MyFileWriter fw = new MyFileWriter();
        Post(String dstAddress, int dstPort, String filename, int filesize, InputStream inputStream) {
            this.dstAddress = dstAddress;
            this.dstPort = dstPort;
            this.filename = filename;
            this.inputStream = inputStream;
            this.filesize = filesize;
        }
        @RequiresApi(api = Build.VERSION_CODES.R)
        @Override
        protected String doInBackground(Void... voids) {
            //String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+filename;
            //String fileName = Environment.DIRECTORY_DOWNLOADS+filename;
            //String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +filename;
            try {
                Log.e("CLIENT", "Before Connection");
                clientSocket = new Socket(dstAddress, dstPort);


                if (clientSocket != null) {

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("Post"+filename);


                    Log.e("CLIENT", "After Connection Upload file");


                    //MainActivity.userArrayList.add(user);
                }
                try {
                    //BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());
                    // Send binary data over the TCP/IP socket connection
                    byte[] fileData = new byte[filesize];
                    int bytesRead = 0;
                    int b;
                    while ((b = inputStream.read()) != -1) {
                        fileData[bytesRead++] = (byte)b;
                    }
                    for (byte i : fileData) {
                        clientSocket.getOutputStream().write(i);
                    }

                    //System.out.println("\r\nSent " + data.length + " bytes to server.");
                    //int fileSize = fw.writeFile(fileName, bis);
                    //bis.close();
                    //System.out.println("\r\nWrote " + fileSize + " bytes to file " + fileName);
                    //serverResponse = input.readLine();

                    //String connected_name = serverResponse.substring(serverResponse.indexOf('_')+1);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e("CLIENT", "Could not read socket");
                }

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("CLIENT", "Could not connect to socket");
                serverResponse = e.getCause().toString();
            } finally {
                if (clientSocket != null) {
                    try {
                        clientSocket.close();
                    } catch (IOException e) {
                        Log.e("CLIENT", "Could Not Close Client");
                        e.printStackTrace();
                    }
                }
            }
            Log.e("ended","not working");
            return serverResponse;
        }

//        @Override
//        protected void onPostExecute(ArrayList<String> Result) {
//            //showDialog("Downloaded " + result + " bytes");
//            Intent intent = new Intent(ConnectToUserActivity.this, UserMainView.class);
//            intent.putExtra("files_list", Result);
//            startActivity(intent);
//        }
    }
}
