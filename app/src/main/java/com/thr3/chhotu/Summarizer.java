package com.thr3.chhotu;

import java.util.*;
import java.util.regex.Pattern;
public class Summarizer 
{
	Summarizer(){}
	private Map<String, Integer> getWordCounts(String text)
	{
		Map<String,Integer> allWords = new HashMap<String, Integer>();
		int count;
		int singleIncrement = 0;
		text=text.trim();
		String[] words = text.split("\\s+");
		for (String word : words)
		{
			count = 0;
			if (allWords.containsKey(word))
			{
				allWords.put(word, singleIncrement += 1);
			}
			else
			{
				allWords.put(word, ++count);
			}
		}
		return allWords;
	}
	private Map<String,Integer> filterStopWords(Map<String, Integer> d)
	{
        String[] stop_words = { "a","able","about","after","all","also","am",
        "an","and","any","are","as","at","be","because","been","but","by","can",
        "do","does","either","else","ever","every","for","from","get","got","had",
        "cannot","could","did","has","have","he","her","hers","him","his","how","I",
        "if","in","into","is","it","its","just","let","like","likely","may","me",
        "might","most","must","my","neither","no","nor","not","of","off",
        "often","on","only","or","other","our","own","said","say","says","she",
        "should","so","some","than","that","the","their","them","then","there",
        "these","they","this","they're","to","too","that's","us","was","we","were",
        "what","when","where","which","while","who","whom","why","will","with",
        "would","yet","you","your", "you're" };
		for (String stop_word : stop_words)
		{
			if (d.containsKey(stop_word))
			{
				d.remove(stop_word);
			}
		}
        return d;
	}
	private List<String> sortByFreqThenDropFreq(Map<String,Integer> wordFrequencies)
	{
		List<String> sortedCollection = new ArrayList<String>(wordFrequencies.keySet());
		Collections.sort(sortedCollection);
		Collections.reverse(sortedCollection);
		return sortedCollection;
	}
	private String[] getSentences(String text)
	{
		text = text.replace("Mr.", "Mr").replace("Ms.", "Ms").replace("Dr.", "Dr").replace("Jan.", "Jan").replace("Feb.", "Feb")
				.replace("Mar.", "Mar").replace("Apr.", "Apr").replace("Jun.", "Jun").replace("Jul.", "Jul").replace("Aug.", "Aug")
				.replace("Sep.","Sep").replace("Spet.", "Sept").replace("Oct.", "Oct").replace("Nov.", "Nov").replace("Dec.", "Dec")
				.replace("St.", "St").replace("Prof.", "Prof").replace("Mrs.", "Mrs").replace("Gen.", "Gen")
                .replace("Corp.", "Corp").replace("Mrs.", "Mrs").replace("Sr.","Sr").replace("Jr.", "Jr").replace("cm.", "cm")
                .replace("Ltd.", "Ltd").replace("Col.", "Col").replace("vs.", "vs").replace("Capt.", "Capt")
                .replace("Univ.", "University").replace("Sgt.", "Sgt").replace("ft.","ft").replace("in.","in")
                .replace("Ave.", "Ave").replace("Univ.", "University").replace("Lt.", "Lt").replace("etc.", "etc").replace("mm.", "mm")
                .replace("\n\n", "").replace("\n", "").replace("\r", "");
		text = text.replaceAll("([A-Z])\\.", "$1");
		String pattern = "(?<!\\d)\\.(?!\\d)|(?<=\\d)\\.(?!\\d)|(?<!\\d)\\.(?=\\d)";
		Pattern pt = Pattern.compile(pattern);
		return pt.split(text);
	}
	private String search(String[] sentences, String word)
	{
		String first_matching_sentence = null;
        for (String sentence : sentences)
        {
            if (sentence.contains(word))
            {
                first_matching_sentence = sentence;
            }
        }
		return first_matching_sentence;
	}
	public String Summarize(String text)
	{
		if(text.equals("") || text.equals(" ") || text.equals("\n"))
		{
			return "Nothing to summarize...";
		}
		Map<String, Integer> wordFrequencies = getWordCounts(text);
		Map<String, Integer> filtered = filterStopWords(wordFrequencies);
		List<String> sorted = sortByFreqThenDropFreq(filtered);
		String[] sentences = getSentences(text);
		String firstSentence = sentences[0];
		String datePatternString = "(Monday|Tuesday|Wednesday|Thursday|Friday|Saturday)\\s\\d{1,2}\\s(January|February|March|April|May|June|July|August|September|October|November|December)\\s\\d{4}\\s\\d{1,2}\\.\\d{2}(\\sEST|\\sPST)";
		firstSentence = firstSentence.replace("Last modified on", "");
		firstSentence = firstSentence.replaceAll(datePatternString,"");
		List<String> setSummarySentences = new ArrayList<String>();
		for(String word : sorted)
		{
			String first_matching_sentence = search(sentences, word);
			setSummarySentences.add(first_matching_sentence);
			if(setSummarySentences.size() == sentences.length/2)
			{
				break;
			}
		}
		StringBuilder summary = new StringBuilder();
		summary.append("• ").append(firstSentence).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
		for(String sentence : sentences)
		{
			if(setSummarySentences.contains(sentence))
			{
				summary.append("• ").append(sentence).append(System.getProperty("line.separator")).append(System.getProperty("line.separator"));
			}
		}
		return summary.toString();
	}
}