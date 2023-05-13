package com.example.android_project;

import android.content.Context;
import android.widget.Toast;

import javax.mail.MessagingException;
import javax.mail.SendFailedException;

public class SendMail {
    String user = ""; // 보내는 계정의 id
    String password = ""; // 보내는 계정의 pw



    public void sendSecurityCode(Context context, String sendTo, String memberPw) {
        try {
            GMailSender gMailSender = new GMailSender(user, password);
            gMailSender.sendMail("버드하우스에서 보낸 메일입니다.", "귀하의 요청으로 비밀번호를 찾았습니다.\n비밀번호: "+memberPw+"\n보안을 위해 빠른 시일 내에 비밀번호를 변경해주세요.", sendTo);
        } catch (SendFailedException e) {
            Toast.makeText(context, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (MessagingException e) {
            Toast.makeText(context, "인터넷 연결을 확인해주십시오", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
