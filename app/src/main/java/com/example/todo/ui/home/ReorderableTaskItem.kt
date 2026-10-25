package com.example.todo.ui.home

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.example.todo.models.SortType
import com.example.todo.models.Task
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

@Composable
fun LazyItemScope.ReorderableTaskItem(
    reorderableLazyListState: ReorderableLazyListState,
    taskItem: Task,
    onTaskClicked: () -> Unit,
    onTaskCompletionToggled: () -> Unit,
    sortType: SortType,
    saveOrder: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = false,
    animateItem: Boolean = false,
    shape: Shape = CardDefaults.shape,
) {
    val hapticFeedback = LocalHapticFeedback.current

    ReorderableItem(
        reorderableLazyListState,
        key = taskItem.id,
        animateItemModifier = if (animateItem) Modifier.animateItem() else Modifier,
        modifier = modifier
    ) { isDragging ->
        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
        val scale by animateFloatAsState(if (isDragging) 1.05f else 1f)

        TaskItem(
            task = taskItem,
            onTaskClicked = onTaskClicked,
            onToggleCompleted = onTaskCompletionToggled,
            shape = if (isDragging) RectangleShape else shape,
            sortType = sortType,
            modifier = Modifier
                .longPressDraggableHandle(
                    enabled = enabled,
                    onDragStarted = {
                    hapticFeedback.performHapticFeedback(
                        HapticFeedbackType.GestureThresholdActivate
                    )
                }, onDragStopped = {
                    saveOrder()
                    hapticFeedback.performHapticFeedback(
                        HapticFeedbackType.GestureEnd
                    )
                })
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .shadow(elevation))
    }
}