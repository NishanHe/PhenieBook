package com.example.yesq.pheniebook;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChooseFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChooseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChooseFragment extends Fragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    ImageButton btn_table,btn_ten,btn_bad,btn_bill;

    public ChooseFragment() {}
    public static ChooseFragment newInstance(String param1, String param2) {
        ChooseFragment fragment = new ChooseFragment();
        Bundle args = new Bundle();
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose,container,false);
        view.findViewById(R.id.btn_table).setOnClickListener(this);
        view.findViewById(R.id.btn_badmin).setOnClickListener(this);
        view.findViewById(R.id.btn_billiards).setOnClickListener(this);
        view.findViewById(R.id.btn_tennis).setOnClickListener(this);
        return view;
    }
    @Override
    public void  onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//

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

    @Override
    public void onClick(View v) {
        int id=v.getId();
        Intent courtIntent;
        switch(id){
            case R.id.btn_table:
                courtIntent = new Intent(getActivity(), PeekActivity.class);
                courtIntent.putExtra("key_courtType", "乒乓球");
                courtIntent.putExtra("frag_id", "choose");
                startActivityForResult(courtIntent, 1);
                break;
            case R.id.btn_badmin:
                courtIntent = new Intent(getActivity(), PeekActivity.class);
                courtIntent.putExtra("key_courtType", "羽毛球");
                courtIntent.putExtra("frag_id", "choose");
                startActivityForResult(courtIntent, 2);
                break;
            case R.id.btn_billiards:
                courtIntent = new Intent(getActivity(), PeekActivity.class);
                courtIntent.putExtra("key_courtType", "台球");
                courtIntent.putExtra("frag_id", "choose");
                startActivityForResult(courtIntent, 3);
                break;
            case R.id.btn_tennis:
                courtIntent = new Intent(getActivity(), PeekActivity.class);
                courtIntent.putExtra("key_courtType", "网球");
                courtIntent.putExtra("frag_id", "choose");
                startActivityForResult(courtIntent, 4);
                break;
        }
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
