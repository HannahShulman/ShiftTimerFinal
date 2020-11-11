package com.shift.timer.activities


object Solution {
    fun lengthOfLongestSubstring(s: String): Int {
        var tempLength = 1;
        val map: HashMap<Char, Int> = HashMap<Char, Int>()
        for(i in 0..s.length){
            val lastPos: Int? = map[s[i]]
            if(lastPos == null){
                map.put(s[i], i)
                'c'.isDigit()
            }else{
                if(i- lastPos > tempLength) tempLength = i else { }
            }
        }
        return tempLength
    }
}