package com.zhbit.util;


import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {
    private static final String ALIDM_SMTP_HOST = "smtpdm.aliyun.com";
    private static final int ALIDM_SMTP_PORT = 25;
    private static final String SMTP_USER = "mail@sharesystem.top";
    private static final String SMTP_PASSWORD = "SendAdmin01";
    private static final String SEND_ADDRESS = "mail@sharesystem.top";

    public static void sendMail(String receiveAddress, String subject, String content){
        // 配置发送邮件的环境属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，需要进行身份验证
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.host", ALIDM_SMTP_HOST);
        props.put("mail.smtp.port", ALIDM_SMTP_PORT);

        // 发件人的账号
        props.put("mail.user", SMTP_USER);
        // 访问SMTP服务时需要提供的密码
        props.put("mail.password", SMTP_PASSWORD);
        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        try {
            // 设置发件人
            InternetAddress from = new InternetAddress(SEND_ADDRESS);
            message.setFrom(from);
            Address[] a = new Address[1];
            a[0] = new InternetAddress(SEND_ADDRESS);
            message.setReplyTo(a);
            // 设置收件人
            InternetAddress to = new InternetAddress(receiveAddress);
            message.setRecipient(MimeMessage.RecipientType.TO, to);
            // 设置邮件标题
            message.setSubject(subject);
            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");
            // 发送邮件
            Transport.send(message);
        }catch (MessagingException e) {
            e.printStackTrace();
        }

    }

    public static String getRegisterEmailContent(String account, String code){
        StringBuffer content = new StringBuffer(account);
        content.append(",您好:");
        content.append("<br/>");
        content.append("您正在进行注册操作，请将以下验证码输入验证框中，完成您在乐享的注册操作!<br>");
        content.append("<h1><font color='red'>");
        content.append(code);
        content.append("</font></h1>");
        content.append("<br/>");
        content.append("<br/>");
        content.append("本邮件由系统自动发出，请勿回复");
        content.append("<br/>");
        return content.toString();
    }

    public static String getForgetPasswordEmailContent(String account, String code){
        StringBuffer content = new StringBuffer(account);
        content.append(",您好:");
        content.append("<br/>");
        content.append("您正在进行重置密码操作，请将以下验证码输入验证框中，完成您在乐享的重置密码操作!(若非本人操作，请尽快修改密码)<br>");
        content.append("<h3 color='red'>");
        content.append(code);
        content.append("</h3>");
        content.append("<br/>");
        content.append("<br/>");
        content.append("本邮件由系统自动发出，请勿回复");
        content.append("<br/>");
        return content.toString();
    }

}