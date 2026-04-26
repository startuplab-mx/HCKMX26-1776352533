package com.example.ada.uriel


import android.os.Handler
import android.os.Looper
import android.util.Log

class MessageProcesor(private val modelManager: AdaModelManager) {

    private val handler = Handler(Looper.getMainLooper())
    private var debounceRunnable: Runnable? = null
    private val debounceDelay: Long = 1500

    private var currentSessionName: String = ""

    private val recentMessagesCache = LinkedHashSet<String>()
    private val cacheLimit = 40

    private val contextWindow = ArrayDeque<ChatMessage>()
    private val windowLimit = 15

    private val exactNoiseBlacklist = setOf(
        "Enviar mensaje...",
        "visto justo ahora",
        "Visto hace un momento",
        "Llamada perdida",
        "Toca para volver a llamar",
        "Llamada",
        "Mensaje",
        "Tú"
    )

    private val timeRegexIg = Regex("^\\d{1,2}:\\d{2}[\\s\\u202F]?([AaPp]\\.?[Mm]\\.?)$")
    private val timeRegexWa = Regex("^\\d{1,2}:\\s?\\d{2}\\s?(a\\.\\s?m\\.|p\\.\\s?m\\.|AM|PM|am|pm)$")
    private val durationRegex = Regex("^\\d+\\s?(min|h|s)$")
    private val unreadRegex = Regex("^\\d+\\s?mensajes no leidos$")
    private val replyingRegex = Regex("^Respondiendo a .*$")
    private val replyRegex = Regex("^(Respondiendo a|Respondido|Mensaje respondido).*$", RegexOption.IGNORE_CASE)

    fun updateSessionName(name: String) {
        currentSessionName = name.trim()
    }

    fun onScreenUpdated(extractedMessages: List<ChatMessage>) {
        debounceRunnable?.let { handler.removeCallbacks(it) }

        debounceRunnable = Runnable {
            processStableScreen(extractedMessages)
        }

        handler.postDelayed(debounceRunnable!!, debounceDelay)
    }

    fun clearContext() {
        debounceRunnable?.let { handler.removeCallbacks(it) }
        recentMessagesCache.clear()
        contextWindow.clear()
    }

    private fun processStableScreen(messages: List<ChatMessage>) {
        val newMessages = mutableListOf<ChatMessage>()

        for (message in messages) {
            if (isNoise(message.text)) continue
            if (isDuplicate(message.text)) continue

            newMessages.add(message)
        }

        if (newMessages.isNotEmpty()) {
            updateContextWindow(newMessages)
        }
    }

    private fun updateContextWindow(newMessages: List<ChatMessage>) {
        contextWindow.addAll(newMessages)

        while (contextWindow.size > windowLimit) {
            contextWindow.removeFirst()
        }

        val formattedContext = contextWindow.joinToString(separator = "\n") {
            "[${it.role}]: ${it.text}"
        }

        Log.d("ADA_LOG", "Ventana de contexto enviada a IA:\n$formattedContext")

        modelManager.analyzeContext(formattedContext)
    }

    private fun isNoise(text: String): Boolean {
        val trimmed = text.trim()

        if (exactNoiseBlacklist.contains(trimmed)) return true
        if (trimmed == currentSessionName) return true
        if (timeRegexIg.matches(trimmed)) return true
        if (timeRegexWa.matches(trimmed)) return true
        if (durationRegex.matches(trimmed)) return true
        if (unreadRegex.matches(trimmed)) return true
        if (replyingRegex.matches(trimmed)) return true
        if (replyRegex.containsMatchIn(trimmed)) return true

        return false
    }

    private fun isDuplicate(text: String): Boolean {
        val cleanText = text.trim()

        if (recentMessagesCache.contains(cleanText)) {
            return true
        }

        recentMessagesCache.add(cleanText)

        if (recentMessagesCache.size > cacheLimit) {
            val iterator = recentMessagesCache.iterator()
            iterator.next()
            iterator.remove()
        }

        return false
    }
}