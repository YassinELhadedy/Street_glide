package kk.ljq.hhh.loca;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class CustomDialog extends DialogFragment {
    static CustomDialog newInstance(String title,String street,float speed) {
        CustomDialog fragment = new CustomDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("street",street);

        args.putFloat("speed",speed);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        String title = getArguments().getString("title");
        String street=getArguments().getString("street");

        float speed=getArguments().getFloat("speed");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_custom_dialog,null);

        TextView textView=(TextView)view.findViewById(R.id.textView3);
        if(speed==0.0){
            textView.setText("Stoped  in "+street);
        }
        else {textView.setText("wakeing with speed  "+speed+"  KM/S"+"\n"+"in "+street+" street");

        }




        return new AlertDialog.Builder(getActivity())

                .setTitle("tracking of "+title).setView(view)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {

                            }
                        }).create();

    }
}
