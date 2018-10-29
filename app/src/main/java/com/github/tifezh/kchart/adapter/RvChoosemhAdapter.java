package com.github.tifezh.kchart.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.github.tifezh.kchart.R;
import com.github.tifezh.kchart.base.SimpleRecAdapter;
import com.github.tifezh.kchart.model.RvChoosemh;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RvChoosemhAdapter extends SimpleRecAdapter<RvChoosemh, RvChoosemhAdapter.MyViewHolder> {
    private MyItemLister myItemLister = null;
    private boolean isOpen = false;//弹出的时候位true

    public RvChoosemhAdapter(Context context, boolean isOpen) {
        super(context);
        this.isOpen = isOpen;
    }

    @Override
    public MyViewHolder newViewHolder(View itemView) {
        return new MyViewHolder(itemView);
    }

    @Override
    public int getLayoutId() {
        return R.layout.adapter_rvchoosemh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.tvValue.setText(data.get(position).getValue());
        if (isOpen) {
            holder.tvValue.setTextColor(context.getResources().getColor(R.color.c9EB2CD));
            holder.tvTab.setVisibility(View.GONE);
        } else {
            if (data.get(position).isChoose()) {
                holder.tvValue.setTextColor(context.getResources().getColor(R.color.cFFFFFF));
                if (data.get(position).getValue().equals("更多")) {
                    holder.tvTab.setVisibility(View.GONE);
                } else {
                    holder.tvTab.setVisibility(View.VISIBLE);
                }
            } else {
                holder.tvValue.setTextColor(context.getResources().getColor(R.color.c9EB2CD));
                holder.tvTab.setVisibility(View.GONE);
            }
            if (position == 6) {
                holder.ivopen.setVisibility(View.VISIBLE);
                if (data.get(position).isMoreOpon()) {
                    holder.ivopen.setImageResource(R.mipmap.iv_chart_date_up);
                } else {
                    holder.ivopen.setImageResource(R.mipmap.iv_chart_date_down);
                }
            } else {
                holder.ivopen.setVisibility(View.GONE);
            }
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myItemLister != null) {
                    myItemLister.setItem(v, data, position);
                }

            }
        });
    }

    public void setMyItemLister(MyItemLister myItemLister) {
        this.myItemLister = myItemLister;
    }

    public interface MyItemLister {
        public void setItem(View v, List<RvChoosemh> data, int position);
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvValue)
        TextView tvValue;
        @BindView(R.id.tvTab)
        TextView tvTab;
        @BindView(R.id.ivopen)
        ImageView ivopen;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
