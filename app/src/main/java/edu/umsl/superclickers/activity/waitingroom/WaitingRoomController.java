package edu.umsl.superclickers.activity.waitingroom;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.GroupConfig;
import edu.umsl.superclickers.app.QuizConfig;
import edu.umsl.superclickers.app.SessionManager;

/**
 * Created by Austin on 5/1/2017.
 *
 */

public class WaitingRoomController extends Fragment {

    private final String TAG = WaitingRoomController.class.getSimpleName();

    private SessionManager session;
    private ProgressDialog pDialog;
    private WaitListener wListener;

    interface WaitListener {
        void postInfo(String response);
        void setGroupStatus(String response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Login activity listener
        wListener = (WaitListener) getActivity();
        // progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getActivity());
    }

    void POSTQuiz(final String courseId, final String userId, final String sessionId,
                  final JSONObject quizObj) {
        String tag_str_req = "req_post_quiz";
        //pDialog.setMessage("Uploading quiz...");();
        String uri = String.format(QuizConfig.URL_POST_USER_QUIZ, courseId,
                userId, sessionId);
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Quiz POST response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = true;
                            try {
                                String err = jObj.getString("error");
                                error = true;
                                Log.e(TAG, err);
                            } catch (JSONException e) {
                                error = false;
                            }
                            if (!error) {
                                // Quiz POST SUCCESSFUL
                                //JSONObject user = jObj.getJSONObject("user");
                                wListener.postInfo(response);
                                Toast.makeText(getActivity(), "Quiz submitted.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                // Error uploading quiz
                                String errMessage = jObj.getString("error_msg");
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Quiz error: " + error.getMessage());
                wListener.postInfo("oops");
                Toast.makeText(getActivity(), "Quiz already submitted.",
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return quizObj.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported encoding");
                    return null;
                }
            }
        };
        // end string request.. phew!
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    void getGroupStatus(final String group_id, final String courseId,
                        final String quiz_id, final String session_id) {
        String tag_str_req = "req_group";
        String uri = String.format(GroupConfig.URL_GROUP_STATUS, group_id, courseId,
                quiz_id, session_id);
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideDialog();
                        Log.d(TAG, " Response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error;
                            try {
                                error = jObj.getString("error");
                            } catch (JSONException e) {
                                error = "false";
                            }
                            // if no errors
                            if (error.equals("false")) {
                                // Group was found
                                wListener.setGroupStatus(response);
                            } else {
                                // Error
                                String errMessage = "error";
                                Toast.makeText(getActivity(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "JSON error: "
                                    + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Group error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    private void showDialog() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.setIcon(R.drawable.seek_bar_text_thumb);
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }

}
