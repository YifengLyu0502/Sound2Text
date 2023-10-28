package comp5216.sydney.edu.au.sound2text;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {


    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textView1, textView2;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.text1);
            textView2 = itemView.findViewById(R.id.text2);
        }
    }

    ArrayList<ModeBean> arrayList = new ArrayList<>();

    @NonNull
    @Override
    public MyAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.MyViewHolder holder, int position) {
        holder.textView1.setText(arrayList.get(position).text);
        holder.textView2.setText(arrayList.get(position).label);
        holder.itemView.setOnClickListener(view -> {
            Intent intent = new Intent(holder.itemView.getContext(), RecordActivity.class);
            intent.putExtra("Text",arrayList.get(position).text);
            intent.putExtra("comments",arrayList.get(position).comments);
            intent.putExtra("label",arrayList.get(position).label);
            intent.putExtra("id",arrayList.get(position).id);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void bindData(ArrayList<ModeBean> list){
        arrayList.clear();
        arrayList.addAll(list);
        notifyDataSetChanged();
    }
}
