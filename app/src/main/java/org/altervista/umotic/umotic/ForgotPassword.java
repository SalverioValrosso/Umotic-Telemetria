package org.altervista.umotic.umotic;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ForgotPassword {

    private Session session;
    private  Context context;
    private  String user,emailTo,subject,textMessage, password;

    public ForgotPassword(String username,String email, String password, Context context){
        user=username;
        emailTo=email;
        this.password=password;
        this. context=context;
        sendEmail();
    }

    private void sendEmail(){

        subject = context.getString(R.string.object_mail);

        /*
        Properties prop = new Properties();
        prop.put("mail.smtp.host","smtp.gmail.com");
        prop.put("mail.smtp.socketFactory.port","465");
        prop.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth","true");
        prop.put("mail.smtp.port","465");*/

        Properties props = new Properties();
        props.put("mail.smtp.user", emailTo);
        props.put("mail.smtp.host","smtp.gmail.com");
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.debug", "true");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");

        session = Session.getDefaultInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("umoticandobd@gmail.com", "Troccoly");
            }
        });
        session.setDebug(true);

        String[] mex= context.getResources().getStringArray((R.array.text_mail));
        textMessage = mex[0]+user+",\n"+mex[1]+emailTo+ mex[2] +password+".\n"+mex[3];

        RetreiveFeedTask task = new RetreiveFeedTask();
        task.execute();
    }

    class RetreiveFeedTask extends AsyncTask<String,Void,String> {
        private ProgressDialog pdialog = null;
        @Override
        protected void onPreExecute(){
            pdialog = ProgressDialog.show(context,"", context.getString(R.string.sending_mail), true);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("umoticandobd@gmail.com"));
                //message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(emailTo));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
                message.setSubject(subject);
                message.setText(textMessage);
                //message.setContent(textMessage, "text/html; charset=");
                Transport transport = session.getTransport("smtps");
                transport.connect("smtp.gmail.com", Integer.valueOf("465"), "umoticandobd@gmail.com", "Troccoly");
                transport.sendMessage(message, message.getAllRecipients());
                transport.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            pdialog.dismiss();
            Toast.makeText(context,context.getString(R.string.sent_mail),Toast.LENGTH_LONG).show();
        }

    }
}
