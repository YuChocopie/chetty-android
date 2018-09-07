package com.chatty.android.chattyClient.view.setting;

import android.support.v7.app.AppCompatActivity;

import com.chatty.android.chattyClient.externalModules.AndroidExtended.Props;
import com.chatty.android.chattyClient.model.TimelineEntry;
import com.chatty.android.chattyClient.presenter.main.TimelineRecyclerViewAdapter;

import java.util.List;
import java.util.function.Consumer;

public class SettingActivityProps extends Props {
  public TimelineRecyclerViewAdapter.RecyclerViewClickListener handleClickTimelineEntry;
  public Consumer<AppCompatActivity> handleClickWriteButton;
  public List<TimelineEntry> timeline;
}
