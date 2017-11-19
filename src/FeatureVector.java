

import java.util.HashMap;

public class FeatureVector {

	private String sentence_ID;
	private String metaphor_ID;
	private String indicator;
	private String metaphor;
	private String metaphorIsGov;
	private String metaphorOccursFirst;
	private String wordDistance;
	private String pos_metaphor;
	private String pos_indicator;
	private String metaphorIsProperNoun;
	private String indicatorIsProperNoun;
	private String dependencyType;
	private String pmi;
	private String avgPmiDep;
	private String avgPmiDep_Indicator;
	private String avgPmiSentence_Metaphor;
	private String avgPmiSentence_Indicator;
	private String pmiSpan;
	private String avgPmiSentenceMetaphor_minus_pmi;
	private String avgPmiSentenceIndicator_minus_pmi;
	private String concreteness_metaphor;
	private String concreteness_indicator;
	private String diff_concreteness;
	private String abs_diff_concreteness;
	private String avg_concreteness;
	private String avg_abs_concreteness;
	private String metaphor_macro_avg_concreteness;
	private String indicator_macro_avg_concreteness;
	private String imageability_metaphor;
	private String imageability_indicator;
	private String diff_imageability;
	private String abs_diff_imageability;
	private String avg_imageability;
	private String metaphor_macro_avg_imageability;
	private String indicator_macro_avg_imageability;
	private String sentiment_metaphor;
	private String sentiment_indicator;
	private String diff_sentiment;
	private String abs_diff_sentiment;
	private String avg_sentiment;
	private String avg_abs_sentiment;
	private String metaphor_macro_avg_sentiment;
	private String indicator_macro_avg_sentiment;
	private String freq_all_metaphor;
	private String freq_all_indicator;
	private String freq_imaginative_metaphor;
	private String freq_imaginative_indicator;
	private String freq_informative_metaphor;
	private String freq_informative_indicator;
	private String metaphor_exists_in_spanishToEnglish;
	private String metaphor_exists_in_germanToEnglish;
	private String metaphor_exists_in_arabicToEnglish;
	private String metaphor_exists_in_chineseToEnglish;
	private String indicator_exists_in_spanishToEnglish;
	private String indicator_exists_in_germanToEnglish;
	private String indicator_exists_in_arabicToEnglish;
	private String indicator_exists_in_chineseToEnglish;
	private String num_retranslated_sentences_containing_metaphor;
	private String num_retranslated_sentences_containing_indicator;
	private String gutenberg_sim_retranslated_spanish;
	private String gutenberg_sim_retranslated_german;
	private String gutenberg_sim_retranslated_arabic;
	private String gutenberg_sim_retranslated_chinese;
	private String wiki_sim_retranslated_spanish;
	private String wiki_sim_retranslated_german;
	private String wiki_sim_retranslated_arabic;
	private String wiki_sim_retranslated_chinese;
	private String familiarity_metaphor;
	private String familiarity_indicator;
	private String diff_familiarity;
	private String abs_diff_familiarity;
	private String avg_familiarity;
	private String metaphor_macro_avg_familiarity;
	private String indicator_macro_avg_familiarity;
	private String ambiguity_metaphor;
	private String ambiguity_indicator;
	private String diff_ambiguity;
	private String abs_diff_ambiguity;
	private String avg_ambiguity;
	private String metaphor_macro_avg_ambiguity;
	private String indicator_macro_avg_ambiguity;
	private String metaphor_bow_features;
	private String indicator_bow_features;
	private String metaphor_sim_features;
	private String indicator_sim_features;
	private String metaphor_prob_features;
	private String indicator_prob_features;
	private HashMap<String, String> metaphor_prob_features_by_key;
	private HashMap<String, String> indicator_prob_features_by_key;
	private HashMap<String, String> prob_metaphor_in_best_indicator_topic;
	private HashMap<String, String> prob_indicator_in_best_metaphor_topic;
	private HashMap<String, String> metaphor_embeddings_by_key;
	private HashMap<String, String> indicator_embeddings_by_key;
	private HashMap<String, String> embeddingCosineSim;
	private String metaphorEmbeddings;
	private String indicatorEmbeddings;
	private String cosineSim;
	private String topic_distance;
	private String sim_dot;
	private String prob_dot;
	private String highest_num_synsets;
	private String lowest_num_synsets;
	private String avg_num_synsets;
	private String abs_diff_num_synsets;
	private String label;
	
