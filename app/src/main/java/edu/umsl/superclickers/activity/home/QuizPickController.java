package edu.umsl.superclickers.activity.home;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import edu.umsl.superclickers.app.AppController;
import edu.umsl.superclickers.app.QuizConfig;
import edu.umsl.superclickers.quizdata.QuizListItem;

/**
 * Created by Austin on 5/7/2017.
 */

public class QuizPickController extends Fragment {

    private final String TAG = QuizPickController.class.getSimpleName();

    private QuizPickDelegate hListener;
    private ArrayList<QuizListItem> quizzes;

    interface QuizPickDelegate {
        void setQuizzes(ArrayList<QuizListItem> quizzes);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hListener = (QuizPickDelegate) getActivity();
        quizzes = new ArrayList<>();
    }


    void getQuizzesFor(final String user_id, final String course_id) {
        String tag_str_req = "req_quiz";
        String uri = String.format(QuizConfig.URL_QUIZZES_FOR_USER, user_id);
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, uri,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, " Response: " + response);
                        try {
                            JSONArray jArr = new JSONArray(response);

                            String error;
                            try {
                                error = jArr.getJSONObject(0).getString("error");
                            } catch (JSONException e) {
                                error = "false";
                            }

                            // if no errors
                            if (error.equals("false")) {
                                // Quiz was found
                                for (int i = 0; i < jArr.length(); i++) {
                                    JSONObject jObj = jArr.getJSONObject(i);
                                    String courseId = jObj.getString("courseId");
                                    JSONObject quiz = jObj.getJSONObject("quiz");
                                    //session.addQuizToDB(new Quiz(quiz));
                                    if (courseId.equals(course_id)) {
                                        quizzes.add(new QuizListItem(quiz, courseId));
                                    }
                                }

                                hListener.setQuizzes(quizzes);

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
                Log.e(TAG, "Quiz error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();

            }
        });
        // end string request.. phew!
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }
}
