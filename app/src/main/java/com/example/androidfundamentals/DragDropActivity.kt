package com.example.androidfundamentals

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import com.example.androidfundamentals.databinding.ActivityDragDropBinding

class DragDropActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDragDropBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drag_drop)
        binding = ActivityDragDropBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.llTop.setOnDragListener(dragListener)
        binding.llBot.setOnDragListener(dragListener)

        binding.dragView.setOnClickListener {
            val clipText = "this is a clip text"
            val item = ClipData.Item(clipText)
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(clipText, mimeTypes, item)

            val dragShadowBuilder = View.DragShadowBuilder(it)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(data, dragShadowBuilder, it, 0)
            }

            it.visibility = View.INVISIBLE
            true
        }
    }

    val dragListener = View.OnDragListener {view, event ->
        when(event.action) {
            DragEvent.ACTION_DRAG_STARTED -> {
                event.clipDescription.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)
            }
            DragEvent.ACTION_DRAG_STARTED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DRAG_LOCATION -> true
            DragEvent.ACTION_DRAG_EXITED -> {
                view.invalidate()
                true
            }
            DragEvent.ACTION_DROP -> {
                val item = event.clipData.getItemAt(0)
                val dragData = item.text

                Toast.makeText(this, dragData, Toast.LENGTH_SHORT).show()

                view.invalidate()

                val v = event.localState as View

                val owner = v.parent as ViewGroup
                owner.removeView(v)

                val destination = view as LinearLayout
                destination.addView(v)

                v.visibility = View.VISIBLE

                true
            }
            DragEvent.ACTION_DRAG_ENDED -> {
                view.invalidate()
                true
            }
            else -> false
        }
    }
}