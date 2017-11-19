/**
 * Synsets.java
 * 
 * Natalie Parde
 * Last Updated: 11/16/2017
 * 
 * Contains a function used to look up WordNet SynSets.
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.didion.jwnl.JWNL;
import net.didion.jwnl.JWNLException;
import net.didion.jwnl.data.IndexWord;
import net.didion.jwnl.data.POS;
import net.didion.jwnl.dictionary.Dictionary;

public class Synsets {

	public final Dictionary dictionary;
	
	public Synsets() {
		try {
			JWNL.initialize(new FileInputStream("resources/jwnl/properties.xml"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (JWNLException e) {
			e.printStackTrace();
		}
		dictionary = Dictionary.getInstance();
	}
	
	/**
	 * Get the synsets associated with the specified word/pos combo.
	 * @param word
	 * @param pos
	 * @return
	 */
	public net.didion.jwnl.data.Synset[] getSynsets(String word, String pos) {
		POS wn_pos = null;
		if(pos.startsWith("n")) {
			wn_pos = POS.NOUN;
		}
		else if(pos.startsWith("v")) {
			wn_pos = POS.VERB;
		}
		else if(pos.startsWith("j")) {
			wn_pos = POS.ADJECTIVE;
		}
		else if(pos.startsWith("r")) {
			wn_pos = POS.ADVERB;
		}
		if(wn_pos != null) {
			try {
				IndexWord indexWord;
				if((indexWord = dictionary.lookupIndexWord(wn_pos, word)) != null) {
					return indexWord.getSenses();
				}
				else {
					return null;
				}
			} catch (JWNLException e) {
				e.printStackTrace();
				return null;
			}
		}
		else {
			return null;
		}
	}
}
