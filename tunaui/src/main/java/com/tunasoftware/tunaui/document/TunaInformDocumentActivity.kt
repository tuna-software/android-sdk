package com.tunasoftware.tunaui.document

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUI

class TunaInformDocumentActivity : AppCompatActivity(), TunaInformDocumentResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuna_inform_document)
    }

    override fun onDocumentInformed(result: String) {
        Intent().apply {
            putExtra(TunaUI.RESULT_DOCUMENT_INFORMED, result)
        }.also {
            setResult(RESULT_OK, it)
        }
        finish()
    }

}

interface TunaInformDocumentResultHandler {

    fun onDocumentInformed(result: String)

}