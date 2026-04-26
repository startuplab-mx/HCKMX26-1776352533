package com.example.ada.uriel

import android.view.accessibility.AccessibilityNodeInfo
import android.graphics.Rect
import com.example.ada.ChatExtractor

class TiktokExtractor : ChatExtractor {

    override fun extractMessages(rootNode: AccessibilityNodeInfo, screenWidth: Int, screenHeight: Int, sessionName: String): List<ChatMessage> {
        val extractedMessages = mutableListOf<ChatMessage>()
        readNode(rootNode, extractedMessages, screenWidth, screenHeight)
        return extractedMessages
    }

    private fun readNode(node: AccessibilityNodeInfo, extractedMessages: MutableList<ChatMessage>, screenWidth: Int, screenHeight: Int) {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        val headerBoundary = (screenHeight * 0.12).toInt()

        if (rect.bottom > headerBoundary) {
            val className = node.className?.toString()

            if (className == "com.ss.android.ugc.aweme.im.messagelist.api.ui.IMTuxTextLayoutView") {
                val textFound = node.text?.toString()?.trim()

                if (!textFound.isNullOrEmpty()) {
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

        for (i in 0 until node.childCount) {
            val child = node.getChild(i)
            if (child != null) {
                readNode(child, extractedMessages, screenWidth, screenHeight)
                child.recycle()
            }
        }
    }
}