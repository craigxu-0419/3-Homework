package cat.dev.catpro;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CatAdapter extends RecyclerView.Adapter<CatAdapter.CatViewHolder> implements View.OnClickListener {
    private Context context;
    public CatAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()) .inflate(R.layout.ll_cat, parent, false);
        CatViewHolder catViewHolder = new CatViewHolder(view);
        catViewHolder.itemView.setOnClickListener(this);
        return catViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        Cat cat = cats.get(position);
        holder.firstNameTextView.setText(cat.getName());
        holder.itemView.setTag(position);

    }
    private List<Cat> cats;

    public void setData(List<Cat> cats) {
        this.cats = cats;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (cats==null){
            return 0;
        }else{
            return  cats.size();
        }
    }

    @Override
    public void onClick(View view) {
        int position= (int) view.getTag();
        Log.e("click",position+"");
        Cat cat = cats.get(position);
        Intent intent = new Intent(context,CatActivity.class);
        intent.putExtra(CatActivity.intent_id,cat.getId());
        intent.putExtra(CatActivity.intent_name,cat.getName());
        context.startActivity(intent);
    }

    public static class CatViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView firstNameTextView;
        public CatViewHolder(View v) {
            super(v);
            firstNameTextView = v.findViewById(R.id.firstName);
        }
    }
}
