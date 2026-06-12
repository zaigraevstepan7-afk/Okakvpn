package com.pingsecure.client.app.handlers;

import com.pingsecure.client.app.utils.SafeResult;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.CoroutineScope;

/* loaded from: classes7.dex */
public final class ReviewHandler$onMethodCall$1 extends SuspendLambda implements Function2 {
    public final /* synthetic */ SafeResult $safeResult;
    public final /* synthetic */ ReviewHandler this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ReviewHandler$onMethodCall$1(ReviewHandler reviewHandler, SafeResult safeResult, Continuation continuation) {
        super(2, continuation);
        this.this$0 = reviewHandler;
        this.$safeResult = safeResult;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Continuation create(Object obj, Continuation continuation) {
        return new ReviewHandler$onMethodCall$1(this.this$0, this.$safeResult, continuation);
    }

    @Override // kotlin.jvm.functions.Function2
    public final Object invoke(Object obj, Object obj2) {
        ReviewHandler$onMethodCall$1 reviewHandler$onMethodCall$1 = (ReviewHandler$onMethodCall$1) create((CoroutineScope) obj, (Continuation) obj2);
        Unit unit = Unit.INSTANCE;
        reviewHandler$onMethodCall$1.invokeSuspend(unit);
        return unit;
    }

    @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
    public final Object invokeSuspend(Object obj) {
        SafeResult safeResult = this.$safeResult;
        CoroutineSingletons coroutineSingletons = CoroutineSingletons.COROUTINE_SUSPENDED;
        ResultKt.throwOnFailure(obj);
        try {
            ReviewHandler.access$openGoogleDialog(this.this$0);
            safeResult.success(null);
        } catch (Exception e) {
            safeResult.error("REVIEW_ERROR", "Failed to show review dialog: " + e.getMessage(), null);
        }
        return Unit.INSTANCE;
    }
}
