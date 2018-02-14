# metaphor-novelty-scoring

This directory contains the Java source code used to extract metaphor novelty features for our papers:

Natalie Parde and Rodney D. Nielsen. Exploring the Terrain of Metaphor Novelty: A Regression-based Approach for Automatically Scoring Metaphors. To appear in the *Proceedings of the Thirty-Second AAAI Conference on Artificial Intelligence (AAAI-18)*. New Orleans, Louisiana, February 2-7, 2018.

and

Natalie Parde and Rodney D. Nielsen. A Corpus of Metaphor Novelty Scores for Syntactically-Related Word Pairs. To appear in the *Proceedings of the 11th International Conference on Language Resources and Evaluation (LREC 2018)*. Miyazaki, Japan, May 7-12, 2018.

It also contains a precompiled JAR file, `featureGenerator.jar`, that you can use to extract the full range of features associated with the AAAI paper if you would prefer not to mess around with the code.  This README is divided into several sections:
1. **Resources:** Describes how you can download the various resources used to compute features.  You'll need to download these regardless of whether you use `featureGenerator.jar` or recompile the source code yourself.
2. **Running `featureGenerator.jar`:** Describes how to run the precompiled JAR file to extract features.
3. **Modifying the Source Code in Eclipse:** Describes how to set up the feature extraction source code in an Eclipse project so you can play around with it and recompile it yourself.

--------------------------

## Resources

### Brysbaert Concreteness Ratings
Download the following file: http://crr.ugent.be/papers/Concreteness_ratings_Brysbaert_et_al_BRM.txt

Place it in the following subdirectory: feature-extraction/resources/brysbaert_concreteness_ratings/

Additional information about the Brysbaert concreteness ratings can be found in the following paper:

Brysbaert, M., Warriner, A.B., & Kuperman, V. (2014). Concretness ratings for
40 thousand generally known English word lemmas. Behavior Research Methods, 46,
904-911.

### SentiWordNet
Download the zipped file here: https://drive.google.com/file/d/0B0ChLbwT19XcOVZFdm5wNXA5ODg/view

Unpack it and copy home/swn/www/admin/dump/SentiWordNet_3.0.0_20130122.txt to feature-extraction/resources/sentiwordnet/.

Additional information about SentiWordNet can be found here: http://sentiwordnet.isti.cnr.it/.

### MRC Pyscholinguistic Database
The link to the MRC dataset file I used in this work is no longer active.  However, you can find the file at:
https://web.archive.org/web/20160208104821/http://www.psych.rl.ac.uk:80/gl_rate.wds

Download it, save it as mrcpd_complete_dataset.wds, and place it in feature-extraction/resources/mrc_plus/.

Additional information about the MRC Psycholinguistic Database can be found here: http://websites.psychology.uwa.edu.au/school/MRCDatabase/uwa_mrc.htm

### MRC+ Expanded Imageability and Concreteness Scores
Getting the expanded MRC+ data (used to get imageability and some concreteness scores) is a little trickier. If you're affiliated with UNT's HiLT Lab, contact me and I can direct you to our copy. If you're not, email Tomek Strzalkowski at SUNY Albany (tomek@albany.edu) for access.

Once you have the data, copy or move english_mrc.txt to feature-extraction/resources/mrc_plus/.

Additional information about the expanded MRC datasets can be found in the following paper:

Liu, T., Cho, K., Broadwell, G. A., Shaikh, S., Strzalkowski, T., Lien, J., Taylor, S., Feldman, L., Yamrom, B., Webb, N., Boz, U., Cases, I., and Lin, C. (2014). Automatic Expansion of the MRC Psycholinguistic Database Imageability Ratings. In the *Proceedings of the Ninth International Conference on Language Resources and Evaluation (LREC-2014)*, (pp. 2800-2805).

### WordNet
To use the Java WordNet Library (JWNL), you'll need to download the WordNet database files here:
http://wordnetcode.princeton.edu/wn3.1.dict.tar.gz

Unpack them, and take note of the path to the dict/ folder. Then, open properties.xml and scroll down to line 41. Replace /local2/WordNet-3.0/dict with the path to the dict/ folder on your own computer.

You can find more details about WordNet in the papers listed here:
https://wordnet.princeton.edu/wordnet/citing-wordnet/

### Google News Embeddings
Download the pretrained Google News embeddings here:
https://drive.google.com/file/d/0B7XkCwpI5KDYNlNUTTlSS21pQmM/edit?usp=sharing

Don't unpack them; just move the downloaded GoogleNews-vectors-negative300.bin.gz over to feature-extraction/resources/embeddings/.

The official website for the embeddings is here:
https://code.google.com/archive/p/word2vec/

-----------------------------------------------------------------

## Running `featureGenerator.jar`

### Installing Required Packages
To run `featureGenerator.jar`, you'll have to install a number of packages on which it relies.  These include:
- Stanford CoreNLP: https://stanfordnlp.github.io/CoreNLP/
- DeepLearning4J: https://deeplearning4j.org/
- JWNL: https://sourceforge.net/projects/jwordnet/
- MySQL Java Connector: https://dev.mysql.com/downloads/connector/j/

You can install these easily with Maven using the `pom.xml` included in this repository.  If you don't have Maven installed on your computer, follow the installation instructions here: https://maven.apache.org/install.html before continuing any further.

Once you're ready to use Maven, navigate to the directory containing `pom.xml` and type the following at your command line:
`mvn install`

