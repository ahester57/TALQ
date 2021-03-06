package edu.umsl.superclickers.activity.quiz.helper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import edu.umsl.superclickers.R;
import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.QuizConfig;
import edu.umsl.superclickers.app.SessionManager;
import edu.umsl.superclickers.database.schema.QuizSchema;
import edu.umsl.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 3/22/2017.
 *
 */

public class QuizGET extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private ProgressDialog pDialog;
    private SessionManager session;
    private QuizGETController qController;

    public interface QuizGETController {
        void setQuiz(Quiz quiz);
        void downloadQuiz(String token);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Session manager
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        session = new SessionManager(getActivity());
    }

    public void setController(QuizGETController qControl) {
        qController = qControl;
    }

    public void getQuiz(final String user_id, final String course_id,
            final String quiz_id, final String token) {
        String tag_str_req = "req_quiz";
        String uri = String.format(QuizConfig.URL_GET_QUIZ, user_id,
                course_id, quiz_id, token);
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, " Response: " + response);
                        hideDialog();
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
                                // Quiz was found
                                String sessionId = jObj.getString(QuizSchema.KEY_SESSION_ID);
                                JSONObject quizObj = jObj.getJSONObject("quiz");
                                Quiz quiz = new Quiz(quizObj, sessionId);
                                session.addQuizToDB(quiz);
                                qController.setQuiz(quiz);
                            } else {
                                // Error
                                String errMessage = jObj.getString("error");
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
                Log.e(TAG, "Quiz error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        });
        // end string request.. phew!
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    public void getToken(final String id) {
        String tag_str_req = "req_token";
        String uri = String.format(QuizConfig.URL_QUIZ_TOKEN, id);
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                // get token
                                JSONObject token = jObj.getJSONObject("token");
                                qController.downloadQuiz(token.getString("accessToken"));
                            } else {
                                // Error
                                String errMessage = jObj.getString("error");
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
                Log.e(TAG, "Quiz error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
        // end string request.. phew!
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
