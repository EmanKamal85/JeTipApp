package com.example.jettipapp.utils

fun calculateTipAmount(totalBillState: Double, tipPercentage: Int): Double {
    return if (totalBillState>1 && totalBillState.toString().isNotEmpty())
        (totalBillState*tipPercentage)/100 else 0.0

}

fun calculateTotalPerPerson( totalBillState: Double, tipPercentage: Int, numberOfPersons : Int) : Double{
    val bill = calculateTipAmount(totalBillState, tipPercentage) + totalBillState
    return bill/ numberOfPersons

}