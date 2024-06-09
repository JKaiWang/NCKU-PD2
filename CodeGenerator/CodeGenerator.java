import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import javax.management.AttributeList;
import java.util.*;


public class CodeGenerator {
    public static void main(String[] args) {
        // 讀取文件*******************************************************************
        if (args.length == 0) {
            System.err.println("請輸入檔案名稱");
            return;
        }
        String fileName = args[0];// 之後要改成String fileName = args[0];
        FileReader filereader = new FileReader();
        //filereader.read(fileName);//return arraylist;
        Writer.set(FileReader.classes(filereader.read(fileName)));
    }
}

class FileReader{
    public static String DetermineDataType(String str){  //決定datetype 要return什麼
        if(str.contains("int")){
            return "return 0";
        }
        else if(str.contains("boolean")){
            return "return false";
        } 
        else if(str.contains("void")){
            return "";
        }
        else if(str.contains("String")){
            return "\"\"";
        }
        else{
            return "";
        }
    }

    public List<String> read(String fileName){ //讀檔讀出來的mermaid把他一行一行變成arraylist
        System.out.println("File name: " + fileName);
        String mermaidCode = "";
        try {
            mermaidCode = Files.readString(Paths.get(fileName));
            // String mermaidCodeArray[] = mermaidCode.split("\n");

        }
        catch (IOException e) {
            System.err.println("無法讀取文件 " + fileName);
            e.printStackTrace();
        }
        // 
        List<String> stringList = new ArrayList<>();
        String[] arr = mermaidCode.split("\n");
        for(int i=0 ; i<arr.length ; i++){
            stringList.add(arr[i]);
        }
        return stringList;
    } 
    
