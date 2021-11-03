package com.tunasoftware.tunacr

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizerOptions
import kotlinx.android.synthetic.main.activity_tuna_card_recognition.*
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class TunaCardRecognition : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    lateinit var viewModel: TunaCardRecognitionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this ).get(TunaCardRecognitionViewModel::class.java)
        subscribe()
        setContentView(R.layout.activity_tuna_card_recognition)
        tuna_toolbar.apply {
            navigationIcon = ContextCompat.getDrawable(
                this@TunaCardRecognition,
                R.drawable.tuna_ic_close
            )
            setNavigationOnClickListener {
                close(false)
            }
        }
        // Request camera permissions
        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(
                this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS
            )
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun subscribe(){
        viewModel.actionsLiveData.observe(this, Observer {
            when(it){
                is ActionNumberDetected -> {
                    vibrate()
                    Handler(Looper.getMainLooper()).postDelayed({
                        setResult(RESULT_OK, Intent().apply {
                            putExtra(RESULT_NAME, it.name)
                            putExtra(RESULT_NUMBER, it.number)
                            putExtra(RESULT_EXPIRATION, it.expiration)
                        })
                        close(true)
                    }, 200)

                }
            }
        })
    }
    fun vibrate(){
        (getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?)?.let { v ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                //deprecated in API 26
                v.vibrate(200)
            }
        }
    }


    fun close(fade:Boolean){
        finishAfterTransition()
        overridePendingTransition(R.anim.no_change, if (fade) R.anim.fade_out else R.anim.modal_out)
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            // Used to bind the lifecycle of cameras to the lifecycle owner
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            // Preview
            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(cameraPreview.surfaceProvider)
                }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, CardAnalyzer { number, name, expiration ->
                        viewModel.onDataCollected(number, name, expiration)
                    })
                }

            // Select back camera as a default
            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                // Unbind use cases before rebinding
                cameraProvider.unbindAll()

                // Bind use cases to camera
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageAnalyzer
                )

            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }

        }, ContextCompat.getMainExecutor(this))
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(
            baseContext, it
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val TAG = "CameraXBasic"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

        const val RESULT_NAME = "RESULT_NAME"
        const val RESULT_NUMBER = "RESULT_NUMBER"
        const val RESULT_EXPIRATION = "RESULT_EXPIRATION"

        @JvmStatic
        fun startForResult(activity:Activity, requestCode: Int){
            activity.startActivityForResult(Intent(activity, TunaCardRecognition::class.java), requestCode)
        }
    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    this,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                ).show()
                close(false)
            }
        }
    }

    private class CardAnalyzer(val callback: (number:String, name:String, expiration:String) -> Unit) : ImageAnalysis.Analyzer {

        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        override fun analyze(imageProxy: ImageProxy) {
            val mediaImage = imageProxy.image
            if (mediaImage != null) {
                val image =
                    InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                val result = recognizer.process(image)
                    .addOnSuccessListener { visionText ->
                        if (visionText.text.isNotEmpty())
                            getCardDetail(visionText.text)
                        imageProxy.close()
                        // Task completed successfully
                        // ...
                    }
                    .addOnFailureListener { e ->
                        Log.d("tunatext", "deu ruim")
                        imageProxy.close()
                        // Task failed with an exception
                        // ...
                    }
            }

        }

        fun getCardDetail(text:String){
            val words = text.split("\n")
            var cardNumber = ""
            var expiration = ""
            var name = ""
            var mergedNumbers = ""
            for (word in words) {
                Log.e("tunatext", "word: $word")
                if (word.trim().length == 4 && word.matches(Regex("([0-9]{4})")))
                    mergedNumbers += word.trim()

               getCardString(word).let {
                   if (it.isNotEmpty()){
                       cardNumber = it
                   }
               }

                val expirationRegex = Regex("\\d\\d\\/\\d\\d")
                val expirationWord = expirationRegex.find(word)?.groups?.first()?.value
                if (expirationWord != null) {
                    expiration = expirationWord
                }

                val nameRegex = Regex("^[A-Z ]+\$")
                val nameWord =  nameRegex.find(word)?.groups?.first()?.value
                if (nameWord != null && nameWord.split(" ").size > 1) {
                    name = nameWord
                    Log.d("tunatext", "name $name")
                }
            }
            Log.d("tunatext", "merged numbers $mergedNumbers")
            if (cardNumber.isEmpty()){
                getCardString(mergedNumbers).let {
                    if (it.isNotEmpty()){
                        cardNumber = it
                    }
                }
            }
            Log.d("tunatext", "cardnumber $cardNumber")
            Log.d("tunatext", "expiration $expiration")
            Log.d("tunatext", "name $name")
            callback(cardNumber, name, expiration)

        }

        fun getCardString(word:String):String{
            //REGEX for detecting a credit card
            val cardRegex = Regex("^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})")
            val cardWord = cardRegex.find(word.replace(" ", ""), 0)?.groups?.first()?.value
            if (cardWord != null) {
                return cardWord
            }
            return  ""
        }
    }



}