package com.mom.soccer.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mom.soccer.R;
import com.mom.soccer.dataDto.RankingVo;
import com.mom.soccer.dto.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sungbo on 2016-07-26.
 */
public class MainViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private User user;
    private int[] layouts;

    public MainViewPagerAdapter() {
    }

    public MainViewPagerAdapter(Context context, int[] layouts, User user) {
        this.context = context;
        this.layouts = layouts;
        this.user = user;
    }

    @Override
    public int getCount() {
        return layouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(layouts[position],container,false);
        container.addView(view);

        if(position == 0){

            ListView listView = (ListView) container.findViewById(R.id.list_total_ranking);
            List<RankingVo> listVos = new ArrayList<RankingVo>();
            listVos.add(new RankingVo(1,"1",user.getProfileimgurl(),user.getUsername(),"서울FC","67","24,320","골드메달"));
            listVos.add(new RankingVo(2,"2",user.getProfileimgurl(),"토탈랭킹1","서울FC","20","57,320","골드메달"));
            listVos.add(new RankingVo(2,"3",user.getProfileimgurl(),"토탈랭킹2","서울FC","20","45,320","골드메달"));

            MainRankingAdapter mainRankingAdapter = new MainRankingAdapter(context, R.layout.adabter_mainlist_layout,listVos);
            listView.setAdapter(mainRankingAdapter);

        }else if(position==1){

            ListView listView = (ListView) container.findViewById(R.id.list_friend_ranking);
            List<RankingVo> listVos = new ArrayList<RankingVo>();
            listVos.add(new RankingVo(2,"2",user.getProfileimgurl(),"친구랭킹1","서울FC","40","24,320","골드메달"));
            listVos.add(new RankingVo(2,"3",user.getProfileimgurl(),"친구랭킹2","서울FC","1","24,320","골드메달"));

            MainRankingAdapter mainRankingAdapter = new MainRankingAdapter(context, R.layout.adabter_mainlist_layout,listVos);
            listView.setAdapter(mainRankingAdapter);

        }else if(position==2){

            ListView listView = (ListView) container.findViewById(R.id.list_team_ranking);
            List<RankingVo> listVos = new ArrayList<RankingVo>();
            listVos.add(new RankingVo(1,"1",user.getProfileimgurl(),user.getUsername(),"서울FC","34","24,320","골드메달"));
            listVos.add(new RankingVo(2,"2",user.getProfileimgurl(),"111111111팀랭킹1","서울FC","23","24,320","골드메달"));
            listVos.add(new RankingVo(3,"3",user.getProfileimgurl(),"팀랭킹2","서울FC","14","24,320","골드메달"));

            MainRankingAdapter mainRankingAdapter = new MainRankingAdapter(context, R.layout.adabter_mainlist_layout,listVos);
            listView.setAdapter(mainRankingAdapter);
        }

        return view;
    }

}
