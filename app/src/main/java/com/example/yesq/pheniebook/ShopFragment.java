package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShopFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShopFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShopFragment extends Fragment implements SearchView.OnQueryTextListener{
    private GridLayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    //    SharedPreferences.Editor editor;
    Button search;
    List<Shop> list;
    FloatingActionButton fab;

    @Override
    public boolean onQueryTextSubmit(String query) {
        if (TextUtils.isEmpty(query)) {
            Intent intent1 = new Intent(getContext(), ShopShowActivity.class);
            startActivity(intent1);
        }
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }


    private OnFragmentInteractionListener mListener;

    public ShopFragment() {
    }

    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
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
        View view = inflater.inflate(R.layout.fragment_shop, container, false);
        Bmob.initialize(getContext(), "8403661f07eba2e08fef16590642ac39");
        list = new ArrayList<Shop>();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.shop_recycler_view);
        initDate();

        mLayoutManager = new GridLayoutManager(getActivity(),3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext()));

        mRecyclerView.addOnItemTouchListener(new RecyclerViewClickListener(getContext(),new RecyclerViewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(getContext(),"Click "+list.get(position),Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), BuyActivity.class);

                intent.putExtra("key_proId",list.get(position).getObjid());
                intent.putExtra("key_proName",list.get(position).getObjname());
                intent.putExtra("key_proPri",list.get(position).getObjprice());
                intent.putExtra("key_objNum",list.get(position).getObjnum());
                startActivity(intent);

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(getActivity(),"Long Click "+list.get(position),Toast.LENGTH_SHORT).show();
            }
        }));


//        search = (Button)view.findViewById(R.id.shop_btn_search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent1 = new Intent(getContext(), ShopShowActivity.class);
//                startActivity(intent1);
//            }
//        });
        fab = (FloatingActionButton)view.findViewById(R.id.fab_search);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getContext(), ShopShowActivity.class);
                startActivity(intent1);
            }
        });
        return view;
    }
    private List<Shop> initDate(){

        BmobQuery<Shop> query = new BmobQuery<Shop>();
        //query.addWhereEqualTo("objname", searchName);
        query.setLimit(500);
        query.findObjects(new FindListener<Shop>() {
            @Override
            public void done(List<Shop> object, BmobException e) {
                if(e==null){
                    for(Shop product : object) {
                        product.getObjid();
                        product.getObjname();
                        product.getObjprice();
                        product.getObjnum();
                        product.getImage();
                        list.add(product);
//                        Log.i("bmob","获取到了" + list.get(0).getObjname());
                    }
                    MyAdapter adapter = new MyAdapter(list);
                    mRecyclerView.setAdapter(adapter);
                }else{
                    Log.i("bmob","失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
        return list;
    }
    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{
        List<Shop> items;
        public MyAdapter(List<Shop> items) {
            this.items = items;
        }

        /**
         * 创建ViewHolder的布局
         * @param parent
         * @param viewType
         * @return
         */

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem,parent,false);
            MyViewHolder holder = new MyViewHolder(view);
            return holder;
        }

        /**
         * 通过ViewHolder将数据绑定到界面上进行显示
         * @param holder
         * @param position
         */
        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            holder.textid.setText((CharSequence) String.valueOf(items.get(position).getObjid()));
            holder.textName.setText((CharSequence) items.get(position).getObjname());
            holder.textPrice.setText((CharSequence) String.valueOf(items.get(position).getObjprice()));
            holder.textNum.setText((CharSequence) String.valueOf(items.get(position).getObjnum()));
//            holder.image.setImageDrawable(items.get(position).getImage());

            holder.buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent1 = new Intent(getContext(), BuyActivity.class);
                    startActivity(intent1);

                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder{
            private TextView textid,textName,textPrice,textNum;
            private ImageView image;
            private Button buy;
            public MyViewHolder(View itemView) {
                super(itemView);
                textid = (TextView) itemView.findViewById(R.id.proId);
                textName = (TextView) itemView.findViewById(R.id.proName);
                textPrice = (TextView) itemView.findViewById(R.id.proPrice);
                textNum = (TextView) itemView.findViewById(R.id.proNum);
//                image = (ImageView) itemView.findViewById(R.id.proPic);
                buy = (Button) itemView.findViewById(R.id.btn_buy);
            }
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void setMenuVisibility(boolean menuVisibile) {
        super.setMenuVisibility(menuVisibile);
        if (this.getView() != null) {
            this.getView().setVisibility(menuVisibile ? View.VISIBLE : View.GONE);
        }
    }
}
