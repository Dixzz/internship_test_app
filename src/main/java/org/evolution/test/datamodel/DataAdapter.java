package org.evolution.test.datamodel;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.evolution.test.async.DownloadImageTask;
import org.evolution.test.databinding.RecDataBinding;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import static org.evolution.test.utils.CommonUtils.HALF_F;
import static org.evolution.test.utils.CommonUtils.ONE_F;
import static org.evolution.test.utils.CommonUtils.VIEW_TRANS_Y;
import static org.evolution.test.utils.CommonUtils.ZERO_F;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    long DURATION = 1000;
    private List<DataModel> list;
    private RecDataBinding recDataBinding;
    private boolean on_attach = true;

    private HashMap<String, String> code = new HashMap<>();

    public DataAdapter(List<DataModel> list) {
        this.list = list;
    }


    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                on_attach = false;
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

        for (int i = 0; i < list.size(); i++) {
            String name = list.get(i).getName();
            if (name.contains(","))
                code.put(list.get(i).getCode(), name.substring(0, name.indexOf(",")));
            else if (name.contains("("))
                code.put(list.get(i).getCode(), name.substring(0, name.indexOf("(")));
            else
                code.put(list.get(i).getCode(), name);

        }
        code.put("RUS", "Russia");
        super.onAttachedToRecyclerView(recyclerView);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        recDataBinding = RecDataBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(recDataBinding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        recDataBinding.setData(list.get(position));
        new DownloadImageTask(recDataBinding.imageRest).execute(list.get(position).getFlag());
        setAnimation(holder.itemView, position);
        try {
            JSONArray j = new JSONArray(list.get(position).getBord());
            if (j.length() == 0)
                recDataBinding.borderText.setText("N/A");
            else {
                for (int i = 0; i < j.length(); i++) {
                    String match = code.get(j.getString(i));
                    if (match != null)
                        recDataBinding.borderText.append(match);
                    if (i + 1 < j.length())
                        recDataBinding.borderText.append(", ");
                    if (recDataBinding.borderText.length() >= 40)
                        recDataBinding.borderText.setTextSize(13);
                    if (match == null)
                        recDataBinding.borderText.append(j.getString(i));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setAnimation(View itemView, int i) {
        if (!on_attach) {
            i = -1;
        }
        boolean isNotFirstItem = i == -1;
        i++;
        itemView.setAlpha(0f);

        AnimatorSet animatorSet = new AnimatorSet();
        ObjectAnimator alphaAnim = ObjectAnimator.ofFloat(itemView, View.ALPHA, ZERO_F, HALF_F, ONE_F);
        ObjectAnimator tranYANim = ObjectAnimator.ofFloat(itemView, View.TRANSLATION_Y, VIEW_TRANS_Y, ZERO_F);

        ObjectAnimator.ofFloat(itemView, View.ALPHA, 0f).start();
        alphaAnim.setStartDelay(isNotFirstItem ? DURATION / 2 : (i * DURATION / 3));
        tranYANim.setStartDelay(isNotFirstItem ? DURATION / 2 : (i * DURATION / 3));

        alphaAnim.setDuration(DURATION);
        tranYANim.setDuration(DURATION + 250);
        animatorSet.play(tranYANim).with(alphaAnim);
        animatorSet.start();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
