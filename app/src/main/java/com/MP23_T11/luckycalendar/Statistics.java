package com.MP23_T11.luckycalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {

    private DateViewModel viewModel; // DateViewModel 인스턴스를 저장하는 변수

    private final String[] monthNames = new String[]{"January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Statistics() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Statistics.
     */
    // TODO: Rename and change types and number of parameters
    public static Statistics newInstance(String param1, String param2) {
        Statistics fragment = new Statistics();
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
        View view = inflater.inflate(R.layout.fragment_statistics, container, false);

        // 데이터 준비
        List<Memo> memos = new ArrayList<>(); // 여기에 메모 데이터를 채워넣으면됨. 예를 들어, new Memo("02", "Thursday"), new Memo("03", "Friday") 등으로 채울 수 있음.

        memos.add(new Memo("01", "Monday", ""));
        memos.add(new Memo("02", "Tuesday", ""));
        memos.add(new Memo("03", "Wednesday", ""));



        // RecyclerView 설정
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        DateBoxAdapter adapter = new DateBoxAdapter(memos); // memos는 메모 데이터 리스트
        recyclerView.setAdapter(adapter);

        // RecyclerView 아이템 간격 설정
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));


        // ViewModel 인스턴스 콜
        viewModel = new ViewModelProvider(requireActivity()).get(DateViewModel.class);

        // 레이아웃의 뷰를 참조.
        ImageButton leftButton = view.findViewById(R.id.leftarrow);
        ImageButton rightButton = view.findViewById(R.id.rightarrow);
        TextView monthTextView = view.findViewById(R.id.month);
        TextView yearTextView = view.findViewById(R.id.year);

        // 현재 날짜를 표시
        updateDate(monthTextView, yearTextView);

        // 왼쪽 버튼 클릭 시 월과 연도를 감소시키는 이벤트 리스너를 설정
        leftButton.setOnClickListener(v -> {
            int month = viewModel.getMonth();
            int year = viewModel.getYear();
            if (month == 1) {
                month = 12;
                year--;
            } else {
                month--;
            }
            viewModel.setMonth(month);
            viewModel.setYear(year);
            updateDate(monthTextView, yearTextView);
        });

        // 오른쪽 버튼 클릭 시 월과 연도를 증가시키는 이벤트 리스너를 설정
        rightButton.setOnClickListener(v -> {
            int month = viewModel.getMonth();
            int year = viewModel.getYear();
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
            viewModel.setMonth(month);
            viewModel.setYear(year);
            updateDate(monthTextView, yearTextView);
        });


        //아래쪽 버튼 클릭 리스너
        ImageButton leftButtonBelow = view.findViewById(R.id.left_arrow_below);
        ImageButton rightButtonBelow = view.findViewById(R.id.right_arrow_below);

        leftButtonBelow.setOnClickListener(v -> {
            adapter.selectPreviousItem();
        });

        rightButtonBelow.setOnClickListener(v -> {
            adapter.selectNextItem();
        });



        // 생성된 뷰를 반환
        return view;
    }

    // 월과 연도 텍스트 뷰를 업데이트하는 메서드
    private void updateDate(TextView monthTextView, TextView yearTextView) {
        monthTextView.setText(monthNames[viewModel.getMonth() - 1]);
        yearTextView.setText(String.valueOf(viewModel.getYear()));
    }
}