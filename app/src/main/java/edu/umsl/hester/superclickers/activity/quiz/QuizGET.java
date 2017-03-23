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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import edu.umsl.hester.superclickers.app.AppController;
import edu.umsl.hester.superclickers.app.SessionManager;
import edu.umsl.hester.superclickers.database.SQLiteHandler;

/**
 * Created by Austin on 3/22/2017.
 */

public class QuizGET extends Fragment {

    private final String TAG = this.getClass().getSimpleName();

    private SQLiteHandler db;
    private SessionManager session;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new SQLiteHandler(getActivity());
        session = new SessionManager(getActivity());
    }

    void getQuiz(final String id) {
        String tag_str_req = "req_login";



        // new string request
        StringRequest strReq = new StringRequest(Request.Method.GET, "http://localhost:10010/v1/quiz",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, " Response: " + response);


                        try {
                            JSONObject jObj = new JSONObject(response);
                            String error = jObj.getString("error");

                            if (error != null) {
                                // LOGIN SUCCESSFUL
                                // create session
                                session.setLogin(true);

                                //store user
                                String uid = jObj.getString("uid");

                                JSONObject user = jObj.getJSONObject("user");
                                String name = user.getString("name");
                                String email = user.getString("email");
                                String created_at = user.getString("created_at");

                                // add user to sql database
                                db.addUser(name, email, uid, created_at);




                            } else {
                                // Error loggin in
                                String errMessage = jObj.getString("error_msg");
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
                Log.e(TAG, "Login error: " + error.getMessage());
                Toast.makeText(getActivity(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("id", id);

                return params;
            }
        };
        // end string request.. phew!


        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }
}
