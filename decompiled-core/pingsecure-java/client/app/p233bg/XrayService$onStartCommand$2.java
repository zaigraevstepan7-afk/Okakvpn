package com.pingsecure.client.app.p233bg;

import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.sync.Mutex;
import kotlinx.coroutines.sync.MutexImpl;

@DebugMetadata(m46516c = "com.pingsecure.client.app.bg.XrayService$onStartCommand$2", m46517f = "XrayService.kt", m46518l = {280, 100}, m46519m = "invokeSuspend")
/* loaded from: classes7.dex */
public final class XrayService$onStartCommand$2 extends SuspendLambda implements Function2 {
    Object L$0;
    Object L$1;
    int label;
    final /* synthetic */ XrayService this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public XrayService$onStartCommand$2(XrayService xrayService, Continuation continuation) {
        super(2, continuation);
        this.this$0 = xrayService;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new XrayService$onStartCommand$2(this.this$0, continuation);
    }

    /* JADX WARN: Code restructure failed: missing block: B:27:0x0043, code lost:
    
        if (r7.lock(r6) == r0) goto L19;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v3, types: [kotlinx.coroutines.sync.Mutex] */
    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    public final Object invokeSuspend(Object obj) {
        Mutex mutex;
        XrayService xrayService;
        MutexImpl mutexImpl;
        Mutex mutex2;
        Throwable th;
        Object startService;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        int i = this.label;
        try {
            if (i == 0) {
                ResultKt.throwOnFailure(obj);
                mutex = this.this$0.lifecycleMutex;
                xrayService = this.this$0;
                this.L$0 = mutex;
                this.L$1 = xrayService;
                this.label = 1;
                mutexImpl = (MutexImpl) mutex;
            } else {
                if (i != 1) {
                    if (i != 2) {
                        throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                    mutex2 = (Mutex) this.L$0;
                    try {
                        ResultKt.throwOnFailure(obj);
                        ((MutexImpl) mutex2).unlock(null);
                        return Unit.INSTANCE;
                    } catch (Throwable th2) {
                        th = th2;
                        ((MutexImpl) mutex2).unlock(null);
                        throw th;
                    }
                }
                xrayService = (XrayService) this.L$1;
                ?? r3 = (Mutex) this.L$0;
                ResultKt.throwOnFailure(obj);
                mutexImpl = r3;
            }
            this.L$0 = mutexImpl;
            this.L$1 = null;
            this.label = 2;
            startService = xrayService.startService(this);
            if (startService != coroutineSingletons) {
                mutex2 = mutexImpl;
                ((MutexImpl) mutex2).unlock(null);
                return Unit.INSTANCE;
            }
            return coroutineSingletons;
        } catch (Throwable th3) {
            mutex2 = mutexImpl;
            th = th3;
            ((MutexImpl) mutex2).unlock(null);
            throw th;
        }
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(CoroutineScope coroutineScope, Continuation continuation) {
        return ((XrayService$onStartCommand$2) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
    }
}
