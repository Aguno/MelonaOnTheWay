package kaist.aguno.melona;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class YesNoDialogFragment extends DialogFragment {
    Button btnYes,btnNo;
    static String DialogBoxTitle;

    public interface YesNoDialogListener {
        void onFinishYesNoDialog(boolean state);
    }

    //---empty constructor required
    public YesNoDialogFragment(){

    }
    //---set the title of the dialog window---
    public void setDialogTitle(String title) {
        DialogBoxTitle= title;
    }

    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState ) {

        View view= inflater.inflate(R.layout.are_you_sure, container);
        //---get the Button views---
        btnYes = (Button) view.findViewById(R.id.yes);
        btnNo = (Button) view.findViewById(R.id.no);

        // Button listener
        btnYes.setOnClickListener(btnListener);
        btnNo.setOnClickListener(btnListener);

        //---set the title for the dialog
        getDialog().setTitle(DialogBoxTitle);

        return view;
    }

    //---create an anonymous class to act as a button click listener
    private OnClickListener btnListener = new OnClickListener()
    {
        public void onClick(View v)
        {
            //---gets the calling activity---
            YesNoDialogListener activity = (YesNoDialogListener) getActivity();
            boolean state =
                    ((Button) v).getText().toString().equals("Yes") ? true : false;
            activity.onFinishYesNoDialog(state);
            //---dismiss the alert---
            dismiss();
        }
    };
}