    public static HashMap<String , List<String>> classes(List<String> classList){//把mermaid的arraylist 改成java class 形式的hashmap 
        HashMap<String , List<String>> sites = new HashMap<>(); 
        String dataType[] = {"int","void","boolean","String"};      
        String TAB = "    ";
        String className = ""; 
        String[] Title = new String[100];//class name 
        int classNum=0;
        for(int i=0 ;i<classList.size();i++){//將mermaid轉型成java class 的樣態
            String currentString = classList.get(i);

            if(currentString.contains("classDiagram")){
                continue;
            }

            else if(currentString.contains("class ")){//class 
                int index = currentString.indexOf("class");
                if(index != -1){
                    List<String> ls = new ArrayList<>();
                    className = currentString.substring(index+5).trim();
                    Title[classNum] = className;
                    classNum +=1;
                    //ls.add(className);
                    ls.add("public class "+className+" {\n");
                    sites.put(className, ls);
                }
            }

            else{  // method or attribute

                if(currentString.contains("(")){ //it is method
                
                    if(currentString.contains("get")){//method contains get
                        String methodArray[] = currentString.split("\s+");
                        int x=0;
                        if(methodArray[0] =="") {//要判斷是否有第一種是tab如果是的話split 出來arr[0] 會是空字串
                            x=1;
                        }
                        else {//如果不是split 出來 array[0]會是class的名稱
                            x=0;
                        }
                        if(currentString.contains("+")){ //public
                            String methodDataType = methodArray[methodArray.length-1];
                            
                            int a = currentString.indexOf("+")+1;
                            int b = currentString.indexOf(")")+1;
                            int c = currentString.indexOf("(");
                            String returnname = currentString.substring(a+3, c);
                            String methodName = currentString.substring(a, b);
                            String method = TAB+"public "+methodDataType+" "+methodName+" {\n";
                            sites.get(methodArray[x]).add(method);
                            sites.get(methodArray[x]).add(TAB+TAB+"return "+returnname.toLowerCase()+";\n");
                            sites.get(methodArray[x]).add(TAB+"}\n");


                        }
                        else if(currentString.contains("-")){//private 
                            
                            String methodDataType = methodArray[methodArray.length-1];
                            
                            int a = currentString.indexOf("+")+1;
                            int b = currentString.indexOf(")")+1;
                            int c = currentString.indexOf("(");
                            String returnname = currentString.substring(a+3, c);
                            String methodName = currentString.substring(a, b);
                            String method = TAB+"private "+methodDataType+" "+methodName+" {\n";
                            sites.get(methodArray[1]).add(method);
                            sites.get(methodArray[1]).add(TAB+TAB+"return "+returnname.toLowerCase()+";\n");
                            sites.get(methodArray[1]).add(TAB+"}\n");
                        }
                    }  

                    else if(currentString.contains("set")){//method contains set
                        String methodArray[] = currentString.split("\s+");
                        int x=0;
                        if(methodArray[0] =="") {//要判斷是否有第一種是tab如果是的話split 出來arr[0] 會是空字串
                            x=1;
                        }
                        else {//如果不是split 出來 array[0]會是class的名稱
                            x=0;
                        }
                        if(currentString.contains("+")){
                            String methodDataType = methodArray[methodArray.length-1];
                            int a = currentString.indexOf("+")+1;
                            int b = currentString.indexOf(")")+1;
                            int c = currentString.indexOf("(");
                            String returnname = currentString.substring(a+3, c);
                            String methodName = currentString.substring(a, b);
                            String method = TAB+"public "+methodDataType+" "+methodName+" {\n";
                            sites.get(methodArray[1]).add(method);
                            sites.get(methodArray[1]).add(TAB+TAB+"this."+returnname.toLowerCase()+" = "+returnname.toLowerCase()+";\n");
                            sites.get(methodArray[1]).add(TAB+"}\n");


                        }
                        else if(currentString.contains("-")){
                            String methodDataType = methodArray[methodArray.length-1];
                            int a = currentString.indexOf("+")+1;
                            int b = currentString.indexOf(")")+1;
                            int c = currentString.indexOf("(");
                            String returnname = currentString.substring(a+3, c);
                            String methodName = currentString.substring(a, b);
                            String method = TAB+"private "+methodDataType+" "+methodName+" {\n";
                            sites.get(methodArray[1]).add(method);
                            sites.get(methodArray[1]).add(TAB+TAB+"this."+returnname.toLowerCase()+" = "+returnname.toLowerCase()+";\n");
                            sites.get(methodArray[1]).add(TAB+"}\n");
                        }
                    }

                    else{ //method whithout set or get
                        String methodArray[] = currentString.split("\s+");
                        
                        int x=0;
                        if(methodArray[0] =="") {//要判斷是否有第一種是tab如果是的話split 出來arr[0] 會是空字串
                            x=1;
                        }
                        else {//如果不是split 出來 array[0]會是class的名稱
                            x=0;
                        }
                        if(currentString.contains("+")){
                            String methodDataType = methodArray[methodArray.length-1];
                            //****************** 在這裡喔(判斷末端有無void)*******************************
                            boolean flag=false;
                            for(String data : dataType){ 
                                if(methodDataType.equals(data)){
                                    flag =true; 
                                    break;
                                }
                            }
                            
                            if(flag == false){
                                StringBuilder sb = new StringBuilder();
                                for(String str : methodArray){
                                    sb.append(str);
                                    sb.append(" ");
                                }
                                sb.append("void");
                                String sbstr = sb.toString();
                                
                                methodArray = new String[methodArray.length+1];
                                methodArray=sbstr.split("\s");
                                methodDataType = methodArray[methodArray.length-1];
                            }
                            int a = currentString.indexOf("+")+1;
                            int b = currentString.indexOf(")")+1;
                            String methodName = currentString.substring(a, b);
                            String method = TAB+"public "+methodDataType+" "+methodName+" {"+DetermineDataType(methodDataType)+";}\n";
                            sites.get(methodArray[x]).add(method);
                        }
                        
                        else if(currentString.contains("-")){
                            String methodDataType = methodArray[methodArray.length-1];
                            //*********************** 在這裡喔(判斷末端有無void)**************************
                            boolean flag=false;
                            for(String data : dataType){ 
                                if(methodDataType.equals(data)){
                                    flag =true; 
                                    break;
                                }
                            }
                            
                            if(flag == false){
                                StringBuilder sb = new StringBuilder();
                                for(String str : methodArray){
                                    sb.append(str);
                                    sb.append(" ");
                                }
                                sb.append("void");
                                String sbstr = sb.toString();
                                
                                methodArray = new String[methodArray.length+1];
                                methodArray=sbstr.split("\s");
                                methodDataType = methodArray[methodArray.length-1];
                            }
                            int a = currentString.indexOf("-")+1;
                            int b = currentString.indexOf(")")+1;
                            String methodName = currentString.substring(a, b);
                            String method = TAB+"private "+methodDataType+" "+methodName+" {"+DetermineDataType(methodDataType)+";}\n";
                            sites.get(methodArray[x]).add(method);
                        }

                    }          
                }
                
                else{ //it is attribute
                    String attributeArray[] = currentString.split("\s+");
                    int x=0;
                    
                    if(attributeArray.length<=1) continue;
                    if(attributeArray[0] ==""||attributeArray[0] =="\n"){//要判斷是否有第一種是tab如果是的話split 出來arr[0] 會是空字串
                        x=1;
                    }
                    else{//如果不是split 出來 array[0]會是class的名稱
                        x=0;
                    }

                    if(currentString.contains("+")){//public 
                        String attributeDataType = attributeArray[2+x].substring(1);
                        String attribute = TAB+"public "+attributeDataType+" "+attributeArray[3+x];
                        sites.get(attributeArray[x]).add(attribute);
                    }
                    else if(currentString.contains("-")){//private
                        String attributeDataType = attributeArray[2+x].substring(1);
                        String attribute = TAB+"private "+attributeDataType+" "+attributeArray[3+x]+";\n";
                        sites.get(attributeArray[x]).add(attribute);

                    }

                }

            }
            
        } 
        for(int j=0 ; j<classNum ;j++){ //add ")"
            sites.get(Title[j]).add("}");
        }
        return sites;
        
    }

}

class Writer{
    public static void set(HashMap<String ,List<String>> map){
        String fileName = ".java";
        for (HashMap.Entry<String, List<String>> entry : map.entrySet()) {
            String key = entry.getKey();
            List<String> ls = new ArrayList<>(entry.getValue());
            String content = "";
            for (String line : ls) {
                content+=line;
            }
            fileName=key+".java";
            //System.out.println(fileName);
            try {

                File file = new File(fileName);
                if (!file.exists()) {
                    file.createNewFile();
                }
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                    bw.write(content);
                }
                // System.out.println("Java class has been generated: " + fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
    }
    
}
