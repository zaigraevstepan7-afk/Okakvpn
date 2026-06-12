package com.pingsecure.client.app.handlers;

import com.pingsecure.client.app.Application;
import com.pingsecure.client.app.utils.SafeResult;
import java.io.File;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlin.p253io.FilesKt;
import kotlin.text.Charsets;
import kotlinx.coroutines.CoroutineScope;
import okio.Okio;

/* loaded from: classes7.dex */
public final class MethodHandler$handleGetLogs$1 extends SuspendLambda implements Function2 {
    public final /* synthetic */ SafeResult $result;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MethodHandler$handleGetLogs$1(SafeResult safeResult, Continuation continuation) {
        super(2, continuation);
        this.$result = safeResult;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new MethodHandler$handleGetLogs$1(this.$result, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Object obj2) {
        MethodHandler$handleGetLogs$1 methodHandler$handleGetLogs$1 = (MethodHandler$handleGetLogs$1) create((CoroutineScope) obj, (Continuation) obj2);
        Unit unit = Unit.INSTANCE;
        methodHandler$handleGetLogs$1.invokeSuspend(unit);
        return unit;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        SafeResult safeResult = this.$result;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        ResultKt.throwOnFailure(obj);
        try {
            Application application = Application.application;
            File file = new File(Okio.getApplication().getFilesDir(), "xray.log");
            safeResult.success(file.exists() ? FilesKt.readText(file, Charsets.UTF_8) : "");
        } catch (Exception e) {
            safeResult.error("LOG_ERROR", "Failed to read logs: " + e.getMessage(), null);
        }
        return Unit.INSTANCE;
    }
}
