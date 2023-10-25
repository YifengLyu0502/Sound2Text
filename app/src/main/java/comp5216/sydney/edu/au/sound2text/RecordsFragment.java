package comp5216.sydney.edu.au.sound2text;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class RecordsFragment extends Fragment {
    ListView RecordsList;
    private ArrayList<String> items;
    private ArrayAdapter<String> adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.records_fragment, container, false);
        RecordsList = view.findViewById(R.id.Records_List);
// Initialize data
        items = new ArrayList<>();


        // Initialize adapter
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item, R.id.textView, items);

        //String receivedString = getArguments().getString("key_string");

        //读取数据库里的records，替换掉下面的Item 1,Item 2 ...
        items.add("Item 1");
        items.add("Item 2");

        //点击Item，跳转到相对应的Item的界面

        //点击翻译按钮，翻译文本 (上半部分是原文本，下半部分是翻译后的文本)

        // Set adapter to the ListView
        RecordsList.setAdapter(adapter);
        // Inflate the layout for this fragment
        return view;
    }
    // Function to add new items to the list
    private void addItem(String newItem) {
        items.add(newItem);
        adapter.notifyDataSetChanged();
    }
    // You can add other lifecycle methods if needed.
}
