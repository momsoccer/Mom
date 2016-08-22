package com.mom.soccer.cardview;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.dto.FeedbackHeader;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;

/**
 * Created by sungbo on 2016-08-23.
 */
public class FeedBackCard extends Card{

    private FeedbackHeader feedbackHeaders;
    private TextView type;
    private TextView content;
    private ImageView insimageview;
    private RatingBar mRatingBar;

    public FeedBackCard(Context context, FeedbackHeader feedbackHeaders) {
        super(context, R.layout.feedback_card_item);
        this.feedbackHeaders = feedbackHeaders;
        init();
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        content = (TextView) parent.findViewById(R.id.content);
        content.setText(feedbackHeaders.getContent());
        if (mRatingBar != null){
            mRatingBar.setNumStars(5);
            mRatingBar.setMax(5);
            mRatingBar.setStepSize(0.5f);
            mRatingBar.setRating(4.7f);
        }

    }

    private void init(){

        //Create a CardHeader
        CardHeader header = new CardHeader(getContext());

        //Set the header title
        header.setTitle("헤더요");

        //Add a popup menu. This method set OverFlow button to visible
        header.setPopupMenu(R.menu.feedback_pop_menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                Toast.makeText(getContext(), "Click on card menu" + "헤더" +" item=" +  item.getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
        addCardHeader(header);

        //Add ClickListener
        setOnClickListener(new OnCardClickListener() {
            @Override
            public void onClick(Card card, View view) {
                Toast.makeText(getContext(), "Click Listener card=" + "헤더", Toast.LENGTH_SHORT).show();
            }
        });

        //Set the card inner text
        setTitle("헤더ㅇㅇㅇㅇㅇ");
    }

}
