package com.airbnb.lottie.parser.moshi;

import androidx.annotation.Nullable;

final class JsonDataException extends RuntimeException {
    JsonDataException(@Nullable String str) {
        super(str);
    }
}
