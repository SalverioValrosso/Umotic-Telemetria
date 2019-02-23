package org.altervista.umotic.umotic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by admin on 02/12/2018.
 */

public class ForgotPasswordDialog extends AppCompatDialogFragment {

    private EditText editTextEmail;
    private ForgotPasswDialogListener forgotPasswDialogListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.forgot_password_dialog,null);

        builder.setView(view).setTitle(R.string.forgotPassword_title).setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).setPositiveButton(R.string.send, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email = editTextEmail.getText().toString().trim();
                forgotPasswDialogListener .applyText(email);
            }
        });

        editTextEmail = view.findViewById(R.id.ed_email);

        return  builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            forgotPasswDialogListener = (ForgotPasswDialogListener) context;
        }catch (ClassCastException e){
            throw  new ClassCastException(context.toString() + "must implement ForgotPasswDialogListener ");
        }
    }

    public interface ForgotPasswDialogListener{
        void applyText(String email);
    }
}
