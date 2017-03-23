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
    private String url = "http://stin.tech/learning-api/wrapper.php?id=%1$s";

    private QuizGETController qController;

    interface QuizGETController {
        void setQuiz(Quiz quiz);
        void setBSQuiz();
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


        String uri = String.format(url, id);
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

                            if (error.equals("false")) {
                                // Quiz was found
                                Quiz quiz = parseQuiz(jObj);

                                if (quiz != null) {
                                    qController.setQuiz(quiz);
                                } else {
                                    qController.setBSQuiz();
                                }

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

    private Quiz parseQuiz(JSONObject jObj) {
        try {

            String id = jObj.getString(QuizSchema.KEY_ID);
            String desc = jObj.getString(QuizSchema.KEY_DESC);
            String text = jObj.getString(QuizSchema.KEY_TEXT);
            String avail = jObj.getString(QuizSchema.KEY_AVAIL_DATE);
            String exp = jObj.getString(QuizSchema.KEY_EXPIRE_DATE);
            JSONArray qArray = jObj.getJSONArray("questions");

            ArrayList<Question> questions = new ArrayList<>();

            int i = 0, j = 0;

            while (i < qArray.length()) {
                JSONObject qObj = qArray.getJSONObject(i);
                String qid = qObj.getString(QuestionSchema.KEY_ID);
                String qtitle = qObj.getString(QuestionSchema.KEY_TITLE);
                String qtext = qObj.getString(QuestionSchema.KEY_TEXT);
                int points = qObj.getInt(QuestionSchema.KEY_POINTS_POSS);
                JSONArray aArray = qObj.getJSONArray(QuestionSchema.KEY_AVAIL_ANSWERS);

                ArrayList<Answer> answers = new ArrayList<>();
                j = 0;

                while (j < aArray.length()) {
                    JSONObject aObj = aArray.getJSONObject(j);
                    String aid = aObj.getString(AnswerSchema.KEY_ID);
                    String avalue = aObj.getString(AnswerSchema.KEY_VALUE);
                    String atext = aObj.getString(AnswerSchema.KEY_TEXT);
                    int sortOrder = aObj.getInt(AnswerSchema.KEY_SORT_ORDER);

                    Answer ans = new Answer(aid, avalue, atext, sortOrder);
                    answers.add(ans);

                    j++;
                }

                Question question = new Question(qid, qtitle, qtext, points, answers);
                questions.add(question);
                i++;
            }

            Quiz quiz = new Quiz(id, desc, text, avail, exp, questions, 0);
            return quiz;
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "JSON error: "
                + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
}
