package com.example.ada.uriel

import android.view.accessibility.AccessibilityNodeInfo

interface ChatExtractor {
    fun extractMessages(rootNode: AccessibilityNodeInfo, screenWidth: Int, screenHeight: Int, sessionName: String): List<ChatMessage>
}