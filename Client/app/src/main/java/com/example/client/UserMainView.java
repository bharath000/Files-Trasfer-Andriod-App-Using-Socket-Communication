package com.example.client;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Optional;

public class UserMainView extends AppCompatActivity {
    public static final int PICKFILE_RESULT_CODE = 1;
    private Uri fileUri;
    private String filePath_local;
    private  Context context;
    private InputStream inputStream1;
    private String ipaddress;
    private  MyCustomAdapter adapter;
    ArrayList<String> fileslist;
    private ArrayList<String> list = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listview_files);

        Intent intent = getIntent();
        ArrayList<String> fileslist = (ArrayList<String>) intent.getSerializableExtra("files_list");
        ipaddress = intent.getStringExtra("ipaddress");

        //Client myClient = new Client("192.168.1.11",3210);
        //myClient.execute();
        //instantiate custom adapter
        MyCustomAdapter adapter = new MyCustomAdapter(fileslist, this);

        //handle listview and assign adapter
        ListView lView = (ListView)findViewById(R.id.my_listview);
        lView.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.floating_action_button);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
                chooseFile.setType("*/*");
                chooseFile = Intent.createChooser(chooseFile, "Choose a file");

                startActivityForResult(chooseFile, PICKFILE_RESULT_CODE);
            }
        });



    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data ) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICKFILE_RESULT_CODE:
                if (resultCode == -1) {
                    fileUri = data.getData();
                    filePath_local = fileUri.getPath();
                    Cursor returnCursor =
                            getContentResolver().query(fileUri, null, null, null, null);
                    int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
                    returnCursor.moveToFirst();
                    final String filename = returnCursor.getString(nameIndex);
                    final int filesize = (int)returnCursor.getLong(sizeIndex);
                    System.out.println(returnCursor.getString(nameIndex));
                    System.out.println(Long.toString(returnCursor.getLong(sizeIndex)));
                    try {
                        inputStream1 = this.getContentResolver().openInputStream(fileUri);
                        System.out.println(inputStream1);
                        if (inputStream1 == null) return;




                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Upload Alert")
                            .setMessage("Are you sure want to continue uploadfile"+ " "+filename)
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(UserMainView.this,"Selected Option: YES",Toast.LENGTH_SHORT).show();
                                    //InputStream inputStream = this.getContentResolver().openInputStream(fileUri);
                                    PostFile.Post post= new PostFile.Post(ipaddress,3210, filename, filesize, inputStream1 );
                                    post.execute();
                                    list.add(filename);
                                    //adapter.notifyDataSetChanged();
                                    //fileslist.add(filename);
                                    //adapter.notifyDataSetChanged();
                                   // ClientFiles.execute();
                                   // Client myClient = new Client("192.168.1.11",3210);
                                    //myClient.execute();


                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(UserMainView.this,"Selected Option: No",Toast.LENGTH_SHORT).show();
                                }
                            });
                    //Creating dialog box
                    AlertDialog dialog  = builder.create();
                    dialog.show();

                    //tvItemPath.setText(filePath);
                   // File file = new File(fileUri.getPath());//create path from uri
                   // final String[] split = file.getPath().split(":");//split the path.
                    //filePath_local = split[1];//assign it to a string(your choice).
                    System.out.println(filePath_local);


                }

                break;
        }
    }



    public class MyCustomAdapter extends BaseAdapter implements ListAdapter {

        private final ArrayList<String> list;
        private Context context;



        public MyCustomAdapter(ArrayList<String> list, Context context) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

//        @Override
//        public long getItemId(int pos) {
//            return list.get(pos).getId();
//            //just return 0 if your list items do not have an Id variable.
//        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.usermainview, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
            listItemText.setText(list.get(position));

            //Handle buttons and add onClickListeners
            Button deleteBtn = (Button)view.findViewById(R.id.delete_btn);
            Button addBtn = (Button)view.findViewById(R.id.add_btn);

         deleteBtn.setOnClickListener(new View.OnClickListener(){
               @Override
                public void onClick(View v) {
                    //do something
                   Object filename  =  getItem(position);
                   String file = String.valueOf(filename);
                   DeleteFiles.Delete del = new DeleteFiles.Delete(ipaddress,3210,"/"+file);
                   del.execute();
                    list.remove(position); //or some other task
                    notifyDataSetChanged();
                   Toast.makeText(UserMainView.this,"Deleted"+file,Toast.LENGTH_SHORT).show();
                }
            });
            addBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    //download the file


                    Object filename  =  getItem(position);
                    String file = String.valueOf(filename);
                    //list.add(file);
                    notifyDataSetChanged();
                    System.out.println(file);
                    GetFile.Get get= new GetFile.Get(ipaddress,3210, "/"+file);
                    get.execute();
                    Toast.makeText(UserMainView.this,"Downloaded"+file,Toast.LENGTH_SHORT).show();

                }
            });

            return view;
        }
    }
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

            ArrayList<String> files_server = new ArrayList<String>();
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

                    String data;
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
            Intent intent = new Intent(UserMainView.this, UserMainView.class);
            intent.putExtra("files_list", Result);
            startActivity(intent);
        }
    }

}
