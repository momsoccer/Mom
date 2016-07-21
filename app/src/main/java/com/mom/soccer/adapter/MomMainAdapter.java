package com.mom.soccer.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mom.soccer.R;
import com.mom.soccer.dataDto.MainTypeListVo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by sungbo on 2016-07-21.
 */
public class MomMainAdapter extends BaseAdapter {

    private static final String TAG = "MomMainAdapter";

    private Context mContext = null;
    private int layout = 0;
    private LayoutInflater inflater = null;
    private List<MainTypeListVo> mainTypeListVos;
    private HashMap<View, MainTypeListVo> mLoaders;


    public MomMainAdapter(Context mContext, int layout, List<MainTypeListVo> mainTypeListVos) {

        this.mContext = mContext;
        this.layout = layout;
        this.mainTypeListVos = mainTypeListVos;

        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLoaders = new HashMap<View, MainTypeListVo>();
    }

    @Override
    public int getCount() {
        return mainTypeListVos.size();
    }

    @Override
    public Object getItem(int i) {
        return mainTypeListVos.get(i).getTypeId();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        View currentRow = convertView;
        MainListHolder listHolder;

        if(currentRow ==null){
            currentRow = inflater.inflate(layout, parent, false);
            listHolder = new MainListHolder();
            listHolder.name = (TextView) currentRow.findViewById(R.id.main_list_name);
            //listHolder.description = (TextView) currentRow.findViewById(R.id.main_list_disp);

            listHolder.name.setText(mainTypeListVos.get(i).getName());
            //listHolder.description.setText(mainTypeListVos.get(i).getDescription());

            Log.d(TAG,"첫번째 리스트");

            currentRow.setTag(listHolder);

        }else{

            listHolder = (MainListHolder) currentRow.getTag();

            if(mainTypeListVos.get(i) != null) {
                listHolder.name.setText(mainTypeListVos.get(i).getName());
                //listHolder.description.setText(mainTypeListVos.get(i).getDescription());

                Log.d(TAG,"두번째 리스트");
            }
        }

        return currentRow;
    }

    static class MainListHolder {
        TextView name;
        //TextView description;
    }

}
