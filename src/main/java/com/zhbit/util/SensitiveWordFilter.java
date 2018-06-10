package com.zhbit.util;

import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensitiveWordFilter {
    private static final String SENSTIVEWORDSFile = "sensitiveWord.txt"; //敏感词文件

    private static HashMap sensitiveWordMap;

    private static final int SENSTIVEWORDSCOUNT = 1000; //初始敏感词数量，避免大量扩容操作

    private static final char SIGN = '*'; //敏感词替换字符

    static{
        try {
            init();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //初始化建立敏感词库
    public static void init(){
        addSenstiveWords(readWordFromFile(SENSTIVEWORDSFile));
    }

    //添加DFA结点，构建敏感词树
    private static void addSenstiveWords(final List<String> words){
        sensitiveWordMap = new HashMap<String, Object>(words.size());
        HashMap<String, Object> newmap = null;
        for(int i = 0; i < words.size(); i++){

            HashMap curMap = sensitiveWordMap;
            String word = words.get(i).trim();

            for(int j = 0; j < word.length(); j++){
                char key = word.charAt(j);
                key = parseChar(key);
                Object obj= curMap.get(key);

                if(obj != null){ //有匹配
                    curMap = (HashMap) obj;
                }else{ //不存在
                    newmap = new HashMap<String, Object>();
                    newmap.put("isEnd", "0");
                    curMap.put(key, newmap);
                    curMap = newmap;
                }
                if(j == word.length() - 1){
                    curMap.put("isEnd", "1");
                }
            }

        }
    }

    /**
     * 替换敏感词
     *@param content 过滤的内容
     *@return
     */
    public static String replaceSenstiveWords(String content){
        if(StringUtils.isBlank(content)){
            return content;
        }
        int flag = 0; //是否存在敏感词
        int pos = 0; //匹配到敏感词的位置
        int src = 0; //敏感词起始位置
        int isMatch = 0;  //是否存在匹配（可以不成功），判断回滚
        HashMap curmap = sensitiveWordMap;
        char result[] = content.toCharArray();
        for(int i = 0; i < content.length(); i++){
            char ch = result[i];
            ch = parseChar(ch);

            curmap = (HashMap) curmap.get(ch);
            if(isMatch == 0){
                src = i;
            }
            if(curmap != null){
                isMatch = 1;
                if("1".equals(curmap.get("isEnd"))){ //存在敏感词
                    pos = i;
                    flag = 1;
                }
            }else{
                if(isMatch == 1 && flag == 0){ //存在匹配，但不成功，回滚
                    i = src;
                }
                isMatch = 0;
                if(flag == 1)
                    i=pos;
                curmap = sensitiveWordMap;
            }
            if(flag == 1){ //替换敏感词
                for(int j = src; j <= pos; j++){
                    result[j] = SIGN;
                }
                if(isMatch == 0)
                    flag = 0;
                src = pos + 1; //从敏感词下一位继续匹配，如王八，王八蛋 ，应替换掉王八蛋
            }
        }
        return new String(result);
    }

    //转换文件敏感词
    private static List<String> readWordFromFile(String path){
        List<String> words = new ArrayList<String>(SENSTIVEWORDSCOUNT);
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(SensitiveWordFilter.class.getClassLoader().getResourceAsStream(path),"UTF-8"));
            for(String buf = ""; (buf = br.readLine()) != null;) {
                if (!StringUtils.isBlank(buf))
                    words.add(buf);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (br != null)
                    br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return words;
    }

    //全角转半角 大写转小写
    private static char parseChar(char ch){
        char result = ch;
        if(ch >= 'A' && ch <= 'Z'){
            result = (char) (ch - 'A' + 'a');
        }else if(ch == 12288){
            result = ' ';
        }else if(ch >= 65281 && ch <= 65374){
            result = (char)(ch - 65248);
        }
        return result;
    }



}
