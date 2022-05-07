/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static final String DEFAULT_STARTER = "skate";
    private Integer wordLength = DEFAULT_WORD_LENGTH;
    private final Set<String> wordSet = new HashSet<>();
    private final Map<String, List<String>> lettersToWords = new HashMap<>();
    private final Map<Integer, List<String>> sizeToWords = new HashMap<>();

    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            String word = line.trim();
            int length = word.length();
            wordSet.add(word);
            if (!sizeToWords.containsKey(length)) {
                sizeToWords.put(length, new ArrayList<String>());
            }
            sizeToWords.get(length).add(word);
            String sortedWord = sortLetters(word);
            if (!lettersToWords.containsKey(sortedWord)) {
                lettersToWords.put(sortedWord, new ArrayList<String>());
            }
            lettersToWords.get(sortedWord).add(word);
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && !word.contains(base);
    }

    public String sortLetters(String input) {
        char[] tempArray = input.toCharArray();
        Arrays.sort(tempArray);
        return new String(tempArray);
    }

    public boolean isAnagram(String word, String targetWord) {
        String sortedWord = sortLetters(word);
        return lettersToWords.containsKey(sortedWord) && lettersToWords.get(sortedWord).contains(targetWord);
    }

    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<>();
        for (String word : wordSet) {
            if (word.length() == targetWord.length() && isAnagram(word, targetWord)) {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            String nextWord = sortLetters(word + c);
            if (lettersToWords.containsKey(nextWord)) {
                for (String possibleWord : lettersToWords.get(nextWord)) {
                    if (isGoodWord(possibleWord, word)) {
                        result.add(possibleWord);
                    }
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        if (sizeToWords.containsKey(wordLength)) {
            for (String word : sizeToWords.get(wordLength)) {
                String sortedWord = sortLetters(word);
                if (lettersToWords.containsKey(sortedWord) && lettersToWords.get(sortedWord).size() > MIN_NUM_ANAGRAMS) {
                    wordLength = Math.min(wordLength + 1, MAX_WORD_LENGTH);
                    return word;
                }
            }
        }
        wordLength = Math.min(wordLength + 1, MAX_WORD_LENGTH);
        return DEFAULT_STARTER;
    }
}