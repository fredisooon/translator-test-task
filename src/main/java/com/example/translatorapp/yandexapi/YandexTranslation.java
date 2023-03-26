package com.example.translatorapp.yandexapi;

public enum YandexTranslation {
    SOURCE("sourceLanguageCode"),
    TARGET("targetLanguageCode"),
    FORMAT("format"),
    TEXT("texts"),
    TEXT_DETECT("text"),
    HINT("languageCodeHints");

    YandexTranslation(String s) {
    }
}
