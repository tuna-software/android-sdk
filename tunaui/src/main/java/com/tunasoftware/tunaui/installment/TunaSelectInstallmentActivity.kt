package com.tunasoftware.tunaui.installment

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.tunasoftware.tunaui.R
import com.tunasoftware.tunaui.TunaUI
import com.tunasoftware.tunaui.domain.entities.InstallmentSelectionResult

class TunaSelectInstallmentActivity : AppCompatActivity(), TunaInstallmentSelectionResultHandler {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuna_select_installment)
    }

    override fun onSelectedInstallment(result: Installment) {
        Intent().apply {
            putExtra(
                TunaUI.RESULT_INSTALLMENT_SELECTION,
                InstallmentSelectionResult(installment = result)
            )
        }.also {
            setResult(RESULT_OK, it)
        }
        finish()
    }
}

interface TunaInstallmentSelectionResultHandler {

    fun onSelectedInstallment(result: Installment)

}