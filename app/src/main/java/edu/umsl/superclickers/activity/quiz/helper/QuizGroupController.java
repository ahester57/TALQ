package edu.umsl.superclickers.activity.quiz.helper;

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
import edu.umsl.superclickers.app.QuizConfig;
import edu.umsl.superclickers.app.SessionManager;

/**
 * Created by Austin on 5/7/2017.
 */

public class QuizGroupController extends Fragment {

    private final String TAG = QuizGroupController.class.getSimpleName();

    private SessionManager session;
    private ProgressDialog pDialog;
    private GroupGradeListener wListener;

    public interface GroupGradeListener {
        void postInfo(JSONObject response);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Login activity listener
        wListener = (GroupGradeListener) getActivity();
        // progress dialog
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        // Session manager
        session = new SessionManager(getActivity());
    }

    public void POSTGroupQuiz(final String quizId, final String groupId, final String sessionId,
                  final JSONObject questionObj) {
        String tag_str_req = "req_post_group_quiz";
        //pDialog.setMessage("Uploading quiz...");();
        String uri = String.format(QuizConfig.URL_POST_GROUP_QUIZ, quizId,
                groupId, sessionId);
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Quiz POST response: " + response);
                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error;
                            try {
                                String err = jObj.getString("error");
                                error = true;
                                Log.e(TAG, err);
                            } catch (JSONException e) {
                                error = false;
                            }
                            if (!error) {
                                // Question POST SUCCESSFUL
                                //JSONObject user = jObj.getJSONObject("user");
                                wListener.postInfo(jObj);
                                Toast.makeText(getActivity(), "Question submitted.",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                // Error uploading question
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
                Log.e(TAG, "Question error: " + error.getMessage());
                wListener.postInfo(new JSONObject());
                Toast.makeText(getActivity(), "Question not found.",
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
                    return questionObj.toString().getBytes("utf-8");
                } catch (UnsupportedEncodingException e) {
                    VolleyLog.wtf("Unsupported encoding");
                    return null;
                }
            }
        };
        hideDialog();
        // end string request.. phew!
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    private void showDialog() {
        if (pDialog != null && !pDialog.isShowing()) {
            pDialog.setIcon(R.drawable.seek_bar_text_thumb);
            pDialog.setMessage("Uploading quiz...");
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog != null && pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }
}
