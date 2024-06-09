import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;

public class YJZFIDFCalculator{
    public static void main(String[] args){
        docsReading read = new docsReading();
        String[] docs ;
        docs = read.parsing(args[0]);
        double size = docs.length;
        int z = 0;
        String[][] docsWords = new String[docs.length][];
        Trie[] tries = new Trie[docs.length];
        for(String doc : docs){
            Trie testWord = new Trie();
            //docs.set(z, docs.get(z).trim());
            docs[z] = docs[z].trim();
            docsWords[z] = docs[z].split("\\s+");
            for(String a : docsWords[z]){
                testWord.insert(a);
            }
            tries[z] = testWord;
            z++;
        }
        String words[] = null;
        String docNum[] = null;
        
        try(BufferedReader br = new BufferedReader(new FileReader(args[1]))){
            String line = br.readLine();
            words = line.split("\\s+");
            line = br.readLine();
            docNum = line.split("\\s+");
        }catch(IOException e){
            e.printStackTrace();
        }
        String[] results = new String[words.length];

        double[] IDFResult = new double[words.length];
        for(int i = 0 ; i<words.length ;i++){
            final int num = Integer.parseInt(docNum[i]);
            final String word = words[i];
            calculator calculate = new calculator(IDFResult);
            double tfResult = calculate.tf(docsWords[num],tries[num], word);
            double idfResult = calculate.idf(tries,word,size,words,i);
            IDFResult[i] = idfResult;
            double tfidf = tfResult * idfResult;
            results[i] = String.format("%.5f", tfidf);
            //System.out.println(result);
        }
        /*for(String a:results){
              System.out.println(a);
          }*/
        String path = "output.txt";
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(path))){
            for(String result :results){
                writer.write(result+" ");
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}

class calculator{
    private double[] IDFResult;

    public calculator(double[] IDFResult) {
        this.IDFResult = IDFResult;
    }
    public double tf(String[] doc,Trie testWord,String word){
        double tfUp = testWord.frequency(word);
        double tfDown = doc.length;
        return (double)tfUp/tfDown;
    }
    public double idf(Trie[] tries,String word,double size,String[] finalWords,int time){
        //System.out.println("文件數 "+idfUp);
        boolean wordExists = false;
        for(int i = 0;i<time;i++){
            double a = IDFResult[i];
            if(finalWords[i].equals(word)){
                //System.out.println(a);
                IDFResult[time] = a;
                wordExists = true;
                break;
            }
        }
        if(!wordExists){
            int count = 0;
            for (Trie trie : tries){
                if(trie.search(word)) count++;
            }
            //System.out.println("idfcount "+count);
            IDFResult[time] = Math.log(size/count);
        }
        return IDFResult[time];
    }
}

class docsReading{
    public String[] parsing(String docsPath){
        List<String> docs = new LinkedList<>();
        try(BufferedReader br = new BufferedReader(new FileReader(docsPath))){
            String line;
            while((line = br.readLine()) != null){
                int index = line.indexOf('\t');
                String numString = (index != -1) ? line.substring(0,index) : line;
                int num = Integer.parseInt(numString);
                line = line.replaceAll("[^a-zA-Z]", " ");
                line = line.toLowerCase();
                line = line.replaceAll("\\s+", " ");
                if(num%5 == 1){
                    docs.add(line);
                }
                else{
                    docs.set((num-1)/5,docs.get((num-1)/5) + line);
                }
            }
            

        }catch(IOException e){
            e.printStackTrace();
        }
        String[] array = docs.toArray(new String[0]);
        return array;
    }

}

class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    double count = 0.0;
}

class Trie {
    TrieNode root = new TrieNode();

    // 插入一個單詞到 Trie
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEndOfWord = true;
        node.count++;
    }

    public double frequency(String word){
        TrieNode node = root;
        for(char c : word.toCharArray()){
            if(node.children[c - 'a']==null){
                return 0;
            }
            node = node.children[c - 'a'];
        }
        //System.out.println("出現次數 "+node.count);
        return node.count;
    }
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            node = node.children[c - 'a'];
            if (node == null) {
                return false;
            }
        }
        return node.isEndOfWord;
    }
}