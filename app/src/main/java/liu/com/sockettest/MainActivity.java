package liu.com.sockettest;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    private EditText host;
    private EditText etMain;
    private Button btnMain;
    private TextView tvMain;
    private ClientThread mClientThread;
    private static final String TAG = "MainActivity";
    //在主线程中定义Handler传入子线程用于更新TextView
    private Handler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        host = (EditText) findViewById(R.id.host);
        etMain = (EditText) findViewById(R.id.et_main);
        btnMain = (Button) findViewById(R.id.btn_main);
        tvMain = (TextView) findViewById(R.id.tv_main);

        mHandler=new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 0) {
                    tvMain.append("\n" + msg.obj.toString());
                }
            }
        };
        mClientThread = new ClientThread(mHandler,host.getText().toString());
        mClientThread.run();
        //点击button时，获取EditText中string并且调用子线程的Handler发送到服务器
        btnMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Message msg = new Message();
                    msg.what = 1;
                    msg.obj = etMain.getText().toString();
                    mClientThread.revHandler.sendMessage(msg);
                    etMain.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
