package wearablebanking.kufinal.com.wearablebanking.Location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableStatusCodes;

import java.util.EventListener;

import wearablebanking.kufinal.com.wearablebanking.Location.NavigationActivity;
import wearablebanking.kufinal.com.wearablebanking.R;

public class MainActivity extends Activity {

    Button queuebtn;
    Button financebtn;
    Button todobtn;
    Button nothistorybtn;

    GoogleApiClient googleApiClient;

    private static final String PATH = "/request-location-atm";
    ImageView menuTransitionImage;

    Animation rotateAboutCenterAnimation;
    Animation appearGraduallyAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appearGraduallyAnimation = AnimationUtils.loadAnimation(this, R.anim.gradual_appearance);
        rotateAboutCenterAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_centre);

        WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                // Now you can access your views
                queuebtn = (Button) stub.findViewById(R.id.main_scr_queue_btn);
                financebtn = (Button) stub.findViewById(R.id.main_scr_finance_btn);
                todobtn = (Button) stub.findViewById(R.id.main_scr_todo);
                nothistorybtn = (Button) stub.findViewById(R.id.main_scr_not_history);

                queuebtn.setOnClickListener(new queueBtnListener());
                financebtn.setOnClickListener(new financeBtnListener());
                todobtn.setOnClickListener(new todobtnListener());
                nothistorybtn.setOnClickListener(new nothistoryBtnListener());

                menuTransitionImage = (ImageView) stub.findViewById(R.id.main_menu_transition_image_view);
                menuTransitionImage.setImageResource(R.drawable.ingbanklogo);
            }
        });
    }

    private void queueBtnClicked() {
        // QUEUE REQUESTED EVENT
        fireMessage();
    }

    private void financeBtnClicked() {

    }

    private void todobtnBtnClicked() {

    }

    private void nothistoryBtnClicked() {

    }


    public class queueBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtons();
            queueBtnClicked();
        }
    }

    public class financeBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtons();
            financeBtnClicked();
        }
    }

    public class todobtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtons();
            todobtnBtnClicked();
        }
    }

    public class nothistoryBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtons();
            nothistoryBtnClicked();
        }
    }

    private void animButtons() {
        queuebtn.startAnimation(rotateAboutCenterAnimation);
        financebtn.startAnimation(rotateAboutCenterAnimation);
        todobtn.startAnimation(rotateAboutCenterAnimation);
        nothistorybtn.startAnimation(rotateAboutCenterAnimation);

        queuebtn.setVisibility(View.GONE);
        financebtn.setVisibility(View.GONE);
        todobtn.setVisibility(View.GONE);
        nothistorybtn.setVisibility(View.GONE);

        menuTransitionImage.startAnimation(appearGraduallyAnimation);
        menuTransitionImage.setVisibility(View.VISIBLE);
    }

    private void fireMessage() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();
        googleApiClient.connect();
        googleApiClient.registerConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {

            @Override
            public void onConnected(Bundle bundle) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await();
                        for (Node node : nodes.getNodes()) {
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), PATH, null).await();
                            if (!result.getStatus().isSuccess()) {
                                Log.e("test", "error");
                            } else {
                                Log.i("test", "success!! sent to: " + node.getDisplayName());
                            }
                        }
                    }
                }).start();

            }

            @Override
            public void onConnectionSuspended(int i) {
                Log.e("test", "not connected");
            }
        });
        // Send the RPC
    }

}
