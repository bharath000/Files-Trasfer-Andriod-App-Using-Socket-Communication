package com.example.client;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ConnectToUserActivity extends AppCompatActivity {


    private EditText ipInput, portInput;
    private Button connectBtn, scanBtn;
    private Socket clientSocket = null;
    public static Client  myClient;
    private String data;

    ArrayList<String> files_server = new ArrayList<String>();

    public void myClient11(String ipInput, int portInput) {
        myClient = new Client(ipInput, portInput);
        myClient.execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ipInput = findViewById(R.id.ipInput);
        portInput = findViewById(R.id.portInput);
        connectBtn = findViewById(R.id.connectBtn);



    }

    public void connectBtnListener(View view) {
        if(portInput.getText().length() < 2 || ipInput.getText().length() < 2){
            Snackbar snackbar = Snackbar
                    .make(ipInput, "Please Enter Valid IP Address and/or Port number.", Snackbar.LENGTH_LONG);
            view = snackbar.getView();
            FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)view.getLayoutParams();
            params.gravity = Gravity.TOP;
            view.setLayoutParams(params);

            snackbar.show();
            return;
        }


        myClient = new Client(ipInput.getText().toString(), Integer.parseInt(portInput.getText().toString()));
        myClient.execute();



    }
    // Client connection with server code

    class Client extends AsyncTask<Void, Void, ArrayList<String>> {
        private String dstAddress, serverResponse = "";
        private int dstPort;
        private Socket clientSocket = null;
        Client(String dstAddress, int dstPort) {
            this.dstAddress = dstAddress;
            this.dstPort = dstPort;
        }
        @Override
        protected ArrayList<String> doInBackground(Void... voids) {

            try {
                Log.e("CLIENT", "Before Connection");
                clientSocket = new Socket(dstAddress, dstPort);


                if (clientSocket != null) {

                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                    out.println("index");


                    Log.e("CLIENT", "After Connection");


                    //MainActivity.userArrayList.add(user);
                }
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    while ((data = input.readLine()) != null) {
                       // System.out.println("\r\nMessage from " + clientAddress + ": " + data);
                        System.out.println(data);
                        if(!files_server.contains(data)) {
                            files_server.add(data);
                        }
                    }
                    //serverResponse = input.readLine();

                    String connected_name = serverResponse.substring(serverResponse.indexOf('_')+1);
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
            return files_server;
        }

        @Override
        protected void onPostExecute(ArrayList<String> Result) {
            //showDialog("Downloaded " + result + " bytes");
            Intent intent = new Intent(ConnectToUserActivity.this, UserMainView.class);
            intent.putExtra("files_list", Result);
            intent.putExtra("ipaddress", dstAddress);
            startActivity(intent);
        }
    }

}

