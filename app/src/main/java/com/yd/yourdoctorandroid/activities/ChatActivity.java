package com.yd.yourdoctorandroid.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.yd.yourdoctorandroid.R;
import com.yd.yourdoctorandroid.adapters.ChapAdpater;
import com.yd.yourdoctorandroid.networks.models.Record;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView btnLogin;
    private Button btnChat;
    private EditText mEditText;
    private List<Record> mListMessage;
    private final String URL_SERVER = "http://192.168.124.114:3000";
    private Socket mSocket;
    private ChapAdpater chatApapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mEditText = findViewById(R.id.edit_message);
        btnChat = findViewById(R.id.btn_send);
        btnLogin = findViewById(R.id.btn_login);
        recyclerView = findViewById(R.id.rc_chat);

        mListMessage = new ArrayList<>();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        try {
            mSocket = IO.socket(URL_SERVER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mSocket.connect();
        mSocket.on("receiver_message",onNewMessage);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("user_login", mEditText.getText().toString());
                chatApapter = new ChapAdpater(getApplicationContext(), mListMessage, mEditText.getText().toString());
                recyclerView.setAdapter(chatApapter);

            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSocket.emit("send_message",mEditText.getText().toString());
            }
        });

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String message;
                    message = data.optString("data");

                    try {

                        JSONObject jsonObject = new JSONObject(message);
                        Record message1 = new Record();
                        message1.recorderID = jsonObject.getString("recorderID");
                        message1.value = jsonObject.getString("value");
                        message1.createdAt = jsonObject.getString("createdAt");
                        message1.type = jsonObject.getString("type");
                        mListMessage.add(message1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //TODO
                    // mListMessage.add(new Message("anhle"));
                    Log.e("message" , message);
                    Log.e("data" , data.toString());
                    chatApapter.notifyDataSetChanged();
                }
            });
        }
    };
}

