package cc.oiuio.mybatis.mail;

import org.springframework.http.MediaType;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.util.ByteArrayDataSource;
import java.io.File;
import java.io.Serializable;

/**
 * 邮件附件
 */
public class SendEmailAttachedEntity implements Serializable {
    private static final long serialVersionUID = 3359289711930548930L;

    //附件名称
    private String fileName;
    //附件
    private DataHandler dh;

    public SendEmailAttachedEntity(String fileName, File file) {
        this.fileName = fileName;
        DataSource ds = new FileDataSource(file);
        this.dh = new DataHandler(ds);
    }

    public SendEmailAttachedEntity(String fileName, byte[] file, MediaType type) {
        this.fileName = fileName;
        DataSource ds = new ByteArrayDataSource(file,type.getType());
        this.dh = new DataHandler(ds);
    }

    public String getFileName() {
        return fileName;
    }

    public DataHandler getDh() {
        return dh;
    }
}
