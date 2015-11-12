package wearablebanking.kufinal.com.wearablebanking.BankQueue;

import android.app.Fragment;
import android.content.Context;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import wearablebanking.kufinal.com.wearablebanking.R;

/**
 * Created by PC on 10.11.2015.
 */
public class BankMenuFragment extends Fragment {

    private BankFragmentListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bank_menu_fragment, container, false);

        Button Bank_info_btn = (Button) view.findViewById(R.id.Bank_info_btn);
        Bank_info_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getInfoClicked();
            }
        });

        Button get_queue_btn = (Button) view.findViewById(R.id.get_queue_btn);
        get_queue_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.getQueueClicked();
            }
        });


        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BankFragmentListener) {
            listener = (BankFragmentListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + " must implemenet BankFragmentListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public interface BankFragmentListener{
        public void getQueueClicked();
        public void getInfoClicked();
    }
}
