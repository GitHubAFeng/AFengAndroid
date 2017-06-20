文章目录

框架引入
优化Adapter代码
 和原始的adapter相对，减少70%的代码量。
添加Item事件
  Item的点击事件
  Item的长按事件
  Item子控件的点击事件
  Item子控件的长按事件
添加列表加载动画
   一行代码轻松切换5种默认动画。
添加头部、尾部
     一行代码搞定，感觉又回到ListView时代。
自动加载
     上拉加载无需监听滑动事件,可自定义加载布局，显示异常提示，自定义异常提示。同时支持下拉加载。
分组布局
     随心定义分组头部。
多布局
    简单配置、无需重写额外方法。
设置空布局
     比Listview的setEmptyView还要好用。
添加拖拽、滑动删除
     开启，监听即可，就是这么简单。
树形列表
     比ExpandableListView还要强大，支持多级。
自定义ViewHolder
    支持自定义ViewHolder，让开发者随心所欲。
扩展框架
   组合第三方框架，轻松实现更多需求定制。
框架引入

先在 build.gradle(Project:XXXX) 的 repositories 添加:

    allprojects {
        repositories {
            ...
            maven { url "https://jitpack.io" }
        }
    }
然后在 build.gradle(Module:app) 的 dependencies 添加:

    dependencies {
            compile 'com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.18'
    }
注意： 一旦出现加载失败的情况，只有两种情况:

配置没配置好
  配置没配置好，有几种情况：
    1. 只配置了dependencies
    2. 配置repositories，但是位置错了，build.gradle(Project:XXXX) 文件下的repositories有两个，一个是buildscript下面的，一个是allprojects下面的，要配置到allprojects下面才是对的。
    3. 版本号前面多一个v，这个是我的锅，在2.1.2版本之前都是带v的，之后（包含2.1.2）都不需要带v。
网络原因（这个就不解释了）
使用Adapter

和原始的adapter相对，减少70%的代码量。
使用代码

public class QuickAdapter extends BaseQuickAdapter<Status, BaseViewHolder> {
    public QuickAdapter() {
        super(R.layout.tweet, DataServer.getSampleData());
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, Status item) {
        viewHolder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .linkify(R.id.tweetText);
                 Glide.with(mContext).load(item.getUserAvatar()).crossFade().into((ImageView) viewHolder.getView(R.id.iv));
    }
}
使用

首先需要继承BaseQuickAdapter,然后BaseQuickAdapter<Status, BaseViewHolder>第一个泛型Status是数据实体类型，第二个BaseViewHolder是ViewHolder其目的是为了支持扩展ViewHolder。

赋值

可以直接使用viewHolder对象点相关方法通过传入viewId和数据进行，方法支持链式调用。如果是加载网络图片或自定义view可以通过viewHolder.getView(viewId)获取该控件。

常用方法

viewHolder.getLayoutPosition() 获取当前item的position
常见问题

这些问题不是使用该库的问题，但是经常有人问这些问题，所以特意写出来，帮助后续遇到以下问题的开发者们。
为什么有数据不显示？
请检查一下你的RecyclerView是否设置了LayoutManager。

为什么有10条数据，只显示1条？
请检查一下item的布局最外层的Layout是不是layout_height设置了match_parent.

数据状态错乱
这个问题无论是RecyclerView还是ListView不做处理都会出现问题，这个本质上是由于布局重用机制导致的，解决办法是通过数据状态来控制控件的状态，一定要设置状态无论什么状态，if和else是少不了的，如下代码：

  if（entity.isCheck）{
    checkBox.isChecked(true);
  } else {
    checkBox.isChecked(false);
  }
解决缓存问题案例：

处理文本框和单选的缓存问题
实现三级伸缩，并解决多选框的复用
添加Item事件

Item的点击事件

adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemClick: ");
                Toast.makeText(ItemClickActivity.this, "onItemClick" + position, Toast.LENGTH_SHORT).show();
            }
        });
Item的长按事件

