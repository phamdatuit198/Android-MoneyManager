package com.inc.apex.moneymanager.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;


import com.inc.apex.moneymanager.Adapter.MoneyAdapter;
import com.inc.apex.moneymanager.Object.DateEvent;
import com.inc.apex.moneymanager.R;
import com.inc.apex.moneymanager.Unit.Constant;
import com.inc.apex.moneymanager.Unit.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment  {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Context mContext;
    public MoneyAdapter adapter;
    public static ListView mListActivity;
    private MoneyAdapter.OnUpdateSummary listenerUpdateTable;
    private OnFragmentInteractionListener mListener;
    public static List<DateEvent> listDateEvent = new ArrayList<DateEvent>();
    private ImageView mImageAdd;

    public HomeFragment(){}

    public HomeFragment(Context context) {
        // Required empty public constructor
        mContext = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2, Context context) {

        HomeFragment fragment = new HomeFragment(context);
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
        // Inflate the layout for this fragment
        View root;
        root =  inflater.inflate(R.layout.fragment_home, container, false);
        mListActivity = (ListView)root.findViewById(R.id.lv_activity_money);
        mImageAdd     = (ImageView)root.findViewById(R.id.img_add);
        onButtonPressed(Constant.HOME);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        //get List Daily from SQL
        DatabaseHelper data = DatabaseHelper.getInstance(mContext);
        listDateEvent = data.getDateEvent();
        //create Adapter
        if(listDateEvent.size()!=0) {
            adapter = new MoneyAdapter(mContext, R.layout.list_statement, listDateEvent);
            adapter.setOnUpdateListner(listenerUpdateTable);
            mListActivity.setAdapter(adapter);
            onButtonPressed(Constant.HOME);
            mImageAdd.setVisibility(View.GONE);
        }else
            mImageAdd.setVisibility(View.VISIBLE);

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(String name) {
        if (mListener != null) {
            mListener.onFragmentInteraction(name);
        }
    }




    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(String name);
    }

}
