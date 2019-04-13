package com.claritynotes.ui.utils;

import android.text.TextUtils;
import android.util.Log;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.data.constant.Constant;
import com.claritynotes.database.helper.CharaterListHelper;
import com.claritynotes.database.helper.PhraseTranslationItemsHelper;
import com.claritynotes.database.helper.PhraseWordsListByCharacterHelper;
import com.claritynotes.database.helper.SentenceAuthoeSourceHelper;
import com.claritynotes.database.helper.SourceItemHelper;
import com.claritynotes.database.table.CharacterListTable;
import com.claritynotes.database.table.PhraseTranslationItemsTable;
import com.claritynotes.database.table.PhraseWordsListByCharacterTable;
import com.claritynotes.database.table.SentenceAuthorSourceTable;
import com.claritynotes.database.table.SentenceItemTable;
import com.claritynotes.model.CharactorList;
import com.claritynotes.model.CiGenList;
import com.claritynotes.model.PhraseTranslationItem;
import com.claritynotes.model.PhraseWordsListByCharacter;
import com.claritynotes.model.SentenceAuthorSource;
import com.claritynotes.model.SentenceItem;
import com.claritynotes.utils.DateUtils;
import com.claritynotes.utils.UIUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.List;

/**
 * Created by 简言 on 2019/3/30  14:02.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.utils
 * Description :
 */
public class CharacterUtils {

    private static final String TAG = "characterUtil";

    private static String[] characters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "G", "H", "L"
            , "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    /**
     * 根据首字母获取 词根和词根对应单词的列表
     *
     * @return
     */
    public static List<CharactorList> getCiGenByCList() {

        List<CharactorList> dataList = new ArrayList<>();

        List<CharacterListTable> characterList = CharaterListHelper.getCharacterList(Constant.CHARACTER_KEY);
        if (characterList != null && characterList.size() > 0) {

            dataList.addAll(CharaterListHelper.convertToNewsList(characterList.get(0).getJson()));
            Log.e(TAG, "getCiGenByCList---从数据库中读取");
            return dataList;
        }

        Log.e(TAG, "getCiGenByCList---实例化数据中读取");
        for (int i = 0; i < characters.length; i++) {

            String character = characters[i];
            CharactorList charactorList = new CharactorList();
            charactorList.setCharactor(character);
            charactorList.setIcon(R.drawable.byx1);

            //可以将词根列表存储成一个Json 字符串
            CiGenList ciGenListItem = new CiGenList("-" + character + "开头的词根", "请输入词根的含义", DateUtils.toDateStr(new Date()));
            charactorList.setCiGenLists(ciGenListItem);
            dataList.add(charactorList);
        }
        return dataList;
    }

