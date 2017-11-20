# metaphor-novelty-scoring

This directory contains the Java source code used to extract metaphor novelty features for our paper:
Natalie Parde and Rodney D. Nielsen. Exploring the Terrain of Metaphor Novelty: A Regression-based Approach for Automatically Scoring Metaphors. To appear in the Proceedings of the Thirty-Second AAAI Conference on Artificial Intelligence (AAAI-18). New Orleans, Louisiana, February 2-7, 2018.

It also contains a precompiled JAR file, featureGenerator.jar, that you can use to extract features if you would prefer not to mess around with the code.  Regardless of whether you recompile the source code yourself or use featureGenerator.jar, you'll have to download the following resources first:
- Brysbaert Concreteness Ratings
- SentiWordNet
- MRC Psycholinguistic Database
- MRC+ Expanded Imageability and Concreteness Scores
- WordNet
- Pretrained Google News Embeddings

Instructions for doing so are as follows:

## Brysbaert Concreteness Ratings
------------------------------------------------------------
Download the following file: http://crr.ugent.be/papers/Concreteness_ratings_Brysbaert_et_al_BRM.txt

Place it in the following subdirectory: feature-extraction/resources/brysbaert_concreteness_ratings/

Additional information about the Brysbaert concreteness ratings can be found in the following paper:
Brysbaert, M., Warriner, A.B., & Kuperman, V. (2014). Concretness ratings for
40 thousand generally known English word lemmas. Behavior Research Methods, 46,
904-911.

## SentiWordNet
-----------------------------
Download the zipped file here: https://drive.google.com/file/d/0B0ChLbwT19XcOVZFdm5wNXA5ODg/view

Unpack it and copy home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt to feature-extraction/resources/sentiwordnet/.

Additional information about SentiWordNet can be found here: http://sentiwordnet.isti.cnr.it/.

## MRC Pyscholinguistic Database
-----------------------------------------------------------
The link to the MRC dataset file I used in this work is no longer active.  However, you can find the file at:
https://web.archive.org/web/20160208104821/http://www.psych.rl.ac.uk:80/gl_rate.wds

Download it, save it as mrcpd_complete_dataset.wds, and place it in feature-extraction/resources/mrc_plus/.

Additional information about the MRC Psycholinguistic Database can be found here: http://websites.psychology.uwa.edu.au/school/MRCDatabase/uwa_mrc.htm

## MRC+ Expanded Imageability and Concreteness Scores
----------------------------------------------------------------------------------------------------
Getting the expanded MRC+ data (used to get imageability and some concreteness scores) is a little trickier. If you're affiliated with UNT's HiLT Lab, contact me and I can direct you to our copy. If you're not, email Tomek Strzalkowski at SUNY Albany (tomek@albany.edu) for access.

Once you have the data, copy or move english_mrc.txt to feature-extraction/resources/mrc_plus/.

Additional information about the expanded MRC datasets can be found in the following paper:
Liu, T., Cho, K., Broadwell, G. A., Shaikh, S., Strzalkowski, T., Lien, J., ... & Boz, U. (2014). Automatic Expansion of the MRC Psycholinguistic Database Imageability Ratings. In the Proceedings of the Ninth International Conference on Language Resources and Evaluation (LREC-2014), (pp. 2800-2805).
