package com.pingsecure.client.app.p233bg;

import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;

@DebugMetadata(m46516c = "com.pingsecure.client.app.bg.XrayService", m46517f = "XrayService.kt", m46518l = {110, 130, 138}, m46519m = "startService")
/* loaded from: classes7.dex */
public final class XrayService$startService$1 extends ContinuationImpl {
    Object L$0;
    int label;
    /* synthetic */ Object result;
    final /* synthetic */ XrayService this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public XrayService$startService$1(XrayService xrayService, Continuation continuation) {
        super(continuation);
        this.this$0 = xrayService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Object startService;
        this.result = obj;
        this.label |= Integer.MIN_VALUE;
        startService = this.this$0.startService(this);
        return startService;
    }
}
