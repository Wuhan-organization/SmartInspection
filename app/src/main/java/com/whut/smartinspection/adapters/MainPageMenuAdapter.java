package com.whut.smartinspection.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whut.smartinspection.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainPageMenuAdapter extends BaseAdapter {

    private List<MainPageMenu> menus;
    private Context mContext;

    public MainPageMenuAdapter(Context context, List<MainPageMenu> menus) {
        this.mContext = context;
        this.menus = menus;
    }

    @Override
    public int getCount() {
        return menus.size();
    }

    @Override
    public Object getItem(int position) {
        return menus.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_main_page_menu, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MainPageMenu menu = menus.get(position);
        Glide.with(mContext).load(menu.getImageLocal()).into(holder.ivMainPageMenu);
        holder.tvMainPageMenuName.setText(menu.getMenuName());

        return convertView;
    }

    class ViewHolder {
        @BindView(R.id.iv_main_page_menu)
        ImageView ivMainPageMenu;
        @BindView(R.id.tv_main_page_menu_name)
        TextView tvMainPageMenuName;

        public ViewHolder(View v) {
            ButterKnife.bind(this, v);
        }
    }

    public static class MainPageMenu {
        private String imageUrl;
        private String menuName;
        private String type;
        private String url;
        private int imageLocal;

        public int getImageLocal() {
            return imageLocal;
        }

        public void setImageLocal(int imageLocal) {
            this.imageLocal = imageLocal;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getMenuName() {
            return menuName;
        }

        public void setMenuName(String menuName) {
            this.menuName = menuName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

}
