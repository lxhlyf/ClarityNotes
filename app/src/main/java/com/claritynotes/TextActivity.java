package com.claritynotes;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.afollestad.appthemeenginesample.R;
import com.claritynotes.data.constant.Constant;
import com.claritynotes.database.helper.CharaterListHelper;
import com.claritynotes.model.CharactorList;
import com.claritynotes.ui.utils.CharacterUtils;
import com.claritynotes.utils.UIUtils;

import java.util.List;

/**
 * Created by 简言 on 2019/4/6  10:02.
 * 努力吧 ！ 少年 ！
 * email : yifeng20161123@163.com
 *
 * @package : com.claritynotes
 * Description :
 */
public class TextActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text);
    }

    public void characterOnClick(View view) {

        //.getInstance(this).addCharacterList(CharacterUtils.getCiGenByCList());

        //进行保存
        CharaterListHelper.save(Constant.CHARACTER_KEY, CharacterUtils.getCiGenByCList());
    }


    public void getCharacterListOnClick(View view) {

        //List<CharactorList> characterList = CharacterTable.getInstance(this).getCharacterList();

        //查询
        Log.e("KLogTA", CharaterListHelper.getCharacterList(Constant.CHARACTER_KEY).get(0).getJson());
    }
}
