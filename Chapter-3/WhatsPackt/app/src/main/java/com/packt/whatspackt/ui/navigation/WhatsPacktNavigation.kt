package com.packt.whatspackt.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.packt.feature.chat.ui.ChatScreen
import com.packt.feature.conversations.ui.ConversationsListScreen
import com.packt.feature.create_chat.ui.CreateConversationScreen
import com.packt.framework.navigation.DeepLinks
import com.packt.framework.navigation.NavRoutes

@Composable
fun MainNavigation(navController: NavHostController) {

    NavHost(navController, startDestination = NavRoutes.ConversationsList) {

        addConversationsList(navController)

        addNewConversation(navController)

        addChat(navController)

    }
}

private fun NavGraphBuilder.addConversationsList(navController: NavHostController) {

    composable(NavRoutes.ConversationsList) {
        ConversationsListScreen(
            onNewConversationClick = { navController.navigate(NavRoutes.NewConversation) },
            onConversationClick = { chatId ->
                navController.navigate(NavRoutes.Chat.replace("{chatId}", chatId))
            }
        )
    }

}

private fun NavGraphBuilder.addNewConversation(navController: NavHostController) {

    composable(NavRoutes.NewConversation) {
        CreateConversationScreen(onCreateConversation = { navController.navigate(NavRoutes.Chat) })
    }
}

private fun NavGraphBuilder.addChat(navController: NavHostController) {

    composable(
        route = NavRoutes.Chat,
        arguments = listOf(navArgument(NavRoutes.ChatArgs.ChatId) { type = NavType.StringType }),
        deepLinks = listOf(navDeepLink { uriPattern = DeepLinks.chatRoute })
    ) { backStackEntry ->
        val chatId = backStackEntry.arguments?.getString(NavRoutes.ChatArgs.ChatId)
        ChatScreen(chatId = chatId, onBack = { navController.popBackStack() })
    }
}