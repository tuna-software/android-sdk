package com.tunasoftware.tunaui.utils

import android.text.Editable
import android.text.Selection
import android.text.TextWatcher
import android.widget.EditText

class Mask{
    companion object {
        private fun replaceChars(text : String) : String{
            return text.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
        }

        var isUpdating : Boolean = false

        fun mask(mask : String) : TextWatcher {

            val textWatcher : TextWatcher = object : TextWatcher {

                var oldString : String = ""
                var count = 0

                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                      this.count = count
                }

                override fun afterTextChanged(s: Editable) {
                    if (isUpdating || count == 0)
                        return
                    isUpdating = true

                    val str = replaceChars(s.toString())
                    var textWithMask = ""

                    var i = 0
                    for (m : Char in mask.toCharArray()){
                        if (m != '#' && str.length > oldString.length){
                            textWithMask += m
                            continue
                        }
                        try {
                            textWithMask += str[i]
                        }catch (e : Exception){
                            break
                        }
                        i++
                    }
                    s.clear()
                    s.append(textWithMask)
                    if (textWithMask.equals(s.toString())) {
                        Selection.setSelection(s, s.length)
                    }
                    isUpdating = false
                }
            }

            return textWatcher
        }
    }
}