package com.chatty.android.chattyClient.view.write;

import android.Manifest;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chatty.android.chattyClient.R;
import com.chatty.android.chattyClient.databinding.ActivityWriteBinding;
import com.chatty.android.chattyClient.model.ChatBalloon;
import com.chatty.android.chattyClient.presenter.write.DialogueAdapter;
import com.chatty.android.chattyClient.presenter.write.WritePresenter;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gun0912.tedbottompicker.TedBottomPicker;

public class WriteActivity extends AppCompatActivity{
  ArrayList<Uri> selectedUriList;
  private WritePresenter presenter;
  private static final String WRITE_TITLE = "Write";

  @BindView(R.id.button_timeline_left)
  public ImageButton backButton;

  @BindView(R.id.recyclerView_dialogue)
  public RecyclerView recyclerView;
  public RecyclerView.Adapter dialogueAdapter;

  @BindView(R.id.editText_writeInput)
  public EditText writeInputEditText;

  @BindView(R.id.button_writeSubmit)
  public Button writeSubmitButton;

  public List<ChatBalloon> chatBalloons;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    construct();
  }

  private void construct() {

    setContentView(R.layout.activity_write);
    loadDependencies();
    render();

    TextView textView = findViewById(R.id.textView_timeline_title);
    textView.setText(WRITE_TITLE);

  }

  private void loadDependencies() {
    presenter = new WritePresenter(this);
    ButterKnife.bind(this);
  }

  public void initView() {
    chatBalloons = new ArrayList<>();

    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    dialogueAdapter = new DialogueAdapter(getApplicationContext(), chatBalloons);
    recyclerView.setAdapter(dialogueAdapter);

    writeSubmitButton.setOnClickListener(presenter.handleClickWriteSubmit());
  }

  public void appendChatBalloon(ChatBalloon chatBalloon) {
    this.chatBalloons.add(chatBalloon);
    this.dialogueAdapter.notifyItemInserted(this.chatBalloons.size() - 1);

    recyclerView.scrollToPosition(this.chatBalloons.size() -1);
  }

  public void render() {
    renderBackButton();
  }

  @OnClick(R.id.btn_write_plus)
  public void renderPlusImage(View view) {
    PermissionListener permissionListener = new PermissionListener() {
      @Override
      public void onPermissionGranted() {
        TedBottomPicker  bottomSheetFragment = new TedBottomPicker.Builder(WriteActivity.this)
          .setOnMultiImageSelectedListener(uriList -> {
            selectedUriList = uriList;
            showUriList(uriList);
          })
          .setPeekHeight(1600)
          .showTitle(false)
          .setCompleteButtonText("첨부")
          .setEmptySelectionText("No Select")
          .setSelectedUriList(selectedUriList)
          .create();
        bottomSheetFragment.show(getSupportFragmentManager());
      }

      @Override
      public void onPermissionDenied(List<String> deniedPermissions) {
        Toast.makeText(WriteActivity.this, "Not allowed\n.", Toast.LENGTH_SHORT).show();
      }
    };

    TedPermission.with(this)
      .setPermissionListener(permissionListener)
      .setDeniedMessage("If you reject permission,you can not use this service\\n\\nPlease turn on permissions at [Setting] > [Permission]")
      .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
      .check();
  }

  private void showUriList(ArrayList<Uri> uriList){
    ActivityWriteBinding binding = ActivityWriteBinding.inflate(getLayoutInflater());
    binding.selectedPhotosContainer.removeAllViews();
    binding.ivImage.setVisibility(View.GONE);
    binding.selectedPhotosContainer.setVisibility(View.VISIBLE);
    int widthPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,150,getResources().getDisplayMetrics());
    int heightPixel = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150,getResources().getDisplayMetrics());
    for (Uri uri : uriList) {
      Log.e("123123","showUriList?");
      View imageHolder = LayoutInflater.from(this).inflate(R.layout.item_image, null);
      ImageView thumbnail = (ImageView) imageHolder.findViewById(R.id.media_image);
      Glide.with(this)
        .load(uri.toString())
        .into(thumbnail);
      binding.selectedPhotosContainer.addView(imageHolder);
      thumbnail.setLayoutParams(new FrameLayout.LayoutParams(widthPixel, heightPixel));
    }
  }

/*  private void check() {

    int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

    if (permissionCheck== PackageManager.PERMISSION_DENIED) {
      // 권한 없음
    }else {
      // 권한 있음

      if(ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
          //권한이 필요한 이유 설명 후 requestPermissions()함수를 호출해 권한허가 요청
//          onRequestPermissionsResult();
//          requestPermissions();

        } else {
//          ActivityCompat.requestPermissions(this,
//            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE});
        }
      }

    PermissionListener permissionlistener = new PermissionListener() {
      @Override
      public void onPermissionGranted() {
        Toast.makeText(WriteActivity.this, "Permission Granted",Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onPermissionDenied(List<String> deniedPermissions) {
        Toast.makeText(WriteActivity.this,"Permission Deneid/n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
      }
    };

    TedPermission.with(this)
      .setPermissionListener(permissionlistener)
      .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
      .setPermissions(Manifest.permission.READ_CONTACTS, Manifest.permission.ACCESS_FINE_LOCATION)
      .check();
    }

    }*/
  private void renderBackButton() {
    backButton.setOnClickListener(view -> {
      finish();
    });
  }
}