	public FeatureVector() {
		indicator = "?";
		metaphor = "?";
		metaphor_prob_features_by_key = new HashMap<String, String>();
		indicator_prob_features_by_key = new HashMap<String, String>();
		prob_metaphor_in_best_indicator_topic = new HashMap<String, String>();
		prob_indicator_in_best_metaphor_topic = new HashMap<String, String>();
		metaphor_embeddings_by_key = new HashMap<String, String>();
		indicator_embeddings_by_key = new HashMap<String, String>();
		embeddingCosineSim = new HashMap<String, String>();
	}

	public String getSentence_ID() {
		return sentence_ID;
	}

	public void setSentence_ID(String sentence_ID) {
		this.sentence_ID = sentence_ID;
	}

	public String getMetaphor_ID() {
		return metaphor_ID;
	}

	public void setMetaphor_ID(String metaphor_ID) {
		this.metaphor_ID = metaphor_ID;
	}

	public String getIndicator() {
		return indicator;
	}

	public void setIndicator(String indicator) {
		this.indicator = indicator;
	}

	public String getMetaphor() {
		return metaphor;
	}

	public void setMetaphor(String metaphor) {
		this.metaphor = metaphor;
	}

	public String getMetaphorIsGov() {
		return metaphorIsGov;
	}

	public void setMetaphorIsGov(String metaphorIsGov) {
		this.metaphorIsGov = metaphorIsGov;
	}

	public String getMetaphorOccursFirst() {
		return metaphorOccursFirst;
	}

	public void setMetaphorOccursFirst(String metaphorOccursFirst) {
		this.metaphorOccursFirst = metaphorOccursFirst;
	}

	public String getWordDistance() {
		return wordDistance;
	}

	public void setWordDistance(String wordDistance) {
		this.wordDistance = wordDistance;
	}

	public String getPos_metaphor() {
		return pos_metaphor;
	}

	public void setPos_metaphor(String pos_metaphor) {
		this.pos_metaphor = pos_metaphor;
	}

	public String getPos_indicator() {
		return pos_indicator;
	}

	public void setPos_indicator(String pos_indicator) {
		this.pos_indicator = pos_indicator;
	}

	public String getMetaphorIsProperNoun() {
		return metaphorIsProperNoun;
	}

	public void setMetaphorIsProperNoun(String metaphorIsProperNoun) {
		this.metaphorIsProperNoun = metaphorIsProperNoun;
	}

	public String getIndicatorIsProperNoun() {
		return indicatorIsProperNoun;
	}

	public void setIndicatorIsProperNoun(String indicatorIsProperNoun) {
		this.indicatorIsProperNoun = indicatorIsProperNoun;
	}

	public String getDependencyType() {
		return dependencyType;
	}

	public void setDependencyType(String dependencyType) {
		this.dependencyType = dependencyType;
	}
	
	public String getPmi() {
		return pmi;
	}
	
	public void setPmi(String pmi) {
		this.pmi = pmi;
	}

	public String getAvgPmiDep() {
		return avgPmiDep;
	}

	public void setAvgPmiDep(String avgPmiDep) {
		this.avgPmiDep = avgPmiDep;
	}

	public String getAvgPmiDep_Indicator() {
		return avgPmiDep_Indicator;
	}

	public void setAvgPmiDep_Indicator(String avgPmiDep_Indicator) {
		this.avgPmiDep_Indicator = avgPmiDep_Indicator;
	}

