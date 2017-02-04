package edu.umsl.hester.superclickers;

import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;


/*
    I think I am going to abandon this asynctask. Instead, I am going
    to move towards a webservice, as that seems like a better way to
    communicate with a server throughout the application's lifecycle.

    For now this will do for testing 
 */
class ConnectAsyncTask extends AsyncTask<String, String, TCPClient> {

    private static final String TAG = "ConnectAsyncTask";
    static String serverMessage = ";";
    private String userMessage = "CLIENT - ";
    private static TCPClient tcpClient;
    private Handler mHandler;


    ConnectAsyncTask(Handler handler) {
        this.mHandler = handler;
        this.userMessage = "";

    }

    @Override
    protected TCPClient doInBackground(String... strings) {
        Log.d(TAG, "In background");
        if (strings.length > 0)
            userMessage = strings[0];

        try {
            tcpClient = new TCPClient(mHandler, userMessage, "192.168.1.125",
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
        Log.d(TAG, "In progress update, values: " + values[0]);
        if (values[0].equals("SERVER - END")) {
            tcpClient.sendMessage("asdf");
            //tcpClient.stopClient();
            serverMessage = values[0];
            mHandler.sendEmptyMessageDelayed(1, 2000);
            tcpClient.stopClient();
        } else {
            //tcpClient.sendMessage("god damn gibbons");
            serverMessage = values[0];
            mHandler.sendEmptyMessageDelayed(1, 2000);
            //mHandler.sendEmptyMessageDelayed(22, 2000);
            //tcpClient.stopClient();
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
