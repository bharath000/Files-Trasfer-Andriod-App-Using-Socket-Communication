package com.example.client;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.os.Environment;

import androidx.annotation.RequiresApi;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Path;
import java.util.ArrayList;

public class GetFile {
    static class Get extends AsyncTask<Void, Void, String> {
        private String dstAddress, serverResponse = "",filename;
        private int dstPort;
        private Socket clientSocket = null;
        ProgressDialog progressDialog;
        MyFileWriter fw = new MyFileWriter();
        Get(String dstAddress, int dstPort, String filename) {
            this.dstAddress = dstAddress;
            this.dstPort = dstPort;
            this.filename = filename;
        }
        @RequiresApi(api = Build.VERSION_CODES.R)

        @Override
        protected String doInBackground(Void... voids) {
            //String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+filename;
            //String fileName = Environment.DIRECTORY_DOWNLOADS+filename;
            String fileName = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) +filename;
            System.out.println("file location on device"+" "+fileName);
            try {
                Log.e("CLIENT", "Before Connection");
                clientSocket = new Socket(dstAddress, dstPort);


                if (clientSocket != null) {

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("get"+filename);


                    Log.e("CLIENT", "After Connection getfile");


                    //MainActivity.userArrayList.add(user);
                }
                try {
                    BufferedInputStream bis = new BufferedInputStream(clientSocket.getInputStream());

                    int fileSize = fw.writeFile(fileName, bis);
                    bis.close();
                    System.out.println("\r\nWrote " + fileSize + " bytes to file " + fileName);
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
