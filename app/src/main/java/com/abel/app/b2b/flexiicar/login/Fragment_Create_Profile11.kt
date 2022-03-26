package com.abel.app.b2b.flexiicar.login

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.acuant.acuantcamera.camera.AcuantCameraActivity
import com.acuant.acuantcamera.camera.AcuantCameraOptions
import com.acuant.acuantcamera.constant.*
import com.acuant.acuantcamera.initializer.MrzCameraInitializer
import com.acuant.acuantcommon.exception.AcuantException
import com.acuant.acuantcommon.helper.CredentialHelper
import com.acuant.acuantcommon.initializer.AcuantInitializer
import com.acuant.acuantcommon.initializer.IAcuantPackageCallback
import com.acuant.acuantcommon.model.*
import com.acuant.acuantcommon.type.CardSide
import com.acuant.acuantdocumentprocessing.AcuantDocumentProcessor
import com.acuant.acuantdocumentprocessing.model.*
import com.acuant.acuantdocumentprocessing.service.listener.CreateInstanceListener
import com.acuant.acuantdocumentprocessing.service.listener.GetDataListener
import com.acuant.acuantdocumentprocessing.service.listener.UploadImageListener
import com.acuant.acuantimagepreparation.AcuantImagePreparation
import com.acuant.acuantimagepreparation.background.EvaluateImageListener
import com.acuant.acuantimagepreparation.initializer.ImageProcessorInitializer
import com.acuant.acuantimagepreparation.model.AcuantImage
import com.acuant.acuantimagepreparation.model.CroppingData
import com.abel.app.b2b.*
import com.abel.app.b2b.Constants.Companion.REQUEST_CAMERA_PHOTO
import com.abel.app.b2b.backgroundtasks.AcuantTokenService
import com.abel.app.b2b.backgroundtasks.AcuantTokenServiceListener
import com.abel.app.b2b.flexiicar.booking.Booking_Activity
import com.abel.app.b2b.flexiicar.user.User_Profile
import com.abel.app.b2b.home.Activity_Home
import kotlinx.android.synthetic.main.fragment_create_profile.*
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList
import java.util.HashMap
import kotlin.concurrent.thread


class Fragment_Create_Profile11 : Fragment() {
    private var TAG:String = "DRIVING";
    private var progressDialog: LinearLayout? = null
    private var progressText: TextView? = null
    private var capturedFrontImage: AcuantImage? = null
    private var capturedBackImage: AcuantImage? = null
    private var capturedSelfieImage: Bitmap? = null
    private var capturedFaceImage: Bitmap? = null
    private var capturedBarcodeString: String? = null
    private var frontCaptured: Boolean = false
    private var isHealthCard: Boolean = false
    private var isRetrying: Boolean = false
    private var insuranceButton: Button? = null
    private var idButton: Button? = null
    private var capturingImageData: Boolean = true
    private var capturingSelfieImage: Boolean = false
    private var capturingFacialMatch: Boolean = false
    private var facialResultString: String? = null
    private var facialLivelinessResultString: String? = null
    private var captureWaitTime: Int = 0
    private var documentInstanceID: String? = null
    private var autoCaptureEnabled: Boolean = true
    private var numberOfClassificationAttempts: Int = 0
    private var isInitialized = false
    private var livenessSelected = 0
    private var isKeyless = false
    private var processingFacialLiveness = false
    private val useTokenInit = true
    private var recentImage: AcuantImage? = null
    private lateinit var livenessSpinner : Spinner
    private lateinit var livenessArrayAdapter: ArrayAdapter<String>

    fun cleanUpTransaction() {
        capturedFrontImage?.destroy()
        capturedBackImage?.destroy()
        facialResultString = null
        capturedFrontImage = null
        capturedBackImage = null
        capturedSelfieImage = null
        capturedFaceImage = null
        facialLivelinessResultString = null
        capturedBarcodeString = null
        isHealthCard = false
        processingFacialLiveness = false
        isRetrying = false
        capturingImageData = true
        documentInstanceID = null
        numberOfClassificationAttempts = 0
    }

