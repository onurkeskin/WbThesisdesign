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

    private static final String ATM_REQUEST_PATH = "/request-location-atm";
    private static final String FINANCE_WEAR_PATH = "/request-finance";

    private boolean lockScreen = false;

    ImageView menuTransitionImage;

    Animation rotateAboutCenterAnimation;
    Animation appearGraduallyAnimation;
    Animation rotateAboutCenterIntoAnimation;
    Animation disappearGraduallyAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appearGraduallyAnimation = AnimationUtils.loadAnimation(this, R.anim.gradual_appearance);
        disappearGraduallyAnimation = AnimationUtils.loadAnimation(this, R.anim.gradual_dissappearance);
        rotateAboutCenterAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_centre);
        rotateAboutCenterIntoAnimation = AnimationUtils.loadAnimation(this, R.anim.rotate_centre_back);

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
        fireMessage(ATM_REQUEST_PATH);
    }

    private void financeBtnClicked() {
        fireMessage(FINANCE_WEAR_PATH);
    }

    private void todobtnBtnClicked() {

    }

    private void nothistoryBtnClicked() {

    }

    public void setLockStatus(boolean lockStatus) {
        this.lockScreen = lockStatus;
    }


    public class queueBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtonsMoveFromScreen();
            queueBtnClicked();
        }
    }

    public class financeBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtonsMoveFromScreen();
            financeBtnClicked();
        }
    }

    public class todobtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtonsMoveFromScreen();
            todobtnBtnClicked();
        }
    }

    public class nothistoryBtnListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            animButtonsMoveFromScreen();
            nothistoryBtnClicked();
        }
    }

    private void animButtonsMoveFromScreen() {
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

    private void animButtonsMoveToScreen() {
        queuebtn.startAnimation(rotateAboutCenterIntoAnimation);
        financebtn.startAnimation(rotateAboutCenterIntoAnimation);
        todobtn.startAnimation(rotateAboutCenterIntoAnimation);
        nothistorybtn.startAnimation(rotateAboutCenterIntoAnimation);

        queuebtn.setVisibility(View.VISIBLE);
        financebtn.setVisibility(View.VISIBLE);
        todobtn.setVisibility(View.VISIBLE);
        nothistorybtn.setVisibility(View.VISIBLE);

        menuTransitionImage.startAnimation(disappearGraduallyAnimation);
        menuTransitionImage.setVisibility(View.GONE);
    }

    private void fireMessage(final String requestName) {
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
                            MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(googleApiClient, node.getId(), requestName, null).await();
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

    @Override
    protected void onStop() {
        Log.i("WEAR", "STOP");
        setLockStatus(true);
        super.onStop();
    }

    @Override
    protected void onPause() {
        setLockStatus(false);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        Log.i("WEAR", "DESTROY");
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        Log.i("WEAR", "SAVE");
        setLockStatus(true);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        Log.i("WEAR", "RESUME");
        if(lockScreen == true){
            animButtonsMoveToScreen();
        }
        super.onResume();
    }

    @Override
    public void onDetachedFromWindow() {
        Log.i("WEAR", "DETACH");
        super.onDetachedFromWindow();
    }
}
