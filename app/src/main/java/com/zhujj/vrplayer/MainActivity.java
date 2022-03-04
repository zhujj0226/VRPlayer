package com.zhujj.vrplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhujj.vrplayer.http.HttpHelper;
import com.zhujj.vrplayer.http.ListBean;
import com.zhujj.vrplayer.http.ResponseCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    int[] pics = {R.drawable.bitmap360, R.drawable.cube_back, R.drawable.dome_pic};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        getPermission();
        new HttpHelper("http://82.156.254.39/live/vr/list").execute(new ResponseCallback() {
            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                Gson gson = new Gson();
                Log.e("zhujj", response);
                try {
                    List<ListBean> listBeans = gson.fromJson(response, new TypeToken<List<ListBean>>() {
                    }.getType());
                    ArrayList<HolderModel> holderModels = new ArrayList<>();
                    for (int i = 0; i < listBeans.size(); i++) {
//                        holderModels.add(new HolderModel(getDrawableUri(pics[i % pics.length]), listBeans.get(i).url));
                        holderModels.add(new HolderModel(listBeans.get(i).pic, listBeans.get(i).url, listBeans.get(i).title));
                    }
                    ListAdapter listAdapter = new ListAdapter(holderModels);
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerView.setAdapter(listAdapter);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "接口挂了", Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void getPermission() {
        int hasPermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (hasPermission == PackageManager.PERMISSION_GRANTED) {
            //已获取权限
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
        int hasPermission1 = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.INTERNET);
        if (hasPermission1 == PackageManager.PERMISSION_GRANTED) {
            //已获取权限
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.INTERNET},
                    1);
        }
    }

    private void start(Context context, Uri uri, Class<? extends Activity> clz) {
        Intent i = new Intent(context, clz);
        i.setData(uri);
        context.startActivity(i);
    }

    private static class HolderModel {
        private Uri uri;
        String pic;
        String rtmp;
        String text;

        private HolderModel(Uri uri, String rtmp) {
            this.uri = uri;
            this.rtmp = rtmp;
        }

        private HolderModel(String pic, String rtmp, String text) {
            this.pic = pic;
            this.rtmp = rtmp;
            this.text = text;
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;
        public TextView textView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.textview);
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ImageHolder> {
        private List<HolderModel> dataList;

        public ListAdapter(List<HolderModel> list) {
            this.dataList = list;
        }

        @NonNull
        @Override
        public ImageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_image, parent, false);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_image, null);
            ImageHolder imageHolder = new ImageHolder(view);
            return imageHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
            Glide.with(MainActivity.this).load(dataList.get(position).pic).into(holder.imageView);
            holder.textView.setText(dataList.get(position).text);
//            holder.imageView.setImageURI(dataList.get(position).uri);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    start(MainActivity.this, Uri.parse(dataList.get(position).rtmp), VideoPlayerActivity.class);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

    }

    private Uri getDrawableUri(@DrawableRes int resId) {
        Resources resources = getResources();
        return Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + resources.getResourcePackageName(resId) + '/' + resources.getResourceTypeName(resId) + '/' + resources.getResourceEntryName(resId));
    }

}