	public String getAvgPmiSentence_Metaphor() {
		return avgPmiSentence_Metaphor;
	}

	public void setAvgPmiSentence_Metaphor(String avgPmiSentence) {
		this.avgPmiSentence_Metaphor = avgPmiSentence;
	}

	public String getAvgPmiSentence_Indicator() {
		return avgPmiSentence_Indicator;
	}

	public void setAvgPmiSentence_Indicator(String avgPmiSentence_Indicator) {
		this.avgPmiSentence_Indicator = avgPmiSentence_Indicator;
	}

	public String getPmiSpan() {
		return pmiSpan;
	}

	public void setPmiSpan(String pmiSpan) {
		this.pmiSpan = pmiSpan;
	}

	public String getAvgPmiSentenceMetaphor_minus_pmi() {
		return avgPmiSentenceMetaphor_minus_pmi;
	}

	public void setAvgPmiSentenceMetaphor_minus_pmi(String avgPmiSentenceMetaphor_minus_pmi) {
		this.avgPmiSentenceMetaphor_minus_pmi = avgPmiSentenceMetaphor_minus_pmi;
	}

	public String getAvgPmiSentenceIndicator_minus_pmi() {
		return avgPmiSentenceIndicator_minus_pmi;
	}

	public void setAvgPmiSentenceIndicator_minus_pmi(
			String avgPmiSentenceIndicator_minus_pmi) {
		this.avgPmiSentenceIndicator_minus_pmi = avgPmiSentenceIndicator_minus_pmi;
	}
	
	public String getConcreteness_metaphor() {
		return concreteness_metaphor;
	}
	
	public void setConcreteness_metaphor(String concreteness_metaphor) {
		this.concreteness_metaphor = concreteness_metaphor;
	}

	public String getConcreteness_indicator() {
		return concreteness_indicator;
	}

	public void setConcreteness_indicator(String concreteness_indicator) {
		this.concreteness_indicator = concreteness_indicator;
	}

	public String getDiff_concreteness() {
		return diff_concreteness;
	}

	public void setDiff_concreteness(String diff_concreteness) {
		this.diff_concreteness = diff_concreteness;
	}

	public String getAbs_diff_concreteness() {
		return abs_diff_concreteness;
	}

	public void setAbs_diff_concreteness(String abs_diff_concreteness) {
		this.abs_diff_concreteness = abs_diff_concreteness;
	}

	public String getAvg_concreteness() {
		return avg_concreteness;
	}

	public void setAvg_concreteness(String avg_concreteness) {
		this.avg_concreteness = avg_concreteness;
	}

	public String getAvg_abs_concreteness() {
		return avg_abs_concreteness;
	}

	public void setAvg_abs_concreteness(String avg_abs_concreteness) {
		this.avg_abs_concreteness = avg_abs_concreteness;
	}

	public String getMetaphor_macro_avg_concreteness() {
		return metaphor_macro_avg_concreteness;
	}

	public void setMetaphor_macro_avg_concreteness(
			String metaphor_macro_avg_concreteness) {
		this.metaphor_macro_avg_concreteness = metaphor_macro_avg_concreteness;
	}

	public String getIndicator_macro_avg_concreteness() {
		return indicator_macro_avg_concreteness;
	}

	public void setIndicator_macro_avg_concreteness(
			String indicator_macro_avg_concreteness) {
		this.indicator_macro_avg_concreteness = indicator_macro_avg_concreteness;
	}

	public String getImageability_metaphor() {
		return imageability_metaphor;
	}

	public void setImageability_metaphor(String imageability_metaphor) {
		this.imageability_metaphor = imageability_metaphor;
	}

	public String getImageability_indicator() {
		return imageability_indicator;
	}

	public void setImageability_indicator(String imageability_indicator) {
		this.imageability_indicator = imageability_indicator;
	}

