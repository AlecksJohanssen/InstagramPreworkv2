package com.example.alecksjohanssen.instagramviewer;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by AlecksJohanssen on 3/13/2016.
 */
public class CommentAdapter extends ArrayAdapter<Comment> {
    //What data do we need from the  activity
    //context data source
    private void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '#' || pTagString.charAt(i) == '@') {
                start = i;
            } else if (pTagString.charAt(i) == ' ' || (i == pTagString.length() - 1 && start != -1)) {
                if (start != -1) {
                    if (i == pTagString.length() - 1) {
                        i++; // case for if hash is last word and there is no
                        // space after word
                    }

                    final String tag = pTagString.substring(start, i);
                    string.setSpan(new ClickableSpan() {

                        @Override
                        public void onClick(View widget) {
                            Log.d("Hash", String.format("Clicked %s!", tag));
                        }

                        @Override
                        public void updateDrawState(TextPaint ds) {
                            // link color
                            ds.setColor(Color.parseColor("#1a75ff"));
                            ds.setUnderlineText(false);
                        }
                    }, start, i, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    start = -1;
                }
                pTextView.setMovementMethod(LinkMovementMethod.getInstance());
                pTextView.setText(string);
            }
        }
    }
    public CommentAdapter(Context context, List < Comment > objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Get data item for position
        Comment cmt = getItem(position);
        //Check if we are using recycled view if not we inflate
        if (convertView == null) { //Create new template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_comment, parent, false);
        }
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername1);
        TextView tvComment = (TextView) convertView.findViewById(R.id.tvComment2);
        TextView tvCreatedTime = (TextView) convertView.findViewById(R.id.Created_Time1);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfileComment);
        tvUsername.setText(cmt.usernamecomment);

        tvComment.setText(cmt.comments);
        setTags(tvComment, cmt.comments);

        com.squareup.picasso.Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(cmt.profile).fit().transform(transformation).into(ivProfile);

        tvCreatedTime.setText("\uD83D\uDD52 " + android.text.format.DateUtils.getRelativeTimeSpanString(cmt.Created_Time1 * 1000).toString());
        return convertView;

    }

    }

