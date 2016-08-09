package com.mom.soccer.board;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mom.soccer.R;
import com.mom.soccer.adapter.BoardListAdapter;
import com.mom.soccer.common.PrefUtil;
import com.mom.soccer.dto.Board;
import com.mom.soccer.dto.ServerResult;
import com.mom.soccer.dto.User;
import com.mom.soccer.retrofitdao.BoardService;
import com.mom.soccer.retropitutil.ServiceGenerator;
import com.mom.soccer.widget.VeteranToast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BoardMainActivity extends AppCompatActivity {

    private static final String TAG = "BoardMainActivity";

    @Bind(R.id.et_board_contnet)
    EditText editText_bd_content;

    @Bind(R.id.board_list_view)
    ListView boardListView;

    private BoardListAdapter boardListAdapter;

    private PrefUtil prefUtil;
    private User user;

    private int userMissionId = 0;
    private int missionuId = 0;

    @Bind(R.id.comment_title)
    TextView comment_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_board_layout);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        userMissionId = intent.getExtras().getInt("usermissionid");
        missionuId = intent.getExtras().getInt("missionuid");

        prefUtil = new PrefUtil(this);
        user = prefUtil.getUser();
        Log.d(TAG,"user mission id : " + userMissionId);


        Toolbar toolbar = (Toolbar) findViewById(R.id.comment_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        //빽버튼?
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.back_arrow);
        comment_title.setText(getString(R.string.toolbar_comment_page)+"("+user.getUsername()+")");

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart() get List ============================================");

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_get_list), true);

        BoardService boardService = ServiceGenerator.createService(BoardService.class,this,user);

        Board board = new Board();
        board.setUsermissionid(userMissionId);
        board.setUid(missionuId);
        Call<List<Board>> call = boardService.getboardlist(board);

        call.enqueue(new Callback<List<Board>>() {
            @Override
            public void onResponse(Call<List<Board>> call, Response<List<Board>> response) {
                if(response.isSuccessful()){
                    List<Board> boardList;
                    boardList = response.body();
                    boardListAdapter = new BoardListAdapter(getApplicationContext(),boardList,user.getUid());
                    boardListView.setAdapter(boardListAdapter);
                    dialog.dismiss();
                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<List<Board>> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });

    }

    //댓글을 입력한다
    @OnClick(R.id.btn_boardcreate)
    public void createContent(){
        if(editText_bd_content.getText().length()== 0){
            VeteranToast.makeToast(getApplicationContext(),getString(R.string.board_validation_word_empty), Toast.LENGTH_SHORT).show();
            return;
        }

        final ProgressDialog dialog;
        dialog = ProgressDialog.show(this, "", getString(R.string.network_create_comment), true);

        BoardService boardService = ServiceGenerator.createService(BoardService.class,this,user);

        Board board = new Board();
        board.setUid(missionuId);
        board.setWriteuid(user.getUid());
        board.setUsermissionid(userMissionId);
        board.setComment(editText_bd_content.getText().toString());

        Call<ServerResult> call = boardService.saveBoard(board);
        call.enqueue(new Callback<ServerResult>() {
            @Override
            public void onResponse(Call<ServerResult> call, Response<ServerResult> response) {
                if(response.isSuccessful()){
                    ServerResult result = response.body();
                    dialog.dismiss();
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_create_complete),Toast.LENGTH_SHORT).show();

                    finish();
                    Intent intent = new Intent(BoardMainActivity.this, BoardMainActivity.class);
                    intent.putExtra("usermissionid",userMissionId);
                    intent.putExtra("missionuid",missionuId);
                    startActivity(intent);


                }else{
                    VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_isnotsuccessful),Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ServerResult> call, Throwable t) {
                VeteranToast.makeToast(getApplicationContext(),getString(R.string.network_error_message1),Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                dialog.dismiss();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //홈버튼클릭시
        if (id == android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
