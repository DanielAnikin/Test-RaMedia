package com.otmutienipen.testrameddia.view.screens

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import com.otmutienipen.testrameddia.controller.PlayerController
import com.otmutienipen.testrameddia.databinding.FragmentWebGameBinding
import com.otmutienipen.testrameddia.utilities.OnBackPressedDelegation
import com.otmutienipen.testrameddia.utilities.OnBackPressedDelegationImpl
import com.otmutienipen.testrameddia.utilities.STORAGE
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates


internal class WebGame : Fragment(), OnBackPressedDelegation by OnBackPressedDelegationImpl() {

    private var _binding: FragmentWebGameBinding? = null
    private val binding: FragmentWebGameBinding
        get() = _binding ?: throw NullPointerException("FragmentWebGameBinding is null")
    private var link by Delegates.notNull<String>()
    private lateinit var prefs: SharedPreferences
    private lateinit var controller: PlayerController
    private lateinit var launcher: ActivityResultLauncher<Intent>
    private var callback: ValueCallback<Array<Uri>>? = null
    private var uri = Uri.EMPTY

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        link = arguments?.getString(LINK) as String
        _binding = FragmentWebGameBinding.inflate(inflater, container, false)
        prefs = inflater.context.getSharedPreferences(STORAGE, Context.MODE_PRIVATE)
        controller = PlayerController(prefs)
        setUpProgressBar()
        setUpWebView()
        initLauncher()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(binding) {
        super.onViewCreated(view, savedInstanceState)
        println("webview link: $link")
        webMain.loadUrl(link)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetJavaScriptEnabled")
    private fun setUpWebView() = with(binding) {
        with(webMain.settings) {
            builtInZoomControls = true // Включение встроенных контролов масштабирования
            displayZoomControls = false // Отключение отображения контролов масштабирования на экране
            javaScriptEnabled = true
            domStorageEnabled = true
            setSupportZoom(true)
        }
        webMain.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                val nextUrl = request?.url

                if (!nextUrl.toString().startsWith("http")) {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(nextUrl.toString()))
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        requireActivity().startActivity(intent)
                    } catch (ex: ActivityNotFoundException) {
                        Toast.makeText(
                            requireContext(),
                            "Can not open on device",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    return true
                }
                return false
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                if (url != null) {
                    controller.saveLink(url)
                }

            }
        }

        registerOnBackPressedDelegation(activity, this@WebGame.lifecycle) {
            if (webMain.canGoBack()) {
                webMain.goBack()
            } else {
                activity?.moveTaskToBack(true)
            }
        }
    }

    private fun onReceiverExist(intentCapture: Intent) {
        val intentPicFile = Intent(Intent.ACTION_GET_CONTENT).addCategory(Intent.CATEGORY_OPENABLE)
            .setType("image/*")
        val picFile = createFileForPhoto()

        uri = FileProvider.getUriForFile(
            requireContext(),
            "${requireActivity().packageName}.provider",
            picFile
        )
        intentCapture.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        launcher.launch(
            Intent(Intent.ACTION_CHOOSER)
                .putExtra(Intent.EXTRA_INTENT, intentCapture)
                .putExtra(Intent.EXTRA_INITIAL_INTENTS, arrayOf(intentPicFile))
        )
    }

    private fun createFileForPhoto(): File {
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val dirFile = requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("${format}_wheel_of_flate", ".jpg", dirFile)
    }

    private fun initLauncher() {
        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val targetUriOoppikan: Uri? =
                if (it.data == null || it.resultCode != RESULT_OK) {
                    null
                } else it.data!!.data
            if (targetUriOoppikan != null && (callback != null)) {
                callback?.onReceiveValue(arrayOf(targetUriOoppikan))
            } else {
                callback?.onReceiveValue(arrayOf(uri))
            }
        }
    }

    private fun setUpProgressBar() = with(binding) {
        pbLoadPage.max = 100

        webMain.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                if (newProgress == 100) {
                    pbLoadPage.visibility = View.GONE
                } else {
                    pbLoadPage.visibility = View.VISIBLE
                    pbLoadPage.progress = newProgress
                }
            }

            override fun onShowFileChooser(
                webView: WebView?,
                filePathCallback: ValueCallback<Array<Uri>>?,
                fileChooserParams: FileChooserParams?
            ): Boolean {
                callback = filePathCallback
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                if (intent.resolveActivity(requireContext().packageManager) != null) {
                    onReceiverExist(intent)
                    return true
                }
                return super.onShowFileChooser(webView, filePathCallback, fileChooserParams)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        private const val LINK = "link"

        @JvmStatic
        fun newInstance(link: String) = WebGame().apply {
            arguments = Bundle().apply {
                putString(LINK, link)
            }
        }
    }
}
