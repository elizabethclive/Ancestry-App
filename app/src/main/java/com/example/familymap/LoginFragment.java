package com.example.familymap;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import Model.AuthToken;
import Model.Model;
import Model.Person;
import Model.Event;
import Server.ServerProxy;
import Util.EventsResult;
import Util.JsonHandler;
import Util.LoginRequest;
import Util.LoginResult;
import Util.PersonsResult;
import Util.RandomString;
import Util.RegisterRequest;
import Util.RegisterResult;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {
    private EditText editServerHost;
    private EditText editServerPort;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editFirstName;
    private EditText editLastName;
    private EditText editEmail;
    private RadioGroup editGender;
    private Button loginButton;
    private Button registerButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        editServerHost = view.findViewById(R.id.edit_server_host);
        editServerPort = view.findViewById(R.id.edit_server_port);
        editUsername = view.findViewById(R.id.edit_username);
        editPassword = view.findViewById(R.id.edit_password);
        editFirstName = view.findViewById(R.id.edit_first_name);
        editLastName = view.findViewById(R.id.edit_last_name);
        editEmail = view.findViewById(R.id.edit_email);
        editGender = view.findViewById(R.id.btn_gender);
        loginButton = view.findViewById(R.id.btn_login);
        registerButton = view.findViewById(R.id.btn_register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().setHost(editServerHost.getText().toString());
                Model.getInstance().setPort(editServerPort.getText().toString());
                LoginRequest loginRequest = new LoginRequest(editUsername.getText().toString(), editPassword.getText().toString());
                new LoginTask().execute(loginRequest);
//                Toast.makeText(getContext(), "WORKS", Toast.LENGTH_SHORT).show();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Model.getInstance().setHost(editServerHost.getText().toString());
                Model.getInstance().setPort(editServerPort.getText().toString());
                int radioButtonID = editGender.getCheckedRadioButtonId();
                View radioButton = editGender.findViewById(radioButtonID);
                int idx = editGender.indexOfChild(radioButton);
                RadioButton r = (RadioButton) editGender.getChildAt(idx);
                String selectedGender = r.getText().toString().substring(0,1);
                RegisterRequest registerRequest = new RegisterRequest(
                        editUsername.getText().toString(),
                        editPassword.getText().toString(),
                        editEmail.getText().toString(),
                        editFirstName.getText().toString(),
                        editLastName.getText().toString(),
                        selectedGender,
                        RandomString.getRandomString()
                );
                new RegisterTask().execute(registerRequest);
            }
        });

        RadioButton female = view.findViewById(R.id.female);
        female.toggle();

        TextWatcher loginWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if ((editServerHost.getText().toString().length() != 0) &&
                        (editServerPort.getText().toString().length() != 0) &&
                        (editUsername.getText().toString().length() != 0) &&
                        (editPassword.getText().toString().length() != 0)) {
                    loginButton.setEnabled(true);
                    loginButton.setAlpha(1f);
                } else {
                    loginButton.setEnabled(false);
                    loginButton.setAlpha(0.4f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        TextWatcher registerWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int count, int after) {
                if ((editServerHost.getText().toString().length() != 0) &&
                        (editServerPort.getText().toString().length() != 0) &&
                        (editUsername.getText().toString().length() != 0) &&
                        (editPassword.getText().toString().length() != 0) &&
                        (editFirstName.getText().toString().length() != 0) &&
                        (editLastName.getText().toString().length() != 0) &&
                        (editEmail.getText().toString().length() != 0)) {
                    registerButton.setEnabled(true);
                    registerButton.setAlpha(1f);
                } else {
                    registerButton.setEnabled(false);
                    registerButton.setAlpha(0.4f);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        };

        editServerHost.addTextChangedListener(loginWatcher);
        editServerPort.addTextChangedListener(loginWatcher);
        editUsername.addTextChangedListener(loginWatcher);
        editPassword.addTextChangedListener(loginWatcher);

        editServerHost.addTextChangedListener(registerWatcher);
        editServerPort.addTextChangedListener(registerWatcher);
        editUsername.addTextChangedListener(registerWatcher);
        editPassword.addTextChangedListener(registerWatcher);
        editFirstName.addTextChangedListener(registerWatcher);
        editLastName.addTextChangedListener(registerWatcher);
        editEmail.addTextChangedListener(registerWatcher);

        return view;
    }

    private class LoginTask extends AsyncTask<LoginRequest, Void, LoginResult> {

        @Override
        protected LoginResult doInBackground(LoginRequest... params) {
            ServerProxy server = new ServerProxy();
            LoginResult loginResult;
            try {
                loginResult = server.login(Model.getInstance().getHost(), Model.getInstance().getPort(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                loginResult = new LoginResult(false, e.getMessage());
            }
            return loginResult;
        }

        @Override
        protected void onPostExecute(LoginResult loginResult) {
            if (loginResult.isSuccess()) {
                AuthToken authToken = JsonHandler.deserialize(loginResult.getResult(), AuthToken.class);
                Model.getInstance().setAuthToken(authToken.getToken());
                Model.getInstance().setUserName(authToken.getUsername());
                Model.getInstance().setPersonID(authToken.getPersonID());
                new DataTask().execute(Model.getInstance().getAuthToken());
            } else {
                Toast.makeText(getContext(), loginResult.getResult(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class RegisterTask extends AsyncTask<RegisterRequest, Void, RegisterResult> {

        @Override
        protected RegisterResult doInBackground(RegisterRequest... params) {
            ServerProxy server = new ServerProxy();
            RegisterResult registerResult;
            try {
                registerResult = server.register(Model.getInstance().getHost(), Model.getInstance().getPort(), params[0]);
            } catch (Exception e) {
                e.printStackTrace();
                registerResult = new RegisterResult(false, e.getMessage());
            }
            return registerResult;
        }

        @Override
        protected void onPostExecute(RegisterResult registerResult) {
            if (registerResult.isSuccess()) {
                AuthToken authToken = JsonHandler.deserialize(registerResult.getResult(), AuthToken.class);
                Model.getInstance().setAuthToken(authToken.getToken());
                Model.getInstance().setUserName(authToken.getUsername());
                Model.getInstance().setPersonID(authToken.getPersonID());

                new DataTask().execute(Model.getInstance().getAuthToken());
            } else {
                Toast.makeText(getContext(), registerResult.getResult(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    private class DataTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            ServerProxy server = new ServerProxy();
            PersonsResult personsResult;
            EventsResult eventsResult;
            try {
                personsResult = server.persons(Model.getInstance().getHost(), Model.getInstance().getPort(), params[0]);
                eventsResult = server.events(Model.getInstance().getHost(), Model.getInstance().getPort(), params[0]);
                Person[] persons = personsResult.getData();
                Event[] events = eventsResult.getData();
                Model.getInstance().setPersons(persons);
                Model.getInstance().setEvents(events);

                Model.getInstance().setFirstName(Model.getInstance().getPersonFromMap(Model.getInstance().getPersonID()).getFirstName());
                Model.getInstance().setLastName(Model.getInstance().getPersonFromMap(Model.getInstance().getPersonID()).getLastName());
                return "success";
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result.equals("success")) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.map_fragment, new MapFragment()).commit();
            } else {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {}
    }

    @Override
    public void onStop() {
        super.onStop();
    }
}
