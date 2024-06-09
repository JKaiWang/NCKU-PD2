import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;


class TFIDFCalculator{
    
    public static void main(String[] args) {
        String filename = args[0];
        String testCase = args[1];
        TF_IDF tf_idf = new TF_IDF();
        tf_idf.Store_Words_To_Trie(filename);

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

class TF_IDF{

    public static Trie[] trie = new Trie[60000];
    public static Trie word_for_text_count = new Trie(); // 用來判斷這個文本中有沒有這個詞

    public static int[] TotalNumber = new int[60000];
    public static int Text_Number = 0;

    public static void Store_Words_To_Trie(String fileName){
        try{
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            List<String> used = new ArrayList<>();
            int LineCount = 0;
            String line ="";
            while((line = bf.readLine()) != null){
                if(LineCount ==0 ){
                    trie[Text_Number] = new Trie();
                }
                line = line.toLowerCase().replaceAll("[^a-z]", " ");
                line = line.replaceAll("\n", " ");
                String[] Token = line.split("\s+");
                for(String token :Token){
                    trie[Text_Number].insert(token);
                    TotalNumber[Text_Number]++;

                    if(!used.contains(token)){
                        word_for_text_count.insert(token);
                        used.add(token);
                    }
                }
                LineCount++;
                if(LineCount == 5){
                    LineCount = 0;
                    Text_Number++;
                    used.clear();
                }
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<Double> TF(String testCase){
        ArrayList<Double> output = new ArrayList<>();
        try {
            BufferedReader testCaseBufferReader = new BufferedReader(new FileReader(testCase));
            String[] Word = testCaseBufferReader.readLine().split("\s");
            String[] Number = testCaseBufferReader.readLine().split("\s");
            for(int i=0 ; i<Word.length ; i++){
                String word = Word[i];
                String number = Number[i];

                int count = trie[Integer.valueOf(number)].searchNum(word);
                int totalcount = TotalNumber[Integer.valueOf(number)];


                double tf = (double)count / (totalcount-5);
                output.add(tf);
            }
            testCaseBufferReader.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(double db : output){
            System.out.println("TF: "+db);
        }
        return output;
    }

    public static ArrayList<Double> IDF(String testCase){
        ArrayList<Double> output = new ArrayList<>();
        try {
            BufferedReader testCaseBufferReader = new BufferedReader(new FileReader(testCase));
            String[] Word = testCaseBufferReader.readLine().split("\s+");
            for(int i=0 ; i<Word.length ; i++){
                String word = Word[i];
                int count = word_for_text_count.searchNum(word);
                int totalcount = Text_Number;

                double tf = Math.log((double)totalcount / count);
                output.add(tf);
                
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        for(double db : output){
            System.out.println("IDF: "+db);
        }
        return output;
    }
}


class TrieNode {
    TrieNode[] children = new TrieNode[26];
    boolean isEndOfWord = false;
    int count =0;
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
        node.count+=1;
    }

    // 搜尋 Trie 中是否存在該單詞
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

    public int searchNum(String word){  
        TrieNode node  = root;
        for(char c : word.toCharArray()){
            node = node.children[c - 'a'];
            if (node == null) {
                return 0;
            }
        }
        return node.count ;
    }
}