	public String getDiff_imageability() {
		return diff_imageability;
	}

	public void setDiff_imageability(String diff_imageability) {
		this.diff_imageability = diff_imageability;
	}

	public String getAbs_diff_imageability() {
		return abs_diff_imageability;
	}

	public void setAbs_diff_imageability(String abs_diff_imageability) {
		this.abs_diff_imageability = abs_diff_imageability;
	}

	public String getAvg_imageability() {
		return avg_imageability;
	}

	public void setAvg_imageability(String avg_imageability) {
		this.avg_imageability = avg_imageability;
	}

	public String getMetaphor_macro_avg_imageability() {
		return metaphor_macro_avg_imageability;
	}

	public void setMetaphor_macro_avg_imageability(
			String metaphor_macro_avg_imageability) {
		this.metaphor_macro_avg_imageability = metaphor_macro_avg_imageability;
	}

	public String getIndicator_macro_avg_imageability() {
		return indicator_macro_avg_imageability;
	}

	public void setIndicator_macro_avg_imageability(
			String indicator_macro_avg_imageability) {
		this.indicator_macro_avg_imageability = indicator_macro_avg_imageability;
	}

	public String getSentiment_metaphor() {
		return sentiment_metaphor;
	}

	public void setSentiment_metaphor(String sentiment_metaphor) {
		this.sentiment_metaphor = sentiment_metaphor;
	}

	public String getSentiment_indicator() {
		return sentiment_indicator;
	}

	public void setSentiment_indicator(String sentiment_indicator) {
		this.sentiment_indicator = sentiment_indicator;
	}

	public String getDiff_sentiment() {
		return diff_sentiment;
	}

	public void setDiff_sentiment(String diff_sentiment) {
		this.diff_sentiment = diff_sentiment;
	}

	public String getAbs_diff_sentiment() {
		return abs_diff_sentiment;
	}

	public void setAbs_diff_sentiment(String abs_diff_sentiment) {
		this.abs_diff_sentiment = abs_diff_sentiment;
	}

	public String getAvg_sentiment() {
		return avg_sentiment;
	}

	public void setAvg_sentiment(String avg_sentiment) {
		this.avg_sentiment = avg_sentiment;
	}

	public String getAvg_abs_sentiment() {
		return avg_abs_sentiment;
	}

	public void setAvg_abs_sentiment(String avg_abs_sentiment) {
		this.avg_abs_sentiment = avg_abs_sentiment;
	}

	public String getMetaphor_macro_avg_sentiment() {
		return metaphor_macro_avg_sentiment;
	}

	public void setMetaphor_macro_avg_sentiment(
			String metaphor_macro_avg_sentiment) {
		this.metaphor_macro_avg_sentiment = metaphor_macro_avg_sentiment;
	}

	public String getIndicator_macro_avg_sentiment() {
		return indicator_macro_avg_sentiment;
	}

	public void setIndicator_macro_avg_sentiment(
			String indicator_macro_avg_sentiment) {
		this.indicator_macro_avg_sentiment = indicator_macro_avg_sentiment;
	}

	public String getFreq_all_metaphor() {
		return freq_all_metaphor;
	}

	public void setFreq_all_metaphor(String freq_all_metaphor) {
		this.freq_all_metaphor = freq_all_metaphor;
	}

	public String getFreq_all_indicator() {
		return freq_all_indicator;
	}

	public void setFreq_all_indicator(String freq_all_indicator) {
		this.freq_all_indicator = freq_all_indicator;
	}

	public String getFreq_imaginative_metaphor() {
		return freq_imaginative_metaphor;
	}

	public void setFreq_imaginative_metaphor(String freq_imaginative_metaphor) {
		this.freq_imaginative_metaphor = freq_imaginative_metaphor;
	}

	public String getFreq_imaginative_indicator() {
		return freq_imaginative_indicator;
	}

