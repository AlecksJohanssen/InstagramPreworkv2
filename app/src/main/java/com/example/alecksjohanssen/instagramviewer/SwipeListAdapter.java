package com.example.alecksjohanssen.instagramviewer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by AlecksJohanssen on 3/8/2016.
 */
public class SwipeListAdapter extends ArrayAdapter<InstagramPhotos> {
    Button button;

    //What data do we need from the  activity
    //context data source
    public SwipeListAdapter(Context context, List<InstagramPhotos> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    //What our items look like
    //Use the template to display photos
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        //Get data item for position
        InstagramPhotos photo = getItem(position);
        //Check if we are using recycled view if not we inflate
        if (convertView == null) { //Create new template
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_photos, parent, false);
        }
        //Look up view for populating the data
        TextView cTime = (TextView) convertView.findViewById(R.id.Created_Time);
        TextView tvUsername = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvUsername1= (TextView) convertView.findViewById(R.id.tvUsername1);
        TextView tvCaption = (TextView) convertView.findViewById(R.id.tvCaption);
        ImageView ivPhotos = (ImageView) convertView.findViewById(R.id.ivPhotos);
        TextView tvLikes = (TextView) convertView.findViewById(R.id.tvLikes);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfile);
        TextView tvcmt1 = (TextView) convertView.findViewById(R.id.cmtlow);
        TextView tvcmt2 = (TextView) convertView.findViewById(R.id.cmtlow1);
        Picasso.with(getContext()).load(photo.imageUrl).into(ivPhotos);
        Transformation transformation = new RoundedTransformationBuilder()
                .borderColor(Color.BLACK)
                .borderWidthDp(0)
                .cornerRadiusDp(30)
                .oval(false)
                .build();
        Picasso.with(getContext()).load(photo.profile).fit().transform(transformation).into(ivProfile);
        ivProfile.setImageResource(0);
       cTime.setText("\uD83D\uDD52" + DateUtils.getRelativeTimeSpanString(photo.Created_Time * 1000));
        ivPhotos.setImageResource(0);
        tvUsername.setText(photo.username);
        tvUsername1.setText(photo.username);
        DecimalFormat formatter = new DecimalFormat("#,###,###");
        String yourFormattedString = formatter.format(photo.likesCount);
        tvLikes.setText(yourFormattedString);
        tvCaption.setText(photo.caption);

        setTags(tvCaption, photo.caption);
        String userCmt1 =  photo.usercomment+ ":" + photo.comment;
        String userCmt2 =  photo.usercomment2+":"+ photo.comment2;
        tvcmt1.setText(userCmt1);
        tvcmt2.setText(userCmt2);

        //Insert the images using Picasso

        //Return created item as a view
        return convertView;


            }
    private void setTags(TextView pTextView, String pTagString) {
        SpannableString string = new SpannableString(pTagString);

        int start = -1;
        for (int i = 0; i < pTagString.length(); i++) {
            if (pTagString.charAt(i) == '#'|| pTagString.charAt(i) == '@') {
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
            }
        }

        pTextView.setMovementMethod(LinkMovementMethod.getInstance());
        pTextView.setText(string);
    }


}
