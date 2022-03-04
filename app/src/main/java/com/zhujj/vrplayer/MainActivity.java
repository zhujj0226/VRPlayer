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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ArrayList<HolderModel> holderModels = new ArrayList<>();
        holderModels.add(new HolderModel(getDrawableUri(R.drawable.bitmap360)));
        holderModels.add(new HolderModel(getDrawableUri(R.drawable.cube_back)));
        holderModels.add(new HolderModel(getDrawableUri(R.drawable.dome_pic)));
        holderModels.add(new HolderModel(getDrawableUri(R.drawable.bitmap360)));
        holderModels.add(new HolderModel(getDrawableUri(R.drawable.cube_back)));
        holderModels.add(new HolderModel(getDrawableUri(R.drawable.dome_pic)));
        ListAdapter listAdapter = new ListAdapter(holderModels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);
        getPermission();
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
    }

    private void start(Context context, Uri uri, Class<? extends Activity> clz) {
        Intent i = new Intent(context, clz);
        i.setData(uri);
        context.startActivity(i);
    }

    private static class HolderModel {
        private Uri uri;

        private HolderModel(Uri uri) {
            this.uri = uri;
        }
    }

    private class ImageHolder extends RecyclerView.ViewHolder {
        public CircleImageView imageView;

        public ImageHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_image, parent,false);
//            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_image, null);
            ImageHolder imageHolder = new ImageHolder(view);
            return imageHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ImageHolder holder, int position) {
            holder.imageView.setImageURI(dataList.get(position).uri);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    start(MainActivity.this,null,VideoPlayerActivity.class);
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