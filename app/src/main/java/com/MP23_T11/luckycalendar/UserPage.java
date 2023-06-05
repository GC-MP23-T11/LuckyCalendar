package com.MP23_T11.luckycalendar;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UserPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        TextView UserName = (TextView) findViewById(R.id.UserName);
        TextView UserEmail = (TextView) findViewById(R.id.UserEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email = user.getEmail();

            UserName.setText(name);
            UserEmail.setText(email);
        }
    }

    public void modUserEmail(View view) {
        showChangeEmailDialog();
    }

    public void modPW(View view) {
        showChangePasswordDialog();
    }

    public void modUserName(View view) {
        showChangeUsernameDialog();
    }


    // 버튼을 클릭했을 때 다이얼로그를 표시하는 메소드
    private void showChangeEmailDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("이메일 변경");
        builder.setMessage("새로운 이메일을 입력하세요.");

        // 다이얼로그에 입력 필드 추가
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setView(input);

        // 다이얼로그의 확인 버튼 클릭 이벤트 처리
        builder.setPositiveButton("확인", (dialog, which) -> {
            String newEmail = input.getText().toString();
            // 이메일 변경 작업 수행
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            assert user != null;
            user.updateEmail(newEmail)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address updated.");
                        }
                    });
            // newEmail 변수에는 새로운 이메일 주소가 포함되어 있습니다.
            TextView UserEmail = (TextView) findViewById(R.id.UserEmail);
            UserEmail.setText(newEmail);
        });

        // 다이얼로그의 취소 버튼 클릭 이벤트 처리
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 표시
        builder.show();

    }



    // 버튼을 클릭했을 때 다이얼로그를 표시하는 메소드
    private void showChangePasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("비밀번호 변경");
        builder.setMessage("새로운 비밀번호를 입력하세요.");

        // 다이얼로그에 입력 필드 추가
        final EditText newPasswordInput = new EditText(this);
        newPasswordInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        newPasswordInput.setLayoutParams(layoutParams);
        builder.setView(newPasswordInput);

        // 다이얼로그의 확인 버튼 클릭 이벤트 처리
        builder.setPositiveButton("확인", (dialog, which) -> {
            String newPass = newPasswordInput.getText().toString();
            // 비밀번호 변경 작업 수행
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            assert user != null;
            user.updatePassword(newPass)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User password updated.");
                        }
                    });
            // newPassword 변수에는 새로운 비밀번호가 포함되어 있습니다.
        });

        // 다이얼로그의 취소 버튼 클릭 이벤트 처리
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 표시
        builder.show();
    }



    // 버튼을 클릭했을 때 다이얼로그를 표시하는 메소드
    private void showChangeUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("유저네임 변경");
        builder.setMessage("새로운 유저네임을 입력하세요.");

        // 다이얼로그에 입력 필드 추가
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(layoutParams);
        builder.setView(input);

        // 다이얼로그의 확인 버튼 클릭 이벤트 처리
        builder.setPositiveButton("확인", (dialog, which) -> {
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
                            Toast.makeText(getApplicationContext(), "유저네임이 변경되었습니다.", Toast.LENGTH_SHORT).show();
                        } else {
                            // 유저네임 변경 실패
                            Toast.makeText(getApplicationContext(), "유저네임 변경에 실패했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // 다이얼로그의 취소 버튼 클릭 이벤트 처리
        builder.setNegativeButton("취소", (dialog, which) -> dialog.cancel());

        // 다이얼로그 표시
        builder.show();
    }



}