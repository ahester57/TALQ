package edu.umsl.hester.superclickers.activity.home;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import edu.umsl.hester.superclickers.R;
import edu.umsl.hester.superclickers.app.AppController;
import edu.umsl.hester.superclickers.app.LoginConfig;
import edu.umsl.hester.superclickers.userdata.User;
import edu.umsl.hester.superclickers.database.SQLiteHandler;

/**
 * Created by Austin on 2/7/2017.
 *
 */

public class GroupActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = GroupActivity.class.getSimpleName();

    private Button btnCreate, btnJoin;
    private EditText editGroupName;

    private ProgressDialog pDialog;
    private SQLiteHandler db;

    private User user;

    // @TODO if user is in a group, then show groups + members

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        btnCreate = (Button) findViewById(R.id.create_group_button);
        btnJoin = (Button) findViewById(R.id.join_group_button);
        editGroupName = (EditText) findViewById(R.id.group_name_edit_text);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // database stuff
        db = new SQLiteHandler(getApplicationContext());
        HashMap<String, String> userDetails = db.getUserDetails();
        this.user = new User(userDetails.get("name"), userDetails.get("email"), userDetails.get("uid"));

        // load fragment ... switch to recycler view
        GroupFragment gFragment = new GroupFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.group_frame, gFragment);
        ft.commit();

        btnCreate.setOnClickListener(this);
        btnJoin.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.create_group_button:
                if (!editGroupName.getText().toString().trim().equals("")) {
                    createGroup(editGroupName.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a group name, I say!",
                            Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.join_group_button:
                if (!editGroupName.getText().toString().trim().equals("")) {
                    joinGroup(user.getUniqueId(), editGroupName.getText().toString());
                } else {
                    Toast.makeText(getApplicationContext(), "Enter a group name, I say!",
                            Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    // Store new group, uploads to group URL
    private void createGroup(final String name) {
        String tag_str_req = "req_create_group";

        pDialog.setMessage("Makin group...");
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_CREATE_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Group make Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                // group create SUCCESSFUL
                                // store group in db

                                //store user
                                String guid = jObj.getString("guid");

                                JSONObject group = jObj.getJSONObject("group");
                                String name = group.getString("name");
                                String created_at = group.getString("created_at");

                                // add user to sql database
                                db.addGroup(name, guid, created_at);
                                joinGroup(user.getUniqueId(), name);

                                Toast.makeText(getApplicationContext(), "Group registered", Toast.LENGTH_LONG).show();



                            } else {
                                // Error loggin in
                                String errMessage = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON error: "
                                    + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Create group error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("user_id", user.getUniqueId());

                return params;
            }
        };
        // end string request.. phew!

        Log.d(TAG, "we made it");

        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    // add user to group, uploads to group URL
    private void joinGroup(final String user_id, final String name) {
        String tag_str_req = "req_join_group";

        pDialog.setMessage("Joinin group...");
        showDialog();
        // new string request
        StringRequest strReq = new StringRequest(Request.Method.POST, LoginConfig.URL_JOIN_GROUP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(TAG, "Group add Response: " + response);
                        hideDialog();

                        try {
                            JSONObject jObj = new JSONObject(response);
                            boolean error = jObj.getBoolean("error");

                            if (!error) {
                                // group add SUCCESSFUL
                                // store group in db

                                //store user


                                JSONObject user_group = jObj.getJSONObject("user_group");
                                String guid = user_group.getString("group_id");
                                String created_at = user_group.getString("created_at");

                                db.addUserToGroup(user.getUniqueId(), guid, created_at);

                                Toast.makeText(getApplicationContext(), "Group joined", Toast.LENGTH_LONG).show();



                            } else {
                                // Error loggin in
                                String errMessage = jObj.getString("error_msg");
                                Toast.makeText(getApplicationContext(), errMessage,
                                        Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON error: "
                                    + e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Join group error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(),
                        Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // put params to login url via POST
                Map<String, String> params = new HashMap<>();
                params.put("user_id", user_id);
                params.put("name", name);

                return params;
            }
        };
        // end string request.. phew!

        Log.d(TAG, "we made it");

        AppController.getInstance().addToRequestQueue(strReq, tag_str_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hideDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();
        }
    }


}
