package com.emraz.parkinghero.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.emraz.parkinghero.R;
import com.emraz.parkinghero.rest.model.NewsDoc;
import java.util.List;

/**
 * Created by Yousuf on 7/28/2017.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {


  private int rowLayout;
  private Context context;
  private List<NewsDoc> newsDocList;

  private String TAG = getClass().getSimpleName();

  public NewsAdapter(int rowLayout, Context context, List<NewsDoc> newsDocList) {
    this.rowLayout = rowLayout;
    this.context = context;
    this.newsDocList = newsDocList;
  }

  @Override public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
    return new NewsViewHolder(view);
  }

  @Override public void onBindViewHolder(NewsViewHolder holder, int position) {

    NewsDoc newsDoc = newsDocList.get(position);

    holder.textViewTitle.setText(newsDoc.getTitle());
    holder.textViewDate.setText(newsDoc.getDate());
    holder.textViewDescription.setText(newsDoc.getDescription());

  }

  @Override public int getItemCount() {
    return newsDocList.size();
  }




  public static class NewsViewHolder extends RecyclerView.ViewHolder{

    TextView textViewTitle, textViewDate, textViewDescription;

    public NewsViewHolder(View itemView) {
      super(itemView);

      textViewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
      textViewDate = (TextView) itemView.findViewById(R.id.textViewDate);
      textViewDescription = (TextView) itemView.findViewById(R.id.textViewDescription);

    }
  }
}