adapter.setOnItemLongClickListener(new BaseQuickAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemLongClick: ");
                Toast.makeText(ItemClickActivity.this, "onItemLongClick" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
注意：嵌套recycleView的情况下需要使用你使用 adapter. setOnItemClickListener 来设置点击事件,如果使用recycleView.addOnItemTouchListener会累计添加的。

Item子控件的点击事件
首先在adapter的convert方法里面通过viewHolder.addOnClickListener绑定一下的控件id

 @Override
    protected void convert(BaseViewHolder viewHolder, Status item) {
        viewHolder.setText(R.id.tweetName, item.getUserName())
                .setText(R.id.tweetText, item.getText())
                .setText(R.id.tweetDate, item.getCreatedAt())
                .setVisible(R.id.tweetRT, item.isRetweet())
                .addOnClickListener(R.id.tweetAvatar)
                .addOnClickListener(R.id.tweetName)
                .linkify(R.id.tweetText);

    }
然后在设置

 adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public boolean onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Log.d(TAG, "onItemChildClick: ");
                Toast.makeText(ItemClickActivity.this, "onItemChildClick" + position, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
Item子控件的长按事件
步骤同上使用方法不同。
adapter中绑定方法将addOnClickListener改成addOnLongClickListener.
设置点击事件方法setOnItemChildClickListener改成setOnItemChildLongClickListener

注意：设置子控件的事件，如果不在adapter中绑定，点击事件无法生效，因为无法找到你需要设置的控件。

如果需要在点击事件中获取其他子控件可以使用：

getViewByPosition(RecyclerView recyclerView, int position, @IdRes int viewId)
注意：如果有header的话需要处理一下position加上 headerlayoutcount。

添加列表加载动画

开启动画(默认为渐显效果)
adapter.openLoadAnimation();
默认提供5种方法（渐显、缩放、从下到上，从左到右、从右到左）

public static final int ALPHAIN = 0x00000001;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SCALEIN = 0x00000002;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_BOTTOM = 0x00000003;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_LEFT = 0x00000004;
    /**
     * Use with {@link #openLoadAnimation}
     */
    public static final int SLIDEIN_RIGHT = 0x00000005;
切换动画

quickAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
自定义动画

quickAdapter.openLoadAnimation(new BaseAnimation() {
                            @Override
                            public Animator[] getAnimators(View view) {
                                return new Animator[]{
                                        ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f, 1),
                                        ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f, 1)
                                };
                            }
                        });
动画默认只执行一次,如果想重复执行可设置

mQuickAdapter.isFirstOnly(false);
设置不显示动画数量

adapter.setNotDoAnimationCount(count);
首次到界面的item都依次执行加载动画

由于进入界面的item都是很多的速度进来的所以不会出现滑动显示的依次执行动画效果，这个时候会一起执行动画，如果觉得这样的效果不好可以使用setNotDoAnimationCount设置第一屏item不执行动画，但是如果需要依次执行动画可以重写startAnim让第一个屏幕的item动画延迟执行即可。
@Override
    protected void startAnim(Animator anim, int index) {
        super.startAnim(anim, index);
        if (index < count)
        anim.setStartDelay(index * 150);
    }
添加头部、尾部

添加

mQuickAdapter.addHeaderView(getView());
mQuickAdapter.addFooterView(getView());
删除指定view

mQuickAdapter.removeHeaderView(getView);
mQuickAdapter.removeFooterView(getView);
删除所有

mQuickAdapter.removeAllHeaderView();
mQuickAdapter.removeAllFooterView();
默认出现了头部就不会显示Empty，和尾部，配置以下方法也支持同时显示：

setHeaderAndEmpty
setHeaderFooterEmpty
默认头部尾部都是占满一行，如果需要不占满可以配置：

setHeaderViewAsFlow
setFooterViewAsFlow
自动加载

上拉加载

// 滑动最后一个Item的时候回调onLoadMoreRequested方法
setOnLoadMoreListener(RequestLoadMoreListener);
默认第一次加载会进入回调，如果不需要可以配置：

mQuickAdapter.disableLoadMoreIfNotFullPage();
回调处理代码

mQuickAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override public void onLoadMoreRequested() {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentCounter >= TOTAL_COUNTER) {
                            //数据全部加载完毕
                            mQuickAdapter.loadMoreEnd();
                        } else {
                            if (isErr) {
                                //成功获取更多数据
                                mQuickAdapter.addData(DataServer.getSampleData(PAGE_SIZE));
                                mCurrentCounter = mQuickAdapter.getData().size();
                                mQuickAdapter.loadMoreComplete();
                            } else {
                                //获取更多数据失败
                                isErr = true;
                                Toast.makeText(PullToRefreshUseActivity.this, R.string.network_err, Toast.LENGTH_LONG).show();
                                mQuickAdapter.loadMoreFail();

                            }
                        }
                    }

                }, delayMillis);
            }
        }, mReyclerView);
加载完成（注意不是加载结束，而是本次数据加载结束并且还有下页数据）