	public void setFreq_imaginative_indicator(String freq_imaginative_indicator) {
		this.freq_imaginative_indicator = freq_imaginative_indicator;
	}

	public String getFreq_informative_metaphor() {
		return freq_informative_metaphor;
	}

	public void setFreq_informative_metaphor(String freq_informative_metaphor) {
		this.freq_informative_metaphor = freq_informative_metaphor;
	}

	public String getFreq_informative_indicator() {
		return freq_informative_indicator;
	}

	public void setFreq_informative_indicator(String freq_informative_indicator) {
		this.freq_informative_indicator = freq_informative_indicator;
	}

	public String getMetaphor_exists_in_spanishToEnglish() {
		return metaphor_exists_in_spanishToEnglish;
	}

	public void setMetaphor_exists_in_spanishToEnglish(
			String metaphor_exists_in_spanishToEnglish) {
		this.metaphor_exists_in_spanishToEnglish = metaphor_exists_in_spanishToEnglish;
	}

	public String getMetaphor_exists_in_germanToEnglish() {
		return metaphor_exists_in_germanToEnglish;
	}

	public void setMetaphor_exists_in_germanToEnglish(
			String metaphor_exists_in_germanToEnglish) {
		this.metaphor_exists_in_germanToEnglish = metaphor_exists_in_germanToEnglish;
	}

	public String getMetaphor_exists_in_arabicToEnglish() {
		return metaphor_exists_in_arabicToEnglish;
	}

	public void setMetaphor_exists_in_arabicToEnglish(
			String metaphor_exists_in_arabicToEnglish) {
		this.metaphor_exists_in_arabicToEnglish = metaphor_exists_in_arabicToEnglish;
	}

	public String getMetaphor_exists_in_chineseToEnglish() {
		return metaphor_exists_in_chineseToEnglish;
	}

	public void setMetaphor_exists_in_chineseToEnglish(
			String metaphor_exists_in_chineseToEnglish) {
		this.metaphor_exists_in_chineseToEnglish = metaphor_exists_in_chineseToEnglish;
	}

	public String getIndicator_exists_in_spanishToEnglish() {
		return indicator_exists_in_spanishToEnglish;
	}

	public void setIndicator_exists_in_spanishToEnglish(
			String indicator_exists_in_spanishToEnglish) {
		this.indicator_exists_in_spanishToEnglish = indicator_exists_in_spanishToEnglish;
	}

	public String getIndicator_exists_in_germanToEnglish() {
		return indicator_exists_in_germanToEnglish;
	}

	public void setIndicator_exists_in_germanToEnglish(
			String indicator_exists_in_germanToEnglish) {
		this.indicator_exists_in_germanToEnglish = indicator_exists_in_germanToEnglish;
	}

	public String getIndicator_exists_in_arabicToEnglish() {
		return indicator_exists_in_arabicToEnglish;
	}

	public void setIndicator_exists_in_arabicToEnglish(
			String indicator_exists_in_arabicToEnglish) {
		this.indicator_exists_in_arabicToEnglish = indicator_exists_in_arabicToEnglish;
	}

	public String getIndicator_exists_in_chineseToEnglish() {
		return indicator_exists_in_chineseToEnglish;
	}

	public void setIndicator_exists_in_chineseToEnglish(
			String indicator_exists_in_chineseToEnglish) {
		this.indicator_exists_in_chineseToEnglish = indicator_exists_in_chineseToEnglish;
	}

	public String getNum_retranslated_sentences_containing_metaphor() {
		return num_retranslated_sentences_containing_metaphor;
	}

	public void setNum_retranslated_sentences_containing_metaphor(
			String num_retranslated_sentences_containing_metaphor) {
		this.num_retranslated_sentences_containing_metaphor = num_retranslated_sentences_containing_metaphor;
	}

	public String getNum_retranslated_sentences_containing_indicator() {
		return num_retranslated_sentences_containing_indicator;
	}

