package com.pingsecure.client.app.p233bg;

import com.pingsecure.client.app.R;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

@DebugMetadata(m46516c = "com.pingsecure.client.app.bg.XrayService$startService$4", m46517f = "XrayService.kt", m46518l = {}, m46519m = "invokeSuspend")
/* loaded from: classes7.dex */
public final class XrayService$startService$4 extends SuspendLambda implements Function2 {
    int label;
    final /* synthetic */ XrayService this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public XrayService$startService$4(XrayService xrayService, Continuation continuation) {
        super(2, continuation);
        this.this$0 = xrayService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new XrayService$startService$4(this.this$0, continuation);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        ServiceNotification serviceNotification;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        if (this.label != 0) {
            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
        }
        ResultKt.throwOnFailure(obj);
        serviceNotification = this.this$0.notification;
        serviceNotification.show("", R.string.status_started);
        return Unit.INSTANCE;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((XrayService$startService$4) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }
}
