package com.pxqngu.smshelper;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.pxqngu.smshelper.adapter.BaseFragmentPagerAdapter;
import com.pxqngu.smshelper.adapter.PhoneListAdapter;
import com.pxqngu.smshelper.bean.YunPianConfig;
import com.pxqngu.smshelper.fragments.NumberListFragment;
import com.pxqngu.smshelper.fragments.SendSMSFragment;
import com.pxqngu.smshelper.fragments.SetNumberFragment;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.hutool.http.HttpUtil;
import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

public class MainActivity extends AppCompatActivity
        implements SetNumberFragment.OnFragmentInteractionListener
        ,NumberListFragment.OnFragmentInteractionListener
        ,SendSMSFragment.OnFragmentInteractionListener{

    @BindView(R.id.qmui_tab_segment) QMUITabSegment mQmuiTabSegment;
    @BindView(R.id.view_pager) ViewPager mViewPager;

    private List<Fragment> fragments;
    private BaseFragmentPagerAdapter adapter;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //申请权限.
        PermissionGen.with(MainActivity.this)
                .addRequestCode(100)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE
                ).request();
    }

    private void initBmob(){
        Bmob.initialize(this , "fa772322e0a6c3812e4e8e264eef295a");
        BmobQuery<YunPianConfig> query = new BmobQuery<>();
        query.setLimit(100);
        query.findObjects(new FindListener<YunPianConfig>() {
            @Override
            public void done(List<YunPianConfig> list, BmobException e) {
                if (e == null){
                    SMSHelper.config = new HashMap<>();
                    for (YunPianConfig yunPianConfig : list){
                        SMSHelper.config.put(yunPianConfig.getParameter_name() , yunPianConfig.getValue());
                    }
                }else {
                    System.out.println("失败！" + e.getMessage());
                }
            }
        });
    }

    private void initView(){
        fragments= new ArrayList<>();
        SetNumberFragment setNumberFragment = new SetNumberFragment();
        final NumberListFragment numberListFragment = new NumberListFragment();
        SendSMSFragment sendSMSFragment = new SendSMSFragment();
        fragments.add(setNumberFragment);
        fragments.add(numberListFragment);
        fragments.add(sendSMSFragment);
        adapter = new BaseFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        mViewPager.setAdapter(adapter);
        mQmuiTabSegment.setupWithViewPager(mViewPager , false);
        mQmuiTabSegment.reset();
        mQmuiTabSegment.addTab(new QMUITabSegment.Tab("设置号码"));
        mQmuiTabSegment.addTab(new QMUITabSegment.Tab("发送列表"));
        mQmuiTabSegment.addTab(new QMUITabSegment.Tab("发送通知"));
        mQmuiTabSegment.setMode(QMUITabSegment.MODE_FIXED);
        mQmuiTabSegment.notifyDataChanged();
        mQmuiTabSegment.addOnTabSelectedListener(new QMUITabSegment.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int index) {
                switch (index){
                    case 0:

                        break;
                    case 1:
                        if (!SMSHelper.phoneList.isEmpty()){
                            SMSHelper.phoneListAdapter = new PhoneListAdapter(R.layout.item_phone_number_list , SMSHelper.phoneList);
                            mRecyclerView = numberListFragment.getView().findViewById(R.id.phone_list_recyclerview);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(numberListFragment.getContext()));
                            mRecyclerView.setAdapter(SMSHelper.phoneListAdapter);
                        }
                        break;
                    case 2:


                        break;
                }

            }

            @Override
            public void onTabUnselected(int index) {

            }

            @Override
            public void onTabReselected(int index) {

            }

            @Override
            public void onDoubleTap(int index) {

            }
        });
    }

    //实现OnFragmentInteractionListener接口中的函数
    public void onFragmentInteraction(Uri uri){

    }

    //申请权限-----------------------------------------------------------------------------------
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    public void loadingDataSuccess(){
        initBmob();
        initView();
    }

    @PermissionFail(requestCode = 100)
    public void loadingDataFail(){
        Toast.makeText(this , "程序需要读写SD卡的权限，否则无法运行！" , Toast.LENGTH_LONG).show();
        this.finish();
    }
}
