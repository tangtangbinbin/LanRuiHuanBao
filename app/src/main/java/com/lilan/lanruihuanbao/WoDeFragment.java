package com.lilan.lanruihuanbao;

import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2016/12/14.
 */

public class WoDeFragment extends Fragment {
    LinearLayout guanyu;
    LinearLayout xgmm;
    LinearLayout exit;
    ToggleButton tobtn;
    SharedPreferences sp;
    ImageView img;
    TextView name;
    TextView time;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wode,container,false);
        guanyu = (LinearLayout) view.findViewById(R.id.wode_guanyu);
        xgmm = (LinearLayout) view.findViewById(R.id.lay_xgmm);
        exit = (LinearLayout) view.findViewById(R.id.wo_exit);
        tobtn = (ToggleButton) view.findViewById(R.id.mTogBtn);
        time = (TextView) view.findViewById(R.id.time1);
        img = (ImageView) view.findViewById(R.id.my_img);
        name = (TextView) view.findViewById(R.id.name);
        time.setText(new SimpleDateFormat("yyyy-MM-dd a KK:mm:ss ").format(new Date()));
        sp = getActivity().getSharedPreferences("userinfo", MODE_PRIVATE);
        String name1 = sp.getString("userName","");
        name.setText(name1);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"点击了头像",Toast.LENGTH_SHORT).show();
            }
        });

        String status = getActivity().getSharedPreferences("btnconfig", MODE_PRIVATE).getString("status","");
        //Toast.makeText(getActivity().getApplicationContext(),"btn状态："+status,Toast.LENGTH_SHORT).show();
        if (status.equals("1")){
            //Toast.makeText(getActivity().getApplicationContext(),"设置有问题",Toast.LENGTH_SHORT).show();
            tobtn.setChecked(true);
            tobtn.setBackgroundResource(R.drawable.toggle_on);
        }else{
            tobtn.setChecked(false);
            tobtn.setBackgroundResource(R.drawable.toggle_off);
            //Toast.makeText(getActivity().getApplicationContext(),"未设置",Toast.LENGTH_SHORT).show();
        }
        tobtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tobtn.isChecked()){
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("btnconfig", MODE_PRIVATE).edit();
                    editor.putString("status","1");
                    editor.commit();
                    tobtn.setBackgroundResource(R.drawable.toggle_on);
                    Toast.makeText(getActivity().getApplicationContext(),"消息提醒已打开",Toast.LENGTH_SHORT).show();
                }else{
                    SharedPreferences.Editor editor = getActivity().getSharedPreferences("btnconfig", MODE_PRIVATE).edit();
                    editor.putString("status","0");
                    editor.commit();
                    tobtn.setBackgroundResource(R.drawable.toggle_off);
                    Toast.makeText(getActivity().getApplicationContext(),"消息提醒已关闭",Toast.LENGTH_SHORT).show();
                }
            }
        });
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = getActivity().getSharedPreferences("config", MODE_PRIVATE).edit();
                editor.putString("account", "");
                editor.putString("password", "");
                editor.commit();
                Intent intent = new Intent();
                intent.setClass(getActivity(),LoginActivity.class);
                startActivity(intent);
            }
        });
        xgmm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),Activity_XGMM.class);
                startActivity(intent);
            }
        });
        guanyu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),Activiy_GuanYu.class);
                startActivity(intent);
            }
        });
        return view;
    }

}
