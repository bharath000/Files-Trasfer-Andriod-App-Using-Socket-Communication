package com.example.client;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private  EditText et;
    private Button continueBtn;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // et = (EditText) findViewById(R.id.message);
        continueBtn = findViewById(R.id.button);
        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(getApplicationContext(), ConnectToUserActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }


//    public void send_data(View v){
//        String message = et.getText().toString();
//        BackgroundTask b1 = new BackgroundTask();
//        b1.execute(message);
//    }
//    class BackgroundTask extends AsyncTask<String,Void,Void> {
//        Socket s;
//        PrintWriter writer;
//        @Override
//        protected Void doInBackground(String... voids){
//
//            try {
//                String message = voids[0];
//                s = new Socket("192.168.1.11",54644);
//
//
//                writer = new PrintWriter(s.getOutputStream());
//                writer.write(message);
//                writer.flush();
//               writer.close();
//               // s.close();
//
//            }catch (IOException e){
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//    }
}