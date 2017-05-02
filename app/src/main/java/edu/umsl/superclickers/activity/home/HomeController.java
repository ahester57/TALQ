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
import edu.umsl.superclickers.app.GroupConfig;
import edu.umsl.superclickers.app.QuizConfig;
import edu.umsl.superclickers.quizdata.QuizListItem;
import edu.umsl.superclickers.userdata.Group;


/**
 * Created by stin on 3/23/17.
 */

public class HomeController extends Fragment {

    private final String TAG = HomeController.class.getSimpleName();

    private HomeListener hListener;
    private ArrayList<QuizListItem> quizzes;
    private ArrayList<String> courseIds;

    interface HomeListener {
        void setQuizzes(ArrayList<QuizListItem> quizzes, ArrayList<String> courseId);
        void setGroup(Group group);
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hListener = (HomeListener) getActivity();
        quizzes = new ArrayList<>();
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

                                    // @TODO return quiz object
                                    quizzes.add(new QuizListItem(quiz));
                                    courseIds.add(courseId);
                                }

                                hListener.setQuizzes(quizzes, courseIds);

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

    void getGroupFor(final String user_id, final String courseId) {
        String tag_str_req = "req_group";
        String uri = String.format(GroupConfig.URL_GROUP_FOR_USER, user_id, courseId);
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
                                // Group was found
                                Group group = new Group(jObj);

                                hListener.setGroup(group);

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

            }
        });
        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

}
