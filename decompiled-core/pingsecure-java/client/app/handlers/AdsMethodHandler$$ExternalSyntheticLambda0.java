package com.pingsecure.client.app.handlers;

import android.util.Log;
import com.fingerprintjs.android.fingerprint.DeviceIdResult;
import com.pingsecure.client.app.utils.SafeResult;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;

/* loaded from: classes7.dex */
public final /* synthetic */ class AdsMethodHandler$$ExternalSyntheticLambda0 implements Function1 {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ SafeResult f$0;

    public /* synthetic */ AdsMethodHandler$$ExternalSyntheticLambda0(SafeResult safeResult, int i) {
        this.$r8$classId = i;
        this.f$0 = safeResult;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                Boolean bool = (Boolean) obj;
                Log.d("AdsMethodHandler", "Ads initialized: " + bool.booleanValue());
                this.f$0.success(bool);
                break;
            default:
                DeviceIdResult deviceResult = (DeviceIdResult) obj;
                Intrinsics.checkNotNullParameter(deviceResult, "deviceResult");
                this.f$0.success(deviceResult.deviceId);
                break;
        }
        return Unit.INSTANCE;
    }
}
