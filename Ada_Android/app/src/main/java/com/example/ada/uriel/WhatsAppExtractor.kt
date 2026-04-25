package com.example.ada.uriel

import android.view.accessibility.AccessibilityNodeInfo
import android.graphics.Rect

class WhatsAppExtractor : ChatExtractor {

    override fun extractMessages(rootNode: AccessibilityNodeInfo, screenWidth: Int, screenHeight: Int, sessionName: String): List<ChatMessage> {
        val extractedMessages = mutableListOf<ChatMessage>()
        readNode(rootNode, extractedMessages, screenWidth, screenHeight, sessionName)
        return extractedMessages
    }

    private fun readNode(node: AccessibilityNodeInfo, extractedMessages: MutableList<ChatMessage>, screenWidth: Int, screenHeight: Int, sessionName: String) {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        val headerBoundary = (screenHeight * 0.15).toInt()

        if (rect.bottom > headerBoundary) {
            if (node.text != null && node.className != null) {
                val className = node.className.toString()

                if (className == "android.widget.TextView" || className == "android.widget.EditText") {

                    if (isInsideQuoteBlock(node, sessionName)) {
                        return
                    }

                    val textFound = node.text.toString().trim()

                    if (textFound.length > 3) {
                        val role = getMessageRole(node, screenWidth)

                        if (role != "IGNORE") {
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

    private fun isInsideQuoteBlock(node: AccessibilityNodeInfo, sessionName: String): Boolean {
        val parent = node.parent ?: return false
        var isQuote = false

        for (i in 0 until parent.childCount) {
            val sibling = parent.getChild(i)
            if (sibling != null && sibling.className == "android.widget.TextView") {
                val text = sibling.text?.toString()?.trim()

                if (text == "Tú" || text == sessionName) {
                    isQuote = true
                }
            }
            sibling?.recycle()
        }
        parent.recycle()
        return isQuote
    }

    private fun getMessageRole(node: AccessibilityNodeInfo, screenWidth: Int): String {
        val rect = Rect()
        node.getBoundsInScreen(rect)

        var bubbleLeft = rect.left
        var bubbleRight = rect.right

        var current: AccessibilityNodeInfo? = node.parent
        var depth = 0

        while (current != null && depth < 3) {
            val parentRect = Rect()
            current.getBoundsInScreen(parentRect)

            if (parentRect.width() > screenWidth * 0.85) {
                current.recycle()
                break
            }

            if (parentRect.left < bubbleLeft) bubbleLeft = parentRect.left
            if (parentRect.right > bubbleRight) bubbleRight = parentRect.right

            val nextParent = current.parent
            current.recycle()
            current = nextParent
            depth++
        }
        current?.recycle()

        val distanceFromLeft = bubbleLeft
        val distanceFromRight = screenWidth - bubbleRight

        if (Math.abs(distanceFromLeft - distanceFromRight) < (screenWidth * 0.05)) {
            return "IGNORE"
        }

        return if (distanceFromLeft < distanceFromRight) {
            "MENSAJERO"
        } else {
            "RECEPTOR"
        }
    }
}