package com.nowcoder.community.util;


import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SensitiveFilter {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    // 敏感词替换符
    private static final String REPLACEMENT = "***";

    // trie
    private class TrieNode {
        // end boolean
        private boolean isKeywordEnd = false;

        // child Node
        private Map<Character, TrieNode> subNode = new HashMap<>();

        // helper function to add node
        public void addNode(Character c, TrieNode node){
            subNode.put(c, node);
        }
        //helper function to get node
        public TrieNode getSubNode(Character c){
            return subNode.get(c);
        }

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }
    }

    // initialize trie based on sensitive-words.txt
    private TrieNode rootNode = new TrieNode();

    @PostConstruct
    public void init(){
        try (
                InputStream is = this.getClass().getClassLoader().getResourceAsStream("templates/sensitive-words.txt");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                ){
            String keyword;
            while((keyword = reader.readLine()) != null){
                this.addKeyword(keyword);
            }

        } catch (IOException e){
            logger.error("failed to load sensitve-words.txt" + e.getMessage());
        }
    }

    /***
     * filter sensitve words
     * @param text raw text
     * @return filtered text
     */
    public String filter(String text){
            if(StringUtils.isBlank(text)){
                return null;
            }
            // trie pointer
            TrieNode currNode = rootNode;

            // String start pointer
            int start = 0;
            // String current pointer
            int position = 0;
            // result
            StringBuilder sb = new StringBuilder();

            while (position < text.length()){
                char c = text.charAt(position);

                //skip special symbol such as - * ^_^ etc
                if(isSymbol(c)){
                    // 若树指针处于根节点， 将此符号计入结果，start指针向后一步
                    if (currNode == rootNode){
                        sb.append(c);
                        start++;

                    }
                    position++;
                    continue;
                }

                // 检查下级节点
                currNode = currNode.getSubNode(c);
                if (currNode == null) {
                    //以start为开头的字符不是敏感词, 把text.charAt(start)加入结果
                    sb.append(text.charAt(start));
                    position = ++start;
                    currNode = rootNode;
                }else if (currNode.isKeywordEnd()){
                    // 发现敏感词，替换start-position 字符串为***
                    sb.append(REPLACEMENT);
                    start = ++position;
                    currNode = rootNode;
                }
                else{
                    // 检查下一个字符
                    position++;
                }
            }
            sb.append(text.substring(start));
            return sb.toString();
    }

    // check character is special symbol
    private boolean isSymbol(Character c){
        // 0x2E80~0X9FFF是东亚文字范围
        return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
    }
    // helper function to add a keyword to trie
    private void addKeyword(String keyWord){
        TrieNode currNode = rootNode;
        for (int i = 0; i < keyWord.length(); i++){
            char c = keyWord.charAt(i);
            TrieNode subNode = currNode.getSubNode(c);

            if(subNode == null){
                //initialize subnode
                subNode = new TrieNode();
                currNode.addNode(c, subNode);
            }
            currNode = subNode;
            if(i == keyWord.length() - 1){
                currNode.setKeywordEnd(true);
            }
        }
    }

}
