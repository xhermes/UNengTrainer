package me.xeno.unengtrainer.view.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import me.xeno.unengtrainer.R;

/**
 * Created by xeno on 2017/5/22.
 */

public class ServiceHolder extends RecyclerView.ViewHolder  {

    private View rootView;
    private TextView addressView;

    private View checkView;// connect success tick
    private View connectingView;// connecting loading progressbar

    public ServiceHolder(View itemView) {
        super(itemView);
        addressView = (TextView) itemView.findViewById(R.id.address);
        rootView = itemView.findViewById(R.id.root);

        checkView = itemView.findViewById(R.id.check);
        connectingView = itemView.findViewById(R.id.connecting);
    }

    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    public TextView getAddressView() {
        return addressView;
    }

    public void setAddressView(TextView addressView) {
        this.addressView = addressView;
    }
}
