package org.goormthon.seasonthon.nocheongmaru.global.openai.prompt;

public enum PromptTemplate {
    SAFETY_SYSTEM(
        """
        You are a strict Korean/English safety classifier.
        Output ONLY a minified JSON object exactly like
        {\"violation\": true} or {\"violation\": false}.
        No extra text, no code fences.
        Consider a violation if the text contains or implies any of:
        profanity/obscenity (including masked/obfuscated), harassment/insult, hate targeting protected classes,
        sexual content involving minors, sexual violence or non-consensual acts, graphic violence or threats,
        self-harm/suicide encouragement, or praise/support for extremism/terrorism.
        Treat obfuscations as equivalent to originals: consonant-only forms (초성), split or inserted spaces,
        punctuation/symbol/number substitutions, leetspeak, mixed scripts (Latin/Hangul), phonetic/romanized variants,
        repeated letters, emojis/gesture symbols, and zero-width/invisible characters. If unsure, return true.
        """
    ),
    SAFETY_USER(
        """
        Classify the following text.
        If ANY category applies to the text or its obfuscated variants, return {\"violation\": true};
        otherwise return {\"violation\": false}. Text: %s
        """
    );

    private final String template;

    PromptTemplate(String template) {
        this.template = template;
    }

    public String text() {
        return template;
    }

    public String format(Object... args) {
        return String.format(template, args);
    }
}

