package com.link2me.android.common

class HangulUtils {
    companion object {
        // 한글 초성검색
        private fun toHexString(decimal: Int): String {
            val intDec = java.lang.Long.valueOf(decimal.toLong())
            return java.lang.Long.toHexString(intDec)
        }

        const val HANGUL_BEGIN_UNICODE = 44032 // 가
        const val HANGUL_END_UNICODE = 55203 // 힣
        const val HANGUL_BASE_UNIT = 588

        val INITIAL_SOUND_UNICODE = intArrayOf(
            12593, 12594, 12596,
            12599, 12600, 12601, 12609, 12610, 12611, 12613, 12614, 12615,
            12616, 12617, 12618, 12619, 12620, 12621, 12622
        )

        val INITIAL_SOUND = charArrayOf(
            'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ',
            'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
        )

        /**
         * 문자를 유니코드(10진수)로 변환 후 반환 한다.
         *
         * @param ch
         * @return
         */
        fun convertCharToUnicode(ch: Char): Int {
            return ch.toInt()
        }

        /**
         * 문자열을 유니코드(10진수)로 변환 후 반환 한다.
         *
         * @param str
         * @return
         */
        fun convertStringToUnicode(str: String?): IntArray? {
            var unicodeList: IntArray? = null
            if (str != null) {
                unicodeList = IntArray(str.length)
                for (i in 0 until str.length) {
                    unicodeList[i] = convertCharToUnicode(str[i])
                }
            }
            return unicodeList
        }

        /**
         * 유니코드(16진수)를 문자로 변환 후 반환 한다.
         *
         * @param hexUnicode
         * @return
         */
        fun convertUnicodeToChar(hexUnicode: String): Char {
            return hexUnicode.toInt(16).toChar()
        }

        /**
         * 유니코드(10진수)를 문자로 변환 후 반환 한다.
         *
         * @param unicode
         * @return
         */
        fun convertUnicodeToChar(unicode: Int): Char {
            return convertUnicodeToChar(toHexString(unicode))
        }

        /**
         *
         * @param value
         * @return
         */
        fun getHangulInitialSound(value: String?): String? {
            val result = StringBuffer()
            val unicodeList = convertStringToUnicode(value)
            for (unicode in unicodeList!!) {
                if (HANGUL_BEGIN_UNICODE <= unicode
                    && unicode <= HANGUL_END_UNICODE
                ) {
                    val tmp = unicode - HANGUL_BEGIN_UNICODE
                    val index = tmp / HANGUL_BASE_UNIT
                    result.append(INITIAL_SOUND[index])
                } else {
                    result.append(convertUnicodeToChar(unicode))
                }
            }
            return result.toString()
        }

        fun getIsChoSungList(name: String?): BooleanArray? {
            if (name == null) {
                return null
            }
            val choList = BooleanArray(name.length)
            for (i in 0 until name.length) {
                val c = name[i]
                var isCho = false
                for (cho in INITIAL_SOUND) {
                    if (c == cho) {
                        isCho = true
                        break
                    }
                }
                choList[i] = isCho
            }
            return choList
        }

        fun getHangulInitialSound(value: String?, searchKeyword: String?): String? {
            return getHangulInitialSound(value, getIsChoSungList(searchKeyword))
        }

        fun getHangulInitialSound(value: String?, isChoList: BooleanArray?): String? {
            val result = StringBuffer()
            val unicodeList = convertStringToUnicode(value)
            for (idx in unicodeList!!.indices) {
                val unicode = unicodeList[idx]
                if (isChoList != null && idx <= isChoList.size - 1) {
                    if (isChoList[idx]) {
                        if (HANGUL_BEGIN_UNICODE <= unicode && unicode <= HANGUL_END_UNICODE) {
                            val tmp = unicode - HANGUL_BEGIN_UNICODE
                            val index = tmp / HANGUL_BASE_UNIT
                            result.append(INITIAL_SOUND[index])
                        } else {
                            result.append(convertUnicodeToChar(unicode))
                        }
                    } else {
                        result.append(convertUnicodeToChar(unicode))
                    }
                } else {
                    result.append(convertUnicodeToChar(unicode))
                }
            }
            return result.toString()
        }
    }
}