package com.otmutienipen.testrameddia.utilities

class ObfuscatorHelper {
    fun uselessMethod1(): Int {
        return (2..10).random()
    }

    fun uselessMethod2(): Boolean {
        return uselessMethod1() > 5
    }
}
