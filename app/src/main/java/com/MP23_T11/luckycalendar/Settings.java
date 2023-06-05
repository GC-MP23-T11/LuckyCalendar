package com.MP23_T11.luckycalendar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Settings extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    TextView UserName;
    TextView UserEmail;
    TextView ModPW;

    public Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Settings newInstance(String param1, String param2) {
        Settings fragment = new Settings();
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
            // TODO: Rename and change types of parameters
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        UserName = view.findViewById(R.id.UserName);
        UserEmail = view.findViewById(R.id.UserEmail);
        ModPW = view.findViewById(R.id.modPassword); // ModPW 초기화 추가

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            if (user.getDisplayName() != null) {
                String name = user.getDisplayName();
                UserName.setText(name);
            }
            String email = user.getEmail();
            UserEmail.setText(email);
        }
        UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeUsernameDialog();
            }
        });
        UserEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangeEmailDialog();
            }
        });
        ModPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showChangePasswordDialog();
            }
        });

        return view;
    }


    // 버튼을 클릭했을 때 다이얼로그를 표시하는 메소드
    private void showChangeEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("이메일 변경");
        builder.setMessage("새로운 이메일을 입력하세요.");

        // 다이얼로그에 입력 필드 추가
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setView(input);

        // 다이얼로그의 확인 버튼 클릭 이벤트 처리
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEmail = input.getText().toString();

                // 파이어베이스 현재 사용자 가져오기
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // 이메일 변경
                assert user != null;
                user.updateEmail(newEmail)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // 이메일 변경 성공
                                Toast.makeText(getActivity(), "이메일이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 이메일 변경 실패
                                Toast.makeText(getActivity(), "이메일 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // 다이얼로그의 취소 버튼 클릭 이벤트 처리
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 표시
        builder.show();
    }


    // 버튼을 클릭했을 때 다이얼로그를 표시하는 메소드
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("비밀번호 변경");
        builder.setMessage("새로운 비밀번호를 입력하세요.");

        // 다이얼로그에 입력 필드 추가
        final EditText newPasswordInput = new EditText(getActivity());
        newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        newPasswordInput.setLayoutParams(layoutParams);
        builder.setView(newPasswordInput);

        // 다이얼로그의 확인 버튼 클릭 이벤트 처리
        builder.setPositiveButton("확인", (dialog, which) -> {
            String newPassword = newPasswordInput.getText().toString();

            // 파이어베이스 현재 사용자 가져오기
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            // 비밀번호 변경
            assert user != null;
            user.updatePassword(newPassword)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // 비밀번호 변경 성공
                            Toast.makeText(getActivity(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 비밀번호 변경 실패
                            Toast.makeText(getActivity(), "비밀번호 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 다이얼로그의 취소 버튼 클릭 이벤트 처리
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 표시
        builder.show();
    }


    // 버튼을 클릭했을 때 다이얼로그를 표시하는 메소드
    private void showChangeUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("유저네임 변경");
        builder.setMessage("새로운 유저네임을 입력하세요.");

        // 다이얼로그에 입력 필드 추가
        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setView(input);

        // 다이얼로그의 확인 버튼 클릭 이벤트 처리
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newUsername = input.getText().toString();

                // 파이어베이스 현재 사용자 가져오기
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                // 유저네임 변경
                assert user != null;
                user.updateProfile(new UserProfileChangeRequest.Builder()
                                .setDisplayName(newUsername)
                                .build())
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                // 유저네임 변경 성공
                                Toast.makeText(getActivity(), "유저네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 유저네임 변경 실패
                                Toast.makeText(getActivity(), "유저네임 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        // 다이얼로그의 취소 버튼 클릭 이벤트 처리
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 표시
        builder.show();
    }

}