This will automatically install all of the packages required for this project to a directory on your computer (probably .m2/repository, unless you specified otherwise).  It will also create feature-extraction/target/, and store a generated snapshot of the code there.  Depending on your internet speed, the download process may take a couple of minutes.

Once the download process has finished, create a new directory in feature-extraction/target/, and name it featureGenerator_lib/.  Then, move or copy the JAR files downloaded by Maven to this location.  A quick way to do this (on UNIX-based systems) is by using the following command:
```
find ~/.m2/repository/ -name \*.jar -mtime -1 -exec cp {} featureGenerator_lib \;
```
That command will search all subdirectories of ~/.m2/repository/ for JAR files created in the past day, and copy them to the new featureGenerator_lib/ directory you just created.  Make sure you're in feature-extraction/ when you run the command.

### A Note About MySQL
To compute PMI features, this program requires access to *either* a database containing Google's Web1T corpus (https://catalog.ldc.upenn.edu/LDC2006T13) *or* a directory of pre-computed, serialized frequency and PMI files.  I have provided the latter of these two in feature-extraction/resources/serialized_pmis/.  So, if you just want to recompute features for instances from our dataset, there should be no need for any database access (the program will throw a warning saying it couldn't connect to the database, but this won't impact anything).  However, if you want to compute features for instances from other datasets, you will need to set up your own Web1T database.  One of the source files, `Web1TDatabase.java`, includes functions for doing this, but you'll have to get a copy of Web1T either from the link above or from your home institution (if you're affiliated with UNT's HiLT Lab, contact me).

### Running the JAR File
After you've downloaded the third-party resources and required libraries, you're ready to run the JAR file!  Make sure you're in the feature-extraction/ directory, and type:
```
java -jar featureGenerator.jar <name of input CSV file>
```

For example:
```
java -jar featureGenerator.jar gold_metaphor_novelty.csv
```

The input CSV file should contain two columns, which we identify in the header row as *ID* and *Label* (naming the columns *ID* and *Label* is not required, but having a header row is).  The first column should have a unique identifier for each instance, and the second column should have the metaphor novelty score associated with that instance.  You can refer to `gold_metaphor_novelty.csv` as an example if you need to generate your own CSV files; if you just want to use our dataset, you can use the provided CSV files `predictions_Folds_1234.csv` (our training set) and `gold_metaphor_novelty.csv` (our test set).

---------------------------------------------------------------------

## Modifying the Source Code in Eclipse

### Downloading Eclipse
These instructions describe how to modify the source code using Eclipse (you're welcome to use any other IDE of your choice, but if you do, some of these instructions won't be particularly relevant to you).  If you don't currently have Eclipse installed on your computer, you can download it here: https://www.eclipse.org/

The installer will walk you through the installation process.

### Importing the Source Code into Eclipse
Open Eclipse and click on File -> New -> Java Project in the menu bar at the top of the screen.

On the window that appears, unselect "Use default location."

You should now be able to edit the "Location:" box.  Click on "Browse..." and navigate to and select feature-extraction/.

At the bottom of the screen, select "Next."

On the next screen that appears, enter "feature-extraction/bin" as the "Default output folder."  Then click "Finish."

A warning screen might appear, asking you if you want to remove all generated resources from the old location.  If it does, click "Yes."

### Recompiling the Source Code
First, use Maven to install all of the required libraries by right-clicking on the project's pom.xml in the Eclipse Package Explorer and then clicking Run As -> Maven install.  If you've already used the `featureGenerator` JAR file (see instructions above), this will run extremely quickly because you've already installed the libraries.  If you haven't already used the JAR file, this may take a few minutes to complete.

Next, right-click on NNFeatureGenerator.java in the Eclipse Package Explorer and then click Run As -> Java Application.

*Note: If you came here to run the benchmark established in Parde and Nielsen (LREC 2018) instead of the full approach evaluated in Parde and Nielsen (AAAI 2018), right-click on LRECBaseline.java in the Eclipse Package Explorer and then click Run As -> Java Application.  LRECBaseline.java does not require all of the resources listed above!  If you are only interested in running this version, you will only need to download the Google News Embeddings.*

That's all there is to it!  You'll be able to see the program output in the Eclipse Console.

### Source Code Guide
A quick guide to some of the lines of source code that you may wish to modify is included below:

#### NNFeatureGenerator.java
Line 241: The location where you have stored a pre-generated file containing, for each word in your dataset, the probability that the word belongs to each of 100 topics generated from Project Gutenberg.  If you're using our dataset, leave this line as-is (I've included the pre-generated file in feature-extraction/resources/vuamc_data/).

Line 242: The location where you have stored a pre-generated file containing, for each word in your dataset, the probability that the word belongs to each of 100 topics generated from English Wikipedia.  If you're using our dataset, leave this line as-is (I've included the pre-generated file in feature-extraction/resources/vuamc_data/).

Line 822: The location where you have stored the Brysbaert concreteness ratings.

Line 823: The location where you have stored SentiWordNet.

Line 824: The location where you have stored the MRC Psycholinguistic Database.

Line 825: The location where you have stored the expanded MRC+ Database.

Line 826: The location where you have stored the pretrained Google News embeddings.

Line 836: The name of your input file.  The input file should be stored in the base directory for this project (feature-extraction/).

#### Web1TDatabase.java
Line 50: The location and name of your database containing the Google Web1T dataset.

Line 51: Your MySQL username.

Line 52: Your MySQL password.

----------------------

## That's All!
If you have any questions about running the code, feel free to contact me on here or at: natalie.parde@unt.edu.
