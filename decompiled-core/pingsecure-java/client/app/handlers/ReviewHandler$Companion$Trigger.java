package com.pingsecure.client.app.handlers;

import kotlin.random.RandomKt;

/* JADX WARN: Failed to restore enum class, 'enum' modifier and super class removed */
/* JADX WARN: Unknown enum class pattern. Please report as an issue! */
/* loaded from: classes7.dex */
public final class ReviewHandler$Companion$Trigger {
    public static final /* synthetic */ ReviewHandler$Companion$Trigger[] $VALUES;

    static {
        ReviewHandler$Companion$Trigger[] reviewHandler$Companion$TriggerArr = {new ReviewHandler$Companion$Trigger("ShowReview", 0)};
        $VALUES = reviewHandler$Companion$TriggerArr;
        RandomKt.enumEntries(reviewHandler$Companion$TriggerArr);
    }

    public static ReviewHandler$Companion$Trigger valueOf(String str) {
        return (ReviewHandler$Companion$Trigger) Enum.valueOf(ReviewHandler$Companion$Trigger.class, str);
    }

    public static ReviewHandler$Companion$Trigger[] values() {
        return (ReviewHandler$Companion$Trigger[]) $VALUES.clone();
    }
}
