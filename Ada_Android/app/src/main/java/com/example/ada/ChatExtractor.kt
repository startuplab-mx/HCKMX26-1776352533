package com.example.ada

import android.view.accessibility.AccessibilityNodeInfo
import com.example.ada.uriel.ChatMessage

interface ChatExtractor {
    fun extractMessages(rootNode: AccessibilityNodeInfo, screenWidth: Int, screenHeight: Int, sessionName: String): List<ChatMessage>
}