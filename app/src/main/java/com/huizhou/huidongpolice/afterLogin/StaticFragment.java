package com.huizhou.huidongpolice.afterLogin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.huizhou.huidongpolice.LoginActivity;
import com.huizhou.huidongpolice.R;
import com.huizhou.huidongpolice.utils.Log4J;
import com.huizhou.huidongpolice.utils.ServerConfig;

import java.io.File;


public class StaticFragment extends Fragment
{
    private View view;
    private Button weekStatic;
    private Button monthStatic;
    private Button quarterStatic;
    private Button yearStatic;
    private Button meeting;
    private   Button files;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    )
    {
        view = inflater.inflate(R.layout.tab03, null);

        weekStatic = (Button) view.findViewById(R.id.weekStatic);
        monthStatic = (Button) view.findViewById(R.id.monthStatic);
        quarterStatic = (Button) view.findViewById(R.id.quarterStatic);
        yearStatic = (Button) view.findViewById(R.id.yearStatic);
        meeting = (Button) view.findViewById(R.id.meeting);
        files = (Button) view.findViewById(R.id.files);

        //引入布局
        return view;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);

        /**
         *
         *
         * @param view
         */
        //Button weekStatic = (Button) getActivity().findViewById(R.id.weekStatic);
        weekStatic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityStaticFilesList.class);
                intent.putExtra("fileType", "1");
                startActivity(intent);
            }
        });


        /**
         *
         *
         * @param view
         */
        // Button monthStatic = (Button) getActivity().findViewById(R.id.monthStatic);
        monthStatic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityStaticFilesList.class);
                intent.putExtra("fileType", "2");
                startActivity(intent);
            }
        });

        /**
         *
         *
         * @param view
         */
        //Button quarterStatic = (Button) getActivity().findViewById(R.id.quarterStatic);
        quarterStatic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityStaticFilesList.class);
                intent.putExtra("fileType", "3");
                startActivity(intent);
            }
        });

        /**
         *
         *
         * @param view
         */
        //Button yearStatic = (Button) getActivity().findViewById(R.id.yearStatic);
        yearStatic.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityStaticFilesList.class);
                intent.putExtra("fileType", "4");
                startActivity(intent);
            }
        });

        /**
         *
         *
         * @param view
         */
        //Button meeting = (Button) getActivity().findViewById(R.id.meeting);
        meeting.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityStaticFilesList.class);
                intent.putExtra("fileType", "5");
                startActivity(intent);
            }
        });

        /**
         *
         *
         * @param view
         */
       // Button files = (Button) getActivity().findViewById(R.id.files);
        files.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), ActivityStaticFilesList.class);
                intent.putExtra("fileType", "6");
                startActivity(intent);
            }
        });

    }

}
