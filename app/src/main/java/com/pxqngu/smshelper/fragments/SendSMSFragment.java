package com.pxqngu.smshelper.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.lfilepickerlibrary.utils.StringUtils;
import com.pxqngu.smshelper.R;
import com.pxqngu.smshelper.SMSHelper;

import org.w3c.dom.Text;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SendSMSFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SendSMSFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SendSMSFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ArrayAdapter<String> arrayAdapter;

    public SendSMSFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SendSMSFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SendSMSFragment newInstance(String param1, String param2) {
        SendSMSFragment fragment = new SendSMSFragment();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_send_sms, container, false);
        final Spinner spinner = view.findViewById(R.id.spinner);
        RequestParams params = new RequestParams(SMSHelper.config.get("get_tpl"));
        params.addQueryStringParameter("apikey" , SMSHelper.config.get("apikey"));

        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                List<String> list = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(result);
                for (int i = 0 ; i < jsonArray.size() ; i++){
                    list.add((String)jsonArray.getJSONObject(i).get("tpl_content"));
                }
                arrayAdapter = new ArrayAdapter<String>(getActivity() , android.R.layout.simple_list_item_1 , list);
                spinner.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

        Button button1 = view.findViewById(R.id.but_send);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                StringBuffer phoneList = new StringBuffer();
                for (Iterator<String> iterator = SMSHelper.phoneList.iterator(); iterator.hasNext();){
                    String string = (String) iterator.next();
                    phoneList.append(string);
                    if (iterator.hasNext()){
                        phoneList.append(",");
                    }
                }
                String tpl = (String)spinner.getSelectedItem();
                RequestParams params = new RequestParams(SMSHelper.config.get("batch_send"));
                params.addQueryStringParameter("apikey" , SMSHelper.config.get("apikey"));
                params.addQueryStringParameter("mobile" , phoneList.toString());
                params.addQueryStringParameter("text" , tpl);
                x.http().post(params, new Callback.CommonCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        JSONObject jsonObject = new JSONObject(result);
                        Toast.makeText(getActivity() , "发送成功!共计" + jsonObject.get("total_count") + "条!", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(Throwable ex, boolean isOnCallback) {
                        ex.getMessage();

                    }

                    @Override
                    public void onCancelled(CancelledException cex) {

                    }

                    @Override
                    public void onFinished() {

                    }
                });
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
        void onFragmentInteraction(Uri uri);
    }
}
