package com.example.ada.uriel

import android.view.accessibility.AccessibilityNodeInfo
import android.graphics.Rect
import kotlin.math.abs
import com.example.ada.ChatExtractor

class InstagramExtractor : ChatExtractor {

    override fun extractMessages(rootNode: AccessibilityNodeInfo, screenWidth: Int, screenHeight: Int, sessionName: String): List<ChatMessage> {
        val extractedMessages = mutableListOf<ChatMessage>()
        readNode(rootNode, extractedMessages, screenWidth, screenHeight, sessionName)
        return extractedMessages
    }

    private fun readNode(node: AccessibilityNodeInfo, extractedMessages: MutableList<ChatMessage>, screenWidth: Int, screenHeight: Int, sessionName: String) {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        val headerBoundary = (screenHeight * 0.12).toInt()
        val footerBoundary = (screenHeight * 0.88).toInt()

        if (rect.bottom > headerBoundary && rect.top < footerBoundary) {
            val className = node.className?.toString()

            if (className == "android.widget.TextView") {
                val textFound = node.text?.toString()?.trim()

                if (!textFound.isNullOrEmpty() && textFound.length > 2) {
                    val ignoreList = setOf(
                        "Doble toque para indicar que te gusta",
                        "Te gusta el mensaje",
                        "Cámara",
                        "Micrófono",
                        "Enviar mensaje",
                        "Mensaje",
                        "Ver foto",
                        "Ver video",
                        "Enviando...",
                        "Stickers",
                        "Galería",
                        "Más"
                    )

                    if (!ignoreList.any { textFound.contains(it, ignoreCase = true) }) {
                        val distanceFromLeft = rect.left
                        val distanceFromRight = screenWidth - rect.right

                        if (abs(distanceFromLeft - distanceFromRight) > (screenWidth * 0.05)) {
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
                readNode(child, extractedMessages, screenWidth, screenHeight, sessionName)
                child.recycle()
            }
        }
    }
}