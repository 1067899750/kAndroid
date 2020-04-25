package com.github.tifezh.kchart.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.tifezh.kchart.R;

import java.util.List;

/**
 * @author puyantao
 * @description :
 * @date 2020/4/25
 */
public class PicChartAdapter extends RecyclerView.Adapter<PicChartAdapter.MyViewHolder> {
    private Context mContext;
    private List<PieChartData> mPieChartData;

    public PicChartAdapter(Context context, List<PieChartData> pieChartData) {
        mContext = context;
        mPieChartData = pieChartData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_piechart, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        PieChartData data = mPieChartData.get(i);
        myViewHolder.icon.setBackgroundColor(data.getColor());
        myViewHolder.name.setText(data.getName());
    }

    @Override
    public int getItemCount() {
        return mPieChartData.size() > 0 ? mPieChartData.size() : 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        private View icon;
        private TextView name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.icon);
            name = itemView.findViewById(R.id.tv_name);
        }
    }
}
