package com.pingsecure.client.app.p233bg;

import android.content.Intent;
import android.net.VpnService;
import android.util.Log;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlinx.coroutines.JobKt;

/* loaded from: classes7.dex */
public final class VPNService extends VpnService {
    private final XrayService service = new XrayService(this);

    @Override // android.net.VpnService
    public void onRevoke() {
        JobKt.runBlocking(EmptyCoroutineContext.INSTANCE, new VPNService$onRevoke$1(this, null));
    }

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Log.i("VPNService", "starting");
        return this.service.onStartCommand$app_release();
    }
}
