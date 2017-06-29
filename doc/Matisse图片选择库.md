

https://github.com/zhihu/Matisse/wiki/Basic-Usage

Basic Usage
Jiaheng Ge edited this page 16 days ago · 5 revisions
 Pages 7
Home
Basic Usage
Capture
Filter
Image Engine
Roadmap
Theme
Clone this wiki locally


https://github.com/zhihu/Matisse.wiki.git
 Clone in Desktop
Basic Usage Snippet

Launch Matisse

Matisse.from(MainActivity.this)
        .choose(MimeType.ofAll(), false)
        .countable(true)
        .maxSelectable(9)
        .gridExpectedSize(getResources().getDimensionPixelSize(R.dimen.grid_expected_size))
        .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
        .thumbnailScale(0.85f)
        .imageEngine(new GlideEngine())
        .forResult(REQUEST_CODE_CHOOSE);
Receive Result

In onActivityResult() callback of the starting Activity or Fragment:

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_CODE_CHOOSE && resultCode == RESULT_OK) {
        Log.d("Matisse", "Uris: " + Matisse.obtainResult(data));
        Log.d("Matisse", "Paths: " + Matisse.obtainPathResult(data));
    }
}
Mime Type

Matisse supports the following mime types:

Mime type	File extensions	Media type
image/jpeg	jpg, jpeg	image
image/png	png	image
image/gif	gif	image
image/x-ms-bmp	bmp	image
image/webp	webp	image
video/mpeg	mpeg, mpg	video
video/mp4	mp4, m4v	video
video/quicktime	mov	video
video/3gpp	3gp, 3gpp	video
video/3gpp2	3g2, 3gpp2	video
video/x-matroska	mkv	video
video/webm	webm	video
video/mp2ts	ts	video
video/avi	avi	video
All images and videos will be shown in Matisse by default, you can't limit the selectable mime types by:

MimeType.ofAll()
MimeType.of(MimeType type, MimeType... rest)
MimeType.ofImage()
MimeType.ofVideo()
You can request Matisse to show only one media type if

invoke showSingleMediaType(true) when launching Matisse
Choosing only images or videos
Launch Entry

You can launch Matisse from Activity or Fragment. Meanwhile you should receive results in the corresponding onActivityResult() callback.

Count

Default

There will be a check mark in the right top corner of the thumbnail and you can't only select one image.

Auto-increased number

Use countable(true) to show an auto-increased number check mark from 1. It's like the Dracula Style screenshot in README.

Max selectable

Use maxSelectable(int maxSelectable) to limit maximum selectable number.

Orientation

Use restrictOrientation(@ScreenOrientation int orientation) to set the desired orientation of image selecting and previewing Activity.

Grid Spec

If you want a fixed span count, use spanCount(int spanCount). The span count will stay the same when orientation changes.
If you expect a grid size which will be flexible fitting different screens, use spanCount(int spanCount). This value won't necessarily be applied cause the photo grid should fill the view container. The measured photo grid's size will be as close to this value as possible.

Thumbnail Scale

Use thumbnailScale(float scale) to set thumbnail bitmap's scale compared to the view's size. It should be a float value in (0.0, 1.0].