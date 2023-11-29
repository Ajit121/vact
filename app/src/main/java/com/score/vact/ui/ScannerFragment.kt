package com.score.vact.ui

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.zxing.Result
import me.dm7.barcodescanner.zxing.ZXingScannerView


class ScannerFragment : Fragment(), ZXingScannerView.ResultHandler {
    private lateinit var mScannerView: ZXingScannerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mScannerView = ZXingScannerView(activity);
        return mScannerView;
    }

    override fun onResume() {
        super.onResume()
        mScannerView.setResultHandler(this)
        mScannerView.startCamera()
    }


    override fun handleResult(rawResult: Result?) {
        Toast.makeText(
            activity, "Contents = " + rawResult?.text.toString() +
                    ", Format = " + rawResult?.barcodeFormat.toString(), Toast.LENGTH_SHORT
        ).show()

        rawResult?.let {
            val navController = this.findNavController()
            val bundle = Bundle()
            bundle.putString("id", rawResult.text)
            val id = rawResult.text

            val action = ScannerFragmentDirections.actionScannerFragmentToHomeFragment()
            navController.navigate(action)
            /* val handler = Handler()
             handler.postDelayed(
                 Runnable { mScannerView.resumeCameraPreview(this) },
                 2000
             )*/
        }

    }

    override fun onPause() {
        super.onPause()
        mScannerView.stopCamera()
    }

}
