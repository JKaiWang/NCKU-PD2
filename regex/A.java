import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class A {
    public static void main(String[] args) {
		    // 讀取文件
        if (args.length == 0) {
            System.err.println("請輸入mermaid檔案名稱");
            return;
        }
        String fileName = args[0];
        String mermaidCode = "";
        
       
        FileReader mermaidCodeReader = new FileReader();
            mermaidCode=mermaidCodeReader.read(fileName);
            mermaidCode = Parser.splitByClass(mermaidCode);
            mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
            ArrayList<String> nameList = new ArrayList<>();
            
                
            while (mermaidCode!=null){
                //tem.out.println("1");
                
                   // 把空格用掉
                for(int a = 0; a < mermaidCode.length(); a++){
                    if(mermaidCode.charAt(a)!=' '&&mermaidCode.charAt(a)!='\n'){
                        mermaidCode=mermaidCode.substring(a);
                        break;
                    }
                }
             
             //把classDiagram用掉
            if(mermaidCode.indexOf("classDiagram")==0){
                
                for(int a=0;a<mermaidCode.length();a++){
                    if(mermaidCode.charAt(a)=='\n'){
                        
                    mermaidCode=mermaidCode.substring(a+1);
                    
                    break;
                    }
                }
            }

            //把空格用掉again
            for(int a = 0; a < mermaidCode.length(); a++){
                if(mermaidCode.charAt(a)!=' '&&mermaidCode.charAt(a)!='\n'){
                    mermaidCode=mermaidCode.substring(a);
                    break;
                }
            }
            //產生一個java檔
            if(mermaidCode.indexOf("class")==0){
                int i=0;
                while(mermaidCode.charAt(i)!='\n'&&mermaidCode.charAt(i)!='{') i++;
                String nam="";
                if(mermaidCode.charAt(i-1)==' ') nam=mermaidCode.substring(6,i-1);
                else nam=mermaidCode.substring(6,i);
                
                String name=nam+".java";
                File file = new File(name);
                 nameList.add(nam);
                
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write("public class "+nam+" {\n");
            }
            
            if(mermaidCode.charAt(i)=='{') mermaidCode=mermaidCode.substring(i+2);
            else mermaidCode=mermaidCode.substring(i+1);
                continue;
            }

            else {
              
                for(int j = 0; j < nameList.size(); j++){
                        //判斷是否為此檔案之資料
                if(mermaidCode.indexOf(nameList.get(j))!=-1){
                    //把title用掉
                    mermaidCode=mermaidCode.substring((nameList.get(j)).length()+mermaidCode.indexOf(nameList.get(j))-1);
                 //第一個不等於空格跟冒號的字符開始算，判斷是public還是private
                    for(int b = 0; b < mermaidCode.length(); b++){
                        if(mermaidCode.charAt(b)!=' '&&mermaidCode.charAt(b)!=':'){
                         //  System.out.println("pppp");
                            String name=nameList.get(j)+".java";
                            if(mermaidCode.charAt(b)=='+'){
                                try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                    bw.write("    public ");
                                }
                            }
                            else if(mermaidCode.charAt(b)=='-'){
                                try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                    bw.write("    private ");
                                }
                            }
                            String thing;
                            if(mermaidCode.indexOf('\n')!=-1){
                                thing=mermaidCode.substring((b+1),mermaidCode.indexOf('\n'));
                                if(thing.charAt(thing.length()-1)==' ')thing=thing.substring(0,thing.length()-1);
                            }
                            else thing=mermaidCode.substring((b+1));
                            if(thing.indexOf(')')==-1){
                                try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                    bw.write(thing+";\n");
                                break;
                                }
                            }
                            else {
                                String title=thing.substring(0,thing.indexOf(')')+1);
                                
                                String[] words=thing.split(" ");
                                    List<String> wordsList = new ArrayList<>();
                                    for (String fruit : words) {
                                        if(!fruit.isEmpty()&&fruit.indexOf(":")==-1)wordsList.add(fruit);
                                        //System.out.println(wordsList);
                                    }///
                                    int lastIndex = wordsList.size() - 1;
                                        String lastFruit = wordsList.get(lastIndex);
                                        if(lastFruit.indexOf("int")==-1&&lastFruit.indexOf("boolean")==-1&&lastFruit.indexOf("String")==-1){
                                        try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                            bw.write("void ");}
                                        }  
                                        else {
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(lastFruit+" ");
                                            }
                                        }
                                        
                                        if(title.indexOf("set")!=-1){
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(title+" {\n");
                                            } 
                                            title=title.toLowerCase();
                                            String str = wordsList.get(wordsList.size() - 2);
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write("        this."+str+" = "+str+";\n    }"+'\n');
                                            } 
                                        }
                                        else if(title.indexOf("get")!=-1){
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(title+" {\n");
                                            } 
                                            title=title.toLowerCase();
                                           // System.out.println(wordsList.get(0));
                                            String str = ((wordsList.get(0)).substring(3)).toLowerCase();
        
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write("        return "+str+";\n    }"+'\n');
                                            }
                                        }
                                        else if(lastFruit.indexOf("int")!=-1){
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(title+" ");
                                            } 
                                            title=title.toLowerCase();
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write("{return 0;}\n");
                                            }
                                        }
                                        else if(lastFruit.indexOf("String")!=-1){
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(title+" ");
                                            } 
                                            title=title.toLowerCase();
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write( "{return "+"\"\""+";}\n");
                                            }
                                        }
                                        else if(lastFruit.indexOf("boolean")!=-1){
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(title+" ");
                                            } 
                                            title=title.toLowerCase();
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write( "{return false;}\n");
                                            }
                                        }
                                        else {
                                            
                                            try (BufferedWriter bw = new BufferedWriter(new FileWriter(name, true))) {
                                                bw.write(title+" {;}\n");
                                            } 
                                            
                                        }
                                        //mermaidCode=mermaidCode.substring(mermaidCode.indexOf('\n'));
                                        break;
                            
                            }
                        }
                        }
                    
                }   
                }
            }
            if(mermaidCode.indexOf('\n')==-1) {
                break;
            }
            else{
                String[] sym=mermaidCode.split("\n");
                int u=0;
                for(int i=0;i<nameList.size();i++){
                    if (sym.length==1)break;
                    else if(sym[0].indexOf("class")==-1&&sym[0].indexOf(nameList.get(i))==-1)mermaidCode=mermaidCode.substring(mermaidCode.indexOf('\n'));
                    for(int a=0;a<sym[1].length();a++){
                        if(sym[1].charAt(a)!=' '&& sym[1].charAt(a)!='\n') u=1;   
                    }
                    if(sym[1].indexOf("class")!=-1 || u==0){
                        mermaidCode=mermaidCode.substring(mermaidCode.indexOf('\n'));
                    }
                }
            }
            
        }
        for(int j = 0; j < nameList.size(); j++){
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(nameList.get(j)+".java", true))) {
                bw.write("}\n");
            }
        }     
    }
        catch (IOException e) {
            System.err.println("無法讀取文件 " + fileName);
            e.printStackTrace();
            return;
        }
    }
}
class FileReader {
    public String read(String fileName) {
        String mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
        }
        catch (IOException e) {
            System.err.println("無法讀取文件 " + fileName);
            e.printStackTrace();
        }return mermaidCode;
    }
    
}
class Parser {
		public static String splitByClass(String input) {
			return input;
		}
}
