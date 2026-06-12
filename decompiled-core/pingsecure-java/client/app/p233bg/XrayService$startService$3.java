package com.pingsecure.client.app.p233bg;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;

/* loaded from: classes7.dex */
public /* synthetic */ class XrayService$startService$3 extends FunctionReferenceImpl implements Function1 {
    public XrayService$startService$3(Object obj) {
        super(1, obj, VPNService.class, "protect", "protect(I)Z", 0);
    }

    public final Boolean invoke(int i) {
        return Boolean.valueOf(((VPNService) this.receiver).protect(i));
    }

    @Override // kotlin.jvm.functions.Function1
    public /* bridge */ /* synthetic */ Object invoke(Object obj) {
        return invoke(((Number) obj).intValue());
    }
}
