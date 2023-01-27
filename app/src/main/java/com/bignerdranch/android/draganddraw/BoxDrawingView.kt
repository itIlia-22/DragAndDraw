package com.bignerdranch.android.draganddraw

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View

/**
 * Класс BoxDrawingView относится к категории простых
представлений и является прямым подклассом View.
Создайте класс BoxDrawingView и назначьте View его
суперклассом. Добавьте в файл BoxDrawingView.kt
конструктор, принимающий объект Context и допускающий
null, и AttributeSet со значением по умолчанию null.

 *@author
 * Установка значения null по умолчанию для набора
атрибутов фактически создает два конструктора для
представления. Нужно именно два конструктора, так как ваше
представление может создаваться в коде или из файла макета.
Представление, экземпляр которого создается из файла макета,
получает экземпляр AttributeSet, содержащий атрибуты
XML, которые были указаны в файле XML. Даже если вы не
планируете использовать оба конструктора, стоит
предусмотреть их оба.

 */

private const val TAG = "BoxDrawingView"

class BoxDrawingView(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {


    private var currentBox: Box? = null
    private val boxen = mutableListOf<Box>()
    private val boxPaint = Paint().apply {
        color = 0x22ff0000.toInt()
    }

    /**
     * Первая часть кода тривиальна: используя серовато-белый
    цвет, мы заполняем «холст» задним фоном для вывода
    прямоугольников.
    Затем для каждого прямоугольника в списке мы определяем
    значения left, right, top и bottom по двум точкам. Значения
    left и top будут минимальными, а bottom и right —
    максимальными.
    После вычисления параметров вызов функции
    Canvas.drawRect(...) рисует красный прямоугольник на
    экране.
     */
    private val backgroundPaint = Paint().apply {
        color = 0xfff8efe0.toInt()
    }

    override fun onDraw(canvas: Canvas) {
        canvas.drawPaint(backgroundPaint)
        boxen.forEach { box ->
            canvas.drawRect(box.left, box.top, box.right, box.bottom, boxPaint)
        }
    }

    /**
     * Для прослушивания событий касания можно назначить
    слушателя события при помощи следующей функции класса
    View:
    fun setOnTouchListener(l: View.OnTouchListener)
    Этот функция работает почти так же, как
    setOnClickListener(View.OnClick Listener). Вы
    предоставляете реализацию View.OnTouchListener, а
    слушатель вызывается каждый раз, когда происходит событие
    касания.
    Но поскольку мы подклассируем View, можно пойти по
    сокращенному пути и переопределить следующую функцию
    класса View:
    override fun onTouchEvent(event: MotionEvent):
    Boolean
    Этот функция получает экземпляр MotionEvent — класса,
    описывающего событие касания, включая его позицию и
    действие. Действие описывает стадию события.

     */
    override fun onTouchEvent(event: MotionEvent): Boolean {

        /**
         * При каждом получении события ACTION_DOWN в поле
        currentBox сохраняется новый объект Box с базовой точкой,
        соответствующей позиции события. Этот объект Box
        добавляется в массив прямоугольников (в следующем разделе,
        когда мы займемся прорисовкой, BoxDrawingView будет
        выводить каждый объект Box из массива).
        В процессе перемещения пальца по экрану приложение
        обновляет currentBox.end. Затем, когда касание отменяется
        или палец не касается экрана, поле currentBox обнуляется для
        завершения операции. Объект Box завершен; он сохранен в
        массиве и уже не будет обновляться событиями перемещения.
        Обратите внимание на вызов invalidate() в функции
        updateCurrentBox(). Он заставляет BoxDrawingView
        перерисовать себя, чтобы пользователь видел прямоугольник в
        процессе перетаскивания.
         */
        val current = PointF(event.x, event.y)
        var action = ""
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                action = "ACTION_DOWN" + "Пользователь прикоснулся к экрану"
                currentBox = Box(current).also {
                    boxen.add(it)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                action = "ACTION_MOVE" + "Пользователь перемещает палец по экрану"
                updateCurrentBox(current)
            }
            MotionEvent.ACTION_UP -> {
                action = "ACTION_UP" + "Пользователь отводит палец от экрана"
                updateCurrentBox(current)
                currentBox = null
            }
            MotionEvent.ACTION_CANCEL -> {
                action =
                    "ACTION_CANCEL" + "Родительское представление перехватило событие касания\n"
                currentBox = null
            }


        }
        Log.i(
            TAG,
            "$action at x=${current.x}, y=${current.y}"
        )
        return true
    }

    private fun updateCurrentBox(current: PointF) {
        currentBox.let {
            it?.end = current
            invalidate()
        }
    }

}

/**
 *  Класс Canvas содержит все выполняемые операции
графического вывода. Функции, вызываемые для объекта
Canvas, определяют, где и что выводится — линия, круг,
слово или прямоугольник.
 * Класс Paint определяет, как будут выполняться эти
операции. Функции, вызываемые для объекта Paint,
определяют характеристики вывода: должны ли фигуры
заполняться, каким шрифтом должен выводиться текст,
каким цветом должны выводиться линии и т.д.

 */