	public void setNum_retranslated_sentences_containing_indicator(
			String num_retranslated_sentences_containing_indicator) {
		this.num_retranslated_sentences_containing_indicator = num_retranslated_sentences_containing_indicator;
	}

	public String getGutenbergSim_retranslated_spanish() {
		return gutenberg_sim_retranslated_spanish;
	}

	public void setGutenbergSim_retranslated_spanish(String sim_retranslated_spanish) {
		this.gutenberg_sim_retranslated_spanish = sim_retranslated_spanish;
	}

	public String getGutenbergSim_retranslated_german() {
		return gutenberg_sim_retranslated_german;
	}

	public void setGutenbergSim_retranslated_german(String sim_retranslated_german) {
		this.gutenberg_sim_retranslated_german = sim_retranslated_german;
	}

	public String getGutenbergSim_retranslated_arabic() {
		return gutenberg_sim_retranslated_arabic;
	}

	public void setGutenbergSim_retranslated_arabic(String sim_retranslated_arabic) {
		this.gutenberg_sim_retranslated_arabic = sim_retranslated_arabic;
	}

	public String getGutenbergSim_retranslated_chinese() {
		return gutenberg_sim_retranslated_chinese;
	}

	public void setGutenbergSim_retranslated_chinese(String sim_retranslated_chinese) {
		this.gutenberg_sim_retranslated_chinese = sim_retranslated_chinese;
	}

	public String getWiki_sim_retranslated_spanish() {
		return wiki_sim_retranslated_spanish;
	}

	public void setWiki_sim_retranslated_spanish(
			String wiki_sim_retranslated_spanish) {
		this.wiki_sim_retranslated_spanish = wiki_sim_retranslated_spanish;
	}

	public String getWiki_sim_retranslated_german() {
		return wiki_sim_retranslated_german;
	}

	public void setWiki_sim_retranslated_german(
			String wiki_sim_retranslated_german) {
		this.wiki_sim_retranslated_german = wiki_sim_retranslated_german;
	}

	public String getWiki_sim_retranslated_arabic() {
		return wiki_sim_retranslated_arabic;
	}

	public void setWiki_sim_retranslated_arabic(
			String wiki_sim_retranslated_arabic) {
		this.wiki_sim_retranslated_arabic = wiki_sim_retranslated_arabic;
	}

	public String getWiki_sim_retranslated_chinese() {
		return wiki_sim_retranslated_chinese;
	}

	public void setWiki_sim_retranslated_chinese(
			String wiki_sim_retranslated_chinese) {
		this.wiki_sim_retranslated_chinese = wiki_sim_retranslated_chinese;
	}

	public String getFamiliarity_metaphor() {
		return familiarity_metaphor;
	}

	public void setFamiliarity_metaphor(String familiarity_metaphor) {
		this.familiarity_metaphor = familiarity_metaphor;
	}

	public String getFamiliarity_indicator() {
		return familiarity_indicator;
	}

	public void setFamiliarity_indicator(String familiarity_indicator) {
		this.familiarity_indicator = familiarity_indicator;
	}

	public String getDiff_familiarity() {
		return diff_familiarity;
	}

	public void setDiff_familiarity(String diff_familiarity) {
		this.diff_familiarity = diff_familiarity;
	}

	public String getAbs_diff_familiarity() {
		return abs_diff_familiarity;
	}

	public void setAbs_diff_familiarity(String abs_diff_familiarity) {
		this.abs_diff_familiarity = abs_diff_familiarity;
	}

	public String getAvg_familiarity() {
		return avg_familiarity;
	}

	public void setAvg_familiarity(String avg_familiarity) {
		this.avg_familiarity = avg_familiarity;
	}

	public String getMetaphor_macro_avg_familiarity() {
		return metaphor_macro_avg_familiarity;
	}

	public void setMetaphor_macro_avg_familiarity(
			String metaphor_macro_avg_familiarity) {
		this.metaphor_macro_avg_familiarity = metaphor_macro_avg_familiarity;
	}

