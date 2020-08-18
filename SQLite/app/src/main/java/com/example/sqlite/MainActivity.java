package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    String TAG = MainActivity.class.getSimpleName() + "My";

    private final String DB_NAME = "MyList.db";
    private String TABLE_NAME = "MyTable";
    private final int DB_VERSION = 1;
    SQLiteDataBaseHelper mDBHelper;

    ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();//取得所有資料
    ArrayList<HashMap<String, String>> getNowArray = new ArrayList<>();//取得被選中的項目資料

    EditText CommodityName, StoreName, Price;
    Button add_btn, edit_btn;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Stetho.initializeWithDefaults(this);
        mDBHelper = new SQLiteDataBaseHelper(this,
                DB_NAME, null, DB_VERSION, TABLE_NAME);
        ; //初始化資料庫
        mDBHelper.checkTable();//確認是否存在資料表，沒有則新增
        arrayList = mDBHelper.showAll();//撈取資料表內所有資料

        CommodityName = findViewById(R.id.editTextCommodityName);
        StoreName = findViewById(R.id.editTextStoreName);
        Price = findViewById(R.id.editTextPrice);
        add_btn = findViewById(R.id.button_add);
        edit_btn = findViewById(R.id.button_modify);
        recyclerViewSetting();//設置RecyclerView

    }

    private void clearAll() {//清空目前所選以及所有editText
        CommodityName.setText("");
        StoreName.setText("");
        Price.setText("");
        getNowArray.clear();
    }

    private void recyclerViewSetting() {//設置RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        myAdapter = new MyAdapter();
        recyclerView.setAdapter(myAdapter);
        setRecyclerFunction(recyclerView);//設置RecyclerView手勢功能
    }

    public void add_event(View view) {
        if (CommodityName.getText().toString().matches("") || StoreName.getText().toString().matches("")
                || Price.getText().toString().matches("")) {
            Toast toast = Toast.makeText(MainActivity.this, "欄位不能是空白!!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            mDBHelper.addData(CommodityName.getText().toString(),
                    StoreName.getText().toString(),
                    Integer.parseInt(Price.getText().toString()));
            arrayList = mDBHelper.showAll();
            myAdapter.notifyDataSetChanged();
            clearAll();//清空目前所選以及所有editText
            Toast toast = Toast.makeText(MainActivity.this, "新增成功", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void modify_event(View view) {
        if (CommodityName.getText().toString().matches("") || StoreName.getText().toString().matches("")
                || Price.getText().toString().matches("")) {
            Toast toast = Toast.makeText(MainActivity.this, "欄位不能是空白!!", Toast.LENGTH_LONG);
            toast.show();
        } else {
            mDBHelper.modify(Integer.parseInt(getNowArray.get(0).get("id")),
                    CommodityName.getText().toString(),
                    StoreName.getText().toString(),
                    Integer.parseInt(Price.getText().toString()));
            arrayList = mDBHelper.showAll();
            myAdapter.notifyDataSetChanged();
            clearAll();//清空目前所選以及所有editText
            Toast toast = Toast.makeText(MainActivity.this, "修改成功", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        //設置Adapter
        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, null);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.tvTitle.setText("日期:" + arrayList.get(position).get("CreateDate") + " | " + arrayList.get(position).get("StoreName"));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getNowArray.clear();
                    getNowArray = mDBHelper.searchById(arrayList.get(position).get("id"));
                    try {
                        CommodityName.setText(getNowArray.get(0).get("CommodityName"));
                        StoreName.setText(getNowArray.get(0).get("StoreName"));
                        Price.setText(getNowArray.get(0).get("Price"));
                    } catch (Exception e) {
                        Log.d(TAG, "onBindViewHolder: " + e.getMessage());
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView tvTitle;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvTitle = itemView.findViewById(android.R.id.text1);
            }
        }
    }

    private void setRecyclerFunction(RecyclerView recyclerView) {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {//設置RecyclerView手勢功能
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return makeMovementFlags(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                    case ItemTouchHelper.RIGHT:
                        mDBHelper.deleteByIdEZ(arrayList.get(position).get("id"));
                        arrayList.remove(position);
                        arrayList = mDBHelper.showAll();
                        myAdapter.notifyItemRemoved(position);
                        break;
                }
            }
        });
        helper.attachToRecyclerView(recyclerView);
    }


}