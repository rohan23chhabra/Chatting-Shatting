package info.devexchanges.bluetoothchatapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import info.devexchanges.bluetoothchatapp.file.MyFile;

public class FileExchangeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    Button chooseButton,sendButton;
    TextView showFile;
    Uri uri;
    MyFile myFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_exchange);
        myFile = new MyFile();
        chooseButton = (Button)findViewById(R.id.choosefile);
        sendButton = (Button)findViewById(R.id.sendfile);
        showFile = (TextView)findViewById(R.id.showfile);

        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("*/*");

                startActivityForResult(intent,REQUEST_CODE);
            }
        });

        myFile.setFileChangeListener(new MyFile.FileChangeListener() {
            @Override
            public void onChange() {
                showFile.setText(myFile.getFile().getPath());
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(uri==null) {
                    Toast.makeText(FileExchangeActivity.this, "Select any file first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.setPackage("com.android.bluetooth");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(sharingIntent, "Share file"));
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CODE && resultCode== Activity.RESULT_OK)
        {
            if(data!=null)
            {
                this.uri = data.getData();
                myFile.setFile(new File(uri.getPath()));
                Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
