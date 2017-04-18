package edu.umsl.hester.superclickers.activity.quiz;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import edu.umsl.hester.superclickers.app.AppController;
import edu.umsl.hester.superclickers.app.QuizConfig;
import edu.umsl.hester.superclickers.app.SessionManager;
import edu.umsl.hester.superclickers.database.AnswerSchema;
import edu.umsl.hester.superclickers.database.QuestionSchema;
import edu.umsl.hester.superclickers.database.QuizSchema;
import edu.umsl.hester.superclickers.database.SQLiteHandler;
import edu.umsl.hester.superclickers.quizdata.Answer;
import edu.umsl.hester.superclickers.quizdata.Question;
import edu.umsl.hester.superclickers.quizdata.Quiz;

/**
 * Created by Austin on 3/22/2017.
 */

public class QuizGET extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private SQLiteHandler db;
    private SessionManager session;

    private QuizGETController qController;

    interface QuizGETController {
        void setQuiz(Quiz quiz);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getActivity());
        session = new SessionManager(getActivity());
        qController = (QuizGETController) getActivity();
    }

    void getQuiz(final String id) {
        String tag_str_req = "req_quiz";
        String uri = String.format(QuizConfig.URL_GET_QUIZ_BY_ID, id);
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
                                // Quiz was found
                                Quiz quiz = new Quiz(jObj);
                                qController.setQuiz(quiz);
                            }
                            else {
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
                },
                new Response.ErrorListener() {
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


}
