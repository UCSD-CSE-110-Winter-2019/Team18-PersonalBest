package com.android.personalbest;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

public class ProfileFragment extends Fragment {

    private TextView mTextMessage;
    SharedPreferences sharedPreferences;
    TextView heightext;
    Button edit_height;
    EditText height_edit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_profile, null);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("user_info", MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "");
        int height = sharedPreferences.getInt("height", 0);
        TextView nametext = (TextView) getView().findViewById(R.id.user_txt);
        heightext = (TextView) getView().findViewById(R.id.current_height);
        height_edit = (EditText) getView().findViewById(R.id.height_edit);
        nametext.setText(name);
        heightext.setText(String.valueOf(height));
        //mTextMessage = (TextView) getView().findViewById(R.id.message);
        //BottomNavigationView navigation = (BottomNavigationView) getView().findViewById(R.id.navigation);
        //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        edit_height = getView().findViewById(R.id.edit_height_btn);
        edit_height.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("my_tag", edit_height.getText().toString());
                if (edit_height.getText().toString() == "save") {
                    Log.d("my_tag", edit_height.getText().toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("height", Integer.parseInt((height_edit.getText()).toString()));
                    edit_height.setText("edit");
                    height_edit.setVisibility(View.GONE);
                }
                if (edit_height.getText().toString() == "edit") {
                    Log.d("my_tag", edit_height.getText().toString());

                    edit_height.setText("save");
                    height_edit.setVisibility(View.VISIBLE);
                    heightext.setVisibility(View.GONE);
                }
            }
        });
    }
}
