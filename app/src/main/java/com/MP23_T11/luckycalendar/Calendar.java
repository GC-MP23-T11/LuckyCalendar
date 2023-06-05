package com.MP23_T11.luckycalendar;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Calendar} factory method to
 * create an instance of this fragment.
 */
public class Calendar extends Fragment {
    private static final String TAG = "Calender";

    CalendarView calendarView;
    TextView nowDate;
    RadioGroup iconGroup;
    EditText oneLineReview;
    TextView addReview;
    String currentDate;
    ProgressBar progressBar;
    public boolean isAlreadyExistDiary;

    public Calendar() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);
        Log.d(TAG, "onCreateView");

        calendarView = (CalendarView) view.findViewById(R.id.calendarView);
        nowDate = (TextView) view.findViewById(R.id.nowDate);
        ImageView todayButton = (ImageView) view.findViewById(R.id.todayButton);
        iconGroup = (RadioGroup) view.findViewById(R.id.icon_group);
        oneLineReview = (EditText) view.findViewById(R.id.MoodInput);
        addReview = (TextView) view.findViewById(R.id.AddMood);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar2);

        Map<Integer, String> drawableMap = new HashMap<>();
        drawableMap.put(R.id.icon_bor, "bored");
        drawableMap.put(R.id.icon_happy, "happy");
        drawableMap.put(R.id.icon_sad, "sad");
        drawableMap.put(R.id.icon_stressed, "stressed");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long currentTimeMillis = System.currentTimeMillis();
                // calendarView의 날짜를 현재 날짜로 설정합니다.
                calendarView.setDate(currentTimeMillis, false, true);
                // 현재 날짜를 "yyyy/MM/dd" 형식으로 변환합니다.
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd", Locale.getDefault());
                currentDate = sdf.format(new Date());

                // dateView의 텍스트를 현재 날짜로 설정합니다.
                nowDate.setText(currentDate);
                // 동시에 원래 상태로 돌림
                iconGroup.clearCheck();
                oneLineReview.setText("");
                isAlreadyExistDiary = false;

                db.collection("dailyDiary")
                        .whereEqualTo("date", currentDate)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        // 가져온 데이터를 사용하세요.
                                        for (Map.Entry<Integer, String> entry : drawableMap.entrySet()) {
                                            Log.d(TAG, entry.getValue());
                                            if (document.getString("emotion").equals(entry.getValue())) {
                                                Log.d(TAG, entry.getValue());
                                                Log.d(TAG, entry.getKey().toString());
                                                RadioButton radioButton = getActivity().findViewById(entry.getKey());
                                                radioButton.setChecked(true);
                                                isAlreadyExistDiary = true;
                                                break;
                                            }
                                        }
                                        oneLineReview.setText(document.getString("review"));
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                // 변경된 날짜를 문자열로 변환합니다. month는 0부터 시작하므로 1을 더합니다.
                String date = String.format(Locale.getDefault(), "%d.%02d.%02d", year, month + 1, dayOfMonth);
                currentDate = date;
                // dateView의 텍스트를 변경합니다.
                nowDate.setText(date);
                // 동시에 원래 상태로 돌림
                iconGroup.clearCheck();
                oneLineReview.setText("");
                isAlreadyExistDiary = false;

                db.collection("dailyDiary")
                        .whereEqualTo("date", currentDate)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        // 가져온 데이터를 사용하세요.
                                        for (Map.Entry<Integer, String> entry : drawableMap.entrySet()) {
                                            Log.d(TAG, entry.getValue());
                                            if (document.getString("emotion").equals(entry.getValue())) {
                                                Log.d(TAG, entry.getValue());
                                                Log.d(TAG, entry.getKey().toString());
                                                RadioButton radioButton = getActivity().findViewById(entry.getKey());
                                                radioButton.setChecked(true);
                                                isAlreadyExistDiary = true;
                                                break;
                                            }
                                        }
                                        oneLineReview.setText(document.getString("review"));
                                    }
                                } else {
                                    Log.d(TAG, "Error getting documents: ", task.getException());
                                }
                            }
                        });

            }
        });


        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "addReview click");
                int emotion = -1;
                emotion = iconGroup.getCheckedRadioButtonId();
                String review = oneLineReview.getText().toString();

                //defensive coding
                if (currentDate.isEmpty()) {
                    Log.d(TAG, "date empty");
                    Toast.makeText(getContext(), "날짜를 선택해주세요", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (review.isEmpty()) {
                    Log.d(TAG, "reveiw empty");
                    Toast.makeText(getContext(), "한줄 일기를 입력해주세요", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (emotion == -1) {
                    Log.d(TAG, "emotion empty");
                    Toast.makeText(getContext(), "오늘의 감정을 선택해주세요", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // 이미 해당 날자에 저장된 일기가 있는지 확인
                // 없으면 그대로 진행
                // 있으면 이전 일기에서 덮어씌우기
                /*
                문제발생 *비동기 호출*
                비동기적으로 호출되어 데이터가 저장되자마자 지워버림
                 */
                String radioButtonString = null;

                for (Map.Entry<Integer, String> entry : drawableMap.entrySet()) {
                    Log.d(TAG, entry.getKey().toString());
                    if (entry.getKey() == emotion) {
                        radioButtonString = entry.getValue();
                        break;
                    }
                    else {
                        Log.w(TAG, "can't find emotion radiobutton index");
                    }
                }

                if (radioButtonString == null) {
                    Log.w(TAG, "emotion을 가져오지 못함");
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "감정 선택 오류", Toast.LENGTH_SHORT).show();
                    return;
                }

                Map<String, Object> data = new HashMap<>();
                data.put("date", currentDate);
                data.put("review", review);
                data.put("emotion", radioButtonString);
                Log.d(TAG, "db 넣기 전");

                // Firestore에 데이터를 저장합니다.
                db.collection("dailyDiary")
                        .whereEqualTo("date", currentDate)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    List<Task<Void>> deleteTasks = new ArrayList<>();
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        deleteTasks.add(
                                                db.collection("dailyDiary").document(document.getId())
                                                        .delete()
                                        );
                                    }

                                    // When all delete operations are completed
                                    Tasks.whenAll(deleteTasks).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                // Firestore에 데이터를 저장합니다.
                                                db.collection("dailyDiary").add(data)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                                                                progressBar.setVisibility(View.GONE);
                                                                Toast.makeText(getContext(), "일기가 저장되었어요", Toast.LENGTH_SHORT).show();
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Log.w(TAG, "Error adding document", e);
                                                            }
                                                        });
                                            } else {
                                                Log.w(TAG, "Error deleting documents", task.getException());
                                            }
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "저장된 일기 없음");
                                }
                            }
                        });


            }
        });

        return view;
    }
}