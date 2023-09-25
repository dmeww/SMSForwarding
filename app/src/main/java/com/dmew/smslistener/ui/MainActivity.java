package com.dmew.smslistener.ui;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dmew.smslistener.databinding.ActivityMainBinding;
import com.dmew.smslistener.tool.Caller;

import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadData();
        initEvents();
    }

    private void initEvents() {
        binding.btn.setOnClickListener(l -> {
            saveData();

            String url = getSharedPreferences("app", MODE_PRIVATE).getString("url", "");
            String token = getSharedPreferences("app", MODE_PRIVATE).getString("token", "");
            String priority = getSharedPreferences("app", MODE_PRIVATE).getString("priority", "");
            boolean valid = checkData(url, token, priority);
            if (valid) {
                Executors.newSingleThreadExecutor().execute(() -> {
                    boolean success = Caller.makeRequestTest(url, token, priority);

                    handler.post(() -> {
                        Toast.makeText(MainActivity.this, success ? "测试成功" : "测试失败", Toast.LENGTH_LONG).show();
                    });
                });
            } else {
                Toast.makeText(MainActivity.this, "参数填写错误，请检测填写的参数", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("app", MODE_PRIVATE);

        binding.url.setText(sp.getString("url", ""));
        binding.token.setText(sp.getString("token", ""));
        binding.priority.setText(sp.getString("priority", ""));
    }

    private void saveData() {

        SharedPreferences.Editor editor = getSharedPreferences("app", MODE_PRIVATE).edit();

        editor.putString("url", binding.url.getText() == null ? "" : binding.url.getText().toString());

        editor.putString("token", binding.token.getText() == null ? "" : binding.token.getText().toString());

        editor.putString("priority", binding.priority.getText() == null ? "" : binding.priority.getText().toString());

        editor.commit();
    }

    boolean checkData(String _url, String _token, String _priority) {

        if (_url == null || _url.isEmpty()) {
            return false;
        }
        if (_token == null || _token.isEmpty()) {
            return false;
        }
        if (_priority == null || _priority.isEmpty()) {
            return false;
        }
        return true;
    }


    Handler handler = new Handler(Looper.getMainLooper());

}