package com.myplaygroup.app.core.presentation.camera

import android.content.Context
import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.myplaygroup.app.core.domain.repository.ImageRepository
import com.myplaygroup.app.core.presentation.BaseViewModel
import com.myplaygroup.app.core.util.BitmapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class CameraViewModel @Inject constructor(
    @ApplicationContext private val context : Context
) : BaseViewModel() {

    lateinit var takePhotoCallback: (Bitmap) -> Unit

    var state by mutableStateOf(CameraScreenState())

    fun onEvent(event: CameraScreenEvent){
        when(event){
            is CameraScreenEvent.TakePhoto -> {
                val rotatedBitmap = BitmapUtils.rotateImageFromExif(event.uri, context)

                state = state.copy(photoBitmap = rotatedBitmap)
            }
            is CameraScreenEvent.AcceptPhoto -> {
                val bitmap = if(state.shouldCrop){
                    BitmapUtils.cropBitmap(
                        bitmap = state.photoBitmap!!,
                        canvasSize = event.imageSize,
                        cutRect = event.cutRect
                    )
                }else{
                    state.photoBitmap!!
                }

                return takePhotoCallback(bitmap)
            }
            is CameraScreenEvent.RejectPhoto -> {
                state = state.copy(photoBitmap = null)
            }
        }
    }
}