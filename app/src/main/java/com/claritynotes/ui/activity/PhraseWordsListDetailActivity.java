package com.claritynotes.ui.activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.afollestad.appthemeengine.customizers.ATEActivityThemeCustomizer;
import com.afollestad.appthemeenginesample.R;
import com.claritynotes.base.BaseThemedActivity;
import com.claritynotes.model.PhraseWordsListByCharacter;
import com.claritynotes.model.event.PhraseRefreshEvent;
import com.claritynotes.utils.UIUtils;
import com.claritynotes.widgets.DividerItemDecoration;
import com.claritynotes.ui.adapter.SampleRVAdapter;
import com.claritynotes.ui.utils.CharacterUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by 简言 on 2019/4/1  17:40.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes.ui.activity
 * Description :
 */
public class PhraseWordsListDetailActivity extends BaseThemedActivity implements ATEActivityThemeCustomizer {

    private static final String WORDLIST = "单词列表";
    private static final String PHRASELIST = "短语列表";
    private SampleRVAdapter sampleRVAdapter;

    private int itemPosition = -1;

    private ArrayList<PhraseWordsListByCharacter> phraseOrWords = new ArrayList<>();

    private String phraseMean;

    private String ciGen;

    private String title;

    @StyleRes
    @Override
    public int getActivityTheme() {
        // Make sure we don't use the one set to the Config, since we want a non-toolbar-actionbar for this activity
        return PreferenceManager.getDefaultSharedPreferences(this).getBoolean("dark_theme", false) ?
                R.style.AppThemeDark_ActionBar : R.style.AppTheme_ActionBar;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        title = Objects.requireNonNull(getIntent().getExtras()).getString("title");
        setTitle(title);
        setContentView(R.layout.activty_words_list_detail);

        //phraseOrWords = getIntent().getParcelableArrayListExtra("phraseOrWords");
        phraseMean = getIntent().getExtras().getString("phraseMean");

        ciGen = getIntent().getExtras().getString("ciGen");
        if (!TextUtils.isEmpty(ciGen)){
            ciGen = ciGen.replace("-", "pw");
        }

        //UIUtils.showToast(phraseMean +"/"+ ciGen);

        if (title.equals(WORDLIST)){

            phraseOrWords = CharacterUtils.getWordsByCList(ciGen);
        }else {

            phraseOrWords = CharacterUtils.getPhraseListByMean(phraseMean);
        }

        //1.根据词根 ，去读取数据库，从数据库中， 获取到对应的单词列表
        RecyclerView recyclerView = findViewById(R.id.words_list_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        sampleRVAdapter = new SampleRVAdapter(phraseOrWords);
        recyclerView.setAdapter(sampleRVAdapter);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerEventBus(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterEventBus(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.words_list_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        //添加 新的单词
        switch (item.getItemId()) {
            case android.R.id.home:

                finish();
                break;
            case R.id.add_words_list:

                editDataDialog(false);
                break;
        }
        return true;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void phraseRefresh(PhraseRefreshEvent event){

        if (event.isDelete()){

            editDataDialog(event.getPostion());
        }else {

            itemPosition = event.getPostion();
            editDataDialog(true);
        }
    }

    private void editDataDialog(int positin) {


        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder
                .setTitle("您确定要删除吗？")
                .setMessage("还是好好考虑考虑吧，都是记忆，过去的和未来的。")
                .setNegativeButton("取消", (dialog1, which) -> {
                    //UIUtils.showToast("点击了取消按钮");
                }).setPositiveButton("确定", (dialog12, which) -> {

                    if (title.equals(WORDLIST)) {

                        phraseOrWords = CharacterUtils.deleteWords(ciGen, positin, phraseOrWords);
                        sampleRVAdapter.notifyDataSetChanged();
                    }else {

                        phraseOrWords = CharacterUtils.deletePhrase(phraseMean, positin, phraseOrWords);
                        sampleRVAdapter.notifyDataSetChanged();
                    }
                }).show();

        //设置窗口参数
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = Objects.requireNonNull(window).getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }

    private void editDataDialog(boolean isEdit) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_phrase_words_list_add, null);

        EditText mInputPhrase = view.findViewById(R.id.dialog_edit_phrase);
        EditText mInputYinBiao = view.findViewById(R.id.dialog_edit_yin_biao);
        EditText mInputTranslation = view.findViewById(R.id.dialog_edit_translation);
        EditText mInputExampleSentence = view.findViewById(R.id.dialog_edit_example_sentence);

         if (itemPosition != -1){

             mInputPhrase.setText(phraseOrWords.get(itemPosition).getWordOrPhrase());
             mInputYinBiao.setText(phraseOrWords.get(itemPosition).getYinbiao());
             mInputTranslation.setText(phraseOrWords.get(itemPosition).getTranslate());
             mInputExampleSentence.setText(phraseOrWords.get(itemPosition).getExampleSentence());
         }

        //谈一个弹框，提示用户输入新增加的词根
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        AlertDialog dialog = builder.setMessage("请输入你新的好句:")
                .setView(view)
                .setNegativeButton("取消", (dialog12, which) -> {
                    //UIUtils.showToast("点击了取消按钮");

                    itemPosition = -1;
                }).setPositiveButton("确定", (dialog1, which) -> {

                    //可以在这里将输入的词根 存入到数据中去，在Fragment中 查找并进行显示
                    updateData(mInputPhrase.getText().toString(), mInputYinBiao.getText().toString(), mInputTranslation.getText().toString(), mInputExampleSentence.getText().toString(),isEdit);
                }).show();

        //设置窗口参数
        Window window = dialog.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
        params.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        params.dimAmount = 0.5f;
        window.setAttributes(params);
    }


    private void updateData(String phrase, String yinBiao, String translation, String exampleSentence, boolean isEdit) {
        if (title.equals(WORDLIST)) {

            if (isEdit && phrase.equals("请输入单词"))
                UIUtils.showToast("请点击右上角添加新的单词");
            phraseOrWords = CharacterUtils.upDataWordsList(ciGen, phrase, yinBiao, translation, exampleSentence, phraseOrWords, isEdit, itemPosition);
        }else{

            if (isEdit && phrase.equals("请输入短语")){

                UIUtils.showToast("请点击右上角添加新的短语");
                return;
            }
            phraseOrWords = CharacterUtils.updataPhraseList(phraseMean, phrase, yinBiao, translation, exampleSentence, phraseOrWords, isEdit, itemPosition);
        }
        sampleRVAdapter.notifyDataSetChanged();

        itemPosition = -1;
    }
}
