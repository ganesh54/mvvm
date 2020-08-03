package com.example.mvvm.activity.view;

import android.os.Bundle;
import android.view.View;

import com.example.mvvm.R;
import com.example.mvvm.activity.viewmodel.UserViewModel;
import com.example.mvvm.adapter.UserAdapter;
import com.example.mvvm.model.User;
import com.example.mvvm.model.Users;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;


public class UserActivity extends AppCompatActivity {
    @BindView(R.id.swipe_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.rv_user)
    RecyclerView rvUser;
    @BindView(R.id.tv_loading)
    AppCompatTextView tvLoading;
    private ArrayList<User> userList = new ArrayList<>();
    private UserAdapter adapter;
    private UserActivity activity;

    private int page = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 0;
    private int perPage = 5;
    private int currentPage = page;
    private UserViewModel userViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        activity = UserActivity.this;
        userViewModel = new UserViewModel(getApplication());
        initView();
        obervableData(page, perPage);
    }

    private void initView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        rvUser.setLayoutManager(layoutManager);
        rvUser.setHasFixedSize(true);
        adapter = new UserAdapter(activity, userList);
        rvUser.setAdapter(adapter);

        rvUser.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {
                        currentPage += 1;
                        isLoading = true;
                        loadMoreUsers(currentPage, perPage);
                    }
                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                page = 1;
                currentPage = page;
                isLastPage = false;
                obervableData(page, perPage);
            }
        });
    }

    public void obervableData(int page, int perPage) {
        userViewModel.callGetUserApi(page, perPage).observe(this, new Observer<Users>() {
            @Override
            public void onChanged(Users users) {
                if (users != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    userList.clear();
                    userList.addAll(users.userList);
                    adapter.notifyDataSetChanged();
                    TOTAL_PAGES = users.getTotalPages();
                    if (currentPage <= TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                    tvLoading.setVisibility(View.GONE);
                    rvUser.setVisibility(View.VISIBLE);
                } else {
                    tvLoading.setVisibility(View.GONE);
                    rvUser.setVisibility(View.GONE);
                }
            }
        });
    }

    public void loadMoreUsers(int page, int perPage) {
        userViewModel.callNextUser(page, perPage).observe(this, new Observer<Users>() {
            @Override
            public void onChanged(Users users) {
                if (users != null) {
                    adapter.removeLoadingFooter();
                    isLoading = false;
                    userList.addAll(users.getUserList());
                    adapter.notifyDataSetChanged();
                    TOTAL_PAGES = users.getTotalPages();
                    if (currentPage <= TOTAL_PAGES) {
                        adapter.addLoadingFooter();
                    } else {
                        isLastPage = true;
                    }
                }
            }
        });
    }
}