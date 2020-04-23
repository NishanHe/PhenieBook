package com.example.yesq.pheniebook;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class InfoFragment extends android.support.v4.app.Fragment implements GestureDetector.OnGestureListener {

    private ViewFlipper flipper;
    private int[] resId = {R.drawable.pc1,R.drawable.pc2,R.drawable.pc3,R.drawable.pc4};
    private GestureDetector detector;

    PullToRefreshListView mPullToRefreshView;
    private ILoadingLayout loadingLayout;
    ListView listview;
    List<Competition> comMsg = new ArrayList<Competition>();
    String lastTime = null;

    private OnFragmentInteractionListener mListener;

    public InfoFragment() {
    }

    public static InfoFragment newInstance(String param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return  LayoutInflater.from(getActivity()).inflate(R.layout.fragment_info, null);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bmob.initialize(getContext(), "a9fdb03fa2353032ad9b00229df6cd9a","bmob");
        flipper = (ViewFlipper)getView().findViewById(R.id.flipper);
        detector = new GestureDetector(this);
        for (int i = 0; i < resId.length; i++) {
            flipper.addView(getImageView(resId[i]));
        }
        flipper.setInAnimation(getContext(), R.anim.left_in);
        flipper.setOutAnimation(getContext(), R.anim.left_out);
        flipper.setFlipInterval(5000);
        flipper.startFlipping();

        initListView();
        queryData(0, STATE_REFRESH);
    }

    private void initListView() {
        mPullToRefreshView = (PullToRefreshListView)getView().findViewById(R.id.list);
        loadingLayout = mPullToRefreshView.getLoadingLayoutProxy();
        loadingLayout.setLastUpdatedLabel("");
        loadingLayout.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
        loadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
        loadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
        // 滑动监听
        mPullToRefreshView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    loadingLayout.setLastUpdatedLabel("");
                    loadingLayout.setPullLabel(getString(R.string.pull_to_refresh_top_pull));
                    loadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_top_refreshing));
                    loadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_top_release));
                } else if (firstVisibleItem + visibleItemCount + 1 == totalItemCount) {
                    loadingLayout.setLastUpdatedLabel("");
                    loadingLayout.setPullLabel(getString(R.string.pull_to_refresh_bottom_pull));
                    loadingLayout.setRefreshingLabel(getString(R.string.pull_to_refresh_bottom_refreshing));
                    loadingLayout.setReleaseLabel(getString(R.string.pull_to_refresh_bottom_release));
                }
            }
        });
        // 下拉刷新监听
        mPullToRefreshView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 下拉刷新(从第一页开始装载数据)
                queryData(0, STATE_REFRESH);
            }
            @Override
            public void onPullUpToRefresh(
                    PullToRefreshBase<ListView> refreshView) {
                // 上拉加载更多(加载下一页数据)
                queryData(curPage, STATE_MORE);
            }
        });
        listview = mPullToRefreshView.getRefreshableView();

        // 再设置adapter
        listview.setAdapter(new DeviceListAdapter(getContext()));
    }
    private static final int STATE_REFRESH = 0;// 下拉刷新
    private static final int STATE_MORE = 1;// 加载更多
    private int limit = 10; // 每页的数据是10条
    private int curPage = 0; // 当前页的编号，从0开始

    private ImageView getImageView(int resId){

        ImageView image = new ImageView(getContext());
        image.setBackgroundResource(resId);
        return image;
    }


    private void queryData(int page, final int actionType) {
        Log.i("error", "pageN:" + page + " limit:" + limit + " actionType:" + actionType);
        BmobQuery<Competition> query = new BmobQuery<>();
        // 按时间降序查询
        query.order("-createdAt");
        int count = 0;
        // 如果是加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                Log.i("0621", date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(page * count + 1);
        } else {
            // 下拉刷新
            page = 0;
            query.setSkip(page);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<Competition>()  {
            @Override
            public void done(List<Competition> list, BmobException e) {
                if(e == null){
                    if (list.size() > 0) {
                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            comMsg.clear();
                            // 获取最后时间
                            lastTime = list.get(list.size() - 1).getCreatedAt();
                        }
                        // 将本次查询的数据添加到bankCards中
                        for (Competition td : list) {
                            comMsg.add(td);
                        }
                        // 这里在每次加载完数据后，将当前页码+1，这样在上拉刷新的onPullUpToRefresh方法中就不需要操作curPage了
                        curPage++;
//					 showToast("第"+(page+1)+"页数据加载完成");
                    } else if (actionType == STATE_MORE) {
                        showToast("没有更多数据了");

                    } else if (actionType == STATE_REFRESH) {
                        showToast("没有数据");
                    }
                    mPullToRefreshView.onRefreshComplete();
                }
                else{
                    showToast("查询失败:" + e.getMessage());
                    mPullToRefreshView.onRefreshComplete();
                }
            }
        });
    }
    private class DeviceListAdapter extends BaseAdapter {
        Context context;
        public DeviceListAdapter(Context context) {
            this.context = context;
        }
        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(
                        R.layout.list_item_commsg, null);
                holder = new ViewHolder();
                holder.list_info_title = (TextView) convertView.findViewById(R.id.list_info_title);
                holder.list_info_content = (TextView) convertView.findViewById(R.id.list_info_content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Competition td = (Competition) getItem(position);
            holder.list_info_title.setText(td.getTitle());
            holder.list_info_content.setText(td.getContent());
            return convertView;
        }
        class ViewHolder {
            TextView list_info_title;
            TextView list_info_content;
        }
        @Override
        public int getCount() {
            return comMsg.size();
        }
        @Override
        public Object getItem(int position) {
            return comMsg.get(position);
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
    }
    private void showToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("onFling", "e1="+e1.getX()+" e2="+e2.getX()+" e1-e2="+(e1.getX()-e2.getX()));
        if(e1.getX()-e2.getX()>120){
            flipper.setInAnimation(getContext(), R.anim.left_in);
            flipper.setOutAnimation(getContext(), R.anim.left_out);
            flipper.showNext();//向右滑动
            return true;
            }else if(e1.getX()-e2.getY()<-120){
            flipper.setInAnimation(getContext(), R.anim.right_in);
            flipper.setOutAnimation(getContext(), R.anim.right_out);
            flipper.showPrevious();//向左滑动
            return true;
             }
        return false;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public boolean onTouchEvent(MotionEvent event) {
// TODO Auto-generated method stub
//
        return this.detector.onTouchEvent(event);
    }
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }

}
