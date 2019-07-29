package com.CK;

public class Main {

    public static void main(String[] args) {
        String s = "acdcb";
        String p = "a*c?b";
        Solution3 solution = new Solution3();
        System.out.println(solution.isMatch(s, p));
    }
}

class Solution {
    public boolean isMatch(String s, String p) {
        if (p.equals(s) || p.equals("*")) return true;
        if (p.isEmpty() || s.isEmpty()) return false;
        boolean[][] dp = new boolean[s.length()][p.length()];
        boolean[][] visited = new boolean[s.length()][p.length()];
        return helper(s, 0, p, 0, dp, visited);
    }

    private boolean helper(String s, int sItr, String p, int pItr, boolean[][] dp, boolean[][] visited) {
        if (pItr == p.length()) return sItr == s.length();

        if (sItr >= s.length()) {
            if (pItr >= p.length()) return true;
            else {
                for (int i = pItr; i < p.length(); i++) {
                    if (p.charAt(i) != '*') return false;
                }
                return true;
            }
        }

        if (visited[sItr][pItr]) return dp[sItr][pItr];

        if (p.charAt(pItr) == '*') {
            dp[sItr][pItr] = helper(s, sItr + 1, p, pItr, dp, visited)
                    || helper(s, sItr, p, pItr + 1, dp, visited);
        } else {
            dp[sItr][pItr] = isMatchSingleChar(s, sItr, p, pItr)
                    && helper(s, sItr + 1, p, pItr + 1, dp, visited);
        }

        visited[sItr][pItr] = true;
        return dp[sItr][pItr];
    }

    private boolean isMatchSingleChar(String s, int sItr, String p, int pItr) {
        return p.charAt(pItr) == '?'
                || p.charAt(pItr) == s.charAt(sItr);
    }
}

// DP
class Solution2 {
    public boolean isMatch(String s, String p) {
        int sLen = s.length(), pLen = p.length();

        // base cases
        if (p.equals(s) || p.equals("*")) return true;
        if (p.isEmpty() || s.isEmpty()) return false;

        // init all matrix except [0][0] element as False
        boolean[][] d = new boolean[pLen + 1][sLen + 1];
        d[0][0] = true;

        // DP compute
        for(int pIdx = 1; pIdx < pLen + 1; pIdx++) {
            // the current character in the pattern is '*'
            if (p.charAt(pIdx - 1) == '*') {
                int sIdx = 1;
                // d[p_idx - 1][s_idx - 1] is a string-pattern match
                // on the previous step, i.e. one character before.
                // Find the first idx in string with the previous math.
                while ((!d[pIdx - 1][sIdx - 1]) && (sIdx < sLen + 1)) sIdx++;
                // If (string) matches (pattern),
                // when (string) matches (pattern)* as well
                d[pIdx][sIdx - 1] = d[pIdx - 1][sIdx - 1];
                // If (string) matches (pattern),
                // when (string)(whatever_characters) matches (pattern)* as well
                while (sIdx < sLen + 1) d[pIdx][sIdx++] = true;
            }
            // the current character in the pattern is '?'
            else if (p.charAt(pIdx - 1) == '?') {
                for(int sIdx = 1; sIdx < sLen + 1; sIdx++)
                    d[pIdx][sIdx] = d[pIdx - 1][sIdx - 1];
            }
            // the current character in the pattern is not '*' or '?'
            else {
                for(int sIdx = 1; sIdx < sLen + 1; sIdx++) {
                    // Match is possible if there is a previous match
                    // and current characters are the same
                    d[pIdx][sIdx] = d[pIdx - 1][sIdx - 1] &&
                            (p.charAt(pIdx - 1) == s.charAt(sIdx - 1));
                }
            }
        }
        return d[pLen][sLen];
    }
}

//Backtracking
class Solution3 {
    public boolean isMatch(String s, String p) {
        int sLen = s.length(), pLen = p.length();
        int sIdx = 0, pIdx = 0;
        int starIdx = -1, sTmpIdx = -1;

        while (sIdx < sLen) {
            // If the pattern caracter = string character
            // or pattern character = '?'
            if (pIdx < pLen && (p.charAt(pIdx) == '?' || p.charAt(pIdx) == s.charAt(sIdx))){
                ++sIdx;
                ++pIdx;
            }
            // If pattern character = '*'
            else if (pIdx < pLen && p.charAt(pIdx) == '*') {
                // Check the situation
                // when '*' matches no characters
                starIdx = pIdx;
                sTmpIdx = sIdx;
                ++pIdx;
            }
            // If pattern character != string character
            // or pattern is used up
            // and there was no '*' character in pattern
            else if (starIdx == -1) {
                return false;
            }
            // If pattern character != string character
            // or pattern is used up
            // and there was '*' character in pattern before
            else {
                // Backtrack: check the situation
                // when '*' matches one more character
                pIdx = starIdx + 1;
                sIdx = sTmpIdx + 1;
                sTmpIdx = sIdx;
            }
        }

        // The remaining characters in the pattern should all be '*' characters
        for(int i = pIdx; i < pLen; i++)
            if (p.charAt(i) != '*') return false;
        return true;
    }
}