mQuickAdapter.loadMoreComplete();
加载失败

mQuickAdapter.loadMoreFail();
加载结束

mQuickAdapter.loadMoreEnd();
注意：如果上拉结束后，下拉刷新需要再次开启上拉监听，需要使用setNewData方法填充数据。

打开或关闭加载（一般用于下拉的时候做处理，因为上拉下拉不能同时操作）

mQuickAdapter.setEnableLoadMore(boolean);
预加载

// 当列表滑动到倒数第N个Item的时候(默认是1)回调onLoadMoreRequested方法
mQuickAdapter.setPreLoadNumber(int);
设置自定义加载布局

mQuickAdapter.setLoadMoreView(new CustomLoadMoreView());
public final class CustomLoadMoreView extends LoadMoreView {

    @Override public int getLayoutId() {
        return R.layout.view_load_more;
    }

    /**
     * 如果返回true，数据全部加载完毕后会隐藏加载更多
     * 如果返回false，数据全部加载完毕后会显示getLoadEndViewId()布局
     */
    @Override public boolean isLoadEndGone() {
        return true;
    }

    @Override protected int getLoadingViewId() {
        return R.id.load_more_loading_view;
    }

    @Override protected int getLoadFailViewId() {
        return R.id.load_more_load_fail_view;
    }

    /**
     * isLoadEndGone()为true，可以返回0
     * isLoadEndGone()为false，不能返回0
     */
    @Override protected int getLoadEndViewId() {
        return 0;
    }
}
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="@dimen/dp_40">

    <LinearLayout
        android:id="@+id/load_more_loading_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:gravity="center"
        android:orientation="horizontal">

        <ProgressBar
            android:id="@+id/loading_progress"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            style="?android:attr/progressBarStyleSmall"
            android:layout_marginRight="@dimen/dp_4"
            android:indeterminateDrawable="@drawable/sample_footer_loading_progress"/>

        <TextView
            android:id="@+id/loading_text"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_marginLeft="@dimen/dp_4"
            android:text="@string/loading"
            android:textColor="#0dddb8"
            android:textSize="@dimen/sp_14"/>
    </LinearLayout>

    <FrameLayout
        android:id="@+id/load_more_load_fail_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:visibility="gone">


        <TextView
            android:id="@+id/tv_prompt"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:layout_gravity="center"
            android:textColor="#0dddb8"
            android:text="@string/load_failed"/>

    </FrameLayout>

</FrameLayout>
下拉加载（符合聊天软件下拉历史数据需求）
设置开启开关

 mAdapter.setUpFetchEnable(true);
设置监听

mAdapter.setUpFetchListener(new BaseQuickAdapter.UpFetchListener() {
            @Override
            public void onUpFetch() {
                startUpFetch();
            }
        });

private void startUpFetch() {
        count++;
        /**
         * set fetching on when start network request.
         */
        mAdapter.setUpFetching(true);
        /**
         * get data from internet.
         */
        mRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.addData(0, genData());
                /**
                 * set fetching off when network request ends.
                 */
                mAdapter.setUpFetching(false);
                /**
                 * set fetch enable false when you don't need anymore.
                 */
                if (count > 5) {
                    mAdapter.setUpFetchEnable(false);
                }
            }
        }, 300);
    }
开始加载的位置

mAdapter.setStartUpFetchPosition(2);
分组布局

实体类必须继承SectionEntity

public class MySection extends SectionEntity<Video> {
    private boolean isMore;
    public MySection(boolean isHeader, String header) {
        super(isHeader, header);
    }

    public MySection(Video t) {
        super(t);
    }
}
adapter构造需要传入两个布局id，第一个是item的，第二个是head的，在convert方法里面加载item数据，在convertHead方法里面加载head数据

public class SectionAdapter extends BaseSectionQuickAdapter<MySection> {
     public SectionAdapter(int layoutResId, int sectionHeadResId, List data) {
        super(layoutResId, sectionHeadResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, MySection item) {
        helper.setImageUrl(R.id.iv, (String) item.t);
    }
    @Override
    protected void convertHead(BaseViewHolder helper,final MySection item) {
        helper.setText(R.id.header, item.header);

        helper.setOnClickListener(R.id.more, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,item.header+"more..",Toast.LENGTH_LONG).show();
            }
        });
    }
多布局

实体类必须实现MultiItemEntity，在设置数据的时候，需要给每一个数据设置itemType

public class MultipleItem implements MultiItemEntity {
    public static final int TEXT = 1;
    public static final int IMG = 2;
    private int itemType;

