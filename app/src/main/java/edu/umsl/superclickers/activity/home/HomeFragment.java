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
import edu.umsl.superclickers.database.QuizSchema;

/**
 * Created by stin on 3/23/17.
 */

public class HomeFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private HomeController hController;
    private ArrayList<String> quizzes;
    private ArrayList<String> quizIds;
    private ArrayList<String> courseIds;

    interface HomeController {
        void setQuizzes(ArrayList<String> quizzes, ArrayList<String> quizIds, ArrayList<String> courseId);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hController = (HomeController) getActivity();
        quizzes = new ArrayList<>();
        quizIds = new ArrayList<>();
        courseIds = new ArrayList<>();
    }

    void getQuizzesFor(final String user_id) {
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
                                    String _id = quiz.getString("_id");
                                    String desc = quiz.getString(QuizSchema.KEY_DESC);

                                    quizzes.add(desc);
                                    quizIds.add(_id);
                                    courseIds.add(courseId);
                                }

                                hController.setQuizzes(quizzes, quizIds, courseIds);



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