    public var afterScanBackTo: Int = 0;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_create_profile, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)

        if (ActivityCompat.checkSelfPermission(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE), 99)
        }

        initializeAcuantSdk(object: IAcuantPackageCallback{
            override fun onInitializeFailed(error: List<Error>) {
                TODO("Not yet implemented")
                Log.e(TAG, "onInitializeFailed: " + error.toString() )
            }

            override fun onInitializeSuccess() {
               /* this@ScanDrivingLicense.runOnUiThread {
                    //setProgress(false)
                    System.out.println("Initialize Success")
                    frontCaptured = false
                    cleanUpTransaction()
                    captureWaitTime = 2
                    showDocumentCaptureCamera()
                }*/
                Log.e(TAG, "onInitializeSuccess: " + "sucess" )

                val mSourceBounds:Rect = Rect(100,1000,100,1000);

                val cameraIntent = Intent(requireActivity(), AcuantCameraActivity::class.java)

                cameraIntent.putExtra(ACUANT_EXTRA_IS_AUTO_CAPTURE, true)//default is true
                cameraIntent.putExtra(ACUANT_EXTRA_BORDER_ENABLED, true)//default is true
                cameraIntent.sourceBounds = mSourceBounds



                startActivityForResult(cameraIntent, REQUEST_CAMERA_PHOTO)
            }

            //override fun onInitializeFailed(error: List<Error>) {
            //   this@ScanDrivingLicense.runOnUiThread {
            //        showAcuDialog("Could not initialize.\n"+error[0].errorDescription)
            //   }
            //}
        })


    }

    private fun initializeAcuantSdk(callback: IAcuantPackageCallback)
    {
        try{

            val initCallback = object: IAcuantPackageCallback {
                override fun onInitializeFailed(error: List<Error>)
                {
                    TODO("Not yet implemented")
                    System.out.println("Initializing 1")

                    callback.onInitializeFailed(error)
                }

                override fun onInitializeSuccess() {
                    System.out.println("Initializing 2")

                    isInitialized = true
                    callback.onInitializeSuccess()

                    if(Credential.get().subscription == null || Credential.get().subscription.isEmpty() ){

                        System.out.println("Initializing 3")

                        isKeyless = true
                        //livenessSpinner.visibility = View.INVISIBLE
                        callback.onInitializeSuccess()
                    }
                    else{
                        if(Credential.get().secureAuthorizations.ozoneAuth) {
                            //findViewById<Button>(R.id.main_mrz_camera).visibility = View.VISIBLE
                        }
                        //getFacialLivenessCredentials(callback)
                    }
                }

                //override fun onInitializeFailed(error: List<Error>) {
                //   callback.onInitializeFailed(error)
                //}
            }
            /*
            * The initFromXml and AcuantTokenService is just for the sample app, you should be
            * generating these tokens on one of your secure servers, passing it to the app,
            * and then calling initializeWithToken passing the config and token.
            */
            @Suppress("ConstantConditionIf")
            if (useTokenInit) {
                //Toast.makeText(activity, "Using Token Init", Toast.LENGTH_SHORT).show()

                System.out.println("Initializing 4")

                Credential.initFromXml("acuant.config.xml", context)
                AcuantTokenService(Credential.get(), object : AcuantTokenServiceListener {
                    override fun onSuccess(token: String) {

                        System.out.println("Initializing 5")

                        if (!isInitialized) {

                            System.out.println("Initializing 6")

                            AcuantInitializer.initializeWithToken("acuant.config.xml",
                                token,
                                activity!!,
                                listOf(ImageProcessorInitializer(), MrzCameraInitializer()),
                                initCallback)

                            System.out.println("Initializing 7")

                        } else {

                            System.out.println("Initializing 8")

                            if(Credential.setToken(token)) {

                                System.out.println("Initializing 9")

                                initCallback.onInitializeSuccess()

                            } else {

                                System.out.println("Initializing 10")

                                //initCallback.onInitializeFailed(listOf(Error(-2, "Error in setToken\nBad/expired token")))
                            }
                        }

                    }

                    override fun onFail(responseCode: Int) {
                        //initCallback.onInitializeFailed(listOf(Error(responseCode, "Error in getToken service.\nCode: $responseCode")))
                    }

                }).execute()
            } else {
                Toast.makeText(activity, "Using Credential Init", Toast.LENGTH_SHORT).show()
                AcuantInitializer.initialize("acuant.config.xml",
                    requireActivity(),
                    listOf(ImageProcessorInitializer(), MrzCameraInitializer()),
                    initCallback)
            }

        } catch(e: AcuantException) {
            Log.e("Acuant Error", e.toString())
           // showAcuDialog(e.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            if (requestCode == Constants.REQUEST_CAMERA_PHOTO && resultCode == AcuantCameraActivity.RESULT_SUCCESS_CODE) {
                Log.e(TAG, "onActivityResult: "+ "IMG 1" )

                val url = data?.getStringExtra(ACUANT_EXTRA_IMAGE_URL)
                capturedBarcodeString = data?.getStringExtra(ACUANT_EXTRA_PDF417_BARCODE)
            }
        }
        catch (e: Exception){
            e.printStackTrace()
        }
    }


}