    /**
     * 更新词根列表 修改和添加
     *
     * @param ciGen
     * @param translation
     * @param dataList
     * @param isEdit
     * @param ciGenPostion
     * @param characterPosstion
     * @return
     */
    public static List<CharactorList> upDateCiGenItem(String ciGen, String translation, List<CharactorList> dataList, boolean isEdit, int ciGenPostion, int characterPosstion) {

        if (TextUtils.isEmpty(ciGen)) {
            //如果相等就进行修改，如果不相等就提示用户
            dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).setCiGen(ciGen);
            dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).setTranslation(translation);
            return dataList;
        }

        if (ciGen.equals("-")) {

            dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).setCiGen(null);
            dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).setTranslation(translation);
            return dataList;
        }
        String captialLetter;
        int index = ciGen.indexOf('-');
        if (index == 0) {
            captialLetter = ciGen.substring(1, 2).toUpperCase();
        } else {

            captialLetter = ciGen.substring(0, 1).toUpperCase();
        }
        String characterTemp;
        boolean isHave = false;
        if (!isEdit) {

            //把当前项的 默认 展示数据删除
            if (dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).getTranslation().equals("请输入词根的含义")){

                dataList.remove(0);
            }

            //添加状态
            for (int i = 0; i < dataList.size(); i++) {

                characterTemp = dataList.get(i).getCharactor();
                if (characterTemp.equals(captialLetter)) {

                    if (dataList.get(characterPosstion).getCiGenLists().get(0).getTranslation().equals("请输入词根的含义")) {

                        dataList.get(characterPosstion).getCiGenLists().remove(0);
                    }

                    for (int j = 0; j < dataList.get(i).getCiGenLists().size(); j++) {

                        //判断是否已经有这个词根了
                        if (ciGen.equals(dataList.get(i).getCiGenLists().get(j).getCiGen())) {
                            isHave = true;
                        }
                    }

                    if (!isHave) {
                        isHave = true;
                        //确定会更改的数据的时候清空数据库
                        // 尽管清空了数据库 但是内存中还有一份数据 所以数据才不会丢失
                        if (CharaterListHelper.deleteAll(Constant.CHARACTER_KEY)) {

                            dataList.get(i).setCiGenLists(new CiGenList(ciGen, translation, DateUtils.toDateStr(new Date())));
                        }
                    } else {

                        UIUtils.showToast("您输入的词根分组中已有<^>");
                    }
                }
            }
        } else {
            //处于编辑的状态

            characterTemp = dataList.get(characterPosstion).getCharactor();
            if (characterTemp.equals(captialLetter)) {

                //如果相等就进行修改，如果不相等就提示用户
                dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).setCiGen(ciGen);
                dataList.get(characterPosstion).getCiGenLists().get(ciGenPostion).setTranslation(translation);
            } else {

                UIUtils.showToast("您输入的词根首字母与分组不对应");
            }
        }

        //在这里里进行保存词根到数据库中
        if (CharaterListHelper.deleteAll(Constant.CHARACTER_KEY))
            CharaterListHelper.save(Constant.CHARACTER_KEY, dataList);
        return dataList;
    }

    /**
     * 获取单词 列表， 通过首字母作为 key 进行存储
     *
     * @param ciGen
     * @return
     */
    @Deprecated
    public static ArrayList<PhraseWordsListByCharacter> getWordsByCList(String ciGen) {

        ArrayList<PhraseWordsListByCharacter> listByCharactors = new ArrayList<>();

        //先从数据库中获取
        List<PhraseWordsListByCharacterTable> tables = PhraseWordsListByCharacterHelper.getWordsList(ciGen);
        if (tables != null && tables.size() > 0) {

            //先从数据库中
            Log.e(TAG, "getWordsByCList---从数据库获取单词列表");
            listByCharactors.addAll(PhraseWordsListByCharacterHelper.convertToNewsList(tables.get(0).getJson()));
            return listByCharactors;
        }

        Log.e(TAG, "getWordsByCList---实例化数据");

        PhraseWordsListByCharacter wordsListByCharactor = new PhraseWordsListByCharacter("请输入单词", "请输入音标", "请输入单词意思", "请输入单词的例句", DateUtils.toDateStr(new Date()));
        listByCharactors.add(wordsListByCharactor);

        return listByCharactors;
    }

    /**
     * 更新单词列表， 此方法
     *
     * @param phrase
     * @param yinBiao
     * @param translation
     * @param exampleSentence
     * @param phraseWordsListByCharacters
     * @param isEdit
     * @param itemPosition
     * @return
     */
    public static ArrayList<PhraseWordsListByCharacter> upDataWordsList(String ciGen, String phrase, String yinBiao, String translation, String exampleSentence, ArrayList<PhraseWordsListByCharacter> phraseWordsListByCharacters, boolean isEdit, int itemPosition) {

        if (phraseWordsListByCharacters.get(0).getWordOrPhrase().equals("请输入单词")) {

            phraseWordsListByCharacters.clear();
        }

        if (!isEdit) {

            PhraseWordsListByCharacter wordsListByCharactor = new PhraseWordsListByCharacter(phrase, yinBiao, translation, exampleSentence, DateUtils.toDateStr(new Date()));
            phraseWordsListByCharacters.add(0, wordsListByCharactor);
        } else {

            phraseWordsListByCharacters.get(itemPosition).setWordOrPhrase(phrase);
            phraseWordsListByCharacters.get(itemPosition).setYinbiao(yinBiao);
            phraseWordsListByCharacters.get(itemPosition).setTranslate(translation);
            phraseWordsListByCharacters.get(itemPosition).setExampleSentence(exampleSentence);
        }

        //将数据库中的 数据清空，存心进行保存
        if (PhraseWordsListByCharacterHelper.deleteWordsAll(ciGen)) {

            PhraseWordsListByCharacterHelper.saveWords(ciGen, phraseWordsListByCharacters);
        }
        return phraseWordsListByCharacters;
    }

    /**
     * 获取短语的 意思 列表
     *
     * @return
     */
    public static List<PhraseTranslationItem> getPhraseTranslationItems() {

        List<PhraseTranslationItem> phraseTranslationItems = new ArrayList<>();

        List<PhraseTranslationItemsTable> tables = PhraseTranslationItemsHelper.getPhraseTranslationItemList(Constant.PHRASE_KEY);
        if (tables != null && tables.size() > 0) {

            Log.e(TAG, "getPhraseTranslationItems--从数据库");
            phraseTranslationItems.addAll(PhraseTranslationItemsHelper.convertToNewsList(tables.get(0).getJson()));
            return phraseTranslationItems;
        }

        Log.e(TAG, "getPhraseTranslationItems--实例化数据");

        PhraseTranslationItem item = new PhraseTranslationItem();
        item.setTranslation("请输入短语的意思");
        item.setRecordTime(DateUtils.toDateStr(new Date()));
        phraseTranslationItems.add(item);

        return phraseTranslationItems;
    }

    /**
     * 创建 短语意思数据
     *
     * @param translation
     * @return
     */
    public static PhraseTranslationItem createPhraseTranslationItem(String translation) {

        PhraseTranslationItem item = new PhraseTranslationItem();
        item.setTranslation("陈钰琪");
        return item;
    }

    /**
     * 创建 句子 意思 数据
     *
     * @return
     */
    public static List<SentenceAuthorSource> getSentenceAuthorSource() {

        List<SentenceAuthorSource> sentenceAuthorSources = new ArrayList<>();

        List<SentenceAuthorSourceTable> sentenceAuthorSourceTables = SentenceAuthoeSourceHelper.getsentenceASTList(Constant.SENTENCEAST_KEY);
        if (sentenceAuthorSourceTables != null && sentenceAuthorSourceTables.size() > 0) {

            Log.e(TAG, "getSentenceAuthorSource--从数据库");
            sentenceAuthorSources.addAll(SentenceAuthoeSourceHelper.convertToNewsList(sentenceAuthorSourceTables.get(0).getJson()));
            return sentenceAuthorSources;
        }

        Log.e(TAG, "getSentenceAuthorSource--实例化数据");

        SentenceAuthorSource sentenceAuthorSource = new SentenceAuthorSource("名句的作者", "名句摘自何书", "内容:你可能来自的地方，都被远方期待!", DateUtils.toDateStr(new Date()));
        sentenceAuthorSources.add(sentenceAuthorSource);
        return sentenceAuthorSources;
    }

    public static ArrayList<SentenceItem> getSentenceItems(String source) {

        ArrayList<SentenceItem> sentenceItems = new ArrayList<>();

        //第一步， 先从数据库中获取
        List<SentenceItemTable> tables = SourceItemHelper.getSentenceTableList(source);
        if (tables != null && tables.size() > 0) {

            sentenceItems.addAll(SourceItemHelper.convertToNewsList(tables.get(0).getJson()));
            return sentenceItems;
        }

        //第二步，如果数据库中没有
        SentenceItem sentenceItem = new SentenceItem("我们总对时间说，留下点什么吧，于是时间带走了一切", "刘一峰", DateUtils.toDateStr(new Date()));
        if (sentenceItems.size() <= 0) {

            sentenceItems.add(sentenceItem);
        }
        return sentenceItems;
    }

    /**
     * @param sentence
     * @param bookSource
     * @param sentenceItems
     * @param isEdit
     * @param itemPosition
     * @return
     */
    public static ArrayList<SentenceItem> upDataSentenceItems(String source, String sentence, String bookSource, ArrayList<SentenceItem> sentenceItems, boolean isEdit, int itemPosition) {

        if (sentenceItems.get(0).getBookSource().equals("刘一峰")) {

            sentenceItems.clear();
        }

        if (!isEdit) {

            SentenceItem sentenceItem = new SentenceItem(sentence, bookSource, DateUtils.toDateStr(new Date()));
            sentenceItems.add(0, sentenceItem);
        } else {

            sentenceItems.get(itemPosition).setSentence(sentence);
            sentenceItems.get(itemPosition).setBookSource(bookSource);
            sentenceItems.get(itemPosition).setRecordTime(DateUtils.toDateStr(new Date()));
        }

        if (SourceItemHelper.deleteAll(source)) {

            SourceItemHelper.save(source, sentenceItems);
        }
        return sentenceItems;
    }

    public static List<PhraseTranslationItem> upDatePhraseTranslationItem(String translation, List<PhraseTranslationItem> phraseTranslationItems, boolean isEdit, int itemPosition) {

        if (phraseTranslationItems.get(0).getTranslation().equals("请输入短语的意思")) {

            phraseTranslationItems.clear();
        }

        if (!isEdit) {

            PhraseTranslationItem item = new PhraseTranslationItem();
            item.setTranslation(translation);
            phraseTranslationItems.add(0, item);
        } else {

            phraseTranslationItems.get(itemPosition).setTranslation(translation);
        }

        if (PhraseTranslationItemsHelper.deleteAll(Constant.PHRASE_KEY)) {

            PhraseTranslationItemsHelper.save(Constant.PHRASE_KEY, phraseTranslationItems);
        }
        return phraseTranslationItems;
    }

    public static List<SentenceAuthorSource> updateSentenceAuthorSource(String author, String source, String content, List<SentenceAuthorSource> sentenceAuthorSources, boolean isEdit, int itemPosition) {

        if (sentenceAuthorSources.get(0).getAuthor().equals("名句的作者")) {

            sentenceAuthorSources.clear();
        }

        if (!isEdit) {

            SentenceAuthorSource item = new SentenceAuthorSource(author, source, content, DateUtils.toDateStr(new Date()));
            sentenceAuthorSources.add(0, item);
        } else {

            sentenceAuthorSources.get(itemPosition).setAuthor(author);
            sentenceAuthorSources.get(itemPosition).setSource(source);
            sentenceAuthorSources.get(itemPosition).setContent(content);
        }

        if (SentenceAuthoeSourceHelper.deleteAll(Constant.SENTENCEAST_KEY)) {

            SentenceAuthoeSourceHelper.save(Constant.SENTENCEAST_KEY, sentenceAuthorSources);
        }
        return sentenceAuthorSources;
    }

    /**
     * 根据短语意思，获取短语列表
     *
     * @param phraseMean
     * @return
     */
    public static ArrayList<PhraseWordsListByCharacter> getPhraseListByMean(String phraseMean) {

        ArrayList<PhraseWordsListByCharacter> listByCharacters = new ArrayList<>();

        List<PhraseWordsListByCharacterTable> tables = PhraseWordsListByCharacterHelper.getPhrasesList(phraseMean);
        if (tables != null && tables.size() > 0) {

            listByCharacters.addAll(PhraseWordsListByCharacterHelper.convertToNewsList(tables.get(0).getJson()));
            return listByCharacters;
        }

        PhraseWordsListByCharacter phraseWordsListByCharacter = new PhraseWordsListByCharacter("请输入短语", null, "请输入翻译", "请输入例句", DateUtils.toDateStr(new Date()));
        listByCharacters.add(0, phraseWordsListByCharacter);
        return listByCharacters;
    }

    /**
     * 添加 或 修改条目
     *
     * @param phraseMean
     * @param phrase
     * @param yinBiao
     * @param translation
     * @param exampleSentence
     * @param phraseOrWords
     * @param isEdit
     * @param itemPosition
     * @return
     */
    public static ArrayList<PhraseWordsListByCharacter> updataPhraseList(String phraseMean, String phrase, String yinBiao, String translation, String exampleSentence, ArrayList<PhraseWordsListByCharacter> phraseOrWords, boolean isEdit, int itemPosition) {

        if (phraseOrWords.get(0).getWordOrPhrase().equals("请输入短语")) {

            phraseOrWords.clear();
        }

        if (!isEdit) {

            PhraseWordsListByCharacter wordsListByCharactor = new PhraseWordsListByCharacter(phrase, yinBiao, translation, exampleSentence, DateUtils.toDateStr(new Date()));
            phraseOrWords.add(0, wordsListByCharactor);
        } else {

            phraseOrWords.get(itemPosition).setWordOrPhrase(phrase);
            phraseOrWords.get(itemPosition).setYinbiao(yinBiao);
            phraseOrWords.get(itemPosition).setTranslate(translation);
            phraseOrWords.get(itemPosition).setExampleSentence(exampleSentence);
        }

        //将数据库中的 数据清空，存心进行保存
        if (PhraseWordsListByCharacterHelper.deleteWordsAll(phraseMean)) {

            PhraseWordsListByCharacterHelper.savePhrase(phraseMean, phraseOrWords);
        }
        return phraseOrWords;
    }

    /**
     * 没点一次都要进行更改
     *
     * @param expanded
     * @param dataList
     */
    public static void updataCharactorList(boolean expanded, List<CharactorList> dataList) {

        if (CharaterListHelper.deleteAll(Constant.CHARACTER_KEY))
            CharaterListHelper.save(Constant.CHARACTER_KEY, dataList);
    }

    /**
     * 删除词根条目
     *
     * @param characterPosition
     * @param ciGenPostion
     * @param dataList
     */
    public static List<CharactorList> deleteCiGenItem(int characterPosition, int ciGenPostion, List<CharactorList> dataList) {


        PhraseWordsListByCharacterHelper.deleteWordsAll(dataList.get(characterPosition).getCiGenLists().get(ciGenPostion).getCiGen());

        dataList.get(characterPosition).getCiGenLists().remove(ciGenPostion);

        if (dataList.get(characterPosition).getCiGenLists().size() <= 0){

            CiGenList ciGenListItem = new CiGenList("-" + dataList.get(characterPosition).getCharactor() + "开头的词根", "请输入词根的含义", DateUtils.toDateStr(new Date()));
            dataList.get(characterPosition).setCiGenLists(ciGenListItem);
        }else {

            if (CharaterListHelper.deleteAll(Constant.CHARACTER_KEY))
                CharaterListHelper.save(Constant.CHARACTER_KEY, dataList);
        }
        return dataList;
    }

    public static ArrayList<PhraseWordsListByCharacter> deletePhrase(String phraseMean, int postion, ArrayList<PhraseWordsListByCharacter> phraseOrWords) {

        phraseOrWords.remove(postion);

        if (phraseOrWords.size() <= 0) {

            PhraseWordsListByCharacter phraseWordsListByCharacter = new PhraseWordsListByCharacter("请输入短语", null, "请输入翻译", "请输入例句", DateUtils.toDateStr(new Date()));
            phraseOrWords.add(0, phraseWordsListByCharacter);
        } else {

            //将数据库中的 数据清空，存心进行保存
            if (PhraseWordsListByCharacterHelper.deleteWordsAll(phraseMean)) {

                PhraseWordsListByCharacterHelper.savePhrase(phraseMean, phraseOrWords);
            }
        }

        return phraseOrWords;
    }

    /**
     * 将短语的意思，所对应的短语列表 都删除
     *
     * @param i
     * @param phraseMean
     * @param phraseTranslationItems
     * @return
     */
    public static List<PhraseTranslationItem> delePhraseTransition(int i, String phraseMean, List<PhraseTranslationItem> phraseTranslationItems) {


        phraseTranslationItems.remove(i);

        if (phraseTranslationItems.size() <= 0){

            PhraseTranslationItem item = new PhraseTranslationItem();
            item.setTranslation("请输入短语的意思");
            item.setRecordTime(DateUtils.toDateStr(new Date()));
            phraseTranslationItems.add(item);
        }else {

            if (PhraseTranslationItemsHelper.deleteAll(Constant.PHRASE_KEY)) {

                PhraseTranslationItemsHelper.save(Constant.PHRASE_KEY, phraseTranslationItems);
            }
        }


        PhraseWordsListByCharacterHelper.deleteWordsAll(phraseMean);
        return phraseTranslationItems;
    }

    public static List<SentenceAuthorSource> deleteSentenceAuthorSources(int i, List<SentenceAuthorSource> sentenceAuthorSources, String source) {

        sentenceAuthorSources.remove(i);

        if (sentenceAuthorSources.size() <= 0) {

            SentenceAuthorSource sentenceAuthorSource = new SentenceAuthorSource("名句的作者", "名句摘自何书", "内容:你可能来自的地方，都被远方期待!", DateUtils.toDateStr(new Date()));
            sentenceAuthorSources.add(sentenceAuthorSource);
        } else {

            if (SentenceAuthoeSourceHelper.deleteAll(Constant.SENTENCEAST_KEY)) {

                SentenceAuthoeSourceHelper.save(Constant.SENTENCEAST_KEY, sentenceAuthorSources);
            }
        }
        return sentenceAuthorSources;
    }

    public static ArrayList<SentenceItem> deleteSentenceItems(int i, String source, ArrayList<SentenceItem> sentenceItems) {

        sentenceItems.remove(i);

        if (sentenceItems.size() <= 0){

            SentenceItem sentenceItem = new SentenceItem("我们总对时间说，留下点什么吧，于是时间带走了一切", "刘一峰", DateUtils.toDateStr(new Date()));
            if (sentenceItems.size() <= 0) {

                sentenceItems.add(sentenceItem);
            }
        }else {

            if (SourceItemHelper.deleteAll(source)) {

                SourceItemHelper.save(source, sentenceItems);
            }
        }
        return sentenceItems;
    }

    public static ArrayList<PhraseWordsListByCharacter> deleteWords(String ciGen, int positin, ArrayList<PhraseWordsListByCharacter> phraseOrWords) {

        phraseOrWords.remove(positin);

        if (phraseOrWords.size() <= 0) {

            PhraseWordsListByCharacter wordsListByCharactor = new PhraseWordsListByCharacter("请输入单词", "请输入音标", "请输入单词意思", "请输入单词的例句", DateUtils.toDateStr(new Date()));
            phraseOrWords.add(wordsListByCharactor);
        } else {

            if (PhraseWordsListByCharacterHelper.deleteWordsAll(ciGen)) {

                PhraseWordsListByCharacterHelper.saveWords(ciGen, phraseOrWords);
            }
        }
        return phraseOrWords;
    }
}
