package wearablebanking.kufinal.com.wearablebanking.Finance;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.CircledImageView;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import wearablebanking.kufinal.com.wearablebanking.R;

public class UserFinanceActivity extends Activity {

    private WearableListView listView;
    private ArrayAdapter mAdapter;
    private String[] values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_finance);

        Intent intent = getIntent();
        String data = intent.getStringExtra("Finances");
        values = data.split("\n");


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                listView = (WearableListView) stub.findViewById(R.id.finance_list_view);
                listView.setAdapter(new FinanceAdapter(UserFinanceActivity.this));
            }
        });
    }

    public class FinanceAdapter extends WearableListView.Adapter {

        private final Context context;

        public FinanceAdapter(Context context) {
            this.context = context;
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            return new WearableListView.ViewHolder(new StockItemView(context));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder viewHolder, final int position) {
            StockItemView SettingsItemView = (StockItemView) viewHolder.itemView;

            String[] toUse = values[position].split(":");

            TextView textView = (TextView) SettingsItemView.findViewById(R.id.text);
            textView.setText(toUse[0]);

            TextView textView2 = (TextView) SettingsItemView.findViewById(R.id.text2);
            textView2.setText(toUse[1]);

            final CircledImageView imageView = (CircledImageView) SettingsItemView.findViewById(R.id.image);
            imageView.setImageResource(R.drawable.stock_market_logo);

        }

        @Override
        public int getItemCount() {
            return values.length;
        }
    }

    public final class StockItemView extends FrameLayout implements WearableListView.OnCenterProximityListener {

        final CircledImageView image;
        final TextView text;
        final TextView text2;

        public StockItemView(Context context) {
            super(context);
            View.inflate(context, R.layout.row_simple_item_layout, this);
            image = (CircledImageView) findViewById(R.id.image);
            text = (TextView) findViewById(R.id.text);
            text2 = (TextView) findViewById(R.id.text2);
        }


        @Override
        public void onCenterPosition(boolean b) {

            //Animation example to be ran when the view becomes the centered one
            image.animate().scaleX(1f).scaleY(1f).alpha(1);
            text.animate().scaleX(1f).scaleY(1f).alpha(1);
            text2.animate().scaleX(1f).scaleY(1f).alpha(1);
        }

        @Override
        public void onNonCenterPosition(boolean b) {

            //Animation example to be ran when the view is not the centered one anymore
            image.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
            text.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
            text2.animate().scaleX(0.8f).scaleY(0.8f).alpha(0.6f);
        }
    }

}
