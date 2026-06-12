package com.pingsecure.client.app.handlers;

import com.pingsecure.client.app.p233bg.XrayService;
import com.pingsecure.client.app.utils.SafeResult;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes7.dex */
public final class MethodHandler$handleStop$1 extends SuspendLambda implements Function2 {
    public final /* synthetic */ SafeResult $result;
    public final /* synthetic */ MethodHandler this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public MethodHandler$handleStop$1(MethodHandler methodHandler, SafeResult safeResult, Continuation continuation) {
        super(2, continuation);
        this.this$0 = methodHandler;
        this.$result = safeResult;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new MethodHandler$handleStop$1(this.this$0, this.$result, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Object obj2) {
        MethodHandler$handleStop$1 methodHandler$handleStop$1 = (MethodHandler$handleStop$1) create((CoroutineScope) obj, (Continuation) obj2);
        Unit unit = Unit.INSTANCE;
        methodHandler$handleStop$1.invokeSuspend(unit);
        return unit;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        Unit unit = Unit.INSTANCE;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        ResultKt.throwOnFailure(obj);
        try {
            if (this.this$0.isStopInProgress) {
                this.$result.success(Boolean.TRUE);
                return unit;
            }
            this.this$0.isStopInProgress = true;
            XrayService.Companion.stop();
            this.$result.success(Boolean.TRUE);
            return unit;
        } catch (Exception e) {
            this.$result.error("STOP_ERROR", "Failed to stop service: " + e.getMessage(), null);
            return unit;
        } finally {
            this.this$0.isStopInProgress = false;
        }
    }
}
