package edu.umsl.hester.superclickers;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import static android.content.ContentValues.TAG;


public class ConnectAsyncTask extends AsyncTask<String, String, TCPClient> {

    private static final String TAG = "ConnectAsyncTask";
    private TCPClient tcpClient;
    private Handler mHandler;

    ConnectAsyncTask(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    protected TCPClient doInBackground(String... strings) {
        Log.d(TAG, "In background");

        try {
            tcpClient = new TCPClient(mHandler, "test", "127.0.0.1",
                        new TCPClient.MessageCallback() {
                                @Override
                                public void callbackMessageReceiver (String message) {
                                    publishProgress(message);
                                }
                        });
        } catch (NullPointerException e) {
            Log.d(TAG, "Null ptr exc.");
        }
        tcpClient.run();
        return null;

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
        Log.d(TAG, "In progress update, values: " + values.toString());
        if (values[0].equals("shutdown")) {
            tcpClient.sendMessage("test");
            tcpClient.stopClient();
            mHandler.sendEmptyMessageDelayed(45, 2000);
        } else {
            tcpClient.sendMessage("wrong");
            mHandler.sendEmptyMessageDelayed(22, 2000);
            tcpClient.stopClient();
        }
    }

    @Override
    protected void onPostExecute(TCPClient result) {
        super.onPostExecute(tcpClient);
        Log.d(TAG, "In post execute");
        if (result != null && result.isRunning()) {
            result.stopClient();
        }
        mHandler.sendEmptyMessageDelayed(67, 4000);
    }
}
