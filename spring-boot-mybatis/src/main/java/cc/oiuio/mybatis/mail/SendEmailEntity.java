package cc.oiuio.mybatis.mail;

import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.util.List;

/**
 * 邮件
 */
public class SendEmailEntity implements Serializable {
    private static final long serialVersionUID = -526809470986760820L;

    //收件人邮件地址
    @NotBlank(message = "收件人邮箱不能为空")
    private String toEmail;
    //主题(标题)
    @NotBlank(message = "邮件主题不能为空")
    private String subject;
    //正文
    @NotBlank(message = "邮件正文不能为空")
    private String content;
    //附件
    private List<SendEmailAttachedEntity> attachedList;

    public String getToEmail() {
        return toEmail;
    }

    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<SendEmailAttachedEntity> getAttachedList() {
        return attachedList;
    }

    public void setAttachedList(List<SendEmailAttachedEntity> attachedList) {
        this.attachedList = attachedList;
    }
}
