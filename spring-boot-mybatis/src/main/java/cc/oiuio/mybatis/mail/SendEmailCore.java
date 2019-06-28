package cc.oiuio.mybatis.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import os.basic.util.data.DataVO;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.List;

@Component
public class SendEmailCore {

    private JavaMailSender mailSender;
    @Value("${email.sender.account}")
    private String SENDER_ACCOUNT;
    @Value("${email.sender.password}")
    private String SENDER_PASSWORD;
    @Value("${email.server.smtp.address}")
    private String SERVER_SMTP_ADDRESS;


   public   SendEmailCore(){
        System.out.println(SENDER_ACCOUNT);
        System.out.println(SENDER_PASSWORD);
        System.out.println(SERVER_SMTP_ADDRESS);
    }

    public void setSERVER_SMTP_ADDRESS(String SERVER_SMTP_ADDRESS) {
        this.SERVER_SMTP_ADDRESS = SERVER_SMTP_ADDRESS;
    }

    public DataVO<Boolean> send(SendEmailEntity entity) {

        System.out.println(mailSender);
        System.out.println(SENDER_ACCOUNT);
        System.out.println(SENDER_PASSWORD);
        System.out.println(SERVER_SMTP_ADDRESS);
        DataVO<Boolean> vo = new DataVO<>();

        mailSender.createMimeMessage();

        MimeMessage msg = mailSender.createMimeMessage();
        try {
            msg.setFrom(SENDER_ACCOUNT);
            msg.setRecipients(Message.RecipientType.TO, entity.getToEmail());
            msg.setSubject(entity.getSubject());

            //MIME消息体
            MimeMultipart msgMultipart = new MimeMultipart("mixed");
            msg.setContent(msgMultipart);

            //正文
            MimeBodyPart contentPart = new MimeBodyPart();
            msgMultipart.addBodyPart(contentPart);
            contentPart.setContent(entity.getContent(), "text/html;charset=utf-8");

            //附件
            List<SendEmailAttachedEntity> attachedList = entity.getAttachedList();
            if (attachedList != null && !attachedList.isEmpty()) {
                for (SendEmailAttachedEntity attachedEntity : attachedList) {
                    MimeBodyPart attach = new MimeBodyPart();
                    msgMultipart.addBodyPart(attach);
                    attach.setDataHandler(attachedEntity.getDh());
                    attach.setFileName(attachedEntity.getFileName());
                }
            }
            //发送
            msg.saveChanges();
            mailSender.send(msg);
        } catch (MessagingException e) {
            e.printStackTrace();
            return vo.setFailureMessage(e.getMessage());
        }
        return vo.setSuccess();
    }

    public String getSENDER_ACCOUNT() {
        return SENDER_ACCOUNT;
    }

    public void setSENDER_ACCOUNT(String SENDER_ACCOUNT) {
        this.SENDER_ACCOUNT = SENDER_ACCOUNT;
    }

    public String getSENDER_PASSWORD() {
        return SENDER_PASSWORD;
    }

    public void setSENDER_PASSWORD(String SENDER_PASSWORD) {
        this.SENDER_PASSWORD = SENDER_PASSWORD;
    }

    public String getSERVER_SMTP_ADDRESS() {
        return SERVER_SMTP_ADDRESS;
    }
}
