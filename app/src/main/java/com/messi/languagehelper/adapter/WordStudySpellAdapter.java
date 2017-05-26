package com.messi.languagehelper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.messi.languagehelper.R;
import com.messi.languagehelper.bean.WordSpellCharacter;
import com.messi.languagehelper.impl.AdapterListener;

import java.util.List;

public class WordStudySpellAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private Context context;
    private List<WordSpellCharacter> mWordSpellCharacter;
    private AdapterListener mlistener;

    public WordStudySpellAdapter(Context mContext, List<WordSpellCharacter> mWordSpellCharacter, AdapterListener mlistener) {
        context = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mWordSpellCharacter = mWordSpellCharacter;
        this.mlistener = mlistener;
    }

    public int getCount() {
        return mWordSpellCharacter.size();
    }

    public WordSpellCharacter getItem(int position) {
        return mWordSpellCharacter.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.word_study_spell_item, null);
            holder = new ViewHolder();
            holder.cover = (View) convertView.findViewById(R.id.layout_cover);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final WordSpellCharacter bean = mWordSpellCharacter.get(position);
        if(bean.isSelected()){
            holder.name.setText(" ");
            holder.name.setBackgroundColor(context.getResources().getColor(R.color.text_tint));
            holder.cover.setClickable(false);
        }else {
            holder.name.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.cover.setClickable(true);
            holder.name.setText(bean.getCharacter().toString());
            holder.cover.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    if (mlistener != null) {
                        mlistener.OnItemClick(bean, position);
                    }
                }
            });
        }
        return convertView;
    }

    static class ViewHolder {
        View cover;
        TextView name;
    }
}
