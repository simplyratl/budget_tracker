package com.example.budget_tracker.ui.scanner

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.budget_tracker.MainActivity
import com.example.budget_tracker.R
import com.example.budget_tracker.api.models.RetrofitInstance
import com.example.budget_tracker.api.models.models.AddBudgetUserRequest
import com.example.budget_tracker.api.models.models.AddTransationRequest
import com.example.budget_tracker.api.models.models.ScanQRCodeRequest
import com.example.budget_tracker.ui.home.AddTransactionActivity
import com.example.budget_tracker.utils.errorNotification
import com.example.budget_tracker.utils.successNotification
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ScannerActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private val cameraPermission = Manifest.permission.CAMERA
    private val requestCode = 100
    private var isPermissionGranted = false
    private var isCodeScannerInitialized = false

    private lateinit var scannerLoadingLayout: LinearLayout

    var scanningLoading = false;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_scanner)

        var backButton = findViewById<LinearLayout>(R.id.back_button)
        scannerLoadingLayout = findViewById(R.id.scanner_loading)


        backButton.setOnClickListener{
            var intent = Intent(this@ScannerActivity, MainActivity::class.java)
            startActivity(intent)
        }

        if (ContextCompat.checkSelfPermission(this, cameraPermission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(cameraPermission), requestCode)
        } else {
            isPermissionGranted = true
        }
    }

    private fun initializeCodeScanner() {
        if (isCodeScannerInitialized) return

        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        codeScanner = CodeScanner(this, scannerView)
        val userManager = UserManager.getInstance()
        val loggedInUser = userManager.getLoggedInUser()

        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                val url = it.text
                val request = ScanQRCodeRequest(url)

                lifecycleScope.launch {
                    try {
                        scanningLoading = true
                        scannerLoadingLayout.visibility = View.VISIBLE

                        val response = withContext(Dispatchers.IO) {
                            RetrofitInstance.api.scanQRCode(request)
                        }

                        if (response.isSuccessful) {
                            val data = response.body()

                            Log.d("SCANNING", data.toString())

                            if(data === null) return@launch

                            val userId = loggedInUser?.id.toString()

                            val requestTransaction = AddTransationRequest(data.amount, userId , data.title, data.items, data.adress)

                            val responseTransaction = withContext(Dispatchers.IO) {
                                RetrofitInstance.api.createTransaction(requestTransaction)
                            }

                            Log.d("SCANNING", responseTransaction.toString())


                            if(responseTransaction.isSuccessful){
                                val addedTransaction = responseTransaction.body()

                                if (addedTransaction != null) {
                                    if (loggedInUser != null && addedTransaction.amount < loggedInUser.budget) {
                                        userManager.updateBudget(addedTransaction.amount)
                                    }
                                }
                                successNotification(this@ScannerActivity, "Successful scan of the check")
                                val intent = Intent(this@ScannerActivity, MainActivity::class.java)
                                startActivity(intent)
                            } else{
                                errorNotification(this@ScannerActivity, "Error while creating a transaction")
                            }

                        } else {
                            // Handle the error condition according to your app's logic
                            errorNotification(this@ScannerActivity, response.message())
                        }
                    } catch (e: Exception) {
                        errorNotification(this@ScannerActivity, "Invalid URL or the check is not fiscalized")
                    } finally {
                        scanningLoading = false
                        scannerLoadingLayout.visibility = View.GONE
                    }
                }

                Toast.makeText(this, "Scan result: ${it.text}", Toast.LENGTH_LONG).show()
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        codeScanner.startPreview()

        // Set the click listener on scannerView
        scannerView.setOnClickListener {
            codeScanner.startPreview()
        }

        isCodeScannerInitialized = true
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == this.requestCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                isPermissionGranted = true
                initializeCodeScanner()
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (isPermissionGranted) {
            initializeCodeScanner()
        }
    }

    override fun onPause() {
        if (isPermissionGranted) {
            codeScanner.releaseResources()
            isCodeScannerInitialized = false
        }
        super.onPause()
    }
}
