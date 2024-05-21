package com.packt.mediarouter

import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.mediarouter.app.MediaRouteButton
import androidx.mediarouter.media.MediaControlIntent
import androidx.mediarouter.media.MediaRouteSelector
import androidx.mediarouter.media.MediaRouter

@Composable
fun MediaRouteDiscoveryOptions(mediaRouter: MediaRouter) {

    val context = LocalContext.current
    val routeSelector = remember {
        MediaRouteSelector.Builder()
            .addControlCategory(MediaControlIntent.CATEGORY_REMOTE_PLAYBACK)
            .build()
    }
    val mediaRoutes = remember { mutableStateListOf<MediaRouter.RouteInfo>() }
    val callback = remember {
        object : MediaRouter.Callback() {
            override fun onRouteAdded(router: MediaRouter, route: MediaRouter.RouteInfo) {
                mediaRoutes.add(route)
            }

            override fun onRouteRemoved(router: MediaRouter, route: MediaRouter.RouteInfo) {
                mediaRoutes.remove(route)
            }
        }
    }

    DisposableEffect(mediaRouter) {
        mediaRouter.addCallback(routeSelector, callback, MediaRouter.CALLBACK_FLAG_PERFORM_ACTIVE_SCAN)
        onDispose {
            mediaRouter.removeCallback(callback)
        }
    }

    AndroidView(
        factory = { ctx ->
            MediaRouteButton(ctx).apply {
                setRouteSelector(routeSelector)
            }
        },
        modifier = Modifier.wrapContentWidth().wrapContentHeight()
    )

}