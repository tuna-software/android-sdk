package com.tunasoftware.tunaui.utils

import android.text.Editable
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


        fun mask(mask : String, editText : EditText) : TextWatcher {

            val textWatcher : TextWatcher = object : TextWatcher {
                var isUpdating : Boolean = false
                var oldString : String = ""
                override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {

                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val str = replaceChars(s.toString())
                    var textWithMask = ""

                    if (count == 0)//is deleting
                        isUpdating = true

                    if (isUpdating){
                        oldString = str
                        isUpdating = false
                        return
                    }

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

                    isUpdating = true
                    editText.setText(textWithMask)
                    editText.setSelection(textWithMask.length)
                }

                override fun afterTextChanged(editable: Editable) {

                }
            }

            return textWatcher
        }
    }
}