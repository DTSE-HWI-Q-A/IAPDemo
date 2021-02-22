package com.example.iapexample.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.iapexample.R;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ProductInfo;

import java.util.List;

public class ProductListAdapter extends BaseAdapter {

    private Context mContext;
    private List<ProductInfo> productInfos;

    public ProductListAdapter(Context context, List<ProductInfo> productInfos) {
        mContext = context;
        this.productInfos = productInfos;
    }

    @Override
    public int getCount() {
        return productInfos.size();
    }


    @Override
    public Object getItem(int position) {
        if (productInfos != null && productInfos.size() > 0) {
            return productInfos.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ProductInfo productInfo = productInfos.get(position);
        ProductListViewHolder holder = null;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_layout, null);
            holder = new ProductListViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ProductListViewHolder) convertView.getTag();
        }

        holder.productName.setText(productInfo.getProductName());
        holder.productPrice.setText(productInfo.getPrice());

        if (productInfo.getPriceType() == IapClient.PriceType.IN_APP_NONCONSUMABLE) {
            holder.imageView.setVisibility(View.GONE);
        }else{
            if(productInfo.getProductName().equals("Power Gem")){
                holder.imageView.setImageResource(R.drawable.violet_ball);
            }else if(productInfo.getProductName().equals("Magic Gem")){
                holder.imageView.setImageResource(R.drawable.blue_ball);
            }else if(productInfo.getProductName().equals("Soul Gem")){
                holder.imageView.setImageResource(R.drawable.green_ball);
            }else if(productInfo.getProductName().equals("Fire Gem")){
                holder.imageView.setImageResource(R.drawable.red_ball);
            }else if(productInfo.getProductName().equals("Earth Gem")){
                holder.imageView.setImageResource(R.drawable.gold_ball);
            }
        }

        return convertView;
    }

    static class ProductListViewHolder {
        TextView productName;
        TextView productPrice;
        ImageView imageView;

        ProductListViewHolder(View view) {
            productName = (TextView) view.findViewById(R.id.item_name);
            productPrice = (TextView) view.findViewById(R.id.item_price);
            imageView = (ImageView) view.findViewById(R.id.item_image);

        }

    }


}
