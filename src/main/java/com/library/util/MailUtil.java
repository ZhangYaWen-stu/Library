package com.library.util;


import com.library.pojo.BookCatalog;
import com.library.pojo.Borrow;
import com.library.pojo.Reader;
import com.library.pojo.Reservation;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;

@Component
public class MailUtil {
    @Autowired
    private JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sendMailer;


    private void sendMail(String context, String email, String subject) throws Exception{
        if(email == null){
            throw new Exception();
        }
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(javaMailSender.createMimeMessage(), true);
        mimeMessageHelper.setFrom(sendMailer);
        mimeMessageHelper.setTo(email.split(","));
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(context);
        mimeMessageHelper.setSentDate(new Date());
        javaMailSender.send(mimeMessageHelper.getMimeMessage());
    }
    public void sendReservation(Reader reader, BookCatalog bookCatalog, Reservation reservation) throws Exception {
        String username = reader.getUserName();
        String email = reader.getEmail();
        String bookName = bookCatalog.getBookName();
        Date reservationData = reservation.getReservationTime();
        String subject = "预约提醒";
        String context = "尊敬的用户" + username + "您好，您于" + reservationData.toString() +"所预约的《" + bookName +"》已有余本，请尽快至图书馆领取书籍，祝您生活愉快!";
        sendMail(context, email, subject);
    }
    public void sendReservationOverdue(Reader reader, BookCatalog bookCatalog, Reservation reservation) throws Exception {
        String username = reader.getUserName();
        String email = reader.getEmail();
        String bookName = bookCatalog.getBookName();
        Date reservationData = reservation.getReservationTime();
        String subject = "预约逾期提醒";
        String context = "尊敬的用户" + username + "您好，您于" + reservationData.toString() +"所预约的《" + bookName +"》因已到预约最长时间，" +
                "现将预约取消，对对您学习生活造成的不便表示抱歉";
        sendMail(context, email, subject);
    }
    public void sendBorrowOverdue(Reader reader, BookCatalog bookCatalog, Borrow borrow) throws Exception{
        String username = reader.getUserName();
        String bookName = bookCatalog.getBookName();
        Date borrowTime = borrow.getBorrowTime();
        Date overdueTime = borrow.getDueTime();
        String email = reader.getEmail();
        String subject = "借书逾期提醒";
        String context = "尊敬的用户" + username + "您好，您于" + borrowTime.toString() +"所预约的《" + bookName +"》" +
                "于"+ overdueTime.toString() +"已超过最长借阅时间，请尽快到图书馆还书并缴纳罚金。";
        sendMail(context, email, subject);
    }
    public void sendBorrowOverdueWarn(Reader reader, BookCatalog bookCatalog, Borrow borrow) throws Exception {
        String username = reader.getUserName();
        String bookName = bookCatalog.getBookName();
        Date borrowTime = borrow.getBorrowTime();
        Date overdueTime = borrow.getDueTime();
        String email = reader.getEmail();
        String subject = "借书逾期提醒";
        String context = "尊敬的用户" + username + "您好，您于" + borrowTime.toString() + "所预约的《" + bookName + "》" +
                "将于" + overdueTime.toString() + "超过最长借阅时间，请尽快到图书馆还书，逾期将会收取罚金。";
        sendMail(context, email, subject);
    }
    public void sendForgetPassword(Reader reader) throws Exception {
        String username = reader.getUserName();
        String email = reader.getEmail();
        String password = reader.getPassword();
        String subject = "找回密码提醒";
        String context = "尊敬的用户" + username + "您好，您于" + new Date().toString() + "进行了找回密码操作，" +
                "您的密码为 -----("+ password +")----- 。";
        sendMail(context, email, subject);
    }
}
