package com.esioner.votecenter.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.esioner.votecenter.MainActivity;

/**
 * 开机自启
 *
 * @author Esioner
 * @date 2018/1/2
 */

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intentBoot = new Intent(context, MainActivity.class);
        intentBoot.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intentBoot);
    }
}
