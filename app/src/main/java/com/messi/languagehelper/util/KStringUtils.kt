package com.messi.languagehelper.util

import android.text.TextUtils
import java.util.*
import kotlin.collections.ArrayList

object KStringUtils {

    private fun getRandomLetter(): Char {
        val base = "abcdefghijlmnopqrstuvwxy"
        val random = Random()
        var letter:Char = 'a'
        val number = random.nextInt(base.length)
        letter = base[number]
        return letter
    }

    private fun getRandomLetterExcept(sLetter: Char): Char {
        var nLetter = 'a'
        while (true){
            nLetter = getRandomLetter()
            if (sLetter != nLetter){
                break
            }
        }
        return nLetter
    }

    private fun getLetterWithNum(letter: String, size: Int): String{
        var sLetter = letter[0]
        var str = ""
        for (index in 1 until size){
            str += getRandomLetterExcept(sLetter)
        }
        return sLetter+str
    }

    private fun changeWordLetter(word: String, pos: Int): String{
        var result = word
        if(!TextUtils.isEmpty(word)){
            var temp = word.toCharArray()
            if (temp.size > pos){
                val ca = getRandomLetterExcept(word[pos])
                temp[pos] = ca
                result = String(temp)
            }
        }
        return result
    }

    fun getWordSamples(word: String): ArrayList<String> {
        var list = ArrayList<String>()
        list.add(word)
        val size = word.length
        println(size)
        if (size > 0) {
            when (size) {
                1 -> {
                    for (index in 1..3){
                        list.add(getRandomLetterExcept(word[0]).toString())
                    }
                }
                2 -> {
                    for (index in 1..3){
                        list.add(getLetterWithNum(word,size))
                    }
                }
                3 -> {
                    for (index in 1 until size){
                        list.add(changeWordLetter(word,index))
                    }
                    list.add(getLetterWithNum(word,size))
                }
                else -> {
                    var poses = getNorepeatNums(1,size-1, 3)
                    for (index in 0 .. 2){
                        list.add(changeWordLetter(word,poses[index]))
                    }

                }
            }
        }
        list.shuffle()
        return list
    }

    fun getNorepeatNums(mini: Int, max: Int, size: Int): List<Int>{
        var list = ArrayList<Int>()
        var result = ArrayList<Int>()
        for (index in mini .. max) {
            list.add(index)
        }
        while(result.size < size){
            val pos = Random().nextInt(list.size)
            result.add(list[pos])
            list.removeAt(pos)
        }
        return result
    }

    fun getTimeMills(time: String): Long {
        val str = time.split(":|\\.".toRegex()).toTypedArray()
        var time = (str[0].toInt() * 60 + str[1].toInt() + str[2].toInt() * 0.01) * 1000
        return time.toLong()
    }

}