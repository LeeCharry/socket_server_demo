package server.tulip.com.socketserver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "lcy";
    private EditText tvIp;
    private EditText tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvIp = findViewById(R.id.tv_ip);
        tvContent = findViewById(R.id.tv_content);

        //1、服务端发送字符串测试
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                acceptSocketRequest();
//            }
//        }).start();

        //2、服务端发送文件测试
         findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 final String path = "/storage/emulated/0/sockettest/dpjh.xml";
                 final String fileName = "dpjh.xml";
                 final String ipAddress = "192.168.1.143";
                 final int port = 10001;

                 Thread sendThread = new Thread(new Runnable() {
                     @Override
                     public void run() {
                         String s = SendFile(fileName, path, tvIp.getText().toString(), port);
                         Log.d(TAG,s);
                     }
                 });
                 sendThread.start();
             }
         });
    }
    public String SendFile(String fileName, String path, String ipAddress, int port) {
        try {
            Socket name = new Socket(ipAddress, port);
            OutputStream outputName = name.getOutputStream();
            OutputStreamWriter outputWriter = new OutputStreamWriter(outputName);

            BufferedWriter bwName = new BufferedWriter(outputWriter);
            bwName.write(fileName);
            bwName.close();
            outputWriter.close();
            outputName.close();
            name.close();

            Socket data = new Socket(ipAddress, port);
            OutputStream outputData = data.getOutputStream();
            FileInputStream fileInput = new FileInputStream(path);
            int size = -1;
            byte[] buffer = new byte[1024];
            while ((size = fileInput.read(buffer, 0, 1024)) != -1) {
                outputData.write(buffer, 0, size);
            }
            outputData.close();
            fileInput.close();
            data.close();
            return fileName + " 发送完成";
        } catch (Exception e) {
            return "发送错误:\n" + e.getMessage();
        }
    }

    private void acceptSocketRequest() {
        //创建一个ServerSocket,用于监听客户端socket的连接请求
        ServerSocket ss= null;
        try {
            ss = new ServerSocket(10001);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //采用循环不断接受来自客户端的请求,服务器端也对应产生一个Socket
        while(true){
            Socket s= null;
            try {
                s = ss.accept();
                OutputStream os=s.getOutputStream();
                os.write("您好，你收到了来自tulip服务端的亲切问候哦！\n".getBytes("utf-8"));
                os.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
