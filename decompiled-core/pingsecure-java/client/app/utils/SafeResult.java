package com.pingsecure.client.app.utils;

import android.os.Handler;
import android.os.Looper;
import com.p232my.target.a3$$ExternalSyntheticLambda0;
import com.p232my.target.g7$$ExternalSyntheticLambda1;
import io.flutter.embedding.engine.systemchannels.RestorationChannel;
import io.flutter.plugin.common.MethodChannel;
import kotlin.jvm.internal.Intrinsics;
import yads.co1$$ExternalSyntheticLambda3;

/* loaded from: classes7.dex */
public final class SafeResult implements MethodChannel.Result {
    public volatile boolean isUsed;
    public final Handler mainHandler = new Handler(Looper.getMainLooper());
    public final RestorationChannel.C175631 result;

    public SafeResult(RestorationChannel.C175631 c175631) {
        this.result = c175631;
    }

    @Override // io.flutter.plugin.common.MethodChannel.Result
    public final void error(String errorCode, String str, Object obj) {
        Intrinsics.checkNotNullParameter(errorCode, "errorCode");
        if (markUsed()) {
            this.mainHandler.post(new co1$$ExternalSyntheticLambda3(this, errorCode, str, obj, 8));
        }
    }

    public final synchronized boolean markUsed() {
        boolean z;
        if (this.isUsed) {
            z = false;
        } else {
            z = true;
            this.isUsed = true;
        }
        return z;
    }

    @Override // io.flutter.plugin.common.MethodChannel.Result
    public final void notImplemented() {
        if (markUsed()) {
            this.mainHandler.post(new a3$$ExternalSyntheticLambda0(this, 29));
        }
    }

    @Override // io.flutter.plugin.common.MethodChannel.Result
    public final void success(Object obj) {
        if (markUsed()) {
            this.mainHandler.post(new g7$$ExternalSyntheticLambda1(25, this, obj));
        }
    }
}
