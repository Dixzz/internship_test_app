package org.evolution.test.async;

import android.os.AsyncTask;
import android.os.Handler;
import android.widget.Toast;

import org.evolution.test.databinding.ActivityMainBinding;
import org.evolution.test.datamodel.DataAdapter;
import org.evolution.test.datamodel.DataModel;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class FetchResultTask extends AsyncTask<String, String, List<DataModel>> {
    Call<ResponseBody> insertAct;
    ActivityMainBinding view;

    public FetchResultTask(Call<ResponseBody> insertAct, ActivityMainBinding view) {
        this.insertAct = insertAct;
        this.view = view;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        view.rec.animate().alpha(0).scaleX(0.2f).scaleY(0.1f).setDuration(500).start();
    }

    @Override
    protected void onPostExecute(List<DataModel> modelList) {
        super.onPostExecute(modelList);
        if (modelList.size() > 0) {
            new Handler().postDelayed(() -> {
                view.loading.animate().alpha(0).scaleX(0).scaleY(0).setDuration(500).start();
                view.rec.animate().alpha(1).scaleY(1).scaleX(1).setDuration(500).setStartDelay(250).start();
                view.rec.animate().setDuration(500).setStartDelay(500).start();
                view.fabCon.animate().setDuration(1000).setStartDelay(250).scaleX(1).scaleY(1).alpha(1).start();
                DataAdapter adapter = new DataAdapter(modelList);
                adapter.setHasStableIds(true);
                view.rec.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }, 2000);
        } else {
            view.fabCon.animate().setDuration(1000).scaleX(0).scaleY(0).alpha(0).start();
            Toast.makeText(view.getRoot().getContext(), "Not found", Toast.LENGTH_SHORT).show();
            view.loading.animate().alpha(0).scaleX(0).scaleY(0).setDuration(500).start();
        }
    }

    @Override
    protected List<DataModel> doInBackground(String... strings) {
        List<DataModel> res = new ArrayList<>();
        try {
            Response<ResponseBody> response = insertAct.execute();
            insertAct.timeout().timeout(10, TimeUnit.SECONDS).throwIfReached();
            if (response.isSuccessful() && response.body() != null) {
                try {
                    JSONArray j = new JSONArray(response.body().string());
                    for (int i = 0; i < j.length(); i++) {
                        res.add(new DataModel(
                                j.getJSONObject(i).getString("alpha3Code"),
                                j.getJSONObject(i).getString("name"),
                                j.getJSONObject(i).getString("capital"),
                                j.getJSONObject(i).getString("flag"),
                                j.getJSONObject(i).getString("region"),
                                j.getJSONObject(i).getString("subregion"),
                                j.getJSONObject(i).getString("population"),
                                j.getJSONObject(i).getString("borders"),
                                j.getJSONObject(i).getJSONArray("languages").getJSONObject(0).getString("name")
                        ));
                        //Log.e("AAA", "" + j.getJSONObject(i).getJSONObject("restaurant").getString("featured_image"));
                        //Log.e("AAA", ""+j.getJSONArray("restaurants").getJSONObject(i).getJSONObject("restaurant").getString("name"));
                    }

                    //Log.e("AAA", (""+j.getJSONArray("restaurants").getJSONObject(1).getJSONObject("restaurant").getJSONObject("R").getJSONObject("has_menu_status").getBoolean("delivery")));
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(view.getRoot().getContext(), "" + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //Log.e("doInBackground", "Size: " + res.size());
        return res;
    }
}