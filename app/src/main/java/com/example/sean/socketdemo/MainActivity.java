package com.example.sean.socketdemo;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText input1;
    private TextView show;
    private Button sendBtn1;
    private OutputStream os;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        input1 = (EditText) findViewById(R.id.input1);
        show = (TextView) findViewById(R.id.show);
        sendBtn1 = (Button) findViewById(R.id.send1);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                // 如果消息来自子线程
                if (msg.what == 0x234) {
                    // 将读取的内容追加显示在文本框中
                    show.append("\n" + msg.obj.toString());
                }
            }
        };
        new Thread() {
            public void run() {
                Socket socket;
                try {
                    socket = new Socket("192.168.1.101", 20000);
                    // 客户端启动ClientThread线程不断读取来自服务器的数据
                    new Thread(new ClientThread(socket, handler)).start();
                    os = socket.getOutputStream();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            ;
        }.start();

        sendBtn1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // 将用户在文本框内输入的内容写入网络
                    os.write((input1.getText().toString() + "\r\n").getBytes());
                    // 清空input文本框数据
                    input1.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
