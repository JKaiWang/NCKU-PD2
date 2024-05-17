import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Retry {
    public static void main(String[] args) {
		    // 讀取文件
        if (args.length == 0) {
            System.err.println("請輸入檔案名稱");
            return;
        }
        String fileName = args[0];
        System.out.println("File name: " + fileName);
        String mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
        }
        catch (IOException e) {
            System.err.println("無法讀取文件 " + fileName);
            e.printStackTrace();
            return;
        }
        
        
        // 寫入文件
        try {
            String output = "Example.java";
            String content = "this is going to be written into file";
            File file = new File(output);
            if (!file.exists()) {
                file.createNewFile();
            }
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(content);
            }
            System.out.println("Java class has been generated: " + output);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class FileReader{
    public static void Read(String mermaidcode){
        List<String> classList = new ArrayList<>();
        HashMap<String, List<String>> sites = new HashMap<>();
        String arr[]  = mermaidcode.split("\n");
        for(String str : arr){
            classList.add(str);
        }
        for(String currentString : classList){
            if(currentString.contains("classDiagram")){
                continue;
            }
            else if(currentString.contains("class ")){ 
                String classArray[] = currentString.split("\s+");
                String className = classArray[classArray.length-1];
                List<String> ls = new ArrayList<>();
                ls.add("public class "+className+" {\n");
                sites.put(className , ls);
            }
            else if(currentString.contains("(")){
                if(currentString.contains("get")){ //get method 
                    if(currentString.contains("+")){
                        //最後 
                        //sites.get({key}).add({整理好的東西});
                    }
                    else{

                    }
                }
                if(currentString.contains("set")){//set method 
                    if(currentString.contains("+")){//public 

                    }
                    else{//private

                    }
                }
                else{//純method
                    if(currentString.contains("+")){

                    }
                    else{

                    }
                }
            }
            else{
                if(currentString.contains("+")){//plubic attribute

                }
                else{

                }
            }
        }
    }
}