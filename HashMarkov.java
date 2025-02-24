import java.util.*;


public class HashMarkov implements MarkovInterface {
    private String[] myWords;		// Training text split into array of words 
	private Random myRandom;		// Random number generator
	private int myOrder;			// Length of WordGrams used
    private HashMap<WordGram, List<String>> myMap = new HashMap<>();
    private static String END_OF_TEXT = "*** ERROR ***"; 

    public HashMarkov() {
		this(3);
	}

	public HashMarkov(int order){
		myOrder = order;
		myRandom = new Random();
        myWords = null;
	}

    @Override
    public void setTraining(String text) {
        // TODO Auto-generated method stub
        myWords = text.split("\\s+");
        myMap.clear();
        WordGram wg = new WordGram(myWords,0,myOrder);
        for (int k = myOrder; k < myWords.length; k += 1) {
            if (myMap.containsKey(wg))
            myMap.get(wg).add(myWords[k]);
            else if (myMap.containsKey(wg) == false) {
            myMap.put(wg, new ArrayList<String>());
            myMap.get(wg).add(myWords[k]);
            }
			// shift word gram by one, add currentWord
			wg = wg.shiftAdd(myWords[k]);
		}
    }

    @Override
    public List<String> getFollows(WordGram wgram) {
        // TODO Auto-generated method stub
        List<String> follows = new ArrayList<>();
        if(!myMap.containsKey(wgram)) return new ArrayList<>();
        follows = myMap.get(wgram);
        return follows;
    }

    private String getNextWord(WordGram wgram) {
		List<String> follows = getFollows(wgram);
		if (follows.size() == 0) {
			return END_OF_TEXT;
		}
		else {
			int randomIndex = myRandom.nextInt(follows.size());
			return follows.get(randomIndex);
		}
	}

    @Override
    public String getRandomText(int length) {
        // TODO Auto-generated method stub
        ArrayList<String> randomWords = new ArrayList<>(length);
		int index = myRandom.nextInt(myWords.length - myOrder + 1);
		WordGram current = new WordGram(myWords,index,myOrder);
		randomWords.add(current.toString());

		for(int k=0; k < length-myOrder; k += 1) {
			String nextWord = getNextWord(current);
			if (nextWord.equals(END_OF_TEXT)) {
				break;
			}
			randomWords.add(nextWord);
			current = current.shiftAdd(nextWord);
		}
		return String.join(" ", randomWords);
    }

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return myOrder;
        }

    @Override
    public void setSeed(long seed) {
        // TODO Auto-generated method stub
        myRandom.setSeed(seed);
        }
    
}
