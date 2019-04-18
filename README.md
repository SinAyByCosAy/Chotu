# Chhotu - The Article Summarizer

**Chhotu** is article summarizer mobile application that can be used to generate **summary** of document provided by user.
It doesn’t use any **Machine Learning** or **Natural Language Processing** Techniques, but **Regex based string processing** to achieve its goal. 

This **article summarizer** was developed in under **24hrs time bracket** during a hackathon competition hosted by **Mozilla**, 
this mobile application fetched our team **Rank 1** in this hackathon. 

## Features
-	**Stunning user interface**, to keep you enchanted while going through **boring** text doccuments.
-	No heavy **Machine Learning** or **Natural Language Processing** scripts used.
-	Totally **Regular Expression** based string processing.
-	Get **audible output** of your text doccument or genarated summary.
- **Enter** your text documment just by **speaking**.
-	**Optical character reader** to directly capture your hard written documents.

### Machine Learning
**Machine learning** is a method of data analysis that automates analytical model building. It is a branch of **artificial intelligence** based on the idea that systems can **learn** from data, identify patterns and **make decisions** with minimal human intervention.

### Natural Language Processing
The field of **study** that focuses on the interactions between human language and computers is called **Natural Language Processing**, or NLP for short. It sits at the **intersection** of computer science, artificial intelligence, and computational linguistics.

## Algorithm
- Initially, Generate a **Map** of raw frequencies of words within the text doccument.
- Lets now **reduce redundancy**, by filtering out any **stopwords** within the generated Map, and keeping only **certain key phrases** and words.
- Now, sort the **Map** in decreasing order of **frequency** of words.
- Then, retrieve sentences from your original text doccument, **format** and making sure any **abbreviations** and **suffixes** with a period or decimal are not counted as the ending of a sentence.
- Next, for text that is **unformatted**, we need to format and adjust it so that certain **un-needed** text is filtered out.
- Finally, obtain the **summary** by going through your list of sentences and then using your **Map**, compare and find the key phrases and words from each sentence. If found, add that sentence to the **summary** list.
- Once all done, return the **summary** as a String.
