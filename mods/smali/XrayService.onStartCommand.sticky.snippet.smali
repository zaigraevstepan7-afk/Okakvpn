    invoke-static {v0, v2, v2, v1, v3}, Lkotlinx/coroutines/JobKt;->launch$default(Lkotlinx/coroutines/CoroutineScope;Lkotlin/coroutines/CoroutineContext;Lkotlinx/coroutines/CoroutineStart;Lkotlin/jvm/functions/Function2;I)Lkotlinx/coroutines/StandaloneCoroutine;

    .line 67
    .line 68
    .line 69
    # stability patch: START_STICKY (0x1) so the OS restarts the VPN service if killed
    const/4 v0, 0x1

    .line 70
    return v0
.end method
