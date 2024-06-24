import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.management.openmbean.ArrayType;

class MyTFIDFCalculator{
    public static void main(String[] args) {
        String filename = args[0];
        String testCase = args[1];
        TF_IDF tf_idf = new TF_IDF();
        tf_idf.storeWordToTrie(filename);

        List<Double> tf = tf_idf.TF(testCase);
        List<Double> idf = tf_idf.IDF(testCase);
        double[] finalAns = new double[tf.size()];
        int i=0;
        int j=0;
        for(double db : tf){
            finalAns[i] = db;
            i++;
        }
        for(double db : idf){
            finalAns[j] = finalAns[j]*db;
            j++;
        }
        // for(double db : finalAns){
        //     System.out.println(String.format("%.5f", db));
        // }
        StringBuffer sb = new StringBuffer();
        for(double db : finalAns){
            sb.append(String.format("%.5f", db));
            sb.append(" ");
        }
        String finalstr = sb.substring(0, sb.toString().length()-1).toString();
        try {
            FileWriter fw = new FileWriter(new File("output.txt"));
            
            //System.out.println(finalstr);
            fw.write(finalstr);
            fw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
}

class TrieNode{
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int count =0;
}

class Trie{
    TrieNode root = new TrieNode();

    public void insert(String word){
        TrieNode node = root;

        for(char c : word.toCharArray()){
            if(node.children[c-'a'] == null){
                node.children[c-'a'] = new TrieNode();
            }
            node = node.children[c-'a'];
        }

        node.isEndOfWord = true;
        node.count +=1;
    }

    public boolean search(String word){
        TrieNode node = root;
        for(char c : word.toCharArray()){
            node = node.children[c-'a'];
            if(node == null){
                return false;
            }
        }
        return node.isEndOfWord;
    }
    public int serachNum(String word){
        TrieNode node = root;
        for(char c: word.toCharArray()){
            node = node.children[c-'a'];
            if(node == null){
                return 0;
            }
        }
        return node.count;
    }
}

class TF_IDF{
    public static Trie[] trie = new Trie[60000];
    public static Trie wordForTextCount = new Trie();
    public static int[] totalNumber = new int[60000];
    public static int text_Number = 0;

    public static void storeWordToTrie(String fileName){
        try(BufferedReader bf = new BufferedReader(new FileReader(fileName))){
            Set<String> used = new HashSet<>();
            int lineCount = 0;
            String line = "";
            while((line = bf.readLine())!= null){
                if(lineCount ==0){
                    trie[text_Number] = new Trie();
                }
                line = line.toLowerCase().replace("[^a-z]", " ");
                line = line.replaceAll("\n", " ");
                String[] Token = line.split("\s+");
                for(String token : Token){
                    trie[text_Number].insert(token);
                    totalNumber[text_Number] ++;
                    if(!used.contains(token)){
                        wordForTextCount.insert(token);
                        used.add(token);
                    }
                }
                lineCount++;
                totalNumber[text_Number] =totalNumber[text_Number]-1;
                if(lineCount ==5){
                    lineCount =0;
                    text_Number++;
                    used.clear();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Double> TF(String testCase){
        ArrayList<Double> output = new ArrayList<>();
        try(BufferedReader bf = new BufferedReader(new FileReader(testCase))) {
            String[] Word = bf.readLine().split("\s");
            String[] Number = bf.readLine().split("\s");
            for(int i=0 ; i<Word.length ;i++){
                String word = Word[i];
                int number = Integer.parseInt(Number[i]);
                int count = trie[number].serachNum(word);
                int totalcount = totalNumber[number];
                double tf = (double)count/ totalcount;
                output.add(tf);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static ArrayList<Double> IDF(String testCase){
        ArrayList<Double> output = new ArrayList<>();
        try(BufferedReader bf = new BufferedReader(new FileReader(testCase))){
            String[] Word = bf.readLine().split(" ");
            String[] Number = bf.readLine().split(" ");
            for(int i=0 ; i<Word.length ; i++){
                String word = Word[i];
                int number = Integer.parseInt(Number[i]);
                int count = wordForTextCount.serachNum(word);
                int totalcount = text_Number;
                double idf = Math.log((double) totalcount / count);
                output.add(idf);
            }
        }catch (Exception e) {
           e.printStackTrace();
        }
        return output;
    }
}