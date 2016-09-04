package com.mom.soccer.cardview;

/**
 * Created by sungbo on 2016-08-23.
 */
public class FeedBackCard {
        /*
        extends Card{

    private FeedbackHeader feedbackHeader;

    private LinearLayout li_feed_back_call,li_feed_back_send,li_youtube;

    private TextView ins_content,user_conten,TextfeedType,date,Textfeeddate,insname;
    private ImageView userimage,insimage,vedioplay;
    private RatingBar mRatingBar;
    private YouTubeThumbnailView video_ThumbnailView;

    private String title = null;
    private User user;
    private Mission mission;
    private Activity activity;

    private String requestType = null;
    private String videoAddr = null ;

    public FeedBackCard(final Activity activity, final FeedbackHeader feedbackHeader, final User user, final Mission mission) {
        super(activity, R.layout.feedback_card_item);
        this.feedbackHeader = feedbackHeader;
        this.user = user;
        this.mission = mission;
        this.activity = activity;

        videoAddr  = feedbackHeader.getVideoaddr();

                CardHeader header = new CardHeader(getContext());

        if(feedbackHeader.getFeedbacktype().equals("video")){
            requestType = getContext().getString(R.string.feedback_type_video);
        }else{
            requestType = getContext().getString(R.string.feedback_type_word);
        }

        if(feedbackHeader.getType().equals("user")){
            title = getContext().getString(R.string.feedback_request_user_request) + " To."+feedbackHeader.getInsname();
            setBackgroundColorResourceId(R.color.color6);
            header.setPopupMenu(R.menu.feedback_pop_menu, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                        FeedbackLine line = new FeedbackLine();
                        line.setFeedbackid(feedbackHeader.getFeedbackid());
                        ratingEval("delete",line);
                }
            });

        }else{
            title = getContext().getString(R.string.feedback_request_ins_reply) + " From."+feedbackHeader.getUsername();
            setBackgroundColorResourceId(R.color.color2);
            header.setPopupMenu(R.menu.feedback_pop_ins, new CardHeader.OnClickCardHeaderPopupMenuListener() {
                @Override
                public void onMenuItemClick(BaseCard card, MenuItem item) {
                    //피드백 신고를 한다
                    VeteranToast.makeToast(getContext(),"준비 중입니다",Toast.LENGTH_SHORT).show();
                }
            });
        }


        header.setTitle(title);
        addCardHeader(header);
    }

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        li_feed_back_call = (LinearLayout) parent.findViewById(R.id.li_feed_back_call);
        li_feed_back_send = (LinearLayout) parent.findViewById(R.id.li_feed_back_send);
        li_youtube = (LinearLayout) parent.findViewById(R.id.li_youtube);
        vedioplay = (ImageView) parent.findViewById(R.id.vedioplay);

        //TextfeedType,date,Textfeeddate
        TextfeedType = (TextView) parent.findViewById(R.id.TextfeedType);
        date    = (TextView) parent.findViewById(R.id.date);
        Textfeeddate    = (TextView) parent.findViewById(R.id.Textfeeddate);
        insname= (TextView) parent.findViewById(R.id.insname);
        video_ThumbnailView = (YouTubeThumbnailView) parent.findViewById(R.id.video_ThumbnailView);

        date.setText(feedbackHeader.getChange_creationdate());
        Textfeeddate.setText(feedbackHeader.getChange_creationdate());
        insname.setText(feedbackHeader.getInsname());

        if(feedbackHeader.getType().equals("user")){
            li_feed_back_send.setVisibility(View.VISIBLE);
            li_feed_back_call.setVisibility(View.GONE);

            user_conten = (TextView) parent.findViewById(R.id.usercontent);
            userimage = (ImageView) parent.findViewById(R.id.userimage);

            user_conten.setText(feedbackHeader.getContent());
            if(feedbackHeader.getFeedbacktype().equals("video")) {
                TextfeedType.setText(getContext().getString(R.string.feedback_type_video));
            }else{
                TextfeedType.setText(getContext().getString(R.string.feedback_type_word));
            }
            Glide.with(getContext())
                    .load(feedbackHeader.getProfileimgurl())
                    .into(userimage);
        }else{
            li_feed_back_send.setVisibility(View.GONE);
            li_feed_back_call.setVisibility(View.VISIBLE);

            ins_content = (TextView) parent.findViewById(R.id.inscontent);
            insimage = (ImageView) parent.findViewById(R.id.insimage);

            ins_content.setText(feedbackHeader.getContent());

            Glide.with(getContext())
                    .load(feedbackHeader.getInsprofileimgurl())
                    .into(insimage);

            mRatingBar = (RatingBar) parent.findViewById(R.id.eval_ratingbar);
            LayerDrawable star =  (LayerDrawable) mRatingBar.getProgressDrawable();
            star.getDrawable(2).setColorFilter(Color.RED, PorterDuff.Mode.SRC_ATOP);

            if (mRatingBar != null){
                mRatingBar.setNumStars(5);
                mRatingBar.setMax(5);
                mRatingBar.setStepSize(0.5f);
                mRatingBar.setRating(feedbackHeader.getEvalscore());
            }

            mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    FeedbackLine line = new FeedbackLine();
                    line.setFeedbackid(feedbackHeader.getFeedbackid());
                    ratingEval("save",line);
                }
            });

        }

        if(feedbackHeader.getFeedbacktype().equals("video")) {
            //video_ThumbnailView.setVisibility(View.VISIBLE);
            //vedioplay.setVisibility(View.VISIBLE);

            TextfeedType.setText(getContext().getString(R.string.feedback_type_video));
            video_ThumbnailView.initialize(Auth.KEY, new YouTubeThumbnailView.OnInitializedListener() {
                @Override
                public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                    youTubeThumbnailLoader.setVideo(videoAddr);
                }

                @Override
                public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

                }
            });

            video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                    intent.putExtra(Common.YOUTUBEVIDEO,videoAddr);
                    activity.startActivity(intent);
                }
            });

        }else{
            TextfeedType.setText(getContext().getString(R.string.feedback_type_word));
            video_ThumbnailView.setVisibility(View.GONE);
            vedioplay.setVisibility(View.GONE);
        }


       video_ThumbnailView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,YoutubePlayerActivity.class);
                intent.putExtra(Common.YOUTUBEVIDEO,videoAddr);
                activity.startActivity(intent);
            }
        });
    }

    public void ratingEval(String trType, FeedbackLine line){
        FeedBackService feedBackService = ServiceGenerator.createService(FeedBackService.class,getContext(),user);
        if(trType.equals("save")){
            WaitingDialog.showWaitingDialog(activity,false);
            line.setFeedbacklineid(feedbackHeader.getFeedbacklineid());
            line.setEvalscore(mRatingBar.getRating());

            Call<ServerResult> c = feedBackService.updateLine(line);
            c.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_eval_save) +mRatingBar.getRating() , Toast.LENGTH_SHORT).show();
                    }else{
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }else if(trType.equals("delete")){
            WaitingDialog.showWaitingDialog(activity,false);
            Call<ServerResult> s = feedBackService.deleteLine(line);
            s.enqueue(new Callback<ServerResult>() {
                @Override
                public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                    WaitingDialog.cancelWaitingDialog();
                    if(response.isSuccessful()){
                        ServerResult result = response.body();
                        if(result.getResult().equals("S")){
                            VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_delete), Toast.LENGTH_SHORT).show();

                            //리프레쉬
                            Intent intent = new Intent(getContext(), MissionMainActivity.class);
                            intent.putExtra(MissionCommon.OBJECT,mission);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            getContext().startActivity(intent);

                        }else if(result.getResult().equals("E")){
                            VeteranToast.makeToast(getContext(),getContext().getString(R.string.feedback_feedback_not_delete), Toast.LENGTH_SHORT).show();
                        }
                    }else{
                    }
                }

                @Override
                public void onFailure(Call<ServerResult> call, Throwable t) {
                    WaitingDialog.cancelWaitingDialog();
                    t.printStackTrace();
                }
            });
        }

    }
*/
}
