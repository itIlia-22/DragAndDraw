package com.bignerdranch.android.draganddraw

import android.graphics.PointF

/**
 * Прежде всего для определения прямоугольника нам
понадобятся две точки: базовая (в которой было сделано
исходное касание) и текущая (в которой находится палец).
Следовательно, для определения прямоугольника
необходимо отслеживать данные от нескольких событий
MotionEvent. Данные будут храниться в объекте Box.

 */
class Box(private val start: PointF) {

    var end: PointF = start

    val left: Float
        get() = start.x.coerceAtMost(end.x)
    val right: Float
        get() = start.x.coerceAtLeast(end.x)
    val top: Float
        get() = start.y.coerceAtMost(end.y)
    val bottom: Float
        get() = start.y.coerceAtLeast(end.y)


}