	public String getIndicator_macro_avg_familiarity() {
		return indicator_macro_avg_familiarity;
	}

	public void setIndicator_macro_avg_familiarity(
			String indicator_macro_avg_familiarity) {
		this.indicator_macro_avg_familiarity = indicator_macro_avg_familiarity;
	}

	public String getAmbiguity_metaphor() {
		return ambiguity_metaphor;
	}

	public void setAmbiguity_metaphor(String ambiguity_metaphor) {
		this.ambiguity_metaphor = ambiguity_metaphor;
	}

	public String getAmbiguity_indicator() {
		return ambiguity_indicator;
	}

	public void setAmbiguity_indicator(String ambiguity_indicator) {
		this.ambiguity_indicator = ambiguity_indicator;
	}

	public String getDiff_ambiguity() {
		return diff_ambiguity;
	}

	public void setDiff_ambiguity(String diff_ambiguity) {
		this.diff_ambiguity = diff_ambiguity;
	}

	public String getAbs_diff_ambiguity() {
		return abs_diff_ambiguity;
	}

	public void setAbs_diff_ambiguity(String abs_diff_ambiguity) {
		this.abs_diff_ambiguity = abs_diff_ambiguity;
	}

	public String getAvg_ambiguity() {
		return avg_ambiguity;
	}

	public void setAvg_ambiguity(String avg_ambiguity) {
		this.avg_ambiguity = avg_ambiguity;
	}

	public String getMetaphor_macro_avg_ambiguity() {
		return metaphor_macro_avg_ambiguity;
	}

	public void setMetaphor_macro_avg_ambiguity(
			String metaphor_macro_avg_ambiguity) {
		this.metaphor_macro_avg_ambiguity = metaphor_macro_avg_ambiguity;
	}

	public String getIndicator_macro_avg_ambiguity() {
		return indicator_macro_avg_ambiguity;
	}

	public void setIndicator_macro_avg_ambiguity(
			String indicator_macro_avg_ambiguity) {
		this.indicator_macro_avg_ambiguity = indicator_macro_avg_ambiguity;
	}

	public String getMetaphor_bow_features() {
		return metaphor_bow_features;
	}

	public void setMetaphor_bow_features(String metaphor_bow_features) {
		this.metaphor_bow_features = metaphor_bow_features;
	}

	public String getIndicator_bow_features() {
		return indicator_bow_features;
	}

	public void setIndicator_bow_features(String indicator_bow_features) {
		this.indicator_bow_features = indicator_bow_features;
	}

	public String getMetaphor_sim_features() {
		return metaphor_sim_features;
	}

	public void setMetaphor_sim_features(String metaphor_sim_features) {
		this.metaphor_sim_features = metaphor_sim_features;
	}

	public String getIndicator_sim_features() {
		return indicator_sim_features;
	}

	public void setIndicator_sim_features(String indicator_sim_features) {
		this.indicator_sim_features = indicator_sim_features;
	}

	public String getMetaphor_prob_features() {
		return metaphor_prob_features;
	}

	public void setMetaphor_prob_features(String metaphor_prob_features) {
		this.metaphor_prob_features = metaphor_prob_features;
	}

	public String getIndicator_prob_features() {
		return indicator_prob_features;
	}

	public void setIndicator_prob_features(String indicator_prob_features) {
		this.indicator_prob_features = indicator_prob_features;
	}

	public String getTopic_distance() {
		return topic_distance;
	}

	public void setTopic_distance(String topic_distance) {
		this.topic_distance = topic_distance;
	}

	public HashMap<String, String> getMetaphor_prob_features_by_key() {
		return metaphor_prob_features_by_key;
	}

	public void setMetaphor_prob_features_by_key(
			HashMap<String, String> metaphor_prob_features_by_key) {
		this.metaphor_prob_features_by_key = metaphor_prob_features_by_key;
	}

