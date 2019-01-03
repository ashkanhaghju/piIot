package org.shr.sockettesting;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;

public class MainActivity extends AppCompatActivity {

    TextView response;
    EditText editTextAddress, editTextPort,editTextmessage;
    Button buttonConnect, buttonClear,buttonsend;
    private Client myClient;
    private Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = (EditText) findViewById(R.id.addressEditText);
        editTextPort = (EditText) findViewById(R.id.portEditText);
        editTextmessage = (EditText) findViewById(R.id.msgEditText);
        buttonConnect = (Button) findViewById(R.id.connectButton);
        buttonsend = (Button) findViewById(R.id.sendButton);
        buttonClear = (Button) findViewById(R.id.clearButton);
        response = (TextView) findViewById(R.id.responseTextView);

        buttonConnect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                myClient = new Client(editTextAddress.getText()
                        .toString(), editTextPort
                        .getText().toString(), response);
                myClient.execute();
            }
        });

        buttonClear.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                response.setText("");
            }
        });

        buttonsend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if (myClient==null)return;
                myClient.sendMessage(editTextmessage.getText().toString());

                //myClient.getSocket().se
            }
        });
    }
}