    public MultipleItem(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
在构造里面addItemType绑定type和layout的关系

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TEXT, R.layout.text_view);
        addItemType(MultipleItem.IMG, R.layout.image_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TEXT:
                helper.setImageUrl(R.id.tv, item.getContent());
                break;
            case MultipleItem.IMG:
                helper.setImageUrl(R.id.iv, item.getContent());
                break;
        }
    }

}
如果考虑到在GridLayoutManager复用item问题可以配置：

  multipleItemAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return data.get(position).getSpanSize();
            }
        });
如果大家觉得这种多布局方式有点由于耦合了实体类，还有支持另外一种多布局方式具体可查看更便捷的多布局, 为 BaseQuickAdapter 设置代理.

设置空布局

// 没有数据的时候默认显示该布局
mQuickAdapter.setEmptyView(getView());
添加拖拽、滑动删除

拖拽和滑动删除的回调方法

OnItemDragListener onItemDragListener = new OnItemDragListener() {
    @Override
    public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos){}
    @Override
    public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {}
    @Override
    public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {}
}

OnItemSwipeListener onItemSwipeListener = new OnItemSwipeListener() {
    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {}
    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {}
    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {}
};
adapter需要继承BaseItemDraggableAdapter

public class ItemDragAdapter extends BaseItemDraggableAdapter<String, BaseViewHolder> {
    public ItemDragAdapter(List data) {
        super(R.layout.item_draggable_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.tv, item);
    }
}
Activity使用代码

mAdapter = new ItemDragAdapter(mData);

ItemDragAndSwipeCallback itemDragAndSwipeCallback = new ItemDragAndSwipeCallback(mAdapter);
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(itemDragAndSwipeCallback);
itemTouchHelper.attachToRecyclerView(mRecyclerView);

// 开启拖拽
mAdapter.enableDragItem(itemTouchHelper, R.id.textView, true);
mAdapter.setOnItemDragListener(onItemDragListener);

// 开启滑动删除
mAdapter.enableSwipeItem();
mAdapter.setOnItemSwipeListener(onItemSwipeListener);
默认不支持多个不同的 ViewType 之间进行拖拽，如果开发者有所需求：

重写ItemDragAndSwipeCallback里的onMove()方法，return true即可
树形列表

例子：三级菜单

// if you don't want to extent a class, you can also use the interface IExpandable.
// AbstractExpandableItem is just a helper class.
public class Level0Item extends AbstractExpandableItem<Level1Item> {...}
public class Level1Item extends AbstractExpandableItem<Person> {...}
public class Person {...}
adapter需要继承BaseMultiItemQuickAdapter

public class ExpandableItemAdapter extends BaseMultiItemQuickAdapter<MultiItemEntity, BaseViewHolder> {
    public ExpandableItemAdapter(List<MultiItemEntity> data) {
        super(data);
        addItemType(TYPE_LEVEL_0, R.layout.item_expandable_lv0);
        addItemType(TYPE_LEVEL_1, R.layout.item_expandable_lv1);
        addItemType(TYPE_PERSON, R.layout.item_text_view);
    }
    @Override
    protected void convert(final BaseViewHolder holder, final MultiItemEntity item) {
        switch (holder.getItemViewType()) {
        case TYPE_LEVEL_0:
            ....
            //set view content
           holder.itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   int pos = holder.getAdapterPosition();
                   if (lv0.isExpanded()) {
                       collapse(pos);
                   } else {
                       expand(pos);
                   }
           }});
           break;
        case TYPE_LEVEL_1:
           // similar with level 0
           break;
        case TYPE_PERSON:
           //just set the content
           break;
    }
}
开启所有菜单：

adapter.expandAll();
删除某一个item（添加和修改的思路是一样的）

// 获取当前父级位置
 int cp = getParentPosition(person);
// 通过父级位置找到当前list，删除指定下级
 ((Level1Item)getData().get(cp)).removeSubItem(person);
// 列表层删除相关位置的数据
 getData().remove(holder.getLayoutPosition());
// 更新视图
 notifyDataSetChanged();
自定义ViewHolder

需要继承BaseViewHolder

 public class MovieViewHolder extends BaseViewHolder
然后修改adapter的第二个泛型为自定义的ViewHolder

public class DataBindingUseAdapter extends BaseQuickAdapter<Movie, DataBindingUseAdapter.MovieViewHolder>
注意：需要单独建一个外部类继承BaseViewHolder，否则部分机型会出现ClassCastException