	public HashMap<String, String> getIndicator_prob_features_by_key() {
		return indicator_prob_features_by_key;
	}

	public void setIndicator_prob_features_by_key(
			HashMap<String, String> indicator_prob_features_by_key) {
		this.indicator_prob_features_by_key = indicator_prob_features_by_key;
	}

	public HashMap<String, String> getProb_metaphor_in_best_indicator_topic() {
		return prob_metaphor_in_best_indicator_topic;
	}

	public void setProb_metaphor_in_best_indicator_topic(
			HashMap<String, String> prob_metaphor_in_best_indicator_topic) {
		this.prob_metaphor_in_best_indicator_topic = prob_metaphor_in_best_indicator_topic;
	}

	public HashMap<String, String> getProb_indicator_in_best_metaphor_topic() {
		return prob_indicator_in_best_metaphor_topic;
	}

	public void setProb_indicator_in_best_metaphor_topic(
			HashMap<String, String> prob_indicator_in_best_metaphor_topic) {
		this.prob_indicator_in_best_metaphor_topic = prob_indicator_in_best_metaphor_topic;
	}

	public HashMap<String, String> getMetaphor_embeddings_by_key() {
		return metaphor_embeddings_by_key;
	}

	public void setMetaphor_embeddings_by_key(
			HashMap<String, String> metaphor_embeddings_by_key) {
		this.metaphor_embeddings_by_key = metaphor_embeddings_by_key;
	}

	public HashMap<String, String> getIndicator_embeddings_by_key() {
		return indicator_embeddings_by_key;
	}

	public void setIndicator_embeddings_by_key(
			HashMap<String, String> indicator_embeddings_by_key) {
		this.indicator_embeddings_by_key = indicator_embeddings_by_key;
	}

	public String getMetaphorEmbeddings() {
		return metaphorEmbeddings;
	}

	public void setMetaphorEmbeddings(String metaphorEmbeddings) {
		this.metaphorEmbeddings = metaphorEmbeddings;
	}

	public String getIndicatorEmbeddings() {
		return indicatorEmbeddings;
	}

	public void setIndicatorEmbeddings(String indicatorEmbeddings) {
		this.indicatorEmbeddings = indicatorEmbeddings;
	}

	public HashMap<String, String> getEmbeddingCosineSim() {
		return embeddingCosineSim;
	}

	public void setEmbeddingCosineSim(HashMap<String, String> embeddingCosineSim) {
		this.embeddingCosineSim = embeddingCosineSim;
	}

	public String getCosineSim() {
		return cosineSim;
	}

	public void setCosineSim(String cosineSim) {
		this.cosineSim = cosineSim;
	}

	public String getSim_dot() {
		return sim_dot;
	}

	public void setSim_dot(String sim_dot) {
		this.sim_dot = sim_dot;
	}

	public String getProb_dot() {
		return prob_dot;
	}

	public void setProb_dot(String prob_dot) {
		this.prob_dot = prob_dot;
	}

	public String getHighest_num_synsets() {
		return highest_num_synsets;
	}

	public void setHighest_num_synsets(String highest_num_synsets) {
		this.highest_num_synsets = highest_num_synsets;
	}

	public String getLowest_num_synsets() {
		return lowest_num_synsets;
	}

	public void setLowest_num_synsets(String lowest_num_synsets) {
		this.lowest_num_synsets = lowest_num_synsets;
	}

	public String getAvg_num_synsets() {
		return avg_num_synsets;
	}

	public void setAvg_num_synsets(String avg_num_synsets) {
		this.avg_num_synsets = avg_num_synsets;
	}

	public String getAbs_diff_num_synsets() {
		return abs_diff_num_synsets;
	}

	public void setAbs_diff_num_synsets(String abs_diff_num_synsets) {
		this.abs_diff_num_synsets = abs_diff_num_synsets;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
