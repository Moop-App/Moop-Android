package soup.movie.data.repository.internal.util

/**
 * 초성 검색 알고리즘을 위한 클래스
 *
 * @author roter
 * @see <a href="http://www.roter.pe.kr">http://www.roter.pe.kr</a>
 */
internal object SearchHelper {

    private const val HANGUL_BEGIN_UNICODE: Char = 44032.toChar() // 가
    private const val HANGUL_LAST_UNICODE: Char = 55203.toChar() // 힣
    private const val HANGUL_BASE_UNIT: Char = 588.toChar()//각자음 마다 가지는 글자수

    //자음
    private val INITIAL_SOUND = charArrayOf(
        'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ',
        'ㅅ', 'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ')

    private const val NO_MATCHED = -1

    /**
     * 해당 문자가 초성인지 검사한다.
     */
    private fun isInitialSound(searchCharacter: Char): Boolean {
        for (c in INITIAL_SOUND) {
            if (c == searchCharacter) {
                return true
            }
        }
        return false
    }

    /**
     * 해당 문자의 자음을 얻는다.
     *
     * @param c 검사할 문자
     */
    private fun getInitialSound(c: Char): Char {
        val hanBegin = c - HANGUL_BEGIN_UNICODE
        val index = hanBegin / HANGUL_BASE_UNIT.toInt()
        return INITIAL_SOUND[index]
    }

    /**
     * 해당 문자가 한글인지 검사
     *
     * @param c 문자 하나
     */
    private fun isHangul(c: Char): Boolean {
        return c in HANGUL_BEGIN_UNICODE..HANGUL_LAST_UNICODE
    }

    fun matchedLegacy(sentence: String, query: String): Boolean =
        if (query.isEmpty()) {
            false
        } else matchedIndex(sentence, query) != NO_MATCHED

    fun matched(sentence: String, query: String): Boolean =
        if (query.isEmpty()) {
            false
        } else matchedIndexInfo(sentence, query) != null

    private fun matchedIndex(sentence: String, query: String): Int {
        val seof = sentence.length - query.length
        val slen = query.length
        if (seof < 0) {
            return NO_MATCHED // 검색어가 더 길면 -1을 리턴한다.
        }
        val fullIdx = indexOfIgnoreCase(sentence, query)
        if (fullIdx != NO_MATCHED) {
            return fullIdx
        }
        for (i in 0..seof) {
            var t = 0
            while (t < slen) {
                if (isInitialSound(query[t]) && isHangul(sentence[i + t])) {
                    // 만약 현재 char이 초성이고 value가 한글이면, 각각의 초성끼리 같은지 비교한다
                    if (getInitialSound(sentence[t]) == query[t]) {
                        t++
                    } else {
                        break
                    }
                } else {
                    // char이 초성이 아니라면, 그냥 같은지 비교한다.
                    if (sentence[i + t] == query[t]) {
                        t++
                    } else {
                        break
                    }
                }
            }
            if (t == slen) {
                return i // 모두 일치한 결과를 찾으면 index를 리턴한다.
            }
        }
        return NO_MATCHED // 일치하는 것을 찾지 못했으면 -1을 리턴한다.
    }

    private fun matchedIndexInfo(sentence: String, query: String): IntRange? {
        val fullIdx = indexOfIgnoreCase(sentence, query)
        if (fullIdx != NO_MATCHED) {
            return IntRange(fullIdx, fullIdx + query.length) // 일치
        }

        val conciseQuery = query.toLowerCase().replace(" ".toRegex(), "")
        val conciseValue = sentence.toLowerCase()

        var startIdx = -1
        var endIdx = -1
        var idx = 0
        val queryLen = conciseQuery.length
        for (k in 0 until conciseValue.length) {
            val ch = conciseValue[k]
            if (ch == ' ') {
                continue
            }
            val i = idx
            if (i < queryLen) {
                val ch2 = conciseQuery[i]
                if (ch2 == ' ') {
                    continue
                }
                if (isNotMatched(ch2, ch)) {
                    idx = 0
                    continue
                }
                idx++
                if (i == 0) {
                    startIdx = k
                }
                if (i == queryLen - 1) {
                    endIdx = k + 1
                    break
                }
            }
        }
        return if (startIdx == -1 || endIdx == -1) null
        else IntRange(startIdx, endIdx)
    }

    private fun isNotMatched(q: Char, v: Char): Boolean = !isMatched(q, v)

    private fun isMatched(q: Char, v: Char): Boolean =
        v == q || isInitialSound(q) && isHangul(v) && getInitialSound(v) == q // korean

    private fun indexOfIgnoreCase(sentence: String, word: String): Int {
        return sentence.toLowerCase().indexOf(word.toLowerCase())
    }
}
