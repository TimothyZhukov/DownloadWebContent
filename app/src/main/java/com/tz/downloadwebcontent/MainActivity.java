package com.tz.downloadwebcontent;

import android.os.AsyncTask;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    EditText editTextUrl;
    TextView textViewResult;
    TextView textViewEnterUrl;

    private String nike = "https://www.nike.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editTextUrl = findViewById(R.id.editTextUrl);
        textViewResult = findViewById(R.id.textViewResult);
        textViewEnterUrl = findViewById(R.id.textViewEnterUrl);
    }

    public void downloadOnClick(View view) {
        if (!editTextUrl.getText().toString().isEmpty()) {
            String targetUrl = editTextUrl.getText().toString();
            DownloadTask task = new DownloadTask();
            try {
                String result = task.execute(targetUrl).get();
                Log.i("URL", result);
                textViewResult.setText(result);
                textViewEnterUrl.setText(R.string.success);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            Toast toast = Toast.makeText(getApplicationContext(), R.string.toast, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    private static class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return result.toString();
        }
    }
}