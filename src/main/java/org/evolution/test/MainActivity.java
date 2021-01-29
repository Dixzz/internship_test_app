package org.evolution.test;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.evolution.test.async.FetchResultTask;
import org.evolution.test.databinding.ActivityMainBinding;
import org.evolution.test.retrofit.RetrofitClient;
import org.evolution.test.utils.CommonUtils;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;

import static org.evolution.test.utils.CommonUtils.ONE_F;
import static org.evolution.test.utils.CommonUtils.VIEW_ANIM_DUR;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding view;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.main_bg));
        getWindow().setNavigationBarColor(getColor(R.color.main_bg));

        view = DataBindingUtil.setContentView(this, R.layout.activity_main);
        view.rec.setLayoutManager(new LinearLayoutManager(this));

        if (CommonUtils.hasNetwork(this)) {
            view.loading.animate().alpha(ONE_F).scaleX(ONE_F).scaleY(ONE_F).setDuration(VIEW_ANIM_DUR).start();
            Call<ResponseBody> getNameResp = RetrofitClient.getInstance().getApi().getDetails();
            new FetchResultTask(getNameResp, view).execute();

            view.rec.addOnScrollListener(new RecyclerView.OnScrollListener() {
                int page_no = 1;

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    page_no += dy;
                    ((TextView) findViewById(R.id.counter_fab)).
                            setText(String.format(Locale.ENGLISH, "%d", (page_no / recyclerView.computeVerticalScrollExtent() + 1)));
                }
            });
        } else {
            Snackbar.make(view.getRoot(), R.string.connect_to_net, Snackbar.LENGTH_SHORT).show();
            new Handler().postDelayed(this::finish, 2000);
        }
    }
}