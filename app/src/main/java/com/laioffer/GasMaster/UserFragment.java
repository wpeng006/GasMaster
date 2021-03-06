package com.laioffer.GasMaster;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.laioffer.GasMaster.Config.Config;
import com.laioffer.GasMaster.Model.User;


import butterknife.BindView;

public class UserFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private User usr = new User.UserBuilder()
//      .email("unknown@gmail.com")
//      .firstName("Joseph")
//      .fullName("Joestar")
//      .build();
// Todo: connect usr to global variable currentUser
    private User usr = Config.currentUser;



    @BindView(R.id.btn_logout) Button _logoutButton;


    public UserFragment() {

    }

    public static UserFragment newInstance(String param1, String param2) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_user, container, false);

        // Todo: Demo how to access usr object and set name

        TextView name = (TextView) view.findViewById(R.id.user_name);
        TextView email = (TextView) view.findViewById(R.id.user_email);
        TextView trips = (TextView) view.findViewById(R.id.user_trip);
        TextView promotion = (TextView) view.findViewById(R.id.user_promotion);

        name.setText(usr.getName());
        email.setText(usr.getEmail());
        trips.setText(usr.getTrip());
        promotion.setText(usr.getPromotion());
        // Todo: End of demo

        Button btnLogout = view.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Todo: Need to implelement about how to deal with session or token

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }

        });

        return view;

 //       return inflater.inflate(R.layout.fragment_user, container, false);

    }
}
