package com.example.ada.uriel

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import android.graphics.Rect
import android.util.Log

data class ChatMessage(val text: String, val role: String)

class AdaAccessibilityService : AccessibilityService() {

    private val messageProcessor = MessageProcessor(AdaModelManager())
    private val whatsappExtractor = WhatsAppExtractor()
    private val instagramExtractor = InstagramExtractor()


    private var currentChatSession: String? = null
    private var needsSessionUpdate: Boolean = true

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        val packageName = event?.packageName?.toString() ?: return
        val eventType = event.eventType



        val targetApps = listOf(
            "com.whatsapp",
            "com.instagram.android",
            "com.facebook.orca",
            "com.zhiliaoapp.musically"
        )

        if (targetApps.contains(packageName)) {
            val rootNode = rootInActiveWindow ?: return

            if (eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
                needsSessionUpdate = true
            }

            val screenWidth = resources.displayMetrics.widthPixels
            val screenHeight = resources.displayMetrics.heightPixels

            if (needsSessionUpdate) {
                val detectedSession = extractSessionName(rootNode, screenHeight)

                if (detectedSession != null) {
                    if (detectedSession != currentChatSession) {
                        currentChatSession = detectedSession
                        messageProcessor.updateSessionName(currentChatSession!!)
                        messageProcessor.clearContext()
                        Log.d("ADA_RADAR", "Sesión actualizada: $currentChatSession")
                    }
                    needsSessionUpdate = false
                }
            } else {
                val invalidMenuScreens = setOf(
                    "Preguntar a Meta AI o buscar",
                    "WhatsApp",
                    "Instagram",
                    "Bandeja de entrada",
                    "Chats",
                    "Buscar"
                )

                if (currentChatSession != null && !invalidMenuScreens.contains(currentChatSession)) {

                    val extractedMessages = when (packageName) {
                        "com.whatsapp" -> whatsappExtractor.extractMessages(rootNode, screenWidth, screenHeight, currentChatSession!!)
                        "com.instagram.android" -> instagramExtractor.extractMessages(rootNode, screenWidth, screenHeight, currentChatSession!!)
                        else -> emptyList()
                    }

                    if (extractedMessages.isNotEmpty()) {
                        messageProcessor.onScreenUpdated(extractedMessages)
                    }
                } else {
                    messageProcessor.clearContext()
                    Log.d("ADA_RADAR", "Pantalla de menú activa. Búfer limpiado.")
                }
            }

            rootNode.recycle()
        }
    }

    override fun onInterrupt() {}

    private fun extractSessionName(node: AccessibilityNodeInfo?, screenHeight: Int): String? {
        if (node == null) return null

        val rect = Rect()
        node.getBoundsInScreen(rect)
        val headerBoundary = (screenHeight * 0.15).toInt()

        if (rect.top < headerBoundary && rect.bottom > 0) {
            val textFound = node.text?.toString()?.trim()
            val descFound = node.contentDescription?.toString()?.trim()

            val candidate = textFound ?: descFound

            if (!candidate.isNullOrEmpty()) {
                val ignoreList = setOf(
                    "Atrás",
                    "Volver",
                    "Llamar",
                    "Videollamada",
                    "Más opciones",
                    "Atrás, botón",
                    "Opciones"
                )

                if (!ignoreList.contains(candidate)) {
                    return candidate.replace("Foto de perfil de ", "").trim()
                }
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            val result = extractSessionName(child, screenHeight)

            if (result != null) {
                child?.recycle()
                return result
            }
            child?.recycle()
        }

        return null
    }
}