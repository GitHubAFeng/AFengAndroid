第一句 , 实例化:

 SearchFragment searchFragment = SearchFragment.newInstance();
第二句 , 设置回调:

 searchFragment.setOnSearchClickListener(new IOnSearchClickListener() {
            @Override
            public void OnSearchClick(String keyword) {
                //这里处理逻辑
                Toast.makeText(ToolBarActivity.this, keyword, Toast.LENGTH_SHORT).show();
            }
        });
第三句 , 显示搜索框:

  searchFragment.show(getSupportFragmentManager(),SearchFragment.TAG);