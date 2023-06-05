package com.MP23_T11.luckycalendar;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Statistics#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Statistics extends Fragment {
    private static final String TAG = "Statistics";

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    DateBoxAdapter adapter;

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



        // RecyclerView 설정
        // 최대 개수 제한 둬야할 듯
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        adapter = new DateBoxAdapter(new ArrayList<>()); // 비어있는 리스트로 어댑터 초기화
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
            loadMemos(Integer.toString(year), Integer.toString(month)); // Load memos for this year and month
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
            loadMemos(Integer.toString(year), Integer.toString(month)); // Load memos for this year and month
        });


        //아래쪽 버튼 클릭 리스너
        ImageButton leftButtonBelow = view.findViewById(R.id.left_arrow_below);
        ImageButton rightButtonBelow = view.findViewById(R.id.right_arrow_below);

        ImageView stateIcon = view.findViewById(R.id.state_icon);
        TextView stateText = view.findViewById(R.id.state_text);

        leftButtonBelow.setOnClickListener(v -> {
            adapter.selectPreviousItem();

            // 현재 선택된 메모 가져오기
            Memo selectedMemo = adapter.getSelectedItem();

            // 선택된 메모가 null이 아닌 경우에만 UI 업데이트
            if (selectedMemo != null) {
                // emotion에 따라 ImageView 업데이트
                switch (selectedMemo.getEmotion()) {
                    case "happy":
                        stateIcon.setImageResource(R.drawable.happy_icon);
                        break;
                    case "sad":
                        stateIcon.setImageResource(R.drawable.sad_icon);
                        break;
                    case "bored":
                        stateIcon.setImageResource(R.drawable.bored_icon);
                        break;
                    case "stressed":
                        stateIcon.setImageResource(R.drawable.stressed_icon);
                        break;
                }

                // TextView 업데이트
                stateText.setText(selectedMemo.getContent());
            }


        });

        rightButtonBelow.setOnClickListener(v -> {
            adapter.selectNextItem();

            // 현재 선택된 메모 가져오기
            Memo selectedMemo = adapter.getSelectedItem();

            // 선택된 메모가 null이 아닌 경우에만 UI 업데이트
            if (selectedMemo != null) {
                // emotion에 따라 ImageView 업데이트
                switch (selectedMemo.getEmotion()) {
                    case "happy":
                        stateIcon.setImageResource(R.drawable.happy_icon);
                        break;
                    case "sad":
                        stateIcon.setImageResource(R.drawable.sad_icon);
                        break;
                    case "bored":
                        stateIcon.setImageResource(R.drawable.bored_icon);
                        break;
                    case "stressed":
                        stateIcon.setImageResource(R.drawable.stressed_icon);
                        break;
                }

                // TextView 업데이트
                stateText.setText(selectedMemo.getContent());
            }
        });

        // 현재 날짜의 메모를 처음에 로드합니다.
        loadMemos(Integer.toString(viewModel.getYear()), Integer.toString(viewModel.getMonth()));



        // 생성된 뷰를 반환
        return view;
    }

    // 월과 연도 텍스트 뷰를 업데이트하는 메서드
    private void updateDate(TextView monthTextView, TextView yearTextView) {
        monthTextView.setText(monthNames[viewModel.getMonth() - 1]);
        yearTextView.setText(String.valueOf(viewModel.getYear()));

        // 월과 연도를 업데이트한 후 해당 월/년도의 메모를 로드.
        loadMemos(Integer.toString(viewModel.getYear()), Integer.toString(viewModel.getMonth()));

    }

    private void loadMemos(String year, String month) {
        String formattedMonth = String.format("%02d", Integer.parseInt(month));
        db.collection("dailyDiary")
                .whereGreaterThanOrEqualTo("date", year + "." + formattedMonth + ".01")
                .whereLessThanOrEqualTo("date", year + "." + formattedMonth + ".31")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<Memo> memos = new ArrayList<>();

                            // 감정 카운트 맵을 초기화합니다.
                            Map<String, Integer> emotionCounts = new HashMap<>();
                            emotionCounts.put("sad", 0);
                            emotionCounts.put("happy", 0);
                            emotionCounts.put("stressed", 0);
                            emotionCounts.put("bored", 0);

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                try {
                                    String dateString = document.getString("date");
                                    String review = document.getString("review");
                                    String emotion = document.getString("emotion");

                                    SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
                                    Date date = format.parse(dateString);

                                    Calendar calendar = Calendar.getInstance();
                                    calendar.setTime(date);

                                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                                    String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);

                                    memos.add(new Memo(String.format("%02d", day), dayOfWeek, emotion, review));

                                    // 해당 메모의 감정 카운트를 증가시킵니다.
                                    if (emotionCounts.containsKey(emotion)) {
                                        emotionCounts.put(emotion, emotionCounts.get(emotion) + 1);
                                    }

                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            Log.d("loadMemos", "Fetched memos: " + memos.size());
                            adapter.setMemos(memos);

                            // Update ProgressBar widths and TextViews
                            updateProgressBarsAndTextViews(emotionCounts);

                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }

    private void updateProgressBarsAndTextViews(Map<String, Integer> emotionCounts) {
        View view = getView();

        if (view != null) {
            updateProgressBarAndTextView(view, R.id.progress_bar1, R.id.sum_num1, emotionCounts.get("bored"));
            updateProgressBarAndTextView(view, R.id.progress_bar2, R.id.sum_num2, emotionCounts.get("happy"));
            updateProgressBarAndTextView(view, R.id.progress_bar3, R.id.sum_num3, emotionCounts.get("sad"));
            updateProgressBarAndTextView(view, R.id.progress_bar4, R.id.sum_num4, emotionCounts.get("stressed"));
        }
    }

    private void updateProgressBarAndTextView(View view, int progressBarId, int textViewId, int count) {
        ImageView progressBar = view.findViewById(progressBarId);
        TextView textView = view.findViewById(textViewId);

        // 각 ProgressBar의 기본 폭은 36dp이며, 각 갯수에 따라 18dp씩 증가(원래는 최대범위인 31에 맞는 6dp여야하지만 Test위해 과장)
        int widthDp = 36 + count * 18;
        ViewGroup.LayoutParams params = progressBar.getLayoutParams();
        params.width = dpToPx(widthDp);
        progressBar.setLayoutParams(params);

        // TextView를 업데이트하여 감정의 개수를 표시
        textView.setText(String.valueOf(count));
    }

    //Dp값을 px로 변환
    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

}