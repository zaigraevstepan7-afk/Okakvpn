package com.pingsecure.client.app.p233bg.xray;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultCallerKt;
import com.google.android.gms.tasks.OnSuccessListener;
import com.unity3d.ads.core.data.datasource.AndroidAppSetIdDataSource;
import kotlin.jvm.functions.Function1;
import libXray.DialerController;

/* loaded from: classes7.dex */
public final /* synthetic */ class Xray$$ExternalSyntheticLambda1 implements ActivityResultCallback, DialerController, OnSuccessListener {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Function1 f$0;

    public /* synthetic */ Xray$$ExternalSyntheticLambda1(Function1 function1, int i) {
        this.$r8$classId = i;
        this.f$0 = function1;
    }

    @Override // androidx.activity.result.ActivityResultCallback
    public void onActivityResult(Object obj) {
        switch (this.$r8$classId) {
            case 1:
                ActivityResultCallerKt.registerForActivityResult$lambda$0(this.f$0, obj);
                break;
            default:
                ActivityResultCallerKt.registerForActivityResult$lambda$1(this.f$0, obj);
                break;
        }
    }

    @Override // com.google.android.gms.tasks.OnSuccessListener
    public void onSuccess(Object obj) {
        AndroidAppSetIdDataSource.invoke$lambda$1(this.f$0, obj);
    }

    @Override // libXray.DialerController
    public boolean protectFd(long j) {
        boolean start$lambda$0;
        start$lambda$0 = Xray.start$lambda$0(this.f$0, j);
        return start$lambda$0;
    }
}
