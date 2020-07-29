package com.example.client;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

//public class ClientFiles extends AsyncTask<Void, Void, ArrayList<String>> {
//    private String dstAddress, serverResponse = "";
//    private int dstPort;
//    ArrayList<String> files_server = new ArrayList<String>();
//    private Socket clientSocket = null;
//    private Context ConnectToUserActivity;
//    // private Object ConnectToUserActivity;
//
//    ClientFiles(String dstAddress, int dstPort) {
//        this.dstAddress = dstAddress;
//        this.dstPort = dstPort;
//    }
//    @Override
//    protected ArrayList<String> doInBackground(Void... voids) {
//
//
//        try {
//            Log.e("CLIENT", "Before Connection");
//            clientSocket = new Socket(dstAddress, dstPort);
//
//
//            if (clientSocket != null) {
//
//                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
//                out.println("index");
//
//
//                Log.e("CLIENT", "After Connection");
//
//
//                //MainActivity.userArrayList.add(user);
//            }
//            try {
//                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//                String data;
//                while ((data = input.readLine()) != null) {
//                    // System.out.println("\r\nMessage from " + clientAddress + ": " + data);
//                    System.out.println(data);
//                    if(!files_server.contains(data)) {
//                        files_server.add(data);
//                    }
//                }
//                //serverResponse = input.readLine();
//
//                String connected_name = serverResponse.substring(serverResponse.indexOf('_')+1);
//            } catch (IOException e) {
//                e.printStackTrace();
//                Log.e("CLIENT", "Could not read socket");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            Log.e("CLIENT", "Could not connect to socket");
//            serverResponse = e.getCause().toString();
//        } finally {
//            if (clientSocket != null) {
//                try {
//                    clientSocket.close();
//                } catch (IOException e) {
//                    Log.e("CLIENT", "Could Not Close Client");
//                    e.printStackTrace();
//                }
//            }
//        }
//        Log.e("ended","not working");
//        return files_server;
//    }
//
//    @Override
//    protected void onPostExecute(ArrayList<String> Result) {
//        //showDialog("Downloaded " + result + " bytes");
//        Intent intent = new Intent(ConnectToUserActivity, UserMainView.class);
//        intent.putExtra("files_list", Result);
//        ConnectToUserActivity.startActivity(intent);
//    }
//}
