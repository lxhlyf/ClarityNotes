package com.claritynotes.database.helper;

import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.database.table.PhraseTranslationItemsTable;
import com.claritynotes.database.table.PhraseWordsListByCharacterTable;
import com.claritynotes.model.PhraseTranslationItem;
import com.claritynotes.model.PhraseWordsListByCharacter;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by 简言 on 2019/4/6  21:15.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.database.helper
 * Description :
 */
public class PhraseWordsListByCharacterHelper extends BaseHelper {

    public static void savePhrase(String phraseKey, List<PhraseWordsListByCharacter> phraseWordsListByCharacters) {

        String json = mGson.toJson(phraseWordsListByCharacters);
        PhraseWordsListByCharacterTable table = new PhraseWordsListByCharacterTable(json, null, phraseKey);
        table.saveOrUpdate("phraseKey = ?", phraseKey);
    }

    public static void saveWords(String ciGen,List<PhraseWordsListByCharacter> phraseWordsListByCharacters){

        String json = mGson.toJson(phraseWordsListByCharacters);
        PhraseWordsListByCharacterTable table = new PhraseWordsListByCharacterTable(json, ciGen, null);
        table.saveOrUpdate("ciGen = ?", ciGen);
    }

    /**
     *  获取短语的列表
     * @param phraseKey
     * @return
     */
    public static List<PhraseWordsListByCharacterTable> getPhrasesList(String phraseKey){

        return DataSupport.where("phraseKey = ?", phraseKey).find(PhraseWordsListByCharacterTable.class);
    }

    /**
     *  获取单词的列表
     * @param ciGen
     * @return
     */
    public static List<PhraseWordsListByCharacterTable> getWordsList(String ciGen){

        return DataSupport.where("ciGen = ?", ciGen).find(PhraseWordsListByCharacterTable.class);
    }

    /**
     *  删除 单词 表中所有数据
     * @param ciGen
     * @return
     */
    public static boolean deleteWordsAll(String ciGen){

        int i = DataSupport.deleteAll(PhraseWordsListByCharacterTable.class, "ciGen = ?", ciGen);
        return i >= 0;
    }

    /**
     *  删除 短语 表中所有数据
     * @param phraseKey
     * @return
     */
    public static boolean deletePhraseAll(String phraseKey){

        int i = DataSupport.deleteAll(PhraseWordsListByCharacterTable.class, "phraseKey = ?", phraseKey);
        return i >= 0;
    }


    public static List<PhraseWordsListByCharacter> convertToNewsList(String json) {
        return mGson.fromJson(json, new TypeToken<List<PhraseWordsListByCharacter>>() {
        }.getType());
    }


}
