package com.pingsecure.client.app;

import android.content.Intent;
import android.net.VpnService;
import com.chartboost.sdk.impl.C3381g;
import com.pingsecure.client.app.utils.SafeResult;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes7.dex */
public final class MainActivity$prepare$2 extends SuspendLambda implements Function2 {
    public final /* synthetic */ MainActivity this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MainActivity$prepare$2(MainActivity mainActivity, Continuation continuation) {
        super(2, continuation);
        this.this$0 = mainActivity;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new MainActivity$prepare$2(this.this$0, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Object obj2) {
        return ((MainActivity$prepare$2) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        MainActivity mainActivity = this.this$0;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        ResultKt.throwOnFailure(obj);
        boolean z = false;
        try {
            Intent prepare = VpnService.prepare(mainActivity);
            if (prepare != null) {
                mainActivity.prepareLauncher.launch(prepare);
                z = true;
            }
        } catch (Exception unused) {
            C3381g c3381g = MainActivity.serviceStartCallback;
            if (c3381g != null) {
                ((SafeResult) c3381g.f11073a).error("PERMISSION_DENIED", "VPN permission was denied", null);
            }
            mainActivity.getClass();
            MainActivity.serviceStartCallback = null;
        }
        return Boolean.valueOf(z);
    }
}
