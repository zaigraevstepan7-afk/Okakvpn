package com.pingsecure.client.app.handlers;

import com.pingsecure.client.app.utils.SafeResult;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.scheduling.DefaultIoScheduler;

/* loaded from: classes7.dex */
public final class AndroidLogsHandler$handleGetAndroidLogs$1 extends SuspendLambda implements Function2 {
    public final /* synthetic */ SafeResult $result;
    public int label;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AndroidLogsHandler$handleGetAndroidLogs$1(SafeResult safeResult, Continuation continuation) {
        super(2, continuation);
        this.$result = safeResult;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new AndroidLogsHandler$handleGetAndroidLogs$1(this.$result, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Object obj2) {
        return ((AndroidLogsHandler$handleGetAndroidLogs$1) create((CoroutineScope) obj, (Continuation) obj2)).invokeSuspend(Unit.INSTANCE);
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        SafeResult safeResult = this.$result;
        try {
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                DefaultIoScheduler defaultIoScheduler = Dispatchers.f53823IO;
                AndroidLogsHandler$handleGetAndroidLogs$1$logs$1 androidLogsHandler$handleGetAndroidLogs$1$logs$1 = new AndroidLogsHandler$handleGetAndroidLogs$1$logs$1(2, null);
                this.label = 1;
                obj = JobKt.withContext(androidLogsHandler$handleGetAndroidLogs$1$logs$1, defaultIoScheduler, this);
                if (obj == coroutineSingletons) {
                    return coroutineSingletons;
                }
            } else {
                if (i != 1) {
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                }
                ResultKt.throwOnFailure(obj);
            }
            safeResult.success((String) obj);
        } catch (Exception e) {
            safeResult.error("LOG_ERROR", "Failed to read Android logs: " + e.getMessage(), null);
        }
        return Unit.INSTANCE;
    }
}
