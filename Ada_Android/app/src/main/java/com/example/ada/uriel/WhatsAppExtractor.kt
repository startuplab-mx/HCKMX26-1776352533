package com.example.ada.uriel

import android.view.accessibility.AccessibilityNodeInfo
import android.graphics.Rect
import com.example.ada.ChatExtractor

class WhatsAppExtractor : ChatExtractor {

    private val timePattern = Regex("^\\d{1,2}:\\d{2}\\s*[a-zA-Z.\\s]*\$")

    override fun extractMessages(rootNode: AccessibilityNodeInfo, screenWidth: Int, screenHeight: Int, sessionName: String): List<ChatMessage> {
        val extractedMessages = mutableListOf<ChatMessage>()
        readNode(rootNode, extractedMessages, screenWidth, screenHeight)
        return extractedMessages
    }

    private fun readNode(node: AccessibilityNodeInfo, extractedMessages: MutableList<ChatMessage>, screenWidth: Int, screenHeight: Int) {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        val headerBoundary = (screenHeight * 0.12).toInt()
        val footerBoundary = (screenHeight * 0.88).toInt()

        if (rect.bottom > headerBoundary && rect.top < footerBoundary) {
            val className = node.className?.toString()

            if (className == "android.widget.TextView") {
                val textFound = node.text?.toString()?.trim()

                if (!textFound.isNullOrEmpty()) {
                    if (!timePattern.matches(textFound)) {
                        val ignoreList = setOf(
                            "Mensaje",
                            "Eliminaste este mensaje.",
                            "Editado",
                            "Escribe un mensaje",
                            "Cámara",
                            "Adjuntar",
                            "Atrás",
                            "Llamada de voz",
                            "Videollamada",
                            "Más opciones"
                        )

                        if (!ignoreList.any { textFound.contains(it, ignoreCase = true) }) {
                            val distanceFromLeft = rect.left
                            val distanceFromRight = screenWidth - rect.right

                            val role = if (distanceFromLeft > distanceFromRight) {
                                "INFANTE"
                            } else {
                                "ATACANTE"
                            }

                            extractedMessages.add(ChatMessage(textFound, role))
                        }
                    }
                }
            }
        }

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                readNode(child, extractedMessages, screenWidth, screenHeight)
                child.recycle()
            }
        }
    }
}