package com.example.calculator01


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log.e
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAdddecimal = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input = findViewById<TextView>(R.id.input)
        val result = findViewById<TextView>(R.id.resultTv)



    }

    fun numberAction(view: View) {
        if(view is Button){
            if(view.text == "."){
                if (canAdddecimal){
                    input.append(view.text)
                    canAdddecimal = false
                }
            }
            else

            input.append(view.text)
            canAddOperation = true
        }
    }

    fun operatorAction(view: View) {
        if (view is Button && canAddOperation){
            input.append(view.text)
            canAddOperation = false
            canAdddecimal = true
        }
    }

    fun equalAction(view: View) {
        resultTv.text = calculateResults()
    }


    private fun calculateResults(): String {
        val digitsOperators = digitOp()
        if (digitsOperators.isEmpty()) return ""

       val timesDiv = timeDivCal(digitsOperators)
        if (timesDiv.isEmpty()) return ""

        val result = subCalculator(timesDiv)
        return result.toString()

    }

    private fun subCalculator(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){

            if(passedList[i] is Char && i != passedList.lastIndex){

                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float

                if(operator == '+')
                    result += nextDigit

                if(operator == '-')
                    result -= nextDigit
            }
        }
        return result

    }


    private fun timeDivCal(passedList: MutableList<Any>): MutableList<Any>{
        var list = passedList
        while (list.contains('x')|| list.contains('/')){
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices){
            if(passedList[i] is Char && i != passedList.lastIndex && i < restartIndex){
               val operator = passedList[i]
                val firstDigit = passedList[i-1] as Float
                val secondDigit = passedList[i+1] as Float

                when (operator){
                    'x' ->
                    {
                        newList.add(firstDigit * secondDigit)
                        restartIndex = i+1
                    }
                    '/' ->
                    {
                       newList.add(firstDigit / secondDigit)
                        restartIndex = i+1
                    }
                    else -> {
                        newList.add(firstDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i>restartIndex)
                newList.add(passedList[i])

        }
        return newList
    }


    private fun digitOp(): MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for(character in input.text){
            if(character.isDigit() || character == '.')
                currentDigit += character

            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if(currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

    fun clearAction(view: View) {
        input.text = ""
        resultTv.text